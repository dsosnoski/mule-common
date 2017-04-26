/**
 *
 */
package org.mule.common.metadata.builder;

import org.mule.common.metadata.DefaultMetaDataField;
import org.mule.common.metadata.property.DescriptionMetaDataProperty;
import org.mule.common.metadata.property.LabelMetaDataProperty;
import org.mule.common.metadata.MetaDataField;
import org.mule.common.metadata.MetaDataModel;
import org.mule.common.metadata.field.property.*;
import org.mule.common.metadata.field.property.dsql.DsqlOrderMetaDataFieldProperty;
import org.mule.common.metadata.field.property.dsql.DsqlQueryOperatorsMetaDataFieldProperty;
import org.mule.common.metadata.field.property.dsql.DsqlSelectMetaDataFieldProperty;
import org.mule.common.metadata.field.property.dsql.DsqlWhereMetaDataFieldProperty;
import org.mule.common.query.expression.*;

import java.util.ArrayList;
import java.util.List;

public class DefaultMetaDataFieldBuilder implements MetaDataFieldBuilder
{

    private String name;
    private List<Class<? extends MetaDataFieldProperty>> undesiredFieldProperties;
    private MetaDataField.FieldAccessType accessType;
    private MetaDataBuilder<?> builder;
    private List<Operator> supportedOperators;
    private String[] enumValues = null;
    private String exampleString = null;
    private String label;
    private String description;
    private String sortKey;

    DefaultMetaDataFieldBuilder(String name, MetaDataBuilder<?> builder)
    {
        this.name = name;
        this.builder = builder;
        this.supportedOperators = new ArrayList<Operator>();
        this.undesiredFieldProperties = new ArrayList<Class<? extends MetaDataFieldProperty>>();
    }

    public MetaDataFieldBuilder isSelectCapable(boolean capable)
    {
        if (!capable)
        {
            undesiredFieldProperties.add(DsqlSelectMetaDataFieldProperty.class);
        }
        return this;
    }

    public MetaDataFieldBuilder isOrderByCapable(boolean capable)
    {
        if (!capable)
        {
            undesiredFieldProperties.add(DsqlOrderMetaDataFieldProperty.class);
        }
        return this;
    }

    public void isWhereCapable(boolean capable)
    {
        if (!capable)
        {
            undesiredFieldProperties.add(DsqlWhereMetaDataFieldProperty.class);
        }
    }

    public void setEnumValues(String... values)
    {
        enumValues = values;
    }

    public void setExample(String example)
    {
        this.exampleString = example;
    }

    public void supportsEquals()
    {
        supportedOperators.add(new EqualsOperator());
    }

    public void supportsNotEquals()
    {
        supportedOperators.add(new NotEqualsOperator());
    }

    public void supportsGreater()
    {
        supportedOperators.add(new GreaterOperator());
    }

    public void supportsGreaterOrEquals()
    {
        supportedOperators.add(new GreaterOrEqualsOperator());
    }

    public void supportsLess()
    {
        supportedOperators.add(new LessOperator());
    }

    public void supportsLessOrEquals()
    {
        supportedOperators.add(new LessOrEqualsOperator());
    }

    public void supportsLike()
    {
        supportedOperators.add(new LikeOperator());
    }

    public void withAccessType(MetaDataField.FieldAccessType accessType)
    {
        this.accessType = accessType;
    }


    public void setLabel(String label)
    {
        this.label = label;
    }

    public void setDescription(String description)
    {

        this.description = description;
    }

    public void setSortKey(String sortKey)
    {
        this.sortKey = sortKey;
    }

    public MetaDataField build()
    {

        DefaultFieldPropertyFactory defaultFieldPropertyFactory = new DefaultFieldPropertyFactory();
        MetaDataModel model = builder.build();

        // add default properties if empty
        List<MetaDataFieldProperty> fieldProperties = new ArrayList<MetaDataFieldProperty>();
        fieldProperties.addAll(defaultFieldPropertyFactory.getProperties("null", model));
        if (!supportedOperators.isEmpty())
        {
            for (MetaDataFieldProperty metaDataFieldProperty : fieldProperties)
            {
                if (metaDataFieldProperty instanceof DsqlQueryOperatorsMetaDataFieldProperty)
                {
                    DsqlQueryOperatorsMetaDataFieldProperty operatorsMetaDataFieldProperty = (DsqlQueryOperatorsMetaDataFieldProperty) metaDataFieldProperty;
                    operatorsMetaDataFieldProperty.setSupportedOperators(supportedOperators);
                }
            }
        }

        List<MetaDataFieldProperty> finalFieldProperties = new ArrayList<MetaDataFieldProperty>();
        // filter undesired properties
        for (MetaDataFieldProperty property : fieldProperties)
        {
            if (!undesiredFieldProperties.contains(property.getClass()))
            {
                finalFieldProperties.add(property);
            }
        }

        if (description != null)
        {
            finalFieldProperties.add(new DescriptionMetaDataProperty(description));
        }

        if (label != null)
        {
            finalFieldProperties.add(new LabelMetaDataProperty(label));
        }
        
        if (sortKey != null)
        {
            finalFieldProperties.add(new SortKeyMetaDataProperty(sortKey));
        }

        if (enumValues != null)
        {
            finalFieldProperties.add(new ValidStringValuesFieldProperty(enumValues));
        }

        if (exampleString != null)
        {
            finalFieldProperties.add(new ExampleFieldProperty(exampleString));
        }

        if (accessType == null)
        {
            accessType = MetaDataField.FieldAccessType.READ_WRITE;
        }
        return new DefaultMetaDataField(name, model, accessType, finalFieldProperties);
    }


}
