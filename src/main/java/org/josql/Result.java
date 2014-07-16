package org.josql;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class Result implements Iterable<Object> {

	private List<Object> values;
	
	public Result() {
		values = Lists.newArrayList();
	}
	
	public Result(final List<Object> _values) {
		values = _values;
	}
	
	public Iterator<Object> iterator() {
		return values.iterator();
	}
	
	public List<Object> getList() {
		return values;
	}

}
