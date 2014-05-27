/*
 * Copyright (c) 2014, Newcastle University, Newcastle-upon-Tyne, England. All rights reserved.
 */
package ncl.tsb.smn.dataflow;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.arjuna.databroker.data.DataProvider;
import com.arjuna.databroker.data.DataSource;
import ncl.tsb.smn.connectors.SimpleDataProvider;

public class SimpleDataSource implements DataSource {
	private static final Logger logger = Logger.getLogger(SimpleDataSource.class.getName());

	public SimpleDataSource(String name, Map<String, String> properties) {
		logger.info("SimpleDataSource: " + name + ", " + properties);

		_name = name;
		_properties = properties;

		_dataProvider = new SimpleDataProvider<String>(this);
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public Map<String, String> getProperties() {
		return Collections.unmodifiableMap(_properties);
	}

	public void dummyGetData(String data) {
		logger.info("SimpleDataSource.dummyGetData: " + data);

		_dataProvider.produce(data);
	}

	@Override
	public Collection<Class<?>> getDataProviderDataClasses() {
		Set<Class<?>> dataProviderDataClasses = new HashSet<Class<?>>();

		dataProviderDataClasses.add(String.class);

		return dataProviderDataClasses;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> DataProvider<T> getDataProvider(Class<T> dataClass) {
		if (dataClass == String.class) {
			return (DataProvider<T>) _dataProvider;
		} else {
			return null;
		}
	}

	private String _name;

	private Map<String, String> _properties;

	private DataProvider<String> _dataProvider;
}
