/*
 * Copyright (c) 2014, Newcastle University, Newcastle-upon-Tyne, England. All rights reserved.
 */
package ncl.tsb.smn.dataflow;

import com.arjuna.databroker.data.DataConsumer;
import com.arjuna.databroker.data.DataProcessor;
import com.arjuna.databroker.data.DataProvider;
import ncl.tsb.smn.connectors.DirectProvider;
import ncl.tsb.smn.connectors.ReflectionConsumer;
import ncl.tsb.smn.data.SmnRecord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class SmnDataProcessor implements DataProcessor {
	private static final Logger logger = Logger.getLogger(SmnDataProcessor.class.getName());

	private String _name;

	private Map<String, String> _properties;

	private DataConsumer<SmnRecord> _dataConsumer;

	private DataProvider<SmnRecord> _dataProvider;

	private List<SmnRecord> history = new ArrayList<>();

	public SmnDataProcessor(String name, Map<String, String> properties) {
		logger.info("SmnDataProcessor: " + name + ", " + properties);

		_name = name;
		_properties = properties;

		_dataConsumer = new ReflectionConsumer<>(this, MethodUtil.getMethod(SmnDataProcessor.class, "process", SmnRecord.class));
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
		Set<Class<?>> dataConsumerDataClasses = new HashSet<>();

		dataConsumerDataClasses.add(SmnRecord.class);

		return dataConsumerDataClasses;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> DataConsumer<T> getDataConsumer(Class<T> dataClass) {
		if (dataClass == SmnRecord.class) {
			return (DataConsumer<T>) _dataConsumer;
		} else {
			return null;
		}
	}

	@Override
	public Collection<Class<?>> getDataProviderDataClasses() {
		Set<Class<?>> dataProviderDataClasses = new HashSet<>();

		dataProviderDataClasses.add(SmnRecord.class);

		return dataProviderDataClasses;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> DataProvider<T> getDataProvider(Class<T> dataClass) {
		if (dataClass == SmnRecord.class) {
			return (DataProvider<T>) _dataProvider;
		} else {
			return null;
		}
	}

	public void process(SmnRecord data) {
		logger.info("SmnDataProcessor.process: " + data);

		_dataProvider.produce(data);

		history.add(data);
	}

	public List<SmnRecord> getHistory() {
		return history;
	}
}
