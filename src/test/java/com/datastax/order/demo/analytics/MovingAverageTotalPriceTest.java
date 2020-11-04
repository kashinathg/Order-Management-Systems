package com.datastax.order.demo.analytics;

import org.junit.Assert;
import org.junit.Test;

import com.datastax.order.demo.analytics.MovingAverageTotalPrice;


public class MovingAverageTotalPriceTest {

	private static final double DELTA = 0.0000001;

	@Test
	public void testHourUnit(){
		
		MovingAverageTotalPrice hourMA = new MovingAverageTotalPrice(5);
		
		hourMA.processOrder(0, 123.1);
		hourMA.processOrder(0, 101.1);
		hourMA.processOrder(1, 12.1);
		hourMA.processOrder(2, 13.1);
		hourMA.processOrder(3, 45);
		hourMA.processOrder(3, 23);
		hourMA.processOrder(3, 92);
		hourMA.processOrder(4, 29);
		hourMA.processOrder(5, 232);
		hourMA.processOrder(6, 102);
		hourMA.processOrder(7, 103);
		hourMA.processOrder(8, 20);
		hourMA.processOrder(8, 62);
		hourMA.processOrder(9, 128.1);
				
		double lastMA = hourMA.getLastMovingAverage();
		
		Assert.assertEquals(109.6, lastMA, DELTA);
	}
	
}
