package org.josql.csv;

import java.util.Date;

import net.sf.josql.Person;

public class Work {

	private int time;
	private Date date;
	private String worker;
	private String superviser;
	private Person person;
	
	public Person getPerson() {
		return person;
	}

	public void setPerson(final Person _person) {
		person = _person;
	}

	public int getTime() {
		return time;
	}
	
	public void setTime(final int _time) {
		time = _time;
	}
	
	public String getWorker() {
		return worker;
	}
	
	public void setWorker(final String _worker) {
		worker = _worker;
	}

	public String getSuperviser() {
		return superviser;
	}

	public void setSuperviser(final String _superviser) {
		superviser = _superviser;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(final Date _date) {
		date = _date;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		String dateAsString = date != null ? date.toString() : "null";
		
		sb.append(time)
			.append(" - ")
			.append(dateAsString)
			.append(" - ")
			.append(worker)
			.append(" - ")
			.append(superviser)
			.append(" - ")
			.append("[Person: ")
				.append(person != null ? person.toString() : "null")
			.append("]");
		
		return sb.toString();
		
	}
	
}
