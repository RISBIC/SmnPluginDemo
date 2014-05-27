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
	@PostConstruct
	public void setup() {
		DataFlowNodeFactory simpleDataFlowNodeFactory = new SmnDataFlowNodeFactory("Simple Data Source Factory", Collections.<String, String>emptyMap());

		_dataFlowNodeFactoryInventory.addDataFlowNodeFactory(simpleDataFlowNodeFactory);
	}

	@PreDestroy
	public void cleanup() {
		_dataFlowNodeFactoryInventory.removeDataFlowNodeFactory("Simple Data Source Factory");
	}

	@EJB(lookup = "java:global/server-ear-1.0.0p1m1/control-core-1.0.0p1m1/DataFlowNodeFactoryInventory")
	private DataFlowNodeFactoryInventory _dataFlowNodeFactoryInventory;
}
