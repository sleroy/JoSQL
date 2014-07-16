package org.josql;

import java.util.List;

import javax.management.QueryEval;

import org.josql.evaluators.OrderByClauseEvaluator;
import org.josql.evaluators.QueryEvaluator;
import org.josql.evaluators.SelectClauseEvaluator;
import org.josql.exceptions.QueryExecutionException;

public class QueryExecutor {
	
	private Query query;
	
	public QueryExecutor(final Query q) {
		
		query = q;
		
	}
	
	/**
     * Execute this query on the specified objects.
     *
     * @param objs The list of objects to execute the query on.
     * @return The list of objects that match the query.
     * @throws QueryExecutionException If the query cannot be executed.
     */
	public void execute(List objs, final Class objClass) throws QueryExecutionException {
		
		if ((objs == null) && (objClass != null))
		{
		    throw new QueryExecutionException ("List of objects must be non-null when an object class is specified.");
		}

		if ((objClass == null) && (objs == null))
		{
		    objs = Query.nullQueryList;
		}

		query.allObjects = objs;

		// See if we have any expressions that are to be executed on 
		// the complete set.
		query.doExecuteOn (objs, Query.ALL);
	
	    query.evalWhereClause ();

		// See if we have any functions that are to be executed on 
		// the results...
	    query.doExecuteOn (query.getQueryResults().results,
	                          Query.RESULTS);

		// If we have a "having" clause execute it here...
	    query.evalHavingClause ();
	
	    if (query.grouper != null)
	    {	           
	    	// Now perform the group by operation.
	    	query.evalGroupByClause ();
	    	
	    }else{

	    	// Now perform the order by.
//	    	QueryEvaluator orderByClauseEvaluator = new OrderByClauseEvaluator();
//	    	orderByClauseEvaluator.evaluate(query);
	        query.evalOrderByClause ();

	        // Finally, if we have a limit clause, restrict the set of objects returned...
	        query.evalLimitClause ();

	        QueryEvaluator selectClauseEvaluator = new SelectClauseEvaluator(query.cols);
			selectClauseEvaluator.evaluate(query);
//	        query.evalSelectClause ();
	        
	        // Clean up ;)
		    //query.clearResults ();

	    }
		
	}
	
}
