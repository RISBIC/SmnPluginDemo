/*
 * Copyright (c) 2014, Newcastle University, Newcastle-upon-Tyne, England. All rights reserved.
 */
package ncl.tsb.smn;

import com.arjuna.databroker.data.*;
import ncl.tsb.smn.dataflow.SmnDataProcessor;
import ncl.tsb.smn.dataflow.SmnDataService;
import ncl.tsb.smn.dataflow.SmnDataSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SmnDataFlowNodeFactory implements DataFlowNodeFactory {
	public static final String SRC_INTERVAL = "Update Interval (seconds)";

	public static final String SRC_DB_HOST = "Database Host";

	public static final String SRC_DB_PORT = "Database Port";

	public static final String SRC_DB_USER = "Database User";

	public static final String SRC_DB_PASS = "Database Password";

	public static final String SRC_DB_DATABASE = "Database Name";

	public static final String SRC_DB_TABLE = "Database Table";

	public static final String SRC_DB_COLUMN = "Column Name for Updates";

	private final String _name;

	private final Map<String, String> _properties;

	public SmnDataFlowNodeFactory(final String name, final Map<String, String> properties) {
		_name = name;
		_properties = properties;
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public Map<String, String> getProperties() {
		return _properties;
	}

	@Override
	public List<Class<? extends DataFlowNode>> getClasses() {
		final List<Class<? extends DataFlowNode>> classes = new LinkedList<>();

		classes.add(DataSource.class);
		classes.add(DataProcessor.class);
		classes.add(DataService.class);

		return classes;
	}

	@Override
	public <T extends DataFlowNode> List<String> getMetaPropertyNames(final Class<T> dataFlowNodeClass) {
		return Collections.emptyList();
	}

	@Override
	public <T extends DataFlowNode> List<String> getPropertyNames(final Class<T> dataFlowNodeClass, final Map<String, String> metaProperties) throws InvalidClassException, InvalidMetaPropertyException, MissingMetaPropertyException {
		final List<String> propertyNames = new ArrayList<>();

		if (dataFlowNodeClass.isAssignableFrom(SmnDataSource.class)) {
			propertyNames.addAll(Arrays.asList(SRC_INTERVAL, SRC_DB_HOST, SRC_DB_PORT, SRC_DB_USER, SRC_DB_PASS, SRC_DB_DATABASE, SRC_DB_TABLE, SRC_DB_COLUMN));
		}

		return propertyNames;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends DataFlowNode> T createDataFlowNode(final String name, final Class<T> dataFlowNodeClass, final Map<String, String> metaProperties, final Map<String, String> properties) throws InvalidNameException, InvalidPropertyException, MissingPropertyException {
		if (dataFlowNodeClass.isAssignableFrom(SmnDataSource.class)) {
			return (T) new SmnDataSource(name, properties);
		} else if (dataFlowNodeClass.isAssignableFrom(SmnDataProcessor.class)) {
			return (T) new SmnDataProcessor(name, properties);
		} else if (dataFlowNodeClass.isAssignableFrom(SmnDataService.class)) {
			return (T) new SmnDataService(name, properties);
		} else {
			return null;
		}
	}
}
