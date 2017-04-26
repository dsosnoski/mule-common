package org.mule.common.metadata.field.property;

import org.mule.common.metadata.MetaDataModelProperty;

/**
 * Property used to specify a sort key to be used in place of the name.
 */
public class SortKeyMetaDataProperty implements MetaDataFieldProperty, MetaDataModelProperty
{
    private String key;

    public SortKeyMetaDataProperty(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
