package org.josql.evaluators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.josql.ColumnValuesExtractor;
import org.josql.Query;
import org.josql.QueryResults;
import org.josql.exceptions.QueryExecutionException;
import org.josql.expressions.NewObjectExpression;
import org.josql.expressions.SelectItemExpression;
import org.josql.functions.CollectionFunctions;
import org.josql.utils.Timer;

public class SelectClauseEvaluator implements QueryEvaluator {

	private Query q;
	private List cols;
	private Timer timer;
	private ColumnValuesExtractor extractor;
	
	public SelectClauseEvaluator(final List _cols, final ColumnValuesExtractor _extractor) {
		
		cols = _cols;
		extractor = _extractor;
	}
	
	public void evaluate(final Query q) throws QueryExecutionException {
		
		this.q = q;
		
		QueryResults qd = q.getQueryResults();
		    
		boolean retNewObjs = false;

		// See if we are a single column of new objects.
		if (!q.isWantObjects() && cols.size() == 1) {

	    	SelectItemExpression sei = (SelectItemExpression) cols.get(0);
		
	    	if (sei.getExpression () instanceof NewObjectExpression) {
	
	    		retNewObjs = true;
		    
	    	}

		}

		timer = qd.getTimeEvaluator().newTimer("Collection of results took");
		timer.start();

		// Now get the columns if necessary, we do this here to get the minimum
		// set of objects required.
		if ((!q.isWantObjects()) && (!retNewObjs)) {

		    Collection resC;

		    if (!q.getWantDistinctResults()){
		    	
		    	resC = new ArrayList (qd.getResults().size());

		    } else {

		    	resC = new LinkedHashSet (qd.getResults().size());

		    }

		    // Get the column values.
//		    getColumnValues(qd.getResults(), resC);
//		    ColumnValuesExtractor extractor = new ColumnValuesExtractor(q, cols);
		    extractor.extractColumnValues(qd.getResults(), resC);

		    if (q.getWantDistinctResults()) {

		    	qd.setResults(new ArrayList(resC));

		    } else {

		    	qd.setResults((List) resC);

		    }		    	  
		    
		    timer.stop();

		} else {

		    if (q.isWantObjects() && q.getWantDistinctResults()) {
	
		    	timer = qd.getTimeEvaluator().newTimer("Collecting unique results took");
				timer.start();
			    
			    qd.setResults(((CollectionFunctions) q.getFunctionHandler (CollectionFunctions.HANDLER_ID)).unique (qd.getResults()));
			    
			    timer.stop();
	
			}

		    // If we want a single column of new objects...
		    if (retNewObjs) {

		    	qd.setResults(getNewObjectSingleColumnValues(qd.getResults()));

		    }

		}
		
	}
	
    private List getNewObjectSingleColumnValues (final List   rows)
            throws QueryExecutionException
	{
	
		int s = rows.size ();
		
		SelectItemExpression nsei = (SelectItemExpression) cols.get (0);
		
		List res = new ArrayList (s);
		
		for (int i = 0; i < s; i++) {
		
			Object o = rows.get (i);
		
			q.setCurrentObject(o);
		
			try {
			
				res.add (nsei.getValue (o, q));
			
			// Now since the expression can set the current object, put it
			// back to rights after the call...
				q.setCurrentObject(o);
			
			} catch (Exception e) {
			
				throw new QueryExecutionException ("Unable to get value for column: " +
							1 + 
							" for: " +
							nsei.toString () + 
							" from result: " +
							i + 
							" (" +
							o + 
							")",
							e);
			
			}
		
		}
		
		return res;
	
	}
	
}
