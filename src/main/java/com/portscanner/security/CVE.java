package com.portscanner.security;

public class CVE {
	private String cveId , description , severity ,   affectedService   , remediation   ;
	private double cvssScore  ;  

public CVE (String cveId , String description , String severity ,  String  affectedService   ,String  remediation , double cvssScore ) {
	this.cveId= cveId ;
	this.description = description ;
	this.severity =severity ;
	this.affectedService = affectedService ;
	this.remediation =remediation ;
	this.cvssScore = cvssScore ;
	
}
//getters & setters 

public String getCveId() {
	return cveId;
}

public void setCveId(String cveId) {
	this.cveId = cveId;
}

public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}

public String getSeverity() {
	return severity;
}

public void setSeverity(String severity) {
	this.severity = severity;
}

public String getAffectedService() {
	return affectedService;
}

public void setAffectedService(String affectedService) {
	this.affectedService = affectedService;
}

public String getRemediation() {
	return remediation;
}

public void setRemediation(String remediation) {
	this.remediation = remediation;
}

public double getCvssScore() {
	return cvssScore;
}

public void setCvssScore(double cvssScore) {
	this.cvssScore = cvssScore;
}

@Override 
   public String toString () {
	return ("["+ cveId + "]" + severity + "(" + cvssScore + ")" + "-" +  affectedService);
	
}


}
