package org.josql.evaluators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.josql.ColumnValuesExtractor;
import org.josql.Query;
import org.josql.QueryResults;
import org.josql.exceptions.QueryExecutionException;
import org.josql.functions.CollectionFunctions;
import org.josql.internal.GroupByExpressionComparator;
import org.josql.internal.Grouper;
import org.josql.internal.ListExpressionComparator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class GroupByClauseEvaluator implements QueryEvaluator {

	private Grouper grouper;
	private QueryResults qd;
	private Query query;
	
	public GroupByClauseEvaluator(Grouper _grouper) {
		
		grouper = _grouper;
	
	}
	
	public void evaluate(Query q) throws QueryExecutionException { /*
		
		long s = System.currentTimeMillis ();
		
		query = q;
		qd = q.getQueryResults();
	     
        // Need to handle the fact that this will return a Map of Lists...
        try {

            // Group the objects.
            Map mres = grouper.group(qd.getResults());

            qd.setGroupByResults(mres);

            List grpBys = Lists.newArrayList(mres.keySet());

            // Convert the keys in the group by to a List.
            Map origSvs = qd.getSaveValues();

            Map nres = Maps.newLinkedHashMap();

            int gs = grpBys.size ();

            // Now for each "group by" list, do:
            // 1. Execute the functions for the GROUP_BY_RESULTS type.
            // 2. Sort the group by results according to the ORDER BY clause.
            // 3. Limit the group by results according to the LIMIT clause.
            for (int i = 0; i < gs; i++) {

                List l = (List) grpBys.get (i);

                List lr = (List) mres.get (l);

                q.setAllObjects(lr);
                q.setCurrentGroupByObjects(l);

                // Now set the save values for the group bys.
                if (qd.getGroupBySaveValues() == null) {

                    qd.setGroupBySaveValues(Maps.newHashMap());

                }
                
                qd.setSaveValues(new HashMap ());

                if (origSvs != null)
                {

                    qd.getSaveValues().putAll(origSvs);
                    
                }

                qd.getGroupBySaveValues().put (l, qd.getSaveValues());

                // Now execute all (any) group by results functions.
                q.doExecuteOn (lr, Query.GROUP_BY_RESULTS);

                // Now sort these according to the order by (if any).
                orderGroupByResult(lr);

                if (!q.isWantObjects())
                {

                    // Now collect the values...
                    Collection res = null;

                    if (!q.getWantDistinctResults()) {

                        res = Lists.newArrayList();

                    } else {

                        res = Sets.newLinkedHashSet();

                    }

                    ColumnValuesExtractor extractor = new ColumnValuesExtractor(q, cols);
        		    extractor.extractColumnValues(lr, res);

                    if (q.getWantDistinctResults()) {

                        lr = new ArrayList (res);

                    } else {

                        lr = (List) res;

                    }

                } else {

                    if (q.getWantDistinctResults()) {

                        qd.setResults(((CollectionFunctions) q.getFunctionHandler (CollectionFunctions.HANDLER_ID)).unique(qd.getResults()));

                    }

                }

                nres.put (l, lr);

            }

            // Restore the save values.
            qd.setSaveValues(origSvs);

            // Set the group by results.
            qd.setGroupByResults(nres);

            long t = System.currentTimeMillis ();

            q.addTiming ("Group column collection and sort took", t - s);
            
            s = t;
            
            // Now order the group bys, if present.
            if (q.getGroupOrderByComp() != null)
            {

                origSvs = qd.getSaveValues();

                Collections.sort (grpBys, q.getGroupOrderByComp());

                // "Restore" the save values.
                qd.setSaveValues(origSvs);

                GroupByExpressionComparator lec = (GroupByExpressionComparator) q.getGroupOrderByComp();

                if (lec.getException () != null)
                {

                    throw new QueryExecutionException ("Unable to order group bys, remember that the current object here is a java.util.List, not the class defined in the FROM clause, you may need to use the org.josq.functions.CollectionFunctions.get(java.util.List,Number) function to get access to the relevant value from the List.",
                                                       lec.getException ());

                }

                lec.clearCache ();

            }

            // Now limit the group bys, if required.
            if (q.groupByLimit != null)
            {

                s = System.currentTimeMillis ();
                
                List oGrpBys = grpBys;
                
                grpBys = groupByLimit.getSubList (grpBys,
                                                       this);
                
                // Now trim out from the group by results any list that isn't in the current grpbys.
                for (int i = 0; i < oGrpBys.size (); i++)
                {

                    List l = (List) oGrpBys.get (i);

                    if (!grpBys.contains (l))
                    {
                        
                        // Remove.
                        qd.groupByResults.remove (l);
                        
                    }
                    
                }
                
                addTiming ("Total time to limit group by results size",
                                System.currentTimeMillis () - s);	

            }

            addTiming ("Group operation took",
                            System.currentTimeMillis () - s);

            // "Restore" the save values.
            qd.setSaveValues(origSvs);

            qd.results = grpBys;
            
            // NOW limit the group by results to a certain size, this needs
            // to be done last so that the group by limit clause can make use of the size of the
            // results.
            if (limit != null)
            {
                
                for (int i = 0; i < qd.results.size (); i++)
                {

                    List l = (List) qd.results.get (i);

                    List lr = (List) qd.groupByResults.get (l);

                    allObjects = lr;
                    currGroupBys = l;
            
                    qd.setSaveValues((Map) qd.groupBySaveValues.get (l));
                        
                    qd.groupByResults.put (l,
                                                limit.getSubList (lr,
                                                                       this));
                
                }            

            }

            qd.setSaveValues(origSvs);

        } catch (Exception e) {

            throw new QueryExecutionException ("Unable to perform group by operation",
                                               e);

        }
                                   
		*/
	}
	
	private void orderGroupByResult(List lr) throws QueryExecutionException {
		
		if ((lr.size () > 1) && (query.getOrderByComparator() != null)) {

            Collections.sort (lr, query.getOrderByComparator());

            ListExpressionComparator lec = (ListExpressionComparator) query.getOrderByComparator();

            if (lec.getException () != null) {

                throw new QueryExecutionException ("Unable to order group by results",
                                                   lec.getException ());

            }

            lec.clearCache ();

        }
		
	}
	
}
