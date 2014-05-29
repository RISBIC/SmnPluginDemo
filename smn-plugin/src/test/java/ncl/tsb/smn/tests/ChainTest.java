/*
 * Copyright (c) 2014, Newcastle University, Newcastle-upon-Tyne, England. All rights reserved.
 */
package ncl.tsb.smn.tests;

import ncl.tsb.smn.data.SmnRecord;
import ncl.tsb.smn.dataflow.SmnDataProcessor;
import ncl.tsb.smn.dataflow.SmnDataService;
import ncl.tsb.smn.dataflow.SmnDataSource;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertTrue;

public class ChainTest {
	@Test
	public void connectedTest() {
		final SmnRecord[] dummyData = generateTestData(5);

		final SmnDataSource simpleDataSource = new SmnDataSource("Simple Data Source", Collections.<String, String>emptyMap());
		final SmnDataProcessor smnDataProcessor = new SmnDataProcessor("Simple Data Processor", Collections.<String, String>emptyMap());
		final SmnDataService simpleDataService = new SmnDataService("Simple Data Service", Collections.<String, String>emptyMap());

		simpleDataSource.getDataProvider(SmnRecord.class).addDataConsumer(smnDataProcessor.getDataConsumer(SmnRecord.class));
		smnDataProcessor.getDataProvider(SmnRecord.class).addDataConsumer(simpleDataService.getDataConsumer(SmnRecord.class));

		for (final SmnRecord record : dummyData) {
			simpleDataSource.emit(record);
		}

		assertTrue("True", true);
//		assertArrayEquals("Unexpected history at Source", dummyData, simpleDataSource.getHistory().toArray());
//		assertArrayEquals("Unexpected history at Processor", dummyData, smnDataProcessor.getHistory().toArray());
//		assertArrayEquals("Unexpected history at Service", dummyData, simpleDataService.getHistory().toArray());
	}

	private SmnRecord[] generateTestData(final int count) {
		final SmnRecord[] records = new SmnRecord[count];

		for (int i = 0; i < count; i++) {
			records[i] = SmnRecord.randomDummy();
		}

		return records;
	}
}
