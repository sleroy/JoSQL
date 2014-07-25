package org.josql.csv;

import java.util.Collections;
import java.util.List;

import org.josql.Query;
import org.josql.Result;
import org.josql.exceptions.QueryExecutionException;
import org.josql.exceptions.QueryParseException;

public class CsvTableQuery {

	private CsvTable table;
	private Query query;
	
	/**
	 * Build a JoSQL query from a CsvTable object and a SQL query string
	 * @param _table the CsvTable on which the query will be executed
	 * @param _sql the SQL query string
	 * @throws QueryParseException
	 */
	public CsvTableQuery(final CsvTable _table, final String _sql) throws QueryParseException {
		
		this(_table, _sql, null);
		
	}
	
	/**
	 * Build a JoSQL query from a CsvTable object and a SQL query string
	 * @param _table the CsvTable on which the query will be executed
	 * @param _sql the SQL query string
	 * @param _classLoader the classLoader that will be used in the query
	 * @throws QueryParseException
	 */
	public CsvTableQuery(final CsvTable _table, final String _sql, final ClassLoader _classLoader) throws QueryParseException {
		
		table = _table;
		query = new Query();
		if (_classLoader != null) {
			query.setClassLoader(_classLoader);
		}
		query.parse(_sql);
		
	}
	
	/**
	 * @return the JoSQL Query that has been parsed
	 */
	public Query getQuery() {
		
		return query;
		
	}
	
	/**
	 * Execute the JoSQL query
	 * @return result of the JoSQL query
	 * @throws QueryExecutionException
	 */
	public List<Result> execute() throws QueryExecutionException {
		
		if (query == null) {
			
			throw new QueryExecutionException("No query found!");
			
		}
		
		List<Object> objects = table.getObjects();
		
		if (objects == null) {
			
			objects = Collections.emptyList();
			
		}
		
		return query.execute(objects).asList();
		
	}
	
}
