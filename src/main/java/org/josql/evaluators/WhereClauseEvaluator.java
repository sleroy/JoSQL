package org.josql.evaluators;

import java.util.List;

import org.josql.Query;
import org.josql.QueryResults;
import org.josql.exceptions.QueryExecutionException;
import org.josql.expressions.Expression;
import org.josql.utils.Timer;

import com.google.common.collect.Lists;

public class WhereClauseEvaluator implements QueryEvaluator {

	private Query query;
	private Expression where;
	private QueryResults qd;
	private List<Object> allObjects;
	
	private void init(final Query _query) {
		
		query = _query;
		where = query.getWhereClause();
		qd = query.getQueryResults();
		allObjects = query.getAllObjects();
				
	}
	
	public void evaluate(final Query _q) throws QueryExecutionException {
		
		init(_q);
		
		Timer timer = qd.getTimeEvaluator()
				.newTimer("Total time to execute Where clause on all objects");
		timer.start();
		
		List<Object> whereResults = Lists.newArrayList();
		
	    if (where != null) {

		    // Create the where results with "about" half the size of the input collection.
		    // Further optimizations may be possible here if some statistics are collected
		    // about how many objects match/fail the where clause and then increase the
		    // capacity of the where results list as required, i.e. to cut down on the number
		    // of array copy and allocation operations performed.  For now though half will do ;)
	    
		    for (Object o : allObjects) {
		    
		    	query.setCurrentObject(o);

		    	if (where.isTrue(o, query)) {
		    		
		    		whereResults.add(o);

		    	}

		    }	    		    

		} else {
		
		    // No limiting where clause so what's passed in is what comes out.
			whereResults = allObjects;

		}
	    	     
	    // The results here are the result of executing the where clause, if present.
	    qd.setWhereResults(whereResults);
	    query.setAllObjects(whereResults);
	    qd.setResults(whereResults);
	    
	    timer.stop();

	}

}
