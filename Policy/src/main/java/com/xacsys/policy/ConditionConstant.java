package com.xacsys.policy;

/**
 * ConditionConstant.
 * 
 * This condition simply evaluates to the condition it was constructed with.
 *
 * It saves having to synthesize some contrived condition if a rule needs to 
 * always evaluate to true.
 */
public class ConditionConstant extends ConditionBase {

	/** CONSTANT. */
	private static final String CONSTANT = "Constant";
	
	/** Is this constant true or false. */
	private final transient boolean isTrue;

	/**
	 * @param isTrue
	 */
	public ConditionConstant(final boolean isTrue) {
		super();
		this.isTrue = isTrue;
	}
	
	/**
	 * @return isTrue
	 */
	public boolean isTrue() {
		return isTrue;
	}

	/* (non-Javadoc)
	 * @see core.ConditionBase#evaluate(core.EvaluationContext, java.lang.String)
	 */
	@Override
	public boolean evaluate(
			final EvaluationContext evaluationContext,
			final String path) {
		evaluationContext.addTrace(path, CONSTANT, isTrue);
		return isTrue;
	}
}
