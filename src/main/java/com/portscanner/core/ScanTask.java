package com.portscanner.core;
import com.portscanner.model.PortResult;
import com.portscanner.scanner.*;
import java.util.concurrent.Callable;



	public class ScanTask implements Callable<PortResult> {
		private String host      ;  
		private int port     ;
		private Scanner scanner  ;
		private ScanPolicy policy     ;
		
		public ScanTask (String host, int port, Scanner scanner, ScanPolicy policy) {
			this.host =host ;
			this.scanner= scanner ;
			this.policy = policy ;
			this .port=port ;
			
		}
		
		public PortResult call() throws Exception {
		    if (policy.isStealthMode()) {
		        long delay = 100 + (long)(Math.random() * 400);
		        Thread.sleep(delay);
		    }
		    return scanner.scan(host, port);
		}
		
		
	}


