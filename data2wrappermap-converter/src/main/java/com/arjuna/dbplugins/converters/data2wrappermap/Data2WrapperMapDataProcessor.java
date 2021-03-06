/*
 * Copyright (c) 2015, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dbplugins.converters.data2wrappermap;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.arjuna.databroker.data.DataConsumer;
import com.arjuna.databroker.data.DataFlow;
import com.arjuna.databroker.data.DataProcessor;
import com.arjuna.databroker.data.DataProvider;
import com.arjuna.databroker.data.jee.annotation.DataConsumerInjection;
import com.arjuna.databroker.data.jee.annotation.DataProviderInjection;
import com.arjuna.databroker.data.jee.annotation.PostConfig;
import com.arjuna.databroker.data.jee.annotation.PostCreated;
import com.arjuna.databroker.data.jee.annotation.PostRecovery;

public class Data2WrapperMapDataProcessor implements DataProcessor
{
    private static final Logger logger = Logger.getLogger(Data2WrapperMapDataProcessor.class.getName());

    public static final String FILENAMEPREFIX_PROPERTYNAME      = "File Name Prefix";
    public static final String FILENAMEPOSTFIX_PROPERTYNAME     = "File Name Postfix";
    public static final String RESOURCENAMEPREFIX_PROPERTYNAME  = "Resource Name Prefix";
    public static final String RESOURCENAMEPOSTFIX_PROPERTYNAME = "Resource Name Postfix";
    public static final String RESOURCEFORMAT_PROPERTYNAME      = "Resource Format";
    public static final String RESOURCEDESCRIPTION_PROPERTYNAME = "Resource Description";

    public Data2WrapperMapDataProcessor()
    {
        logger.log(Level.FINE, "Data2WrapperMapDataProcessor");
    }

    public Data2WrapperMapDataProcessor(String name, Map<String, String> properties)
    {
        logger.log(Level.FINE, "Data2WrapperMapDataProcessor: " + name + ", " + properties);

        _name       = name;
        _properties = properties;
        _dataFlow   = null;
    }

    @Override
    public String getName()
    {
        return _name;
    }

    @Override
    public void setName(String name)
    {
        _name = name;
    }

    @Override
    public Map<String, String> getProperties()
    {
        return Collections.unmodifiableMap(_properties);
    }

    @Override
    public void setProperties(Map<String, String> properties)
    {
        _properties = properties;
    }

    @PostConfig
    @PostCreated
    @PostRecovery
    public void setup()
    {
        _fileNamePrefix      = _properties.get(FILENAMEPREFIX_PROPERTYNAME);
        _fileNamePostfix     = _properties.get(FILENAMEPOSTFIX_PROPERTYNAME);
        _resourceNamePrefix  = _properties.get(RESOURCENAMEPREFIX_PROPERTYNAME);
        _resourceNamePostfix = _properties.get(RESOURCENAMEPOSTFIX_PROPERTYNAME);
        _resourceFormat      = _properties.get(RESOURCEFORMAT_PROPERTYNAME);
        _resourceDescription = _properties.get(RESOURCEDESCRIPTION_PROPERTYNAME);
    }

    @Override
    public DataFlow getDataFlow()
    {
        return _dataFlow;
    }

    @Override
    public void setDataFlow(DataFlow dataFlow)
    {
        _dataFlow = dataFlow;
    }

    public void converterFromString(String data)
    {
        Map wrapperMap = converter(data.getBytes());

        if ((wrapperMap != null) && (! wrapperMap.isEmpty()))
            _dataProvider.produce(wrapperMap);
    }

    public void converterFromBytes(byte[] data)
    {
        Map wrapperMap = converter(data);

        if ((wrapperMap != null) && (! wrapperMap.isEmpty()))
            _dataProvider.produce(wrapperMap);
    }

    private Map converter(byte[] data)
    {
        Map<String, Object> wrapperMap = new HashMap<String, Object>();

        wrapperMap.put("data", data);
        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
            String           dateString = dateFormat.format(new Date());

            wrapperMap.put("filename", _fileNamePrefix + dateString + _fileNamePostfix);
            wrapperMap.put("resourcename", _resourceNamePrefix + dateString + _resourceNamePostfix);
            wrapperMap.put("resourceformat", _resourceFormat);
            wrapperMap.put("resourcedescription", _resourceDescription);
        }
        catch (Throwable throwable)
        {
            logger.log(Level.WARNING, "Failed to process");
        }

        return wrapperMap;
    }

    @Override
    public Collection<Class<?>> getDataConsumerDataClasses()
    {
        Set<Class<?>> dataConsumerDataClasses = new HashSet<Class<?>>();

        dataConsumerDataClasses.add(String.class);
        dataConsumerDataClasses.add(byte[].class);

        return dataConsumerDataClasses;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> DataConsumer<T> getDataConsumer(Class<T> dataClass)
    {
        if (String.class.isAssignableFrom(dataClass))
            return (DataConsumer<T>) _dataConsumerString;
        else if (byte[].class.isAssignableFrom(dataClass))
            return (DataConsumer<T>) _dataConsumerBytes;
        else
            return null;
    }

    @Override
    public Collection<Class<?>> getDataProviderDataClasses()
    {
        Set<Class<?>> dataProviderDataClasses = new HashSet<Class<?>>();

        dataProviderDataClasses.add(Map.class);

        return dataProviderDataClasses;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> DataProvider<T> getDataProvider(Class<T> dataClass)
    {
        if (Map.class.isAssignableFrom(dataClass))
            return (DataProvider<T>) _dataProvider;
        else
            return null;
    }

    private String _fileNamePrefix;
    private String _fileNamePostfix;
    private String _resourceNamePrefix;
    private String _resourceNamePostfix;
    private String _resourceFormat;
    private String _resourceDescription;

    private String               _name;
    private Map<String, String>  _properties;
    private DataFlow             _dataFlow;
    @DataConsumerInjection(methodName="converterFromString")
    private DataConsumer<String> _dataConsumerString;
    @DataConsumerInjection(methodName="converterFromBytes")
    private DataConsumer<byte[]> _dataConsumerBytes;
    @DataProviderInjection
    private DataProvider<Map>    _dataProvider;
}
