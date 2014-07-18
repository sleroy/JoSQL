package org.josql.csv;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateConverter extends StringConverter<Date> {

	private SimpleDateFormat formatter;
	
	public SimpleDateConverter(final String pattern) {
		
		formatter = new SimpleDateFormat(pattern);
		
	}
	
	@Override
	public Date convertValue(final String _value) {
		
		try {
			
			return formatter.parse(_value);
			
		} catch (ParseException e) {
			
			return null;
			
		}
		
	}

}
