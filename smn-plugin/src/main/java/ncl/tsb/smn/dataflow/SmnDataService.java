/*
 * Copyright (c) 2014, Newcastle University, Newcastle-upon-Tyne, England. All rights reserved.
 */
package ncl.tsb.smn.dataflow;

import com.arjuna.databroker.data.DataConsumer;
import com.arjuna.databroker.data.DataProvider;
import com.arjuna.databroker.data.DataService;
import ncl.tsb.smn.connectors.ReflectionConsumer;
import ncl.tsb.smn.connectors.DirectProvider;
import ncl.tsb.smn.data.SmnRecord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class SmnDataService implements DataService {
	private String _name;

	private Map<String, String> _properties;

	private DataConsumer<SmnRecord> _dataConsumer;

	private DataProvider<SmnRecord> _dataProvider;

	private List<SmnRecord> history = new ArrayList<>();

	private static final Logger logger = Logger.getLogger(SmnDataService.class.getName());

	public SmnDataService(final String name, final Map<String, String> properties) {
		logger.info("StringDataService: " + name + ", " + properties);

		_name = name;
		_properties = properties;

		_dataConsumer = new ReflectionConsumer<>(this, MethodUtil.getMethod(SmnDataService.class, "export", SmnRecord.class));
		_dataProvider = new DirectProvider<>(this);
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public Map<String, String> getProperties() {
		return Collections.unmodifiableMap(_properties);
	}

	@Override
	public Collection<Class<?>> getDataConsumerDataClasses() {
		final Set<Class<?>> dataConsumerDataClasses = new HashSet<>();

		dataConsumerDataClasses.add(SmnRecord.class);

		return dataConsumerDataClasses;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> DataConsumer<T> getDataConsumer(final Class<T> dataClass) {
		if (dataClass == SmnRecord.class) {
			return (DataConsumer<T>) _dataConsumer;
		} else {
			return null;
		}
	}

	@Override
	public Collection<Class<?>> getDataProviderDataClasses() {
		final Set<Class<?>> dataProviderDataClasses = new HashSet<>();

		dataProviderDataClasses.add(SmnRecord.class);

		return dataProviderDataClasses;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> DataProvider<T> getDataProvider(final Class<T> dataClass) {
		if (dataClass == SmnRecord.class) {
			return (DataProvider<T>) _dataProvider;
		} else {
			return null;
		}
	}

	public void export(final SmnRecord data) {
		logger.info("SmnDataService.export: " + data);
		history.add(data);
	}

	public List<SmnRecord> getHistory() {
		return history;
	}
}
