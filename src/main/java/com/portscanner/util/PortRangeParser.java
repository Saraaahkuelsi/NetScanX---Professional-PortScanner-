package com.portscanner.util;
import java.util.ArrayList ;
import java.util.List;  

public class PortRangeParser {
	public static List<Integer> parse(String input) {
	    List<Integer> ports = new ArrayList<>();
	    String[] parts = input.split(",");
	    for (String part : parts) {
	        if (part.contains("-")) {
	            String[] range = part.split("-");
	            int start = Integer.parseInt(range[0]);
	            int end = Integer.parseInt(range[1]);
	            for (int i = start; i <= end; i++) {
	                ports.add(i);
	            }
	        } else {
	            ports.add(Integer.parseInt(part));
	        }
	    }
	    return ports;
	}
}
