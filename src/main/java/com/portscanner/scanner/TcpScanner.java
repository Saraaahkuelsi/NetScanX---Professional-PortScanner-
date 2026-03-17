package com.portscanner.scanner;
import com.portscanner.model.PortResult;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import com.portscanner.core.ScanPolicy; 

import java.net.Socket;
import java.net.InetSocketAddress;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TcpScanner implements  Scanner {
	private int connectionTimeout  ;
	private boolean grabBanner ;
	private ServiceDetector serviceDetector;
	private ScanPolicy policy;
	
	
	public TcpScanner(ScanPolicy policy) {
	    this.policy = policy;
	    this.connectionTimeout = policy.getConnectionTimeout();
	    this.grabBanner = policy.isBannerGrabbing();
	    this.serviceDetector = new OfflineServiceDetector();
	}
	
	public PortResult scan(String host, int port) {
	    PortResult result = new PortResult(host, port, "TCP");
	    boolean connected = false;

	    for (int i = 0; i <= policy.getRetryCount(); i++) {
	        try {
	            Socket socket = new Socket();
	            long start = System.currentTimeMillis();
	            socket.connect(new InetSocketAddress(host, port), connectionTimeout);
	            long end = System.currentTimeMillis();
	            
	            connected = true;
	            result.setResponseTimeMs(end - start);
	            result.setServiceName(serviceDetector.detect(port));

	            if (grabBanner) {
	                try {
	                    socket.setSoTimeout(2000);
	                    BufferedReader reader = new BufferedReader(
	                        new InputStreamReader(socket.getInputStream())
	                    );
	                    String banner = reader.readLine();
	                    if (banner != null) result.setBanner(banner);
	                } catch (Exception e) {}
	            }
	            socket.close();
	            break;
	        } catch (ConnectException e) {
	                // port fermé — réponse rapide de refus
	                result.setState("CLOSED");
	                break; // inutile de réessayer
	            }
	            catch (SocketTimeoutException e) {
	                // firewall — pas de réponse dans le délai
	                result.setState("FILTERED");
	                result.setServiceName(serviceDetector.detect(port));
	                break; // inutile de réessayer
	            }
	            catch (Exception e) {
	                // autre erreur — on réessaye
	            }
	    }

	    if (connected) {
	        result.setState("OPEN");
	    } else if (result.getState() == null) {
	        result.setState("CLOSED");
	    }
	    return result;
	}
	
	public boolean isAvailable() {
	    return true;
	}
}
