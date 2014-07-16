package org.josql.evaluators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.josql.Query;
import org.josql.QueryResults;
import org.josql.exceptions.QueryExecutionException;
import org.josql.expressions.NewObjectExpression;
import org.josql.expressions.SelectItemExpression;
import org.josql.functions.CollectionFunctions;

public class SelectClauseEvaluator implements QueryEvaluator {

	private Query q;
	private List cols;
	
	public SelectClauseEvaluator(final List _cols) {
		cols = _cols;
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

		long s = System.currentTimeMillis ();

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
		    getColumnValues (qd.getResults(), resC);

		    if (q.getWantDistinctResults()) {

		    	qd.setResults(new ArrayList(resC));

		    } else {

		    	qd.setResults((List) resC);

		    }

		   q.addTiming ("Collection of results took",
				    System.currentTimeMillis () - s);

		} else {

		    if (q.isWantObjects() && q.getWantDistinctResults()) {
	
			    s = System.currentTimeMillis ();
			    
			    qd.setResults(((CollectionFunctions) q.getFunctionHandler (CollectionFunctions.HANDLER_ID)).unique (qd.getResults()));
			    
			    q.addTiming ("Collecting unique results took",
					    System.currentTimeMillis () - s);
	
			}

		    // If we want a single column of new objects...
		    if (retNewObjs) {

		    	qd.setResults(getNewObjectSingleColumnValues(qd.getResults()));

		    }

		}
		
	}


	private void getColumnValues (final List       res,
			  final Collection rs)
                        throws QueryExecutionException {
	
		int s = res.size ();
		
		int cs = cols.size ();
		
		boolean addItems = false;
		
		for (int i = 0; i < s; i++) {
		
		  Object o = res.get (i);
		
		  q.setCurrentObject(o);
		
		  List sRes = new ArrayList (cs);
		
		  for (int j = 0; j < cs; j++)
		  {
		
			SelectItemExpression v = (SelectItemExpression) cols.get (j);
		
			try
			{
		
			    if (v.isAddItemsFromCollectionOrMap()) {
		
			    	addItems = true;
				    
			    }
				
			    // Get the value from the object...
			    Object ov = v.getValue (o, q);
			    
			    if (addItems) {
				    
			    	rs.addAll(v.getAddItems(ov));
				
			    } else {
				
			    	sRes.add (ov);
				
			    }
			    
			    // Now since the expression can set the current object, put it
			    // back to rights after the call...
			    q.setCurrentObject(o);
		
			} catch (Exception e) {
		
			    throw new QueryExecutionException ("Unable to get value for column: " +
							       j + 
							       " for: " +
							       v.toString () + 
							       " from result: " +
							       i + 
							       " (" +
							       o + 
							       ")",
							       e);
								   
			}
			
		  }
		
		  if (!addItems) {
		
			  rs.add(sRes);
		
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
