package com.datastax.order.demo.analytics;

import java.util.LinkedHashMap;
import java.util.Map;

import org.mortbay.log.Log;

public class MovingAverageTotalPrice {
	
	private Map<Long, Double> timeUnitTotalMap = new LinkedHashMap<Long, Double>();
	private Map<Long, Double> movingAverageMap = new LinkedHashMap<Long, Double>();
	private Double[] slidingWindow;
	
	private int windowLocation = 0;
	private int windowSize;
	private long oldTime = -1;
	
	public MovingAverageTotalPrice(int windowSize){
		this.windowSize = windowSize;
		this.slidingWindow = new Double[this.windowSize];
	}
	
	public void processOrder(long timeValue, double totalPrice){
		
		if (oldTime == -1){ oldTime = timeValue; }
			
		if (timeUnitTotalMap.containsKey(timeValue)){
			
			double runningTotal = this.timeUnitTotalMap.get(timeValue);
			runningTotal = runningTotal + totalPrice;
			this.timeUnitTotalMap.put(timeValue, runningTotal);
		}else{
			this.timeUnitTotalMap.put(timeValue, totalPrice);
		}
		
		//We have a new value so move the old value to the window. 
		if (oldTime != timeValue){
			Double totalForOldTime = this.timeUnitTotalMap.get(oldTime);
			
			slidingWindow[windowLocation++ % windowSize] = new Double(totalForOldTime);
		
			if (windowLocation >= windowSize){
				double movingAverage = sumWindow();
			
				movingAverageMap.put(oldTime, movingAverage);
			}
			
			//Clean up by removing obsolete old times.
			timeUnitTotalMap.remove(oldTime);
		}	
		
		this.oldTime = timeValue;
	}

	private double sumWindow() {
		double movingAverage = 0;
		
		for (Double totalForTime : this.slidingWindow){
			movingAverage = movingAverage + totalForTime;
		}
		return movingAverage/this.windowSize;
	}

	public double getLastMovingAverage() {
		if (!movingAverageMap.containsKey(oldTime-1)){
			return -1;
		}
		return movingAverageMap.get(oldTime-1);		
	}
}