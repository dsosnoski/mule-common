package org.mule.common.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.lang.String;
import java.util.Map;

import org.mule.common.metadata.datatype.DataType;
import org.mule.common.metadata.parser.json.*;

/**
 * Created by studio on 18/07/2014.
 */
public class JSONSchemaMetaDataFieldFactory implements MetaDataFieldFactory {

    Map<JSONObjectType, DefaultStructuredMetadataModel> visitedTypes = null;

    private JSONType jsonSchemaType;

    public JSONSchemaMetaDataFieldFactory(JSONObjectType type) {
        jsonSchemaType = type;
    }

    public JSONSchemaMetaDataFieldFactory(JSONObjectType type, Map<JSONObjectType, DefaultStructuredMetadataModel> visitedTypesParameter) {
        this(type);
        visitedTypes = visitedTypesParameter;
    }

    public List<MetaDataField> createFields(){
        List<MetaDataField> metaDataFields = new ArrayList<MetaDataField>();
        if (visitedTypes == null) {
            visitedTypes = new HashMap<JSONObjectType, DefaultStructuredMetadataModel>();
        }
        loadFields((JSONObjectType) jsonSchemaType, metaDataFields);
        return metaDataFields;
    }

    private void processJSONSchemaElement(JSONType property, String name, List<MetaDataField> metadata)  {
        if (property.isJSONObject()) {
            processJSONSchemaObject((JSONObjectType) property, name, metadata);
        } else if (property.isJSONPrimitive()) {
            processJSONSchemaPrimitive(property, name, metadata);
        } else if (property.isJSONArray()) {
            processJSONSchemaArray((JSONArrayType) property, name, metadata);
        } else if(property.isJSONPointer()){
            processJSONPointer((JSONPointerType)property, name, metadata);
        }
    }

    private void processJSONSchemaObject(JSONObjectType type, String name, List<MetaDataField> metadata){

        DefaultStructuredMetadataModel model = buildJSONMetaDataModel(type);
        metadata.add(new DefaultMetaDataField(name, model));

    }

    private DefaultStructuredMetadataModel buildJSONMetaDataModel(JSONObjectType type) {

        DefaultStructuredMetadataModel model;
        if (visitedTypes.containsKey(type)) {
            model = visitedTypes.get(type);
        } else {
            model = new DefaultStructuredMetadataModel(DataType.JSON);
            visitedTypes.put(type, model);
            model.loadFieldsFrom(new JSONSchemaMetaDataFieldFactory(type, visitedTypes));
        }
        return model;
    }

    private void loadFields(JSONObjectType type, List<MetaDataField> metadata)  {
        String[] properties = type.getProperties();
        for (String key : properties) {
            JSONType propertyType = type.getPropertyType(key);
            processJSONSchemaElement(propertyType, key, metadata);
        }
    }

    private void processJSONSchemaArray(JSONArrayType property, String name, List<MetaDataField> metadata){
        JSONType itemsType = property.getItemsType();

        if (itemsType.isJSONPrimitive()) { // Case List<String>
            DataType dataType = getDataType(itemsType);
            MetaDataModel model = dataType==DataType.UNKNOWN ? new DefaultUnknownMetaDataModel(): new DefaultSimpleMetaDataModel(dataType);
            metadata.add(new DefaultMetaDataField(name, new DefaultListMetaDataModel(model)));
        }else {
            DefaultStructuredMetadataModel model = null;
            if(itemsType.isJSONPointer()){
                 model = buildJSONMetaDataModel((JSONObjectType) ((JSONPointerType) itemsType).resolve());
            }else if (itemsType.isJSONObject()){
                 model = buildJSONMetaDataModel((JSONObjectType) itemsType);
            }
            metadata.add(new DefaultMetaDataField(name, new DefaultListMetaDataModel(model)));
        }

    }

    private void processJSONSchemaPrimitive(JSONType property, String name, List<MetaDataField> metadata) {
        DataType dataType = getDataType(property);
        MetaDataModel model = dataType==DataType.UNKNOWN ? new DefaultUnknownMetaDataModel(): new DefaultSimpleMetaDataModel(dataType);
        metadata.add(new DefaultMetaDataField(name, model));
    }

    private void processJSONPointer(JSONPointerType ptr, String name, List<MetaDataField> metadata) {
        processJSONSchemaElement(ptr.resolve(), name, metadata);
    }

    private DataType getDataType(JSONType jsonType) {
        return JSONTypeUtils.getDataType(jsonType);
    }

}
