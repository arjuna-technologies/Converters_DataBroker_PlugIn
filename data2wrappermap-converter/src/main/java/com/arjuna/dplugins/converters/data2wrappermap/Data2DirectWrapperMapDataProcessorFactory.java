/*
 * Copyright (c) 2015, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dplugins.converters.data2wrappermap;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.arjuna.databroker.data.DataFlowNode;
import com.arjuna.databroker.data.DataFlowNodeFactory;
import com.arjuna.databroker.data.DataProcessor;
import com.arjuna.databroker.data.InvalidClassException;
import com.arjuna.databroker.data.InvalidMetaPropertyException;
import com.arjuna.databroker.data.InvalidNameException;
import com.arjuna.databroker.data.InvalidPropertyException;
import com.arjuna.databroker.data.MissingMetaPropertyException;
import com.arjuna.databroker.data.MissingPropertyException;
import com.arjuna.dbplugins.converters.data2wrappermap.Data2DirectWrapperMapDataProcessor;

public class Data2DirectWrapperMapDataProcessorFactory implements DataFlowNodeFactory
{
    public static final String DESCRIPTION = "Creates data flow nodes which wraps a blob of data in a direct discriptive map object";

    public Data2DirectWrapperMapDataProcessorFactory(String name, Map<String, String> properties)
    {
        _name       = name;
        _properties = properties;
    }

    @Override
    public String getName()
    {
        return _name;
    }

    @Override
    public Map<String, String> getProperties()
    {
        return _properties;
    }

    @Override
    public List<Class<? extends DataFlowNode>> getClasses()
    {
        List<Class<? extends DataFlowNode>> classes = new LinkedList<Class<? extends DataFlowNode>>();

        classes.add(DataProcessor.class);

        return classes;
    }

    @Override
    public <T extends DataFlowNode> List<String> getMetaPropertyNames(Class<T> dataFlowNodeClass)
    {
        return Collections.emptyList();
    }

    @Override
    public <T extends DataFlowNode> List<String> getPropertyNames(Class<T> dataFlowNodeClass, Map<String, String> metaProperties)
        throws InvalidClassException, InvalidMetaPropertyException, MissingMetaPropertyException
    {
        List<String> propertyNames = new LinkedList<String>();

        propertyNames.add(Data2DirectWrapperMapDataProcessor.FILENAME_PROPERTYNAME);
        propertyNames.add(Data2DirectWrapperMapDataProcessor.RESOURCENAME_PROPERTYNAME);
        propertyNames.add(Data2DirectWrapperMapDataProcessor.RESOURCEFORMAT_PROPERTYNAME);
        propertyNames.add(Data2DirectWrapperMapDataProcessor.RESOURCEDESCRIPTION_PROPERTYNAME);

        return propertyNames;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends DataFlowNode> T createDataFlowNode(String name, Class<T> dataFlowNodeClass, Map<String, String> metaProperties, Map<String, String> properties)
        throws InvalidNameException, InvalidPropertyException, MissingPropertyException
    {
        if (dataFlowNodeClass.isAssignableFrom(Data2DirectWrapperMapDataProcessor.class))
            return (T) new Data2DirectWrapperMapDataProcessor(name, properties);
        else
            return null;
    }

    private String              _name;
    private Map<String, String> _properties;
}
