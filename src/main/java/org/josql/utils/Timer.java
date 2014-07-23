package org.josql.utils;


public class Timer {

	private TimeEvaluator parent;
	private String label;
	private long t0;
	private long t1;
	
	public Timer(final String _label, final TimeEvaluator _parent) {
		
		label = _label;
		parent = _parent;
		
	}
	
	public void start() {
		
		t0 = System.currentTimeMillis();
		
	}
	
	public void stop() {
		
		t1 = System.currentTimeMillis();
		
		if (parent != null) {
			parent.record(this);
		}
		
	}
	
	public String getLabel() {
		
		return label;
		
	}
	
	public long getTime() {
		
		return t1 - t0;
		
	}
	
	
}
