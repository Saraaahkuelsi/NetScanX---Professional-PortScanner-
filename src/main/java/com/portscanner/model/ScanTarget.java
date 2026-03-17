package com.portscanner.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)

public class ScanTarget {

	private String   host  , ipAddress   ;
	private boolean isReachable   ;   
	private String operatingSystem   ; 
	private int startPort ; 
	private int endPort   ;  
	private String scanType ;   
	
	public ScanTarget() {
	}
	
	public ScanTarget (String host ,int startPort ,int endPort ) {
		isReachable =false ;
		scanType ="tcp" ;
		this.endPort= endPort  ;
		this.startPort= startPort  ;
		this.host=host ;
	}
	//getters et setters 

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public boolean isReachable() {
		return isReachable;
	}

	public void setReachable(boolean isReachable) {
		this.isReachable = isReachable;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public int getStartPort() {
		return startPort;
	}

	public void setStartPort(int startPort) {
		this.startPort = startPort;
	}

	public int getEndPort() {
		return endPort;
	}

	public void setEndPort(int endPort) {
		this.endPort = endPort;
	}

	public String getScanType() {
		return scanType;
	}

	public void setScanType(String scanType) {
		this.scanType = scanType;
	}
	
}
