package com.itba.automatic;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;

public class ScalabilityChecker {
	public static void requestsPerSecond(String uri, int requests, long timeout) {
		AtomicInteger responsesReceived = new AtomicInteger(0);
		AtomicLong responseTimes = new AtomicLong(0);
		final long startTime = System.currentTimeMillis();
		long oneRequestTime = -1;
		for (int i = 0; i < requests; i++) {
			bla(uri, responsesReceived, responseTimes, startTime);
			if (i == 0) {
				oneRequestTime = responseTimes.get();
			}
		}
		while (System.currentTimeMillis() - startTime < timeout && responsesReceived.get() < requests);
		System.out.println("AVG = " + responseTimes.get() / responsesReceived.get() + " milliseconds");
		System.out.println("ONE REQUEST = " + oneRequestTime + " milliseconds");
	}
	
	private static void bla(String uri, final AtomicInteger responsesReceived, final AtomicLong responseTimes, final long startTime) {
		Unirest.get(uri).header("accept", "application/json").asBinaryAsync(new Callback<InputStream>() {
		    public void failed(UnirestException e) {
		        System.out.println(e.getMessage());
		    }

		    public void completed(HttpResponse<InputStream> response) {
		    	System.out.println("COMPLETED!");
		    	responsesReceived.incrementAndGet();
		    	responseTimes.addAndGet(System.currentTimeMillis() - startTime);
		    }

		    public void cancelled() {
		    	System.out.println("CANCEL");
		    }
		});
	} 
}
