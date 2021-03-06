package org.mule.common.metadata.field.property.dsql;

import org.mule.common.query.expression.Operator;

import java.util.List;

/**
 * Properties regarding operators in WHERE conditions
 */
public class DsqlQueryOperatorsMetaDataFieldProperty implements DsqlMetaDataFieldProperty {
    private List<Operator> supportedOperators;

    public DsqlQueryOperatorsMetaDataFieldProperty(List<Operator> supportedOperators) {
        this.supportedOperators = supportedOperators;
    }

    public List<Operator> getSupportedOperators() {
        return supportedOperators;
    }

    public void setSupportedOperators(List<Operator> supportedOperators) {
        this.supportedOperators = supportedOperators;
    }
}
