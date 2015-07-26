/*
 * Tomitribe Confidential
 *
 * Copyright Tomitribe Corporation. 2015
 *
 * The source code for this program is not published or otherwise divested 
 * of its trade secrets, irrespective of what has been deposited with the 
 * U.S. Copyright Office.
 */
package com.tomitribe.blackops;

import com.amazonaws.services.ec2.model.Filter;

public class OperationId {
    private final String id;

    public OperationId(String id) {
        this.id = id;
    }

    public String get() {
        return id;
    }


    public Filter asFilter() {
        return new Filter("tag:" + UserData.ID).withValues(id);
    }
}
