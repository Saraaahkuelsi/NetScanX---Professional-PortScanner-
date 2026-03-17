package com.portscanner.security;
import java.time.LocalDateTime ;

public class AuthorizationManager {
	private String targetHost , operatorName  ;
	private boolean consentGranted   ;  
	private  LocalDateTime consentTimestamp ;
	
	public AuthorizationManager(String targetHost , String operatorName ) {
		consentGranted =false ;
		this.targetHost= targetHost ;
		this.operatorName =  operatorName ;
		
	}
	public void  GrantConsent() {
		consentGranted =true ;
		consentTimestamp = LocalDateTime.now() ;
		
		
	}
	public boolean isAuthorized() {
		return consentGranted ;
	}
	public boolean  verifyLegalConsent() {
		if (consentGranted) {
			return true ;
			
		} else {
			System.out.print(" AVERTISSEMENT : Scan non autorisé. Vous devez donner votre consentement avant de scanner " + targetHost);
			return false ;
		}
	}
	//getters & setters 
	public String getTargetHost() {
		return targetHost;
	}
	public void setTargetHost(String targetHost) {
		this.targetHost = targetHost;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public boolean isConsentGranted() {
		return consentGranted;
	}
	public void setConsentGranted(boolean consentGranted) {
		this.consentGranted = consentGranted;
	}
	public LocalDateTime getConsentTimestamp() {
		return consentTimestamp;
	}
	public void setConsentTimestamp(LocalDateTime consentTimestamp) {
		this.consentTimestamp = consentTimestamp;
	}
	
}
