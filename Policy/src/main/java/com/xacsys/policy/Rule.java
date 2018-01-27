package com.xacsys.policy;


/**
 * Rule.
 * 
 * Contains any kind of condition as its LHS
 * 
 * and any kind of object as its RHS.
 * 
 * Evaluation is passed to the condition.
 *
 */
public class Rule {
	
	private String id;
	private ConditionBase condition;
	private Object result;
	private String name;
	private String description;
	
	public Rule(
			final String name,
			final String description,
			final ConditionBase condition,
			final Object result) {
		
		this.name = name;
		this.description = description;	
		this.condition = condition;
		this.result = result;
	}
	
	
	/**
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @return
	 */
	public ConditionBase getCondition() {
		return condition;
	}

	/**
	 * @return
	 */
	public Object getResult() {
		return result;
	}
	
	/**
	 * @param evaluationContext
	 * @return
	 */
	public boolean evaluate (final EvaluationContext evaluationContext) {
		final boolean isTrue = condition.evaluate(evaluationContext, name + ".CONDITION");

		evaluationContext.addTrace(this.getName(), isTrue);

		return isTrue; 
	}
}
