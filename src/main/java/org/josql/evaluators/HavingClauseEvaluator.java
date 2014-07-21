package org.josql.evaluators;

import java.util.List;
import java.util.Map;

import org.josql.Query;
import org.josql.QueryResults;
import org.josql.exceptions.QueryExecutionException;
import org.josql.expressions.Expression;

import com.google.common.collect.Lists;

public class HavingClauseEvaluator implements QueryEvaluator {

	private Query query;
	private Expression having;
	private QueryResults qd;
	private Map<Object, Object> groupByResults;
	
	public void init(final Query _query) {
		
		query = _query;
		having = _query.getHavingClause();
		qd = _query.getQueryResults();
		groupByResults = qd.getGroupByResults();
		
	}
	
	public void evaluate(final Query q) throws QueryExecutionException {
	
		init(q);
		
		if (having != null) {	

    		List<Object> notMatched = Lists.newArrayList();
    		
    		for (Object o : groupByResults.keySet()) {
    			
    			if (!having.isTrue(o, query)) {
    				
    				notMatched.add(o);
    				
    			}
    			
    		}

    		for (Object o : notMatched) {
    			
    			groupByResults.remove(o);
    			
    		}
    		
    		qd.setGroupByResults(groupByResults);
    		
	    }
		
	}

}
