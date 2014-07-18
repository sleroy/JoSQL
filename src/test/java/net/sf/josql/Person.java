package net.sf.josql;

public class Person {
	
	private String name;
	private String firstName;
	
	public Person(final String _name, final String _firstName) {
		name = _name;
		firstName = _firstName;
	}
	
	public String getName() {
		return name;
	}
	public void setName(final String _name) {
		name = _name;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(final String _firstName) {
		firstName = _firstName;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(name).append(" - ").append(firstName);
		return sb.toString();
	}
}
