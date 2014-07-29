package org.josql.csv;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

public class FileDescriptor implements CsvFileDescriptor {

	private Class<?> clazz;
	private Map<Class<?>, StringConverter<?>> converters;
	private List<String> columnMapping;
	private CsvOptions options;
	
	public FileDescriptor(final Class<?> _class, final List<String> _columnMapping, final CsvOptions _options) {
		this(_class, _columnMapping);
		setOptions(_options);
	}
	
	public FileDescriptor(final Class<?> _class, final List<String> _columnMapping) {
		clazz = _class;
		converters = Maps.newHashMap();
		columnMapping = _columnMapping;
	}
	
	public void setOptions(final CsvOptions _options) {
		options = _options;
	}
	
	public void addConverter(final Class<?> _class, final StringConverter<?> _converter) {
		converters.put(_class, _converter);
	}
	
	public Class<?> getRowClass() {
		return clazz;
	}

	public Map<Class<?>, StringConverter<?>> getConverters() {
		return converters;
	}

	public List<String> getColumnMapping() {
		return columnMapping;
	}

	public CsvOptions getOptions() {
		return options;
	}
	
}