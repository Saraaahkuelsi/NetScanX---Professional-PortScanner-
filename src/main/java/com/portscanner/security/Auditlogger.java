package com.portscanner.security;
import java.util.List ;
import java.util.ArrayList ;
import java.util.UUID;
import java.time.LocalDateTime;

public class Auditlogger {
	private String sessionId , logFile ; 
	private List<String> logs ;
	
	
	public Auditlogger(String logFile ) {
		this.logFile=logFile ;
		this.logs = new ArrayList<>() ;
		this.sessionId = UUID.randomUUID().toString() ;
		
		
	}
	public void log(String message) {
	    String entry = "[" + LocalDateTime.now() + "] [" + sessionId + "] " + message;
	    logs.add(entry);
	    System.out.println(entry);
	}
	public void logScanStart(String host) {
		log("Scan started on "+ host) ;
		
	}
	public void logScanEnd(String host) {
		log("Scan ended  on "+ host) ;
		
	}
	 
	public void logOpenPort(String host, int port) {
		log("Open port found: " + port + " on " + host);
		
	}
	
	public List<String> getLogs() {
		return logs ;
	}
}
