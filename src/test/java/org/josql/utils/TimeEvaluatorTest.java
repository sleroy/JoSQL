package org.josql.utils;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

public class TimeEvaluatorTest {

	private TimeEvaluator timeEvaluator = new TimeEvaluator();
	
	@Test
	public void testTimers() throws InterruptedException {
		
		String label1 = "Timer1";
		String label2 = "Timer2";
		
		Timer timer1 = timeEvaluator.newTimer(label1);
		Timer timer2 = timeEvaluator.newTimer(label2);
		
		timer1.start();
		Thread.sleep(500);
		timer1.stop();
		
		timer2.start();
		Thread.sleep(321);
		timer2.stop();
		
		Map<String, Double> timings = timeEvaluator.getMap();
		
		assertEquals(2, timings.size());
		isEquals(0.50, timings.get(label1));
		isEquals(0.321, timings.get(label2));
		
	}
	
	private void isEquals(final Double d1, final Double d2) {
		
		Double d3 = d2 * 100;
		Double d4 = d1 * 100;
		
		assertEquals(d4.intValue(), d3.intValue());
		
	}
	
}
