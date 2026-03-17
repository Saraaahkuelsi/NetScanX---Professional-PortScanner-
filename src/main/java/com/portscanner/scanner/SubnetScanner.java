package com.portscanner.scanner;

import com.portscanner.model.ScanReport;
import com.portscanner.model.ScanTarget;
import com.portscanner.model.PortResult;
import com.portscanner.security.Auditlogger;
import com.portscanner.core.ScanEngine;
import com.portscanner.core.ScanPolicy;
import com.portscanner.util.NetworksUtils;
import java.util.List;
import java.util.ArrayList;

public class SubnetScanner {

    private ScanPolicy policy;
    private Auditlogger auditLogger;
    private int startPort;
    private int endPort;

    public SubnetScanner(ScanPolicy policy, Auditlogger auditLogger, int startPort, int endPort) {
        this.policy = policy;
        this.auditLogger = auditLogger;
        this.startPort = startPort;
        this.endPort = endPort;
    }

    public List<ScanReport> scan(String cidr) {
        List<ScanReport> reports = new ArrayList<>();
        List<String> ips = generateIps(cidr);

        System.out.println("Sous-réseau : " + cidr);
        System.out.println("IPs à tester : " + ips.size());

        for (String ip : ips) {
            System.out.print("Testing " + ip + "... ");
            if (NetworksUtils.isReachable(ip, 1000)) {
                System.out.println("ACTIVE ✅");
                ScanTarget target = new ScanTarget(ip, startPort, endPort);
                ScanEngine engine = new ScanEngine(policy, auditLogger,null);
                ScanReport report = engine.scan(target);
                reports.add(report);
                engine.shutdown();
            } else {
                System.out.println("inactive ❌");
            }
        }
        return reports;
    }

    private List<String> generateIps(String cidr) {
        List<String> ips = new ArrayList<>();
        try {
            String[] parts = cidr.split("/");
            String baseIp = parts[0];
            int mask = Integer.parseInt(parts[1]);

            // Calcule le nombre d'IPs selon le masque
            int totalIps = (int) Math.pow(2, 32 - mask) - 2;

            String[] octets = baseIp.split("\\.");

            // Convertit l'IP en nombre entier
            long ipLong = (Long.parseLong(octets[0]) << 24)
                        + (Long.parseLong(octets[1]) << 16)
                        + (Long.parseLong(octets[2]) << 8)
                        + Long.parseLong(octets[3]);

            // Génère toutes les IPs
            for (int i = 1; i <= totalIps; i++) {
                long newIp = ipLong + i;
                String ip = ((newIp >> 24) & 0xFF) + "."
                          + ((newIp >> 16) & 0xFF) + "."
                          + ((newIp >> 8)  & 0xFF) + "."
                          + (newIp & 0xFF);
                ips.add(ip);
            }
        } catch (Exception e) {
            System.out.println("Erreur CIDR : " + e.getMessage());
        }
        return ips;
    }
}