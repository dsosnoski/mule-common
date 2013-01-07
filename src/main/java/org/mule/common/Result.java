/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.common;

public interface Result<T>
{
    T get();
    
    Status getStatus();

    String getMessage();
    
    FailureType getFailureType();
    
    String getStacktrace();

    public static enum Status
    {
        SUCCESS, FAILURE
    }
    
    public static enum FailureType
    {
        INVALID_CONFIGURATION,
        INVALID_CREDENTIALS,
        NOT_AUTHORIZED,
        UNKNOWN_HOST,
        CONNECTION_FAILURE,
        RESOURCE_UNAVAILABLE,
        UNSPECIFIED
    }
}

