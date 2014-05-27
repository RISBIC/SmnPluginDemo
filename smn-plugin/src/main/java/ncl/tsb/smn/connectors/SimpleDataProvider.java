/*
 * Copyright (c) 2014, Newcastle University, Newcastle-upon-Tyne, England. All rights reserved.
 */
package ncl.tsb.smn.connectors;

import com.arjuna.databroker.data.DataConsumer;
import com.arjuna.databroker.data.DataFlowNode;
import com.arjuna.databroker.data.DataProvider;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class SimpleDataProvider<T> implements DataProvider<T> {
	private static final Logger logger = Logger.getLogger(SimpleDataProvider.class.getName());

	public SimpleDataProvider(DataFlowNode dataFlowNode) {
		logger.fine("SimpleDataProvider: " + dataFlowNode);

		_dataFlowNode = dataFlowNode;
		_dataConsumers = new LinkedList<>();
	}

	@Override
	public DataFlowNode getDataFlowNode() {
		return _dataFlowNode;
	}

	@Override
	public Collection<DataConsumer<T>> getDataConsumers() {
		return Collections.unmodifiableList(_dataConsumers);
	}

	@Override
	public void addDataConsumer(DataConsumer<T> dataConsumer) {
		_dataConsumers.add(dataConsumer);
	}

	@Override
	public void removeDataConsumer(DataConsumer<T> dataConsumer) {
		_dataConsumers.remove(dataConsumer);
	}

	@Override
	public void produce(T data) {
		for (DataConsumer<T> dataConsumer : _dataConsumers) {
			dataConsumer.consume(this, data);
		}
	}

	private DataFlowNode _dataFlowNode;

	private List<DataConsumer<T>> _dataConsumers;
}
