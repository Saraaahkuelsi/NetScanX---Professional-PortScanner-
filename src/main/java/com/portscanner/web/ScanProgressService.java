package com.portscanner.web;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;


@Service
public class ScanProgressService {
	private int progress = 0 ;
	private List<String> alerts = new ArrayList<>();
	
	
	
	public void setProgress(int value) {
		this.progress =value ;
		
	}
	public int getProgress() {
		return progress ;
	}
	
	public void reset() {
        this.progress = 0;
        clearAlerts();
    }
	
	public void addAlert(String message) {
	    alerts.add(message);
	}

	public List<String> getAlerts() {
	    return alerts;
	}

	public void clearAlerts() {
	    alerts.clear();
	}
	



}
