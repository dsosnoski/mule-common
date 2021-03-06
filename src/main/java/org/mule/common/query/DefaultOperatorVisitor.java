package org.mule.common.query;

import org.mule.common.query.expression.OperatorVisitor;

/**
 * <p>Operator visitor intended to translate <strong>DSQL</strong> query operators to your native ones. The behaviour is similar to {@link DefaultQueryVisitor}. With the difference that translation must be atomic (ie, the visit methods must return an string). </p>
 */
public class DefaultOperatorVisitor implements OperatorVisitor {
	public static final String LIKE = " like ";
	public static final String GREATER_OR_EQUALS = " >= ";
	public static final String NOT_EQUALS = " <> ";
	public static final String EQUALS = " = ";
	public static final String LESS_OR_EQUALS = " <= ";
	public static final String GREATER = " > ";
	public static final String LESS = " < ";

	@Override
	public String lessOperator() {
		return LESS;
	}

	@Override
	public String greaterOperator() {
		return GREATER;
	}

	@Override
	public String lessOrEqualsOperator() {
		return LESS_OR_EQUALS;
	}

	@Override
	public String equalsOperator() {
		return EQUALS;
	}

	@Override
	public String notEqualsOperator() {
		return NOT_EQUALS;
	}

	@Override
	public String greaterOrEqualsOperator() {
		return GREATER_OR_EQUALS;
	}

    @Override
    public String likeOperator()
    {
        return LIKE;
    }

    @Override
    public void _dont_implement_OperatorVisitor___instead_extend_DefaultOperatorVisitor() {

    }
}
