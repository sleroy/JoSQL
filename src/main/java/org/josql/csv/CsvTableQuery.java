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
	private String sql;
	
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
	public CsvTableQuery(final CsvTable _table, final String _sql, final ClassLoader _classLoader) {
		
		table = _table;
		query = new Query();
		if (_classLoader != null) {
			query.setClassLoader(_classLoader);
		}
		sql = _sql;
		
	}
	
	/**
	 * Parse the query
	 * @return the parsed query
	 * @throws QueryParseException
	 */
	public Query parse() throws QueryParseException {
		
		query.parse(sql);	
		return query;
		
	}
	
	/**
	 * Add a function handler object 
	 * that contains methods that can be called inside the query
	 * @param _handler
	 */
	public void addFunctionHandler(final Object _handler) {
		
		query.addFunctionHandler(_handler);
		
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
	 * @throws QueryParseException 
	 */
	public List<Result> execute() throws QueryExecutionException, QueryParseException {
		
		if (query == null) {
			
			throw new QueryExecutionException("No query found!");
			
		}
		
		if (!query.parsed()) {
			
			parse();
			
		}
		
		List<Object> objects = table.getObjects();
		
		if (objects == null) {
			
			objects = Collections.emptyList();
			
		}
		
		return query.execute(objects).asList();
		
	}
	
}
