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
import java.util.UUID;
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

public class Data2FormattedWrapperMapDataProcessor implements DataProcessor
{
    private static final Logger logger = Logger.getLogger(Data2FormattedWrapperMapDataProcessor.class.getName());

    public static final String FILENAME_PROPERTYNAME            = "File Name";
    public static final String RESOURCENAME_PROPERTYNAME        = "Resource Name";
    public static final String RESOURCEFORMAT_PROPERTYNAME      = "Resource Format";
    public static final String RESOURCEDESCRIPTION_PROPERTYNAME = "Resource Description";

    public Data2FormattedWrapperMapDataProcessor()
    {
        logger.log(Level.FINE, "Data2FormattedWrapperMapDataProcessor");
    }

    public Data2FormattedWrapperMapDataProcessor(String name, Map<String, String> properties)
    {
        logger.log(Level.FINE, "Data2FormattedWrapperMapDataProcessor: " + name + ", " + properties);

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
        _fileName            = _properties.get(FILENAME_PROPERTYNAME);
        _resourceName        = _properties.get(RESOURCENAME_PROPERTYNAME);
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
            String           uuidString     = UUID.randomUUID().toString();
            Date             date           = new Date();
            SimpleDateFormat dateFormat     = new SimpleDateFormat("yyyyMMdd");
            String           dateString     = dateFormat.format(date);
            SimpleDateFormat timeFormat     = new SimpleDateFormat("HHmmss");
            String           timeString     = timeFormat.format(date);
            SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
            String           datetimeString = datetimeFormat.format(date);

            String uuidRegex           = "\\$\\{uuid\\}";
            String dateRegex           = "\\$\\{date\\}";
            String timeRegex           = "\\$\\{time\\}";
            String datetimeRegex       = "\\$\\{datetime\\}";
            String fileName            = _fileName.replaceAll(uuidRegex, uuidString).replaceAll(dateRegex, dateString).replaceAll(timeRegex,timeString).replaceAll(datetimeRegex, datetimeString);
            String resourceName        = _resourceName.replaceAll(uuidRegex, uuidString).replaceAll(dateRegex, dateString).replaceAll(timeRegex,timeString).replaceAll(datetimeRegex, datetimeString);
            String resourceDescription = _resourceDescription.replaceAll(uuidRegex, uuidString).replaceAll(dateRegex, dateString).replaceAll(timeRegex,timeString).replaceAll(datetimeRegex, datetimeString);

            wrapperMap.put("filename", fileName);
            wrapperMap.put("resourcename", resourceName);
            wrapperMap.put("resourceformat", _resourceFormat);
            wrapperMap.put("resourcedescription", resourceDescription);
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

    private String _fileName;
    private String _resourceName;
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
