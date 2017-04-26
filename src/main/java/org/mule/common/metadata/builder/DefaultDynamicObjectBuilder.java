package org.mule.common.metadata.builder;

import org.mule.common.metadata.DefaultDefinedMapMetaDataModel;
import org.mule.common.metadata.DefinedMapMetaDataModel;
import org.mule.common.metadata.MetaDataField;
import org.mule.common.metadata.datatype.DataType;

import java.util.ArrayList;
import java.util.List;

public class DefaultDynamicObjectBuilder<P extends MetaDataBuilder<?>> implements EnumMetaDataBuilder<P> {
    private String name;
    private List<DefaultMetaDataFieldBuilder> fields;
    private P parentBuilder;

    DefaultDynamicObjectBuilder(String name,P parentBuilder) {
        this.name = name;
        this.parentBuilder = parentBuilder;
        this.fields = new ArrayList<DefaultMetaDataFieldBuilder>();
    }

    @Override
    public PropertyCustomizableMetaDataBuilder<P> addSimpleField(String name, DataType dataType) {
        fields.add(new DefaultMetaDataFieldBuilder(name, new DefaultSimpleMetaDataBuilder(dataType, this)));
        return this;
    }

    @Override
    public PropertyCustomizableMetaDataBuilder<P> addSimpleField(String name, DataType dataType, String implClass) {
    	DefaultSimpleMetaDataBuilder<?> builder = new DefaultSimpleMetaDataBuilder(dataType, this);
    	builder.setImplClass(implClass);
		fields.add(new DefaultMetaDataFieldBuilder(name, builder));
    	return this;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public DynamicObjectFieldBuilder<P> addPojoField(String name, Class<?> pojo) {
        fields.add(new DefaultMetaDataFieldBuilder(name, new DefaultPojoMetaDataBuilder(pojo, this)));
        return this;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public DynamicObjectFieldBuilder<DynamicObjectFieldBuilder<P>> addListOfDynamicObjectField(String name) {
        DefaultListMetaDataBuilder<?> builder = new DefaultListMetaDataBuilder<DynamicObjectFieldBuilder<?>>(this);
        DefaultDynamicObjectBuilder<DynamicObjectFieldBuilder<?>> dynamicObjectBuilder = (DefaultDynamicObjectBuilder) builder.ofDynamicObject(name);
        fields.add(new DefaultMetaDataFieldBuilder(name, builder));
        //Change the parent to this, as list metadata builder is not returned we need to reparent the DynamicObjectBuilder.
        dynamicObjectBuilder.parentBuilder = this;
        return (DynamicObjectFieldBuilder) dynamicObjectBuilder;
    }

    @Override
    public ListMetaDataBuilder<DynamicObjectFieldBuilder<P>> addList(String name) {
        DefaultListMetaDataBuilder<DynamicObjectFieldBuilder<P>> builder = new DefaultListMetaDataBuilder<DynamicObjectFieldBuilder<P>>(this);
        fields.add(new DefaultMetaDataFieldBuilder(name, builder));
        return builder;
    }

    @SuppressWarnings("unchecked")
	@Override
    public DynamicObjectFieldBuilder<DynamicObjectFieldBuilder<P>> addDynamicObjectField(String name) {
    	@SuppressWarnings("rawtypes")
		DefaultDynamicObjectBuilder builder = new DefaultDynamicObjectBuilder<DefaultDynamicObjectBuilder<?>>(name,this);
        fields.add(new DefaultMetaDataFieldBuilder(name, builder));
        return builder;
    }

    @Override
    public P endDynamicObject() {
        return parentBuilder;
    }

    @Override
    public DefinedMapMetaDataModel build() {
        List<MetaDataField> fieldList = new ArrayList<MetaDataField>();
        for (DefaultMetaDataFieldBuilder field : fields)
        {
            fieldList.add(field.build());
        }
        return new DefaultDefinedMapMetaDataModel(fieldList, name);
    }


    private DefaultMetaDataFieldBuilder getCurrentField() {
        return fields.get(fields.size() - 1);
    }

    @Override
    public DynamicObjectFieldBuilder<P> withAccessType(MetaDataField.FieldAccessType accessType) {
        getCurrentField().withAccessType(accessType);
        return this;
    }

	@Override
	public PropertyCustomizableMetaDataBuilder<P> isSelectCapable(boolean capable) {
		getCurrentField().isSelectCapable(capable);
		return this;
	}

	@Override
	public PropertyCustomizableMetaDataBuilder<P> isOrderByCapable(boolean capable) {
		getCurrentField().isOrderByCapable(capable);
		return this;
	}

	@Override
	public CustomizingWhereMetaDataFieldBuilder<P> isWhereCapable(boolean capable) {
		getCurrentField().isWhereCapable(capable);
		return this;
	}

	@Override
	public AddingOperatorsMetaDataFieldBuilder<P> withSpecificOperations() {
		return this;
	}

	@Override
	public PropertyCustomizableMetaDataBuilder<P> withDefaultOperations() {
		return this;
	}

	@Override
	public AddingOperatorsMetaDataFieldBuilder<P> supportsEquals() {
		getCurrentField().supportsEquals();
		return this;
	}

	@Override
	public AddingOperatorsMetaDataFieldBuilder<P> supportsNotEquals() {
		getCurrentField().supportsNotEquals();
		return this;
	}

	@Override
	public AddingOperatorsMetaDataFieldBuilder<P> supportsGreater() {
		getCurrentField().supportsGreater();
		return this;
	}

	@Override
	public AddingOperatorsMetaDataFieldBuilder<P> supportsGreaterOrEquals() {
		getCurrentField().supportsGreaterOrEquals();
		return this;
	}

	@Override
	public AddingOperatorsMetaDataFieldBuilder<P> supportsLess() {
		getCurrentField().supportsLess();
		return this;
	}

	@Override
	public AddingOperatorsMetaDataFieldBuilder<P> supportsLessOrEquals() {
		getCurrentField().supportsLessOrEquals();
		return this;
	}

	@Override
	public AddingOperatorsMetaDataFieldBuilder<P> supportsLike() {
		getCurrentField().supportsLike();
		return this;
	}

	@Override
	public EnumMetaDataBuilder<P> setValues(String... strings) {
		getCurrentField().setEnumValues(strings);
		return this;
	}

	@Override
	public EnumMetaDataBuilder<P> addEnumField(String name) {
        fields.add(new DefaultMetaDataFieldBuilder(name, new DefaultSimpleMetaDataBuilder(DataType.ENUM, this)));
        return this;
	}

    @Override
    public EnumMetaDataBuilder<P> addEnumField(String name, String implClass) {
        DefaultSimpleMetaDataBuilder<?> builder = new DefaultSimpleMetaDataBuilder(DataType.ENUM, this);
        builder.setImplClass(implClass);
        fields.add(new DefaultMetaDataFieldBuilder(name, builder));
        return this;
    }

	@Override
	public PropertyCustomizableMetaDataBuilder<P> setExample(String example) {
		getCurrentField().setExample(example);
		return this;
	}

    @Override
    public PropertyCustomizableMetaDataBuilder<P> setLabel(String label) {
        getCurrentField().setLabel(label);
        return this;
    }

    @Override
    public PropertyCustomizableMetaDataBuilder<P> setDescription(String description) {
        getCurrentField().setDescription(description);
        return this;
    }

    @Override
	public EnumMetaDataBuilder<P> setValues(List<String> values) {
		getCurrentField().setEnumValues(values.toArray(new String[values.size()]));
		return this;
	}

    public PropertyCustomizableMetaDataBuilder<P> setSortKey(String key) {
        getCurrentField().setSortKey(key);
        return this;
    }
}
