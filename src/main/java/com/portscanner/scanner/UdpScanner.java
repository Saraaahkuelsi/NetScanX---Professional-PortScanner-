package com.portscanner.scanner;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import com.portscanner.model.PortResult;

public class UdpScanner implements Scanner {
	private int timeout ;
	public UdpScanner(int timeout ){
		this.timeout =timeout ;
		
	}
	public PortResult  scan(String host , int port ) {
		PortResult result =  new PortResult(host,port ,"UDP") ;
		try {
			DatagramSocket sc = new DatagramSocket() ;
			sc.setSoTimeout(timeout );
			
			 byte[] buffer = new byte[0];
		     InetAddress address = InetAddress.getByName(host);
		     DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
		     sc.send(packet);
		     
		     byte[] responseBuffer = new byte[1024];
		     DatagramPacket response = new DatagramPacket(responseBuffer, responseBuffer.length);
		     sc.receive(response);
		        
		     result.setState("OPEN");
		     sc.close();
	
			
		}catch (Exception e) {
	        result.setState("FILTERED");
		
	}
		return result ;
	}
		
	public boolean isAvailable() {
		return true ;
	}
	

}
