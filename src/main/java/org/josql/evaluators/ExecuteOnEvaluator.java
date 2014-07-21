package org.josql.evaluators;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.josql.Query;
import org.josql.exceptions.QueryExecutionException;
import org.josql.expressions.AliasedExpression;
import org.josql.utils.Timer;

public class ExecuteOnEvaluator implements QueryEvaluator {

	private Query query;
	private List<Object> objects; 
	private String type;
	private Map<Object, Object> executeOn;
	
	public ExecuteOnEvaluator(final Map<Object, Object> _executeOn, final List<Object> _objects, final String _type) {
		
		objects = _objects;
		type = _type;
		executeOn = _executeOn != null ? _executeOn : Collections.emptyMap();
		
	}
	
	public ExecuteOnEvaluator(final List<Object> _objects, final String _type) {
		
		this(Collections.emptyMap(), _objects, _type);
		
	}
	
	private void init(final Query _q) {
		
		query = _q;
		
		Map<Object, Object> functions = query.getExecuteOnFunctions();
		
		if (functions != null) {
			
			executeOn = functions;
			
		}
		
	}
	
	public void evaluate(final Query _q) throws QueryExecutionException {
		
		init(_q);	
		
		if (executeOn.isEmpty()) {
                     
            return;		// Do nothing.
            
        }

		if (!query.parsed()) {

			throw new QueryExecutionException ("Query has not been initialised.");

		}

	    // Set the "all objects".
	    query.setAllObjects(objects);

	    List<AliasedExpression> fs = (List<AliasedExpression>) executeOn.get(type);
	    	     
	    if (fs != null) {

	    	int si = fs.size (); 
	    	
	    	String timerName = "Total time to execute: " + si + " expression(s) on " + type + " objects";
	    	Timer timer = query.getQueryResults().getTimeEvaluator().newTimer(timerName);
		    timer.start();

		    for(AliasedExpression f : fs) {
		    	
		    	Object o = f.getValue(null, query);
		    	String af = f.getAlias();
		    	 
		    	if (af != null) {
		    	
		    		query.setSaveValue(af, o);
		    		
		    	}
		    	 
		    }
		    
		    timer.stop();

	    }
		
	}

}
