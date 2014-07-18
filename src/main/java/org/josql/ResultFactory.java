package org.josql;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class ResultFactory {

	public static List<Result> createResults(final List _results) {
		
		List<Result> results = Lists.newArrayList();
		
		Iterator it = _results.iterator();
		
		while (it.hasNext()) {
			
			Object o = it.next();
			List<Object> l;
			
			if (o instanceof List) {
				
				l = (List<Object>) o;
				
			} else {
				
				l = Lists.newArrayList();
				l.add(o);
				
			}
			
			results.add(new Result(l));		
			
		}
		
		return results;
		
	}
	
	public static List<Result> createGroupByResult(final Map _groupByResult) {

		List<Result> results = Lists.newArrayList();
		
		for(Object k : _groupByResult.keySet()) {

			List<?> list = (List<?>) _groupByResult.get(k);
			List<Object> sublist = (List<Object>) list.get(0); 
			
			results.add(new Result(sublist));		
			
		}
		
		return results;
		
	}
	
}
