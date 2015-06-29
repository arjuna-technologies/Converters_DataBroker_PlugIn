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
import com.arjuna.dplugins.converters.data2wrappermap.Data2WrapperMapDataProcessorFactory;

@Startup
@Singleton
public class ConvertersFactoriesSetup
{
    @PostConstruct
    public void setup()
    {
        Map<String, String> data2WrapperMapDataProcessorFactoryProperties = new HashMap<String, String>();
        data2WrapperMapDataProcessorFactoryProperties.put("Description", Data2WrapperMapDataProcessorFactory.DESCRIPTION);

        Data2WrapperMapDataProcessorFactory data2WrapperMapDataProcessorFactory = new Data2WrapperMapDataProcessorFactory("Data2WrapperMap Data Processor Factory", data2WrapperMapDataProcessorFactoryProperties);

        _dataFlowNodeFactoryInventory.addDataFlowNodeFactory(data2WrapperMapDataProcessorFactory);
    }

    @PreDestroy
    public void cleanup()
    {
        _dataFlowNodeFactoryInventory.removeDataFlowNodeFactory("Data2WrapperMap Data Processor Factory");
    }

    @EJB(lookup="java:global/databroker/data-core-jee/DataFlowNodeFactoryInventory")
    private DataFlowNodeFactoryInventory _dataFlowNodeFactoryInventory;
}
