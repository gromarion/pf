package com.itba.automatic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class Latency {
	
	public static String check(String hostaddr, int port, int times) {
		int total = 0;
		long totalping = 0;
		Socket s = null;
		while(total < times) {
			total++;
			long start = System.currentTimeMillis();
			try {
				SocketAddress sockaddr = new InetSocketAddress(hostaddr, port);
				s = new Socket();
				s.connect(sockaddr, 1000);
			} catch(SocketTimeoutException e) {
				System.out.println("Socket Request["+total+"]: Connection timed out.");
				continue;
			} catch(UnknownHostException e) {
			} catch(IOException e) {
			}
			long end = System.currentTimeMillis();
			totalping += (end-start);
			long totaltime = (end-start);
			long avg = (long)(totalping/total);
			System.out.println("Socket Request["+total+"]: Time(In MS): "+totaltime+" Average: "+avg);
		}
		long avg = (long)(totalping/total);
		return "\nFinal Result: Average request - " + avg;
	}
}
