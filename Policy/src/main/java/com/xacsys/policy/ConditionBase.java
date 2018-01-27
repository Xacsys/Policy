package com.xacsys.policy;

/**
 * ConditionBase
 *
 * Used to refer to Conditions.
 */
public abstract class ConditionBase {

	/**
	 * evaluate.
	 * 
	 * What the policy engine does.
	 * 
	 * @param evaluationContext
	 * @param path
	 * @return
	 */
	public abstract boolean evaluate(
			final EvaluationContext evaluationContext,
			final String path);

}
