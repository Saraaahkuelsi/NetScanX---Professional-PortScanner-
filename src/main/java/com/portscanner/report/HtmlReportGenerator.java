package com.portscanner.report;
import com.portscanner.model.ScanReport;
import com.portscanner.model.PortResult;
import java.io.FileWriter;

public class HtmlReportGenerator implements ReportGenerator {

    public String generate(ScanReport report) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head>");
        sb.append("<title>Port Scanner Report</title>");
        sb.append("<style>");
        sb.append("body { font-family: Arial, sans-serif; background: #1a1a2e; color: #eee; padding: 20px; }");
        sb.append("h1 { color: #00d4ff; text-align: center; }");
        sb.append(".info { background: #16213e; padding: 15px; border-radius: 8px; margin-bottom: 20px; }");
        sb.append("table { width: 100%; border-collapse: collapse; }");
        sb.append("th { background: #0f3460; color: #00d4ff; padding: 12px; text-align: left; }");
        sb.append("td { padding: 10px; border-bottom: 1px solid #333; }");
        sb.append("tr:hover { background: #16213e; }");
        sb.append(".CRITICAL { color: #ff4444; font-weight: bold; }");
        sb.append(".HIGH { color: #ff8800; font-weight: bold; }");
        sb.append(".MEDIUM { color: #ffcc00; }");
        sb.append(".LOW { color: #00cc44; }");
        sb.append(".OPEN { color: #00cc44; font-weight: bold; }");
        sb.append(".CLOSED { color: #888888; }");
        sb.append(".FILTERED { color: #ff00ff; font-weight: bold; }");
        sb.append("</style></head><body>");

        sb.append("<h1>🔍 Port Scanner Report</h1>");

        sb.append("<div class='info'>");
        sb.append("<p><b>Host :</b> " + report.getTarget().getHost() + "</p>");
        sb.append("<p><b>Scan ID :</b> " + report.getScanId() + "</p>");
        sb.append("<p><b>Date :</b> " + report.getStartTime() + "</p>");
        sb.append("<p><b>OS détecté :</b> " + report.getTarget().getOperatingSystem() + "</p>");

        long openCount = report.getResults().stream()
            .filter(PortResult::isOpen).count();
        long filteredCount = report.getResults().stream()
            .filter(r -> "FILTERED".equals(r.getState())).count();

        sb.append("<p><b>Ports ouverts :</b> " + openCount + "</p>");
        sb.append("<p><b>Ports filtrés :</b> " + filteredCount + "</p>");
        sb.append("</div>");

        sb.append("<table>");
        sb.append("<tr><th>Port</th><th>Protocol</th><th>State</th><th>Service</th><th>Banner</th><th>Risk</th><th>CVEs</th><th>Response</th></tr>");

        for (PortResult result : report.getResults()) {
            if (result.isOpen() || "FILTERED".equals(result.getState())) {
                String risk = result.getRiskLevel() != null ? result.getRiskLevel() : "LOW";
                String state = result.getState();
                sb.append("<tr>");
                sb.append("<td>" + result.getPort() + "</td>");
                sb.append("<td>" + result.getProtocol() + "</td>");
                sb.append("<td class='" + state + "'>" + state + "</td>");
                sb.append("<td>" + (result.getServiceName() != null ? result.getServiceName() : "-") + "</td>");
                sb.append("<td>" + (result.getBanner() != null ? result.getBanner() : "-") + "</td>");
                sb.append("<td class='" + risk + "'>" + risk + "</td>");
                sb.append("<td>" + (result.getVulnerabilities().isEmpty() ? "-" : result.getVulnerabilities()) + "</td>");
                sb.append("<td>" + result.getResponseTimeMs() + "ms</td>");
                sb.append("</tr>");
            }
        }

        sb.append("</table></body></html>");
        return sb.toString();
    }

    public void export(ScanReport report, String filePath) {
        try {
            String result = generate(report);
            FileWriter writer = new FileWriter(filePath);
            writer.write(result);
            writer.close();
        } catch (Exception e) {
            System.out.println("Erreur génération rapport HTML : " + e.getMessage());
        }
    }
}
