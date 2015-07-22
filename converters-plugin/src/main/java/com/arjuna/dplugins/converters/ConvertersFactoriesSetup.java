/*
 * Copyright (c) 2015, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dplugins.converters;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import com.arjuna.databroker.data.DataFlowNodeFactoryInventory;
import com.arjuna.dplugins.converters.data2wrappermap.Data2FormattedWrapperMapDataProcessorFactory;
import com.arjuna.dplugins.converters.data2wrappermap.Data2WrapperMapDataProcessorFactory;
import com.arjuna.dplugins.converters.data2wrappermap.Data2DirectWrapperMapDataProcessorFactory;

@Startup
@Singleton
public class ConvertersFactoriesSetup
{
    @PostConstruct
    public void setup()
    {
        Map<String, String> data2WrapperMapDataProcessorFactoryProperties = new HashMap<String, String>();
        data2WrapperMapDataProcessorFactoryProperties.put("Description", Data2WrapperMapDataProcessorFactory.DESCRIPTION);

        Map<String, String> data2DirectWrapperMapDataProcessorFactoryProperties = new HashMap<String, String>();
        data2DirectWrapperMapDataProcessorFactoryProperties.put("Description", Data2DirectWrapperMapDataProcessorFactory.DESCRIPTION);

        Map<String, String> data2FormattedWrapperMapDataProcessorFactoryProperties = new HashMap<String, String>();
        data2FormattedWrapperMapDataProcessorFactoryProperties.put("Description", Data2FormattedWrapperMapDataProcessorFactory.DESCRIPTION);

        Data2WrapperMapDataProcessorFactory          data2WrapperMapDataProcessorFactory          = new Data2WrapperMapDataProcessorFactory("Data2WrapperMap Data Processor Factory", data2WrapperMapDataProcessorFactoryProperties);
        Data2DirectWrapperMapDataProcessorFactory    data2DirectWrapperMapDataProcessorFactory    = new Data2DirectWrapperMapDataProcessorFactory("Data2DirectWrapperMap Data Processor Factory", data2DirectWrapperMapDataProcessorFactoryProperties);
        Data2FormattedWrapperMapDataProcessorFactory data2FormattedWrapperMapDataProcessorFactory = new Data2FormattedWrapperMapDataProcessorFactory("Data2FormattedWrapperMap Data Processor Factory", data2FormattedWrapperMapDataProcessorFactoryProperties);

        _dataFlowNodeFactoryInventory.addDataFlowNodeFactory(data2WrapperMapDataProcessorFactory);
        _dataFlowNodeFactoryInventory.addDataFlowNodeFactory(data2DirectWrapperMapDataProcessorFactory);
        _dataFlowNodeFactoryInventory.addDataFlowNodeFactory(data2FormattedWrapperMapDataProcessorFactory);
    }

    @PreDestroy
    public void cleanup()
    {
        _dataFlowNodeFactoryInventory.removeDataFlowNodeFactory("Data2WrapperMap Data Processor Factory");
        _dataFlowNodeFactoryInventory.removeDataFlowNodeFactory("Data2DirectWrapperMap Data Processor Factory");
        _dataFlowNodeFactoryInventory.removeDataFlowNodeFactory("Data2FormattedWrapperMap Data Processor Factory");
    }

    @EJB(lookup="java:global/databroker/data-core-jee/DataFlowNodeFactoryInventory")
    private DataFlowNodeFactoryInventory _dataFlowNodeFactoryInventory;
}
