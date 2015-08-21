/*
 * Copyright (c) 2015, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dbplugins.converters.test.data2wrappermap;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;
import com.arjuna.databroker.data.DataProcessor;
import com.arjuna.databroker.data.connector.ObservableDataProvider;
import com.arjuna.databroker.data.connector.ObserverDataConsumer;
import com.arjuna.databroker.data.core.DataFlowNodeLifeCycleControl;
import com.arjuna.dbplugins.converters.data2wrappermap.Data2WrapperMapDataProcessor;
import com.arjuna.dbutils.testsupport.dataflownodes.dummy.DummyDataSink;
import com.arjuna.dbutils.testsupport.dataflownodes.dummy.DummyDataSource;
import com.arjuna.dbutils.testsupport.dataflownodes.lifecycle.TestJEEDataFlowNodeLifeCycleControl;

public class SimpleTest
{
    @Test
    public void simpleInvocation()
    {
        DataFlowNodeLifeCycleControl dataFlowNodeLifeCycleControl = new TestJEEDataFlowNodeLifeCycleControl();

        String              name       = "Data2WrapperMap Data Processor";
        Map<String, String> properties = new HashMap<String, String>();

        DummyDataSource dummyDataSource                 = new DummyDataSource("Dummy Data Source", Collections.<String, String>emptyMap());
        DataProcessor   jsonArrayFieldPassDataProcessor = new Data2WrapperMapDataProcessor(name, properties);
        DummyDataSink   dummyDataSink                   = new DummyDataSink("Dummy Data Sink", Collections.<String, String>emptyMap());

        dataFlowNodeLifeCycleControl.completeCreationAndActivateDataFlowNode(UUID.randomUUID().toString(), dummyDataSource, null);
        dataFlowNodeLifeCycleControl.completeCreationAndActivateDataFlowNode(UUID.randomUUID().toString(), jsonArrayFieldPassDataProcessor, null);
        dataFlowNodeLifeCycleControl.completeCreationAndActivateDataFlowNode(UUID.randomUUID().toString(), dummyDataSink, null);

        ((ObservableDataProvider<String>) dummyDataSource.getDataProvider(String.class)).addDataConsumer((ObserverDataConsumer<String>) jsonArrayFieldPassDataProcessor.getDataConsumer(String.class));
        ((ObservableDataProvider<Map>) jsonArrayFieldPassDataProcessor.getDataProvider(Map.class)).addDataConsumer((ObserverDataConsumer<Map>) dummyDataSink.getDataConsumer(Map.class));

        dummyDataSource.sendData("[ { \"a\": \"1\", \"b\": \"2\" }, { \"c\": \"3\", \"d\": \"4\" }, { \"b\": \"2\", \"d\": \"4\" }]");

        dataFlowNodeLifeCycleControl.removeDataFlowNode(dummyDataSource);
        dataFlowNodeLifeCycleControl.removeDataFlowNode(jsonArrayFieldPassDataProcessor);
        dataFlowNodeLifeCycleControl.removeDataFlowNode(dummyDataSink);

        assertNotNull("Unexpected 'null' data set", dummyDataSink.receivedData());
        assertEquals("Unexpected number of data items", 1, dummyDataSink.receivedData().size());
    }
}
