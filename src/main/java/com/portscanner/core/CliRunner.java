package com.portscanner.core;

import com.portscanner.security.AuthorizationManager;
import com.portscanner.security.Auditlogger;
import com.portscanner.scanner.SubnetScanner;
import com.portscanner.model.ScanTarget;
import com.portscanner.model.ScanReport;
import com.portscanner.model.PortResult;
import com.portscanner.report.HtmlReportGenerator;
import com.portscanner.report.JsonReportGenerator;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class CliRunner {

    private static boolean isOnline() {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("google.com", 80), 3000);
            socket.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Port Scanner ===");

        // Détection automatique du mode
        boolean online = isOnline();
        if (online) {
            System.out.println("Mode : ONLINE ✅");
        } else {
            System.out.println("Mode : OFFLINE ❌ (pas de connexion internet)");
        }

        // Saisie utilisateur
        Scanner input = new Scanner(System.in);

        System.out.print("Entrez l'hôte à scanner : ");
        String host = input.nextLine();

        System.out.print("Port de début : ");
        int startPort = Integer.parseInt(input.nextLine());

        System.out.print("Port de fin : ");
        int endPort = Integer.parseInt(input.nextLine());

        System.out.print("Mode furtif ? (o/n) : ");
        boolean stealth = input.nextLine().equalsIgnoreCase("o");

        System.out.print("Scanner un sous-réseau ? (o/n) : ");
        boolean subnetMode = input.nextLine().equalsIgnoreCase("o");

        // Configuration commune
        Auditlogger logger = new Auditlogger("audit.log");
        ScanPolicy policy = new ScanPolicy();
        policy.setConnectionTimeout(2000);
        policy.setStealthMode(stealth);
        policy.setOnlineMode(online);

        if (subnetMode) {
            // Mode sous-réseau
            System.out.print("Entrez le CIDR (ex: 192.168.1.0/24) : ");
            String cidr = input.nextLine();

            SubnetScanner subnetScanner = new SubnetScanner(policy, logger, startPort, endPort);
            List<ScanReport> reports = subnetScanner.scan(cidr);

            System.out.println("\n=== Résultats Sous-Réseau ===");
            System.out.println("Total machines actives : " + reports.size());
            for (ScanReport r : reports) {
                long openCount = r.getResults().stream()
                    .filter(PortResult::isOpen).count();
                System.out.println("→ " + r.getTarget().getHost()
                    + " | OS: " + r.getTarget().getOperatingSystem()
                    + " | Ports ouverts: " + openCount);
            }

        } else {
            // Mode scan normal
            AuthorizationManager auth = new AuthorizationManager(host, "Operateur");
            auth.GrantConsent();
            if (!auth.verifyLegalConsent()) {
                System.out.println("Scan non autorisé !");
                input.close();
                return;
            }

            ScanTarget target = new ScanTarget(host, startPort, endPort);
            ScanEngine engine = new ScanEngine(policy, logger);

            System.out.println("\n🔍 Scan en cours...\n");
            ScanReport report = engine.scan(target);

            System.out.println("\n=== Résultats ===");
            int openCount = 0;
            int filteredCount = 0;
            for (PortResult result : report.getResults()) {
                if (result.isOpen() || result.getState().equals("FILTERED")) {
                    System.out.println(result.toString());
                    if (result.isOpen()) openCount++;
                    else filteredCount++;
                }
            }

            System.out.println("\nOS détecté : " + report.getTarget().getOperatingSystem());
            System.out.println("Total ports ouverts : " + openCount);
            System.out.println("Total ports filtrés : " + filteredCount);

            new HtmlReportGenerator().export(report, "rapport.html");
            new JsonReportGenerator().export(report, "rapport.json");
            System.out.println("Rapports générés : rapport.html, rapport.json");

            engine.shutdown();
        }

        input.close();
    }
}
