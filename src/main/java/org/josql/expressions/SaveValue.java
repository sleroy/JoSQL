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
package org.josql.expressions;

import java.util.List;

import org.josql.Query;
import org.josql.exceptions.QueryExecutionException;
import org.josql.exceptions.QueryParseException;
import org.josql.internal.Utilities;

import com.gentlyweb.utils.Getter;

public class SaveValue extends ValueExpression
{

    private String name = null;
    private String acc = null;
    private Getter get = null;

    @Override
	public Class getExpectedReturnType (final Query  q)
	                                throws QueryParseException
    {

	// See if the save value is already present.
	Object sv = q.getSaveValue (name);

	if (sv != null)
	{

	    if (acc != null)
	    {

		// Init the getter.
		try
		{

		    get = new Getter (acc,
					   sv.getClass ());

		} catch (Exception e) {
		    
		    throw new QueryParseException ("Unable to create dynamic getter from instance of type: " +
						   sv.getClass ().getName () + 
						   " for save value: " +
						   name +
						   " using accessor: " +
						   acc,
						   e);

		}

		return get.getType ();

	    }

	    return sv.getClass ();

	}

	// No idea what it could be...
	return Object.class;

    }

    @Override
	public void init (final Query  q)
    {

	// Nothing to do...

    }

    public String getName ()
    {

	return name;

    }

    public void setName (final String name)
    {

	this.name = name;

    }

    @Override
	public Object getValue (final Object o, final Query  q)
	                    throws QueryExecutionException
    {

		Object v = q.getSaveValue (name);
	
		if (v == null) {
	
			try {
				
				v = q.getGroupBySaveValue(name, (List) o);
				
			} catch(Exception e) {
				
				v = null;
				
			}
			
			if (v == null) {
				
				return v;
				
			}
		    
		}
	
		// See if we have an accessor...
	
		if ((acc != null) && (get == null)) {
	
		    try {
	
		    	get = new Getter (acc, v.getClass ());
	
		    } catch (Exception e) {
	
				throw new QueryExecutionException ("Unable to create dynamic getter from instance of type: " +
								   v.getClass ().getName () + 
								   " for save value: " + name +
								   " using accessor: " + acc,
								   e);
		
		    }
	
		}
	
		if (get != null) {
	
		    try {
	
		    	v = get.getValue (v);
	
		    } catch (Exception e) {
	
				throw new QueryExecutionException ("Unable to get value from instance of type: " + 
								   v.getClass ().getName () +
								   " for save value: " + name + 
								   " using accessor: " + acc,
								   e);
		
		    }
	
		}
	
		return v;

    }

    @Override
	public boolean isTrue (Object o,
			   final Query  q)
	                   throws QueryExecutionException
    {

		o = getValue (o, q);
	
		if (o == null) {
		    
		    return false;
	
		}
	
		if (Utilities.isNumber (o))
		{
	
		    return Utilities.getDouble (o) > 0;
	
		}
	
		// Not null so return true...
		return true;

    }

    public String getAccessor ()
    {

	return acc;

    }

    public void setAccessor (final String acc)
    {

	this.acc = acc;

    }

    @Override
	public Object evaluate (final Object o,
			    final Query  q)
	                    throws QueryExecutionException
    {

	return getValue (o,
			      q);

    }

    @Override
	public String toString ()
    {

	StringBuffer buf = new StringBuffer ();

	buf.append ("@");
	buf.append (name);

	if (acc != null)
	{

	    buf.append (".");
	    buf.append (acc);

	}

	if (isBracketed ())
	{

	    buf.insert (0,
			"(");
	    buf.append (")");

	}

	return buf.toString ();

    }

    @Override
	public boolean hasFixedResult (final Query q)
    {

	// A save value cannot have a fixed result.
	return false;

    }

}
