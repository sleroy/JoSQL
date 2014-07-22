package org.josql.csv;

import java.util.List;
import java.util.Map;

public interface CsvFileDescriptor {

	public Class<?> getRowClass();
	
	public Map<Class<?>, StringConverter<?>> getConverters();
	
	public List<String> getColumnMapping();
	
}
