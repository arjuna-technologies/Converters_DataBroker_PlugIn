/*
 * Copyright (c) 2015, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dplugins.converters.data2wrappermap;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Deprecated
public class Data2FormattedWrapperMapDataProcessor extends com.arjuna.dbplugins.converters.data2wrappermap.Data2FormattedWrapperMapDataProcessor
{
    private static final Logger logger = Logger.getLogger(Data2FormattedWrapperMapDataProcessor.class.getName());

    public Data2FormattedWrapperMapDataProcessor()
    {
        super();

        logger.log(Level.FINE, "Data2FormattedWrapperMapDataProcessor");
    }

    public Data2FormattedWrapperMapDataProcessor(String name, Map<String, String> properties)
    {
        super(name, properties);

        logger.log(Level.FINE, "Data2FormattedWrapperMapDataProcessor: " + name + ", " + properties);
    }
}
