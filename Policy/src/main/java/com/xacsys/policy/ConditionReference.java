package com.xacsys.policy;


/**
 * ConditionReference
 *
 * The Reference Condition.
 * 
 * This is the condition that is plugged into the
 * Condition structure that simply takes the id of a shared condition.
 */
public class ConditionReference extends ConditionBase {
	
	/** libraryId */
	private final String libraryId;
	
	/**
	 * ConditionReference.
	 * 
	 * @param libraryId
	 */
	public ConditionReference(
			final String libraryId) {
		super();
		this.libraryId = libraryId;
	}
	
	/**
	 * @return the Library ID.
	 */
	public String getLibraryId() {
		return libraryId;
	}

	/* (non-Javadoc)
	 * @see core.ConditionBase#evaluate(core.EvaluationContext, java.lang.String)
	 */
	public boolean evaluate (
			final EvaluationContext evaluationContext,
			final String path) {
		boolean isTrue = false;
		
		final ConditionLibrary library = evaluationContext.getLibrary();
		if (library != null) {
			final SharedCondition shared = library.getShared(libraryId);
			if (shared != null) {
				final ConditionBase condition = shared.getCondition();
				if (condition != null) {
					isTrue = condition.evaluate(evaluationContext, path + ".REF(" + libraryId + ")");
				}
			}
		}
		
		return isTrue;
	}
}
