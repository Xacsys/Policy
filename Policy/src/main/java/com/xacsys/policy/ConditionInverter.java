package com.xacsys.policy;

/**
 * ConditionInverter.
 * 
 * Rather than putting a possible NOT into every 
 * condition, this engine uses an explicit Inverter.
 * 
 * This emphasizes the idea of this engine of plugging the 
 * conditions together like LEGO.
 *
 */
public class ConditionInverter  extends ConditionBase {

	/** subCondition. */
	private final ConditionBase subCondition;
	


	/**
	 * Construct an inverter.
	 * 
	 * @param subCondition
	 */
	public ConditionInverter (final ConditionBase subCondition) {
		super();
		this.subCondition = subCondition;
	}
	
	/**
	 * getSubCondition.
	 * @return the sub condition.
	 */
	public ConditionBase getSubCondition() {
		return subCondition;
	}

	/* (non-Javadoc)
	 * @see core.ConditionBase#evaluate(core.EvaluationContext, java.lang.String)
	 */
	@Override
	public boolean evaluate (
			final EvaluationContext evaluationContext,
			final String path) {
		boolean isTrue = false;
		
		if (subCondition != null) {
			final String nextPath = path + ".NOT";
			isTrue = !subCondition.evaluate(evaluationContext, nextPath);
			evaluationContext.addTrace(path, isTrue);
		}
		
		return isTrue;
	}
	
}
