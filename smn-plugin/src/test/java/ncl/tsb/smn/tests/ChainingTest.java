/*
 * Copyright (c) 2014, Newcastle University, Newcastle-upon-Tyne, England. All rights reserved.
 */

package ncl.tsb.smn.tests;

import ncl.tsb.smn.dataflow.SimpleDataProcessor;
import ncl.tsb.smn.dataflow.SimpleDataService;
import ncl.tsb.smn.dataflow.SimpleDataSink;
import ncl.tsb.smn.dataflow.SimpleDataSource;
import ncl.tsb.smn.dataflow.SimpleDataStore;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertArrayEquals;

public class ChainingTest
{
    @Test
    public void simplestChain()
    {
        SimpleDataSource    simpleDataSource    = new SimpleDataSource("Simple Data Source", Collections.<String, String>emptyMap());
        SimpleDataProcessor simpleDataProcessor = new SimpleDataProcessor("Simple Data Processor", Collections.<String, String>emptyMap());
        SimpleDataSink      simpleDataSink      = new SimpleDataSink("Simple Data Sink", Collections.<String, String>emptyMap());

        simpleDataSource.getDataProvider(String.class).addDataConsumer(simpleDataProcessor.getDataConsumer(String.class));
        simpleDataProcessor.getDataProvider(String.class).addDataConsumer(simpleDataSink.getDataConsumer(String.class));

        simpleDataSource.dummyGetData("Data Bundle 1");
        simpleDataSource.dummyGetData("Data Bundle 2");
        simpleDataSource.dummyGetData("Data Bundle 3");
        simpleDataSource.dummyGetData("Data Bundle 4");

        assertArrayEquals("Unexpected history at DataSink", new String[]{"[Data Bundle 1]", "[Data Bundle 2]", "[Data Bundle 3]", "[Data Bundle 4]"}, simpleDataSink.getSentHistory().toArray());
    }

    @Test
    public void fullConnectedChain()
    {
        SimpleDataSource    simpleDataSource    = new SimpleDataSource("Simple Data Source", Collections.<String, String>emptyMap());
        SimpleDataProcessor simpleDataProcessor = new SimpleDataProcessor("Simple Data Processor", Collections.<String, String>emptyMap());
        SimpleDataSink      simpleDataSink1     = new SimpleDataSink("Simple Data Sink 1", Collections.<String, String>emptyMap());
        SimpleDataSink      simpleDataSink2     = new SimpleDataSink("Simple Data Sink 2", Collections.<String, String>emptyMap());
        SimpleDataSink      simpleDataSink3     = new SimpleDataSink("Simple Data Sink 3", Collections.<String, String>emptyMap());
        SimpleDataService   simpleDataService   = new SimpleDataService("Simple Data Service", Collections.<String, String>emptyMap());
        SimpleDataStore     simpleDataStore     = new SimpleDataStore("Simple Data Store", Collections.<String, String>emptyMap());

        simpleDataSource.getDataProvider(String.class).addDataConsumer(simpleDataService.getDataConsumer(String.class));
        simpleDataService.getDataProvider(String.class).addDataConsumer(simpleDataSink1.getDataConsumer(String.class));

        simpleDataSource.getDataProvider(String.class).addDataConsumer(simpleDataProcessor.getDataConsumer(String.class));
        simpleDataProcessor.getDataProvider(String.class).addDataConsumer(simpleDataSink2.getDataConsumer(String.class));

        simpleDataSource.getDataProvider(String.class).addDataConsumer(simpleDataService.getDataConsumer(String.class));
        simpleDataStore.getDataProvider(String.class).addDataConsumer(simpleDataSink3.getDataConsumer(String.class));

        simpleDataService.dummyImport("Import Bundle 1");
        simpleDataSource.dummyGetData("Data Bundle 1");
        simpleDataStore.dummyQueryReport("Report Bundle 1");
        simpleDataService.dummyImport("Import Bundle 2");
        simpleDataSource.dummyGetData("Data Bundle 2");
        simpleDataStore.dummyQueryReport("Report Bundle 2");

        assertArrayEquals("Unexpected history at DataSink 1", new String[]{"Import Bundle 1", "Import Bundle 2"}, simpleDataSink1.getSentHistory().toArray());
        assertArrayEquals("Unexpected history at DataSink 2", new String[]{"[Data Bundle 1]", "[Data Bundle 2]"}, simpleDataSink2.getSentHistory().toArray());
        assertArrayEquals("Unexpected history at DataSink 3", new String[]{"Report Bundle 1", "Report Bundle 2"}, simpleDataSink3.getSentHistory().toArray());
}
}
