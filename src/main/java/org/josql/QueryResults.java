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
package org.josql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class holds all the "result" information about the execution of a particular
 * Query.  It should be noted that this class holds no reference to the Query object
 * so that a query can be executed, the results "processed" in some way and then the
 * results can be cleaned up by the GC. 
 * <p>
 * @see org.josql.Query#execute(List)
 */
public class QueryResults
{

    // Execution data.
    Map saveValues = new HashMap ();
    Map timings = null;
    List results = null;
    
    public Map getGroupBySaveValues() {
		return groupBySaveValues;
	}

	public void setGroupBySaveValues(final Map _groupBySaveValues) {
		groupBySaveValues = _groupBySaveValues;
	}

	public List<Result> getGlobalResults() {
		return globalResults;
	}

	public void setGlobalResults(final List<Result> _globalResults) {
		globalResults = _globalResults;
	}

	public void setResults(final List _results) {
		results = _results;
	}

	public void setGroupByResults(final Map _groupByResults) {
		groupByResults = _groupByResults;
	}

	List whereResults = null;
    List havingResults = null;
    Map groupByResults = null;

    Map groupBySaveValues = null;
    
    List<Result> globalResults;
    

    public QueryResults ()
    {

    }

    public Map getGroupBySaveValues (final List k)
    {

	if (groupBySaveValues == null)
	{

	    return null;

	}

	return (Map) groupBySaveValues.get (k);

    }

    /**
     * Get the save values.
     *
     * @return The save values.
     */
    public Map getSaveValues ()
    {

	return saveValues;

    }

    /**
     * Get a particular save value for the passed in key.
     *
     * @param id The key of the save value.
     * @return The value it maps to.
     */
    public Object getSaveValue (Object id)
    {

	if (saveValues == null)
	{

	    return null;

	}

	if (id instanceof String)
	{

	    id = ((String) id).toLowerCase ();

	}

	return saveValues.get (id);

    }

    /**
     * Get the results of executing the query, this is the "final" results, i.e.
     * of executing ALL of the query.
     *
     * @return The results.
     */
    public List getResults ()
    {

	return results;

    }
    
    public List<Result> getResultsAsList() {
    	
    	return globalResults;
    	
    }

    /**
     * Get the timing information, is a Map of string to double values.
     *
     * @return The timings.
     */
    public Map getTimings ()
    {

	return timings;

    }

    /**
     * Get the group by results.
     *
     * @return The group by results.
     */
    public Map getGroupByResults ()
    {

	return groupByResults;

    }    

    /**
     * Get the having results.
     *
     * @return The having results.
     */
    public List getHavingResults ()
    {

	return havingResults;

    }

    /**
     * Get the where results.
     *
     * @return The where results.
     */
    public List getWhereResults ()
    {

	return whereResults;

    }

}
