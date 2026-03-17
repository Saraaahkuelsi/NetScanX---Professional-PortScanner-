package com.portscanner.util;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.portscanner.model.PortResult;

public class OsFingerprinter {

    public static String detect(String host, List<PortResult> results) {
        // Technique 1 — Banner (confiance absolue)
        for (PortResult result : results) {
            if (result.getBanner() != null) {
                String osFromBanner = detectFromBanner(result.getBanner());
                if (!osFromBanner.equals("Unknown")) {
                    return osFromBanner + " (via banner)";
                }
            }
        }
        // Pas de bannière → vote majoritaire
        return voteFromTechniques(host, results);
    }

    private static String voteFromTechniques(String host, List<PortResult> results) {
        Map<String, Integer> votes = new HashMap<>();

        // Vote 1 — Ports
        String fromPorts = detectFromPorts(results);
        if (!fromPorts.equals("Unknown")) {
            votes.put(fromPorts, votes.getOrDefault(fromPorts, 0) + 1);
        }

        // Vote 2 — HTTP
        String fromHttp = detectFromHttpHeaders(host);
        if (!fromHttp.equals("Unknown")) {
            votes.put(fromHttp, votes.getOrDefault(fromHttp, 0) + 1);
        }

        // Vote 3 — TTL
        String fromTtl = detectFromTtl(host);
        if (!fromTtl.equals("Unknown")) {
            votes.put(fromTtl, votes.getOrDefault(fromTtl, 0) + 1);
        }

        // Retourne le plus voté
        return votes.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("Unknown");
    }

    private static String detectFromPorts(List<PortResult> results) {
        for (PortResult result : results) {
            if (!result.isOpen()) continue;
            switch (result.getPort()) {
                case 3389: return "Windows (RDP détecté)";
                case 445:  return "Windows (SMB détecté)";
                case 139:  return "Windows (NetBIOS détecté)";
                case 548:  return "macOS (AFP détecté)";
                case 631:  return "Linux (CUPS détecté)";
            }
        }
        return "Unknown";
    }

    private static String detectFromHttpHeaders(String host) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, 80), 2000);
            socket.setSoTimeout(2000);

            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.println("GET / HTTP/1.1");
            writer.println("Host: " + host);
            writer.println("Connection: close");
            writer.println();
            writer.flush();

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().startsWith("server:")) {
                    String serverHeader = line.substring(7).trim();
                    String os = detectFromBanner(serverHeader);
                    socket.close();
                    return os.equals("Unknown") ? serverHeader : os;
                }
            }
            socket.close();
        } catch (Exception e) {}
        return "Unknown";
    }

    private static String detectFromTtl(String host) {
        try {
            Process process = Runtime.getRuntime().exec("ping -n 1 -w 1000 " + host);
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("TTL=")) {
                    int ttl = Integer.parseInt(line.split("TTL=")[1].split(" ")[0]);
                    return getOsFromTtl(ttl);
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Erreur TTL : " + e.getMessage());
        }
        return "Unknown";
    }

    private static String detectFromBanner(String banner) {
        if (banner == null || banner.isEmpty()) return "Unknown";
        banner = banner.toLowerCase();
        if (banner.contains("ubuntu"))    return "Linux Ubuntu";
        if (banner.contains("debian"))    return "Linux Debian";
        if (banner.contains("centos"))    return "Linux CentOS";
        if (banner.contains("fedora"))    return "Linux Fedora";
        if (banner.contains("red hat"))   return "Linux Red Hat";
        if (banner.contains("windows"))   return "Windows";
        if (banner.contains("microsoft")) return "Windows";
        if (banner.contains("iis"))       return "Windows IIS";
        if (banner.contains("darwin"))    return "macOS";
        if (banner.contains("freebsd"))   return "FreeBSD";
        if (banner.contains("apache"))    return "Linux (Apache)";
        if (banner.contains("nginx"))     return "Linux (Nginx)";
        return "Unknown";
    }

    private static String getOsFromTtl(int ttl) {
        if (ttl >= 200) return "Cisco/Network Device";
        if (ttl >= 100) return "Windows";
        if (ttl >= 50)  return "Linux/macOS";
        return "Unknown";
    }
}
