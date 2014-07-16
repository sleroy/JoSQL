package org.josql;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class GroupByResult implements Iterable<Result> {

	private List<Result> results;
	
	public GroupByResult() {
		results = Lists.newArrayList();
	}
	
	public GroupByResult(final List<Result> _results) {
		results = _results;
	}
	
	public Iterator<Result> iterator() {
		return results.iterator();
	}

	public Result get(final int i) {
		return results.get(i);
	}
	
}
