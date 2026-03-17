package com.portscanner.report;
import com.portscanner.model.ScanReport;
import com.portscanner.model.PortResult;
import java.io.FileWriter;
import java.util.stream.Collectors;

public class JsonReportGenerator implements ReportGenerator {
	
	public String generate(ScanReport report) {
		StringBuilder sb =new StringBuilder();
		sb.append("{");
		sb.append("\"host\": \"" + report.getTarget().getHost() + "\",");
		sb.append("\"os\": \"" + report.getTarget().getOperatingSystem() + "\",");
		sb.append("\"scanId\": \"" + report.getScanId() + "\",");
		sb.append("\"startTime\": \"" + report.getStartTime() + "\",");
		long openPorts = report.getResults().stream()
			    .filter(PortResult::isOpen)
			    .count();
	    sb.append("\"openPorts\": " + openPorts);
		sb.append("}");
		return sb.toString() ;
		
	}
	public void export(ScanReport report, String filePath) {
		try {
		String result = generate(report);
	    FileWriter writer = new FileWriter(filePath);
	    writer.write(result);
		writer.close();

		
	} catch (Exception e ) {
		System.out.print("Erreur de genration de Report ");
	}
	}
}


