/*
 * Copyright (c) 2015, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dplugins.converters.data2wrappermap;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Deprecated
public class Data2WrapperMapDataProcessor extends com.arjuna.dbplugins.converters.data2wrappermap.Data2WrapperMapDataProcessor
{
    private static final Logger logger = Logger.getLogger(Data2WrapperMapDataProcessor.class.getName());

    public Data2WrapperMapDataProcessor()
    {
        super();

        logger.log(Level.FINE, "Data2WrapperMapDataProcessor");
    }

    public Data2WrapperMapDataProcessor(String name, Map<String, String> properties)
    {
        super(name, properties);

        logger.log(Level.FINE, "Data2WrapperMapDataProcessor: " + name + ", " + properties);
    }
}
