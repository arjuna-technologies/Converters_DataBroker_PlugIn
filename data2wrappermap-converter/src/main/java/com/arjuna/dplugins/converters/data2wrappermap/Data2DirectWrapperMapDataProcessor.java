/*
 * Copyright (c) 2015, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dplugins.converters.data2wrappermap;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Deprecated
public class Data2DirectWrapperMapDataProcessor extends com.arjuna.dbplugins.converters.data2wrappermap.Data2DirectWrapperMapDataProcessor
{
    private static final Logger logger = Logger.getLogger(Data2DirectWrapperMapDataProcessor.class.getName());

    public Data2DirectWrapperMapDataProcessor()
    {
        super();
        logger.log(Level.FINE, "Data2DirectWrapperMapDataProcessor(): Deprecated");
    }

    public Data2DirectWrapperMapDataProcessor(String name, Map<String, String> properties)
    {
        super(name, properties);
        logger.log(Level.FINE, "Data2DirectWrapperMapDataProcessor: Deprecated - " + name + ", " + properties);
    }
}
