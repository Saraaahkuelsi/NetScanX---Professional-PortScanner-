package com.portscanner.model;
import java.time.LocalDateTime ;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List ;
import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScanReport {
	private String scanId , exportedBy  , globalRisk   ; 
	private ScanTarget target  ;   
	private List<PortResult> results    ;     
	private  LocalDateTime startTime ;
	private LocalDateTime endTime  ;       
	private int totalPorts  ;
	private int openPorts   ;   
	
	public ScanReport() {
	}
	
	public ScanReport(ScanTarget target , String exportedBy ){
		startTime = LocalDateTime.now() ;
		scanId = UUID.randomUUID().toString() ;
		this.exportedBy =exportedBy ;
		this.target =target ;
		this.results = new ArrayList<>() ;
		
	}
//getters et setters 
	public String getScanId() {
		return scanId;
	}
	public void setScanId(String scanId) {
		this.scanId = scanId;
	}
	public String getExportedBy() {
		return exportedBy;
	}
	public void setExportedBy(String exportedBy) {
		this.exportedBy = exportedBy;
	}
	public String getGlobalRisk() {
		return globalRisk;
	}
	public void setGlobalRisk(String globalRisk) {
		this.globalRisk = globalRisk;
	}
	public ScanTarget getTarget() {
		return target;
	}
	public void setTarget(ScanTarget target) {
		this.target = target;
	}
	public List<PortResult> getResults() {
		return results;
	}
	public void setResults(List<PortResult> results) {
		this.results = results;
	}
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	public int getTotalPorts() {
		return totalPorts;
	}
	public void setTotalPorts(int totalPorts) {
		this.totalPorts = totalPorts;
	}
	public int getOpenPorts() {
		return openPorts;
	}
	public void setOpenPorts(int openPorts) {
		this.openPorts = openPorts;
	}
	

}
