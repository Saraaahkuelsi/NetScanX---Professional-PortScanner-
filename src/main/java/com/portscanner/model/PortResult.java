package com.portscanner.model;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class PortResult {
	private  String host;
	private int port;
	private String protocol;
	private String state;
    private String serviceName;
	private String banner;
	private String riskLevel;
	private long responseTimeMs;
	private List<String> vulnerabilities;
	private LocalDateTime scannedAt;
	
	public PortResult() {
	}

    // ✅ Constructeur corrigé (pas de "void", initialisation des champs)
    public PortResult(String host, int port, String protocol) {
        this.host = host;
        this.port = port;
        this.protocol = protocol;
        this.vulnerabilities = new ArrayList<>();
        this.scannedAt = LocalDateTime.now();
    }

    // Getters & Setters
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getBanner() { return banner; }
    public void setBanner(String banner) { this.banner = banner; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public long getResponseTimeMs() { return responseTimeMs; }
    public void setResponseTimeMs(long responseTimeMs) { this.responseTimeMs = responseTimeMs; }

    public List<String> getVulnerabilities() { return vulnerabilities; }
    public void setVulnerabilities(List<String> vulnerabilities) { this.vulnerabilities = vulnerabilities; }

    public LocalDateTime getScannedAt() { return scannedAt; }
    public void setScannedAt(LocalDateTime scannedAt) { this.scannedAt = scannedAt; }

    // ✅ Méthodes métier simplifiées
    public boolean isOpen() {
        return "OPEN".equals(this.state);
    }

    public boolean hasVulnerabilities() {
        return vulnerabilities != null && !vulnerabilities.isEmpty();
    }

    // ✅ toString() complété
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[" + protocol + "] ");
        sb.append(host + ":" + port);
        sb.append("- " + state);
        sb.append(" Service :" + serviceName);
        sb.append(" (" + responseTimeMs + "ms)");
        if (riskLevel != null) sb.append(" - Risk: " + riskLevel);
        if (banner != null) {
            sb.append(" - Banner: " + banner);
        }
        if (!vulnerabilities.isEmpty()) {
            sb.append(" - CVEs: " + vulnerabilities);
        }
        return sb.toString();
    }
}