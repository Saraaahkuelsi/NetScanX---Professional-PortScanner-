package com.portscanner.util;
import java.net.InetAddress;


public class NetworksUtils {
	
	public static Boolean  isValidHost(String host) {
		if (host != null && !host.isEmpty()) {
			return true ;
			
		}
		return false ;
		
	}
	public static Boolean isPrivateRang( String ip ) {
		if (ip.startsWith("192.168.") || ip.startsWith("10.") || ip.startsWith("172.") ) {
			return true ;
		}
		return false ;
	}
	
	public static Boolean isReachable(String host, int timeout) {
		try {
		return InetAddress.getByName(host).isReachable(timeout) ;
	} catch (Exception e) {
			 return false ;
		}
			
		}
	public static String  resolveHost(String host){
		try {
			return InetAddress.getByName(host).getHostAddress();
	} catch (Exception e ) {
		return null ;
	}
}
}
