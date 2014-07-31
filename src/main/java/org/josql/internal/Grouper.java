/*
 * Copyright 2004-2007 Gary Bentley 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.josql.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.josql.Query;
import org.josql.exceptions.QueryExecutionException;
import org.josql.expressions.Expression;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Grouper 
{

    private List cols = new ArrayList ();
    private Query q = null;
    private int cs = -1;

    public Grouper (final Query q)
    {

	this.q = q;

    }

    public List getExpressions ()
    {

	return cols;

    }

    public void addExpression (final Expression e) 
    {

	cols.add (e);
	cs = cols.size ();

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Map group (final List   objs) throws QueryExecutionException {

    	Map retVals = Maps.newHashMap();

    	int s = objs.size (); 
    	
	
		for (int j = 0; j < s; j++)
		{
	
		    Object o = objs.get(j);
	
		    q.setCurrentObject(o);
	
		    List l = Lists.newArrayList();
	
		    // Get the values...
		    for (int i = 0; i < cs; i++) {
	
		    	Expression exp = (Expression) cols.get (i);
	
				try {
					
					Object value = exp.getValue (o, q);
					
					if (value != null) {
						
						l.add(value);
						
					}
		
				} catch (Exception e) {
		
				    throw new QueryExecutionException ("Unable to get group by value for expression: " +
								       exp,
								       e);
		
				}
	
		    }

		    if (l.isEmpty()) {
		    	
		    	continue;
		    	
		    }
		    
		    List v = null;
		    
		    for(Object ls : retVals.keySet()) {

		    	if (l.equals(ls)) {
		    		
		    		v = (List) retVals.get(ls);
		    		break;
		    	}
		    	
		    }
	
		    if (v == null) {
	
		    	v = Lists.newArrayList();
	
		    	retVals.put (l, v);
	
		    }
	
		    v.add (o);
	
		}		
		
		return retVals;

    }

}
