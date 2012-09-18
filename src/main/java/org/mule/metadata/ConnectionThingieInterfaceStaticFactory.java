/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.metadata;

import org.mule.metadata.api.ConnectionThingieInterface;

import java.util.ServiceLoader;

public class ConnectionThingieInterfaceStaticFactory
{

    public static ConnectionThingieInterface getConnectionThingieInterface()
    {
        return ServiceLoader.load(ConnectionThingieInterface.class).iterator().next();
    }

}
