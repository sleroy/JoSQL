package org.josql.utils;

import java.util.Map;

import com.google.common.collect.Maps;

public class TimeEvaluator {

	private Map<String, Double> timings;
	
	public TimeEvaluator() {		
		timings = Maps.newLinkedHashMap();		
	}
	
	public Timer newTimer(final String _label) {		
		return new Timer(_label, this);	
	}
	
	public Map<String, Double> getMap() {
		return timings;
	}
	
	public void record(final Timer _timer) {
		Double s = _timer.getTime() / 1000.00;
		timings.put(_timer.getLabel(), s);
	}
	
}
