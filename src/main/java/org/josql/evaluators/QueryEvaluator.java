package org.josql.evaluators;

import org.josql.Query;
import org.josql.exceptions.QueryExecutionException;

public interface QueryEvaluator {

	public void evaluate(Query q) throws QueryExecutionException;
	
}
