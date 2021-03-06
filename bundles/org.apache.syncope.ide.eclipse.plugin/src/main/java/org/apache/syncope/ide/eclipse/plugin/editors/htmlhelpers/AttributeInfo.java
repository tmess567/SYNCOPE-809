/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.syncope.ide.eclipse.plugin.editors.htmlhelpers;

import java.util.ArrayList;
import java.util.List;

public class AttributeInfo {

    private String attributeName;
    private boolean hasValue;
    private int attributeType;
    private boolean required = false;
    private String description;
    private List<String> values = new ArrayList<String>();

    public static final int NONE       = 0;
    public static final int ALIGN      = 1;
    public static final int VALIGN     = 2;
    public static final int INPUT_TYPE = 3;
    public static final int CSS        = 4;
    public static final int FILE       = 5;
    public static final int ID         = 6;
    public static final int IDREF      = 7;
    public static final int IDREFS     = 8;
    public static final int TARGET     = 9;

    public AttributeInfo(final String attributeName, final boolean hasValue) {
        this(attributeName, hasValue, NONE);
    }

    public AttributeInfo(final String attributeName, final boolean hasValue, final int attributeType) {
        this(attributeName, hasValue, attributeType, false);
    }

    public AttributeInfo(final String attributeName, final boolean hasValue, final int attributeType,
            final boolean required) {
        this.attributeName = attributeName;
        this.hasValue      = hasValue;
        this.attributeType = attributeType;
        this.required      = required;
    }

    public int getAttributeType() {
        return this.attributeType;
    }

    public void setAttributeType(final int type) {
        this.attributeType = type;
    }

    public String getAttributeName() {
        return this.attributeName;
    }

    public boolean hasValue() {
        return this.hasValue;
    }

    public boolean isRequired() {
        return this.required;
    }

    public void addValue(final String value) {
        this.values.add(value);
    }

    public String[] getValues() {
        return this.values.toArray(new String[this.values.size()]);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
