package org.mule.common.metadata;

import org.mule.common.metadata.datatype.DataType;

import java.util.List;


public class DefaultStructuredMetadataModel extends AbstractMetaDataModel implements StructuredMetaDataModel{

    private List<MetaDataField> fields;

    protected DefaultStructuredMetadataModel(DataType dataType, MetaDataFieldFactory fieldFactory) throws Exception {
        super(dataType);
        fields = fieldFactory.createFields();
    }


    @Override
    public void accept(MetaDataModelVisitor modelVisitor) {

    }



    @Override
    public List<MetaDataField> getFields() {
        return fields;
    }
}
