package org.josql;

import java.util.List;

import org.josql.evaluators.GroupByClauseEvaluator;
import org.josql.evaluators.HavingClauseEvaluator;
import org.josql.evaluators.LimitClauseEvaluator;
import org.josql.evaluators.OrderByClauseEvaluator;
import org.josql.evaluators.QueryEvaluator;
import org.josql.evaluators.SelectClauseEvaluator;
import org.josql.exceptions.QueryExecutionException;
import org.josql.utils.Timer;

public class QueryExecutor {
	
	private Query query;
	private List<Object> objs;
	private Class<?> objClass;
	private ColumnValuesExtractor columnExtractor;
	
	/**
	 * Create a new QueryExecutor for executing a JoSQL query
	 * @param _query the JoSQL Query object
	 * @param _objs The list of objects to execute the query on.
	 * @param _objClass the type of the objects
	 */
	public QueryExecutor(final Query _query, final List<Object> _objs, final Class<?> _objClass) {
		
		query = _query;
		objs = _objs;
		objClass = _objClass;
		
	}
	
	/**
     * Execute this query on the specified objects.
     * @throws QueryExecutionException If the query cannot be executed.
     */
	public void execute() throws QueryExecutionException {
		
		if ((objs == null) && (objClass != null)) {
		    throw new QueryExecutionException ("List of objects must be non-null when an object class is specified.");
		}
		
		if ((objClass == null) && (objs == null)) {
		    objs = Query.nullQueryList;
		}
		
		Timer timer = query.getQueryResults().getTimeEvaluator().newTimer("Query executed in");
		timer.start();

		query.allObjects = objs;

		// See if we have any expressions that are to be executed on 
		// the complete set.
		query.doExecuteOn(objs, Query.ALL);
	
	    query.evalWhereClause();

		// See if we have any functions that are to be executed on 
		// the results...
	    query.doExecuteOn(query.getQueryResults().getResults(),Query.RESULTS);
	
	    columnExtractor = new ColumnValuesExtractor(query, query.cols);
	    
	    if (query.grouper != null) {	           
	    	
	    	QueryEvaluator groupByEvaluator = new GroupByClauseEvaluator(query.grouper, columnExtractor);
	    	groupByEvaluator.evaluate(query);
	    	
	    	// If we have a "having" clause execute it here...
	    	QueryEvaluator havingEvaluator = new HavingClauseEvaluator();
	    	havingEvaluator.evaluate(query);	 
	    	
	    }else{

	    	// Now perform the order by.
	    	QueryEvaluator orderByClauseEvaluator = new OrderByClauseEvaluator();
	    	orderByClauseEvaluator.evaluate(query);

	        // Finally, if we have a limit clause, restrict the set of objects returned...
	    	QueryEvaluator limitClauseEvaluator = new LimitClauseEvaluator();
	    	limitClauseEvaluator.evaluate(query);

	        QueryEvaluator selectClauseEvaluator = new SelectClauseEvaluator(query.cols, columnExtractor);
			selectClauseEvaluator.evaluate(query);
	        
	        // Clean up ;)
		    //query.clearResults ();

	    }
	    
	    timer.stop();
		
	}
	
}
