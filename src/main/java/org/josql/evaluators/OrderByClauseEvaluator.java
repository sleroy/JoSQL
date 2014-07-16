package org.josql.evaluators;

import java.util.Collections;
import java.util.Comparator;

import org.josql.Query;
import org.josql.QueryResults;
import org.josql.exceptions.QueryExecutionException;
import org.josql.internal.ListExpressionComparator;

public class OrderByClauseEvaluator implements QueryEvaluator {

	public void evaluate(Query q) throws QueryExecutionException {
		
		QueryResults qd = q.getQueryResults();
		Comparator orderByComp = q.getOrderByComparator();
		
		if ((qd.getResults().size () > 1) && (orderByComp != null)) {

		    long s = System.currentTimeMillis ();

		    // It should be noted here that the comparator will set the
		    // "current object" so that it can be used in the order by
		    // clause.
		    Collections.sort (qd.getResults(), orderByComp);

		    q.addTiming ("Total time to order results",
				    System.currentTimeMillis () - s);	

		}

		if (orderByComp != null) {

		    ListExpressionComparator lec = (ListExpressionComparator) orderByComp;

		    if (lec.getException () != null) {

		    	throw new QueryExecutionException ("Unable to order results",
							   lec.getException ());

		    }

		    lec.clearCache ();

		}
		
	}

}
