package org.josql;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class ResultFactory {

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
