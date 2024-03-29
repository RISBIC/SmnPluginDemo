/*
 * Copyright (c) 2014, Newcastle University, Newcastle-upon-Tyne, England. All rights reserved.
 */
package ncl.tsb.smn.connectors;

import com.arjuna.databroker.data.DataConsumer;
import com.arjuna.databroker.data.DataFlowNode;
import com.arjuna.databroker.data.DataProvider;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReflectionConsumer<T> implements DataConsumer<T> {
	private static final Logger logger = Logger.getLogger(ReflectionConsumer.class.getName());

	private DataFlowNode _dataFlowNode;

	private Method _method;

	public ReflectionConsumer(final DataFlowNode dataFlowNode, final Method method) {
		logger.fine("ReflectionConsumer: " + dataFlowNode + ", " + method);

		_dataFlowNode = dataFlowNode;
		_method = method;
	}

	@Override
	public DataFlowNode getDataFlowNode() {
		return _dataFlowNode;
	}

	@Override
	public void consume(final DataProvider<T> dataProvider, final T data) {
		try {
			_method.invoke(_dataFlowNode, data);
		} catch (final Throwable throwable) {
			logger.log(Level.WARNING, "Problem invoking consumer", throwable);
		}
	}

	// TODO: Update to Java 8 and replace this mechanism with lambdas.
	public static Method getMethod(final Class<?> nodeClass, final String nodeMethodName, final Class<?> dataClass) {
		try {
			return nodeClass.getMethod(nodeMethodName, new Class[] { dataClass });
		} catch (final Throwable throwable) {
			logger.log(Level.WARNING, "Unable to find method \"" + nodeMethodName + "\"", throwable);

			return null;
		}
	}
}
