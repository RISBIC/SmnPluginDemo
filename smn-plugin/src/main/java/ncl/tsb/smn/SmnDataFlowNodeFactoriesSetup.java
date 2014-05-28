/*
 * Copyright (c) 2014, Newcastle University, Newcastle-upon-Tyne, England. All rights reserved.
 */
package ncl.tsb.smn;

import java.util.Collections;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.arjuna.databroker.data.DataFlowNodeFactory;
import com.arjuna.databroker.data.DataFlowNodeFactoryInventory;

@Startup
@Singleton
public class SmnDataFlowNodeFactoriesSetup {
	public static final String FACTORY_NAME = "SMN Data Flow Factory";

	@EJB(lookup = "java:global/server-ear-1.0.0p1m1/control-core-1.0.0p1m1/DataFlowNodeFactoryInventory")
	private DataFlowNodeFactoryInventory _dataFlowNodeFactoryInventory;

	@PostConstruct
	public void setup() {
		final DataFlowNodeFactory simpleDataFlowNodeFactory = new SmnDataFlowNodeFactory(FACTORY_NAME, Collections.<String, String>emptyMap());

		_dataFlowNodeFactoryInventory.addDataFlowNodeFactory(simpleDataFlowNodeFactory);
	}

	@PreDestroy
	public void cleanup() {
		_dataFlowNodeFactoryInventory.removeDataFlowNodeFactory(FACTORY_NAME);
	}
}
