package org.josql;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.josql.evaluators.ExecuteOnEvaluator;
import org.josql.evaluators.GroupByClauseEvaluator;
import org.josql.evaluators.HavingClauseEvaluator;
import org.josql.evaluators.LimitClauseEvaluator;
import org.josql.evaluators.OrderByClauseEvaluator;
import org.josql.evaluators.QueryEvaluator;
import org.josql.evaluators.SelectClauseEvaluator;
import org.josql.evaluators.WhereClauseEvaluator;
import org.josql.exceptions.QueryExecutionException;
import org.josql.utils.Timer;

public class QueryExecutor {
	
	private Query query;
	private List<Object> objs;
	private Class<?> objClass;
	private Map<Object, Object> executeOnFunctions;
	
	private ColumnValuesExtractor columnExtractor;
	private Stack<QueryEvaluator> evaluators;
	
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
		executeOnFunctions = query.getExecuteOnFunctions();
		
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

		init();
		executeStack();
	    
	    timer.stop();
		
	}
	
	private void init() {
		
		query.allObjects = objs;
		
		evaluators = new Stack<QueryEvaluator>();		
		
		// See if we have any expressions that are to be executed on 
		// the complete set.
		evaluators.push(new ExecuteOnEvaluator(executeOnFunctions, objs, Query.ALL));

		evaluators.push(new WhereClauseEvaluator());

		// See if we have any functions that are to be executed on the results...
	    evaluators.push(new ExecuteOnEvaluator(executeOnFunctions, query.getQueryResults().getResults(), Query.RESULTS));    
	    
	    columnExtractor = new ColumnValuesExtractor(query, query.cols);
	    
	    if (query.grouper != null) {	           
	    	
	    	evaluators.push(new GroupByClauseEvaluator(query.grouper, columnExtractor));
	    	
	    	// If we have a "having" clause execute it here...
	    	evaluators.push(new HavingClauseEvaluator()); 
	    	
	    }else{

	    	// Now perform the order by.
	    	evaluators.push(new OrderByClauseEvaluator());

	        // Finally, if we have a limit clause, restrict the set of objects returned...
	    	evaluators.push(new LimitClauseEvaluator());

	        evaluators.push(new SelectClauseEvaluator(query.cols, columnExtractor));	       

	    }
		
	}
	
	private void executeStack() throws QueryExecutionException {
		
		Iterator<QueryEvaluator> it = evaluators.iterator();
		
		while(it.hasNext()) {
			it.next().evaluate(query);
		}
		
	}
	
}
