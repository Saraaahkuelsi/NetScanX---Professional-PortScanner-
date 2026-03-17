package com.portscanner.scanner;
import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class OfflineServiceDetector implements ServiceDetector {
	private Map<Integer, String> portServices ;
	
	public OfflineServiceDetector () {
		this.portServices = new HashMap<>();
		loadServices();
		
	}
	
	private void loadServices() {
	    try {
	        BufferedReader reader = new BufferedReader(
	            new java.io.FileReader("C:/Users/SARA/eclipse-workspace/portscanner/src/main/resources/nmap-services.txt")
	        );
	        String line;
	        while ((line = reader.readLine()) != null) {
	            if (line.startsWith("#") || line.trim().isEmpty()) {
	                continue;
	            }
	            String[] parts = line.split("\\s+");
	            if (parts.length >= 2) {
	                String serviceName = parts[0];
	                String[] portProtocol = parts[1].split("/");
	                if (portProtocol[1].equalsIgnoreCase("tcp")) {
	                    int port = Integer.parseInt(portProtocol[0]);
	                    portServices.put(port, serviceName);
	                }
	            }
	        }
	        reader.close();
	    } catch (Exception e) {
	        System.out.println("Erreur chargement nmap-services : " + e.getMessage());
	    }
	}
	public String detect(int port) {
		return portServices.getOrDefault(port, "Unknown");
		
		
	}
	

}
