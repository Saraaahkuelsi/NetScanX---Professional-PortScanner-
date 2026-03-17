package com.portscanner.scanner;
import com.portscanner.model.PortResult;

public interface Scanner {
	PortResult scan(String host, int port)  ;
	boolean isAvailable() ;
		
	

}
