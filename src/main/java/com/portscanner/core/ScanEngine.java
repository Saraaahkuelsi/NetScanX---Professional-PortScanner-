package com.portscanner.core;

import com.portscanner.security.*;
import com.portscanner.util.OsFingerprinter;
import com.portscanner.security.OnlineCVEFetcher;
import com.portscanner.scanner.*;
import com.portscanner.model.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Future;
import com.portscanner.web.ScanProgressService;

public class ScanEngine {

    private ScanPolicy policy;
    private ExecutorService threadPool;
    private VulnerabilityChecker vulnerabilityChecker;
    private Auditlogger auditlogger;
    private ScanProgressService progressService;

    public ScanEngine(ScanPolicy policy, Auditlogger auditlogger, ScanProgressService progressService) {
        this.auditlogger = auditlogger;
        this.policy = policy;
        this.progressService = progressService;
        threadPool = Executors.newFixedThreadPool(policy.getMaxThreads());
        vulnerabilityChecker = new VulnerabilityChecker();
    }

    public ScanReport scan(ScanTarget target) {
        ScanReport report = new ScanReport(target, "scanner");

        auditlogger.logScanStart(target.getHost());

        TcpScanner scanner = new TcpScanner(policy);

        List<Future<PortResult>> futures = new ArrayList<>();

        for (int port = target.getStartPort(); port <= target.getEndPort(); port++) {
            ScanTask task = new ScanTask(target.getHost(), port, scanner, policy);
            Future<PortResult> future = threadPool.submit(task);
            futures.add(future);
        }

        for (Future<PortResult> future : futures) {
            try {
                PortResult result = future.get();
                if (result.isOpen()) {
                    auditlogger.logOpenPort(target.getHost(), result.getPort());
                    String serviceName = result.getServiceName() != null ? result.getServiceName() : "";
                    String banner = result.getBanner() != null ? result.getBanner() : "";
                    OnlineCVEFetcher fetcher = new OnlineCVEFetcher();
                    List<CVE> cves = fetcher.fetch(result.getServiceName());

                    for (CVE cve : cves) {
                        if (cve.getCvssScore() >= 9.0) result.setRiskLevel("CRITICAL");
                        else if (cve.getCvssScore() >= 7.0) result.setRiskLevel("HIGH");
                        else if (cve.getCvssScore() >= 4.0) result.setRiskLevel("MEDIUM");
                        else result.setRiskLevel("LOW");

                        result.getVulnerabilities().add(cve.toString());
                    }

                    if (cves.isEmpty()) result.setRiskLevel("LOW");

                    // Alerte si CRITICAL ou HIGH
                    if (progressService != null) {
                        String riskLevel = result.getRiskLevel();
                        if (riskLevel != null &&
                            (riskLevel.equals("CRITICAL") || riskLevel.equals("HIGH"))) {
                            progressService.addAlert(
                                result.getPort() + " | " +
                                result.getServiceName() + " | " +
                                riskLevel + " | " +
                                result.getVulnerabilities()
                            );
                        }
                    }
                }
                report.getResults().add(result);
            } catch (Exception e) {
                System.out.println("Erreur sur un port : " + e.getMessage());
            }
        }

        auditlogger.logScanEnd(target.getHost());

        // Détection OS
        String os = OsFingerprinter.detect(target.getHost(), report.getResults());
        target.setOperatingSystem(os);
        System.out.println("OS détecté : " + os);

        // Scan UDP
        int[] udpPorts = {53, 67, 68, 123, 161, 162, 500};
        UdpScanner udpScanner = new UdpScanner(2000);
        OfflineServiceDetector udpDetector = new OfflineServiceDetector();

        for (int port : udpPorts) {
            if (port >= target.getStartPort() && port <= target.getEndPort()) {
                PortResult udpResult = udpScanner.scan(target.getHost(), port);
                if (udpResult.getState().equals("OPEN")) {
                    udpResult.setServiceName(udpDetector.detect(port));
                    auditlogger.logOpenPort(target.getHost(), port);
                    report.getResults().add(udpResult);
                }
            }
        }

        return report;
    }

    public void shutdown() {
        threadPool.shutdown();
    }
}