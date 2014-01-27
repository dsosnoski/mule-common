package org.mule.common.metadata;

import org.mule.common.metadata.datatype.DataType;

import java.util.List;


public abstract class AbstractStructuredMetaDataModel extends AbstractMetaDataModel implements StructuredMetaDataModel
{

    private List<MetaDataField> fields;


    protected AbstractStructuredMetaDataModel(DataType dataType, MetaDataFieldFactory fieldFactory)
    {
        super(dataType);
        this.fields = fieldFactory.createFields();
    }

    protected AbstractStructuredMetaDataModel(DataType dataType, List<MetaDataField> fields)
    {
        super(dataType);
        this.fields = fields;
    }

    @Override
    public List<MetaDataField> getFields()
    {
        return fields;
    }
}
