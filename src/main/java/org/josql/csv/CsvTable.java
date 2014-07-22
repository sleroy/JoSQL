package org.josql.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.josql.exceptions.CsvMappingNotFoundException;
import org.josql.exceptions.QueryParseException;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;

import com.google.common.collect.Maps;

public class CsvTable {

	private FileReader file;
	private Class<?> pojoClass;
	private List<Object> objects;
	private Map<Class<?>, StringConverter<?>> converters;
	private List<String> columnMapping;
	
	private CsvOptions options;
	
	public CsvTable(final FileReader _csvFile, final Class<?> _pojoClass) {
		
		file = _csvFile;
		pojoClass = _pojoClass;
		converters = Maps.newHashMap();
		columnMapping = Collections.emptyList();
		options = new CsvOptions();
		
	}
	
	public CsvTable(final FileReader _csvFile, final CsvFileDescriptor _descriptor) {
		
		this(_csvFile, _descriptor.getRowClass());
		setConverters(_descriptor.getConverters());
		setColumnMapping(_descriptor.getColumnMapping());
		
	}
	
	public CsvTable(final String _csvFile, final Class<?> _pojoClass) throws FileNotFoundException {
		
		this(new FileReader(_csvFile), _pojoClass);
			
	}	
	
	public CsvTable(final String _csvFile, final CsvFileDescriptor _descriptor) throws FileNotFoundException {
		
		this(new FileReader(_csvFile), _descriptor);
		
	}
	
	public CsvTable(final File _csvFile, final Class<?> _pojoClass) throws FileNotFoundException {
		
		this(new FileReader(_csvFile), _pojoClass);
		
	}
	
	public CsvTable(final File _csvFile, final CsvFileDescriptor _descriptor) throws FileNotFoundException {
		
		this(new FileReader(_csvFile), _descriptor);
		
	}
	
	/**
	 * Define options for parsing the CSV file
	 * @param csv_char_separator char used as a separator between two fields
	 * @param csv_char_quote char used as a quote
	 * @param csv_first_line number of the first line to parse
	 */
	public void setOptions(final char csv_char_separator, final char csv_char_quote, final int csv_first_line) {
		
		setOptions(new CsvOptions(csv_char_separator, csv_char_quote, csv_first_line));
		
	}
	
	/**
	 * Define options for parsing the CSV file
	 * @param _options
	 */
	public void setOptions(final CsvOptions _options) {
		
		options = _options;
		
	}
	
	/**
	 * Define a converter that will be used for the creation
	 * the java objects from the CSV file
	 * @param _class
	 * @param _converter 
	 */
	public void setConverter(final Class<?> _class, final StringConverter<?> _converter) {
		
		converters.put(_class, _converter);
		
	}
	
	/**
	 * Defines the converters that will be used for the
	 * creation of the java objects from the CSV file
	 * @param _converters
	 */
	public void setConverters(final Map<Class<?>, StringConverter<?>> _converters) {
		
		for(Class<?> clazz : _converters.keySet()) {		
			setConverter(clazz, _converters.get(clazz));			
		}
		
	}
	
	/**
	 * Defines the properties of the java class that will be matched
	 * to the columns of the CSV file
	 * (in the same order that they appear in the CSV file)
	 * @param _columnMapping List of properties
	 */
	public void setColumnMapping(final List<String> _columnMapping) {
		
		columnMapping = _columnMapping;
		
	}
	
	/**
	 * Read the CSV file and convert each row into a java object
	 * @exception Throws an exception if the column mapping information has not been initialized
	 * @return list of java objects generated from the CSV file
	 * @throws CsvMappingNotFoundException
	 */
	public List<Object> read() throws CsvMappingNotFoundException {
		
		if (columnMapping.size() < 1) {
			
			throw new CsvMappingNotFoundException();
			
		}
		
		return read(columnMapping.toArray(new String[columnMapping.size()]));
		
	}
	
	/**
	 * Read the CSV file and convert each row into a java object
	 * @param _properties properties of the java class in the same order that they appear in the CSV file
	 * @return list of java objects generated from the CSV file
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Object> read(final String... _properties) {
		
		JoCsvToBean csv = new JoCsvToBean(converters);
		
		CSVReader csvReader = new CSVReader(file, options.getSeparator(), options.getQuote(), options.getFirstLine());
		
		ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
		strat.setColumnMapping(_properties);	
		strat.setType(pojoClass);
		
		objects = csv.parse(strat, csvReader);
		
		if (objects == null) {
			
			objects = Collections.emptyList();
			
		}
		
		return objects;
		
	}
	
	/**
	 * @return the objects that has been created from the CSV file
	 */
	public List<Object> getObjects() {
		
		return objects;
		
	}
	
	/**
	 * Create a new JoSQL query on the objects generated from the CSV file
	 * @param _sql the SQL query string
	 * @return a CsvTableQuery for executing the query
	 * @throws QueryParseException
	 */
	public CsvTableQuery query(final String _sql) throws QueryParseException {
		
		return new CsvTableQuery(this, _sql);
		
	}
	
}
