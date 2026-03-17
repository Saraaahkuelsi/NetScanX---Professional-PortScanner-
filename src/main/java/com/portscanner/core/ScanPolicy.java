package com.portscanner.core;

public class ScanPolicy {
	private int maxPortsPerSecond , connectionTimeout, retryCount ,maxThreads   ;
	private boolean stealthMode , bannerGrabbing ,   serviceDetection   ;  
	private boolean onlineMode; 
	
	
	public ScanPolicy() {
				maxPortsPerSecond = 100;
				connectionTimeout = 1000;
				retryCount = 1;
				stealthMode = false;
				bannerGrabbing = true;
				serviceDetection = true;
				maxThreads = 10 ;
		
	}
	//getters 


	public int getMaxPortsPerSecond() {
		return maxPortsPerSecond;
	}


	public void setMaxPortsPerSecond(int maxPortsPerSecond) {
		this.maxPortsPerSecond = maxPortsPerSecond;
	}


	public int getConnectionTimeout() {
		return connectionTimeout;
	}


	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}


	public int getRetryCount() {
		return retryCount;
	}


	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}


	public int getMaxThreads() {
		return maxThreads;
	}


	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}


	public boolean isStealthMode() {
		return stealthMode;
	}


	public void setStealthMode(boolean stealthMode) {
		this.stealthMode = stealthMode;
	}


	public boolean isBannerGrabbing() {
		return bannerGrabbing;
	}


	public void setBannerGrabbing(boolean bannerGrabbing) {
		this.bannerGrabbing = bannerGrabbing;
	}


	public boolean isServiceDetection() {
		return serviceDetection;
	}


	public void setServiceDetection(boolean serviceDetection) {
		this.serviceDetection = serviceDetection;
	}


	public boolean isOnlineMode() {
		return onlineMode;
	}


	public void setOnlineMode(boolean onlineMode) {
		this.onlineMode = onlineMode;
	}
	

}
