/*
 * Copyright (c) 2014, Newcastle University, Newcastle-upon-Tyne, England. All rights reserved.
 */
package ncl.tsb.smn;

import com.arjuna.databroker.data.*;
import ncl.tsb.smn.dataflow.SmnDataProcessor;
import ncl.tsb.smn.dataflow.SmnDataService;
import ncl.tsb.smn.dataflow.SmnDataSource;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SmnDataFlowNodeFactory implements DataFlowNodeFactory {
	private String _name;

	private Map<String, String> _properties;

	public SmnDataFlowNodeFactory(String name, Map<String, String> properties) {
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
		List<Class<? extends DataFlowNode>> classes = new LinkedList<>();

		classes.add(DataSource.class);
		classes.add(DataProcessor.class);
		classes.add(DataService.class);

		return classes;
	}

	@Override
	public <T extends DataFlowNode> List<String> getMetaPropertyNames(Class<T> dataFlowNodeClass) {
		return Collections.emptyList();
	}

	@Override
	public <T extends DataFlowNode> List<String> getPropertyNames(Class<T> dataFlowNodeClass, Map<String, String> metaProperties) throws InvalidClassException, InvalidMetaPropertyException, MissingMetaPropertyException {
		return Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends DataFlowNode> T createDataFlowNode(String name, Class<T> dataFlowNodeClass, Map<String, String> metaProperties, Map<String, String> properties) throws InvalidNameException, InvalidPropertyException, MissingPropertyException {
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
