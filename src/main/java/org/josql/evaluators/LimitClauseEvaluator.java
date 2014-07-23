package org.josql.evaluators;

import org.josql.Query;
import org.josql.QueryResults;
import org.josql.exceptions.QueryExecutionException;
import org.josql.internal.Limit;
import org.josql.utils.Timer;

public class LimitClauseEvaluator implements QueryEvaluator {

	public void evaluate(final Query q) throws QueryExecutionException {
		
		Limit limit = q.getLimit();
		QueryResults qd = q.getQueryResults();
		
        if (limit != null) {

	    	Timer timer = qd.getTimeEvaluator().newTimer("Total time to limit results size");
	    	timer.start();
	    	
	    	qd.setResults(limit.getSubList (qd.getResults(), q));

		    timer.stop();

        }
		
	}
	
	
}
