package com.xacsys.policy;

import java.util.Set;

/**
 * ConditionUnary.
 * 
 * This engine supports unary conditions.
 *
 */
public class ConditionUnary extends ConditionBase {

	/**
	 * The type of the condition
	 *
	 */
	public enum OPERATOR {
		EXISTS
	}
	
	private String lhs;
	private OPERATOR operator;
	
	/**
	 * Construct a unary conditon
	 * @param lhs
	 * @param operator
	 */
	public ConditionUnary(
			final String lhs,
			final OPERATOR operator) {		
		this.lhs = lhs;
		this.operator = operator;
	}

	/* (non-Javadoc)
	 * @see core.ConditionBase#evaluate(core.EvaluationContext, java.lang.String)
	 */
	public boolean evaluate (
			final EvaluationContext evaluationContext,
			final String path) {
		boolean isTrue = false;
		
		final Set <String> lhsValue = evaluationContext.getAttributeValues(lhs);

		switch (operator) {
			case EXISTS:
				if ((lhsValue != null) && (!lhsValue.isEmpty())) {
					isTrue = true;
				}
				break;
			default :
				break;
		}
		
		trace(evaluationContext, path, isTrue);
	
		return isTrue;
	}
	
	/**
	 * Add the trace of what occurred onto the trace log in the context.
	 * 
	 * @param evaluationContext
	 * @param path
	 * @param isTrue
	 */
	private void trace(
			final EvaluationContext evaluationContext,
			final String path,
			final boolean isTrue) {
		
		final StringBuffer buffer = new StringBuffer();
		buffer.append(lhs);
		buffer.append(' ');
		buffer.append(operator);
		
		evaluationContext.addTrace(path, buffer.toString(), isTrue);
	}
}
