package org.josql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.josql.exceptions.QueryExecutionException;
import org.josql.expressions.SelectItemExpression;

public class ColumnValuesExtractor {

	private Query query;
	private List cols;
	
	public ColumnValuesExtractor(final Query q, final List cols) {
		
		this.query = q;
		this.cols = cols;
		
	}
	
	public void extractColumnValues(final List res, final Collection rs)
                      throws QueryExecutionException {
	
		int s = res.size ();
		
		int cs = cols.size ();
		
		boolean addItems = false;
		
		for (int i = 0; i < s; i++) {
		
		  Object o = res.get (i);
		
		  query.setCurrentObject(o);
		
		  List sRes = new ArrayList (cs);
		
		  for (int j = 0; j < cs; j++)
		  {
		
			SelectItemExpression v = (SelectItemExpression) cols.get (j);
		
			try {
		
			    if (v.isAddItemsFromCollectionOrMap()) {
		
			    	addItems = true;
				    
			    }
				
			    // Get the value from the object...
			    Object ov = v.getValue (o, query);
			    
			    if (addItems) {
				    
			    	rs.addAll(v.getAddItems(ov));
				
			    } else {
				
			    	sRes.add (ov);
				
			    }
			    
			    // Now since the expression can set the current object, put it
			    // back to rights after the call...
			    query.setCurrentObject(o);
		
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
	
}
