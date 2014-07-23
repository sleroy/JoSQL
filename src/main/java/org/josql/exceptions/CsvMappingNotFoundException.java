package org.josql.exceptions;

public class CsvMappingNotFoundException extends Exception {

	private static final long serialVersionUID = -5732820541717056947L;
	
	public CsvMappingNotFoundException() {
		
		super("No Mapping information found for"
				+ " matching the CSV columns to the java objects properties");
		
	}

}
