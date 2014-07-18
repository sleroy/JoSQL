package org.josql.csv;

import com.google.common.base.Converter;

public abstract class StringConverter<V> extends Converter<String, V> {

	@Override
	protected String doBackward(final V _arg) {
		return _arg.toString();
	}

	@Override
	protected V doForward(final String _value) {
		return convertValue(_value);
	}
	
	abstract public V convertValue(final String _value);
	
}
