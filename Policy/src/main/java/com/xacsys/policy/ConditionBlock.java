package com.xacsys.policy;

import java.util.List;

public class ConditionBlock extends ConditionBase {

	/**
	 * The BLock Type: TYPE
	 *
	 */
	public enum TYPE {
		AND,
		OR
	}
	
	private TYPE type;
	private List <ConditionBase> subConditions;
	
	/**
	 * Constructor.
	 * 
	 * Simply: the Type of the block and its list of subordinates.
	 * 
	 * @param type
	 * @param subConditions
	 */
	public ConditionBlock(
			final TYPE type,
			final List<ConditionBase> subConditions) {
		super();
		this.type = type;
		this.subConditions = subConditions;
	}
	
	/**
	 * @return the type
	 */
	public TYPE getType() {
		return type;
	}
	
	/**
	 * @return the subordinate conditions
	 */
	public List<ConditionBase> getSubConditions() {
		return subConditions;
	}
	
	/* (non-Javadoc)
	 * @see core.ConditionBase#evaluate(core.EvaluationContext, java.lang.String)
	 * 
	 * Though AND and OR are similar to evaluate, they are not quite similar enough,
	 * so in the end, decided to split their implementation into two simpler methods
	 * rather than try to cram the logic into one loop.
	 */
	public boolean evaluate (
			final EvaluationContext evaluationContext,
			final String path) {
		boolean isTrue = false;
		if (null != type) {
			if (type.equals(TYPE.OR)) {
				isTrue = evaluateOR(evaluationContext, path);
			} else {
				isTrue = evaluateAND(evaluationContext, path);
			}
		}
		return isTrue;
	}
	
	/**
	 * Evaluate the ORs
	 * 
	 * which is a case of running through the subordinates
	 * until any are true, then OR is true.
	 * 
	 * @param evaluationContext
	 * @param path
	 * @return
	 */
	private boolean evaluateOR (
			final EvaluationContext evaluationContext,
			final String path) {
		boolean isTrue = false;
		String nextPath = path + "." + TYPE.OR.toString();
		int child = 0;
		for (ConditionBase subCondition : subConditions) {
			String childPath = nextPath + "." + child++;
			boolean matches = subCondition.evaluate(evaluationContext, childPath);
			if (matches) {
				isTrue = true;
				break;
			}	
		}

		evaluationContext.addTrace(nextPath, isTrue);

		return isTrue;
	}
	
	/**
	 * evaluateAND
	 * 
	 * which is a case of running though the subordinates
	 * until any are false, then AND is false.
	 * 
	 * @param evaluationContext
	 * @param path
	 * @return
	 */
	private boolean evaluateAND (
			final EvaluationContext evaluationContext,
			final String path) {
		boolean isTrue = false;
		final String nextPath = path + "." + TYPE.AND.toString();
		int child = 0;
		if (!subConditions.isEmpty()) {
			boolean anyFalse = false; 
			for (ConditionBase subCondition : subConditions) {
				String childPath = nextPath + "." + child++;
				boolean matches = subCondition.evaluate(evaluationContext, childPath);		
				if (!matches) {
					anyFalse = true;
					break;
				}
			}
			isTrue = !anyFalse;
		}
		
		evaluationContext.addTrace(nextPath, isTrue);
		
		return isTrue;
	}
}
