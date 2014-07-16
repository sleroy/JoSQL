package net.sf.josql;

public class Work {
	
	private Person worker;
	private Person superviser;
	private int time;
	
	public Work(final Person _worker, final Person _superviser, final int _time) {
		worker = _worker;
		superviser = _superviser;
		time = _time;
	}
	
	public Person getWorker() {
		return worker;
	}
	public void setWorker(final Person _worker) {
		worker = _worker;
	}
	public Person getSuperviser() {
		return superviser;
	}
	public void setSuperviser(final Person _superviser) {
		superviser = _superviser;
	}
	public int getTime() {
		return time;
	}
	public void setTime(final int _time) {
		time = _time;
	}
	
}
