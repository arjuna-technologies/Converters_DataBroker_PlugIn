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

@Startup
@Singleton
public class ConvertersFactoriesSetup
{
    @PostConstruct
    public void setup()
    {
//        Map<String, String> exampleConverterDataProcessorFactoryProperties = new HashMap<String, String>();
//        exampleConverterDataProcessorFactoryProperties.put("Description", ExampleConverterDataProcessorFactory.DESCRIPTION);

//        ExampleConverterDataProcessorFactory exampleConverterDataProcessorFactory  = new ExampleConverterDataProcessorFactory("Example Convertor Data Processor Factory", exampleConverterDataProcessorFactoryProperties);

//        _dataFlowNodeFactoryInventory.addDataFlowNodeFactory(exampleConverterDataProcessorFactory);
    }

    @PreDestroy
    public void cleanup()
    {
//        _dataFlowNodeFactoryInventory.removeDataFlowNodeFactory("Example Converter Data Processor Factory");
    }

    @EJB(lookup="java:global/databroker/data-core-jee/DataFlowNodeFactoryInventory")
    private DataFlowNodeFactoryInventory _dataFlowNodeFactoryInventory;
}
