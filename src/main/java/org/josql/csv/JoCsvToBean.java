package org.josql.csv;

import com.opencsv.bean.CsvToBean;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class JoCsvToBean extends CsvToBean<Object> {

    private Map<Class<?>, StringConverter<?>> converters;

    public JoCsvToBean(final Map<Class<?>, StringConverter<?>> _converters) {

        converters = _converters;

    }

    @Override
    protected Object convertValue(final String value, final PropertyDescriptor prop) throws InstantiationException, IllegalAccessException {

        StringConverter<?> converter = converters.get(prop.getPropertyType());

        if (converter != null) {
            return converter.convertValue(value);
        }

        return super.convertValue(value, prop);
    }

}
