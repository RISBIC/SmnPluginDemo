/*
 * Copyright (c) 2014, Newcastle University, Newcastle-upon-Tyne, England. All rights reserved.
 */
package ncl.tsb.smn.dataflow;

import com.arjuna.databroker.data.DataProvider;
import com.arjuna.databroker.data.DataSource;
import ncl.tsb.smn.connectors.DirectProvider;
import ncl.tsb.smn.data.SmnRecord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SmnDataSource implements DataSource {
	private static final Logger logger = Logger.getLogger(SmnDataSource.class.getName());

	private String _name;

	private Map<String, String> _properties;

	private DataProvider<SmnRecord> _dataProvider;

	private List<SmnRecord> history = new ArrayList<>();

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public SmnDataSource(String name, Map<String, String> properties) {
		logger.info("SmnDataSource: " + name + ", " + properties);

		_name = name;
		_properties = properties;

		_dataProvider = new DirectProvider<>(this);

		setupPeriodicTask();
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

	public void dummyPublishData(SmnRecord data) {
		logger.info("SmnDataSource.dummyPublishData: " + data);

		history.add(data);
		_dataProvider.produce(data);
	}

	public List<SmnRecord> getHistory() {
		return history;
	}

	private void setupPeriodicTask() {
		final Runnable beeper = new Runnable() {
			public void run() {
				dummyPublishData(SmnRecord.randomDummy());
			}
		};

		scheduler.scheduleAtFixedRate(beeper, 10, 10, TimeUnit.SECONDS);
	}
}
