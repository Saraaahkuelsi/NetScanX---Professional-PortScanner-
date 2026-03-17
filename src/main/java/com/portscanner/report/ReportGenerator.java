package com.portscanner.report;
import com.portscanner.model.ScanReport;

public interface ReportGenerator {
	String generate(ScanReport report);
	void export(ScanReport report, String filePath);

}
