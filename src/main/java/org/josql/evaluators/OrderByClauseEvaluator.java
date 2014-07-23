package org.josql.evaluators;

import java.util.Collections;
import java.util.Comparator;

import org.josql.Query;
import org.josql.QueryResults;
import org.josql.exceptions.QueryExecutionException;
import org.josql.internal.ListExpressionComparator;
import org.josql.utils.Timer;

public class OrderByClauseEvaluator implements QueryEvaluator {

	private Timer timer;
	
	public void evaluate(final Query q) throws QueryExecutionException {
		
		QueryResults qd = q.getQueryResults();
		Comparator orderByComp = q.getOrderByComparator();
		
		if ((qd.getResults().size () > 1) && (orderByComp != null)) {

			timer = qd.getTimeEvaluator().newTimer("Total time to order results");
			timer.start();

		    // It should be noted here that the comparator will set the
		    // "current object" so that it can be used in the order by
		    // clause.
		    Collections.sort (qd.getResults(), orderByComp);

			timer.stop();

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
