package com.portscanner.security;

	import java.io.BufferedReader;
	import java.io.InputStreamReader;
	import java.net.HttpURLConnection;
	import java.net.URL;
	import java.util.ArrayList;
	import java.util.List;

	public class OnlineCVEFetcher {

	    private static final String NVD_API_URL = 
	        "https://services.nvd.nist.gov/rest/json/cves/2.0?keywordSearch=";

	    public List<CVE> fetch(String serviceName) {
	        List<CVE> cves = new ArrayList<>();
	        try {
	            URL url = new URL(NVD_API_URL + serviceName);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("GET");
	            connection.setRequestProperty("Accept", "application/json");
	            connection.setConnectTimeout(5000);
	            connection.setReadTimeout(5000);

	            BufferedReader reader = new BufferedReader(
	                new InputStreamReader(connection.getInputStream())
	            );
	            StringBuilder response = new StringBuilder();
	            String line;
	            while ((line = reader.readLine()) != null) {
	                response.append(line);
	            }
	            reader.close();

	            cves = parseCVEs(response.toString(), serviceName);

	        } catch (Exception e) {
	            System.out.println("Erreur API NVD : " + e.getMessage());
	        }
	        return cves;
	    }

	    private List<CVE> parseCVEs(String json, String serviceName) {
	        List<CVE> cves = new ArrayList<>();
	        try {
	            String[] parts = json.split("\"CVE-");
	            for (int i = 1; i < parts.length && i <= 5; i++) {
	                String cveId = "CVE-" + parts[i].split("\"")[0];
	                
	                String description = "N/A";
	                if (parts[i].contains("\"value\":\"")) {
	                    description = parts[i].split("\"value\":\"")[1].split("\"")[0];
	                }

	                double score = 5.0;
	                if (parts[i].contains("\"baseScore\":")) {
	                    try {
	                        score = Double.parseDouble(
	                            parts[i].split("\"baseScore\":")[1].split("[,}]")[0].trim()
	                        );
	                    } catch (Exception e) {}
	                }

	                String severity = score >= 9.0 ? "CRITICAL" :
	                                  score >= 7.0 ? "HIGH" :
	                                  score >= 4.0 ? "MEDIUM" : "LOW";

	                cves.add(new CVE(cveId, description, severity, serviceName, "Check NVD for remediation", score));
	            }
	        } catch (Exception e) {
	            System.out.println("Erreur parsing CVE : " + e.getMessage());
	        }
	        return cves;
	    }
	}


