package org.josql.csv;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import net.sf.josql.Person;

import org.josql.Result;
import org.josql.exceptions.QueryExecutionException;
import org.josql.exceptions.QueryParseException;
import org.junit.Before;
import org.junit.Test;

public class CsvTableTest {

	private File resource;
	private File resource2;
	private File resource3;
	private File resource4;
	private File resource5;
	
	@Before
	public void setUp() throws URISyntaxException {
		
		resource = loadResource("sample.csv");
		resource2 = loadResource("sample2.csv");
		resource3 = loadResource("sample3.csv");
		resource4 = loadResource("sample4.csv");
		resource5 = loadResource("sample5.csv");
		
	}
	
	private File loadResource(final String _path) throws URISyntaxException {
		
		URL url = getClass().getResource(_path);
		return new File(url.toURI());
		
	}
	
	@Test
	public void testRead() throws FileNotFoundException {
				
		CsvTable table = new CsvTable(resource, Work.class);
		
		table.read("time", "worker", "superviser");
		
		List<Object> objects = table.getObjects();
		
		assertEquals(3, objects.size());
		
		for(Object o : objects) {
			
			assertEquals(Work.class, o.getClass());
			
			System.out.println(o.toString());
			
		}
		
	}
	
	@Test
	public void restReadWithCustomSeparator() throws FileNotFoundException {
		
		CsvTable table = new CsvTable(resource4, Work.class);
		
		table.setOptions(',', CsvOptions.DEFAULT_CSV_QUOTE, CsvOptions.DEFAULT_CSV_FIRST_LINE);
		
		table.read("time", "worker", "superviser");
		
		List<Object> objects = table.getObjects();
		
		assertEquals(3, objects.size());
		
		for(Object o : objects) {
			
			assertEquals(Work.class, o.getClass());
			
			System.out.println(o.toString());
			
		}
		
	}
	
	@Test
	public void restReadWithoutColumnHeaders() throws FileNotFoundException {
		
		CsvTable table = new CsvTable(resource5, Work.class);
		
		table.setOptions(CsvOptions.DEFAULT_CSV_SEPARATOR, CsvOptions.DEFAULT_CSV_QUOTE, 0);
		
		table.read("time", "worker", "superviser");
		
		List<Object> objects = table.getObjects();
		
		assertEquals(3, objects.size());
		
		for(Object o : objects) {
			
			assertEquals(Work.class, o.getClass());
			
			System.out.println(o.toString());
			
		}
		
	}
	
	@Test
	public void testSelect() throws FileNotFoundException, QueryExecutionException, QueryParseException {
		
		CsvTable table = new CsvTable(resource, Work.class);
		
		table.read("time", "worker", "superviser");
		
		List<Result> results = table.query("SELECT * FROM org.josql.csv.Work WHERE time > 2").execute();
		
		assertEquals(2, results.size());
		
		showResults(results);		
		
	}
	
	@Test
	public void testWithDateConverter() throws FileNotFoundException, QueryExecutionException, QueryParseException {
		
		CsvTable table = new CsvTable(resource2, Work.class);

		table.setConverter(Date.class, new SimpleDateConverter("dd/MM/yyyy"));
		
		table.read("time", "date", "worker", "superviser");
		
		List<Result> results = table.query(
				"SELECT * FROM org.josql.csv.Work "
				+ "WHERE date BETWEEN toDate ('17-05-2014', 'dd-MM-yyyy') AND toDate('20-05-2014', 'dd-MM-yyyy')"
		).execute();
		
		assertEquals(2, results.size());
		
		showResults(results);	
		
	}
	
	@Test
	public void testUsingConverters() throws FileNotFoundException, QueryExecutionException, QueryParseException {
		
		StringConverter<Person> personConverter = new StringConverter<Person>() {
			@Override
			public Person convertValue(final String _value) {
				return new Person(null, _value);
			}		
		};
		
		CsvTable table = new CsvTable(resource3, Work.class);
		
		table.setConverter(Person.class, personConverter);
		table.setConverter(Date.class, new SimpleDateConverter("dd/MM/yyyy"));
		
		table.read("time", "date", "worker", "superviser", "person");
		
		List<Result> results = table.query("SELECT * FROM org.josql.csv.Work WHERE time > 2").execute();
		
		assertEquals(2, results.size());
		
		showResults(results);
		
	}
	
	@Test
	public void testQueryWithoutReading() throws FileNotFoundException, QueryExecutionException, QueryParseException {
		
		CsvTable table = new CsvTable(resource, Work.class);
		
		List<Result> results = table.query("SELECT * FROM org.josql.csv.Work WHERE time > 2").execute();
			
		assertEquals(0, results.size());
		
		showResults(results);		
		
	}
	
	private void showResults(final List<Result> _results) {
		
		for(Result result : _results) {
			
			for(Object w : result) {
				
				System.out.println(w.toString());
				
			}
			
		}
		
	}
	
}
