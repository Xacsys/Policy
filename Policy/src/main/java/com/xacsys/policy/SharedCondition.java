package com.xacsys.policy;

/**
 * SharedCondition.
 * 
 * Simply a named wrapper around a Condition, with an id.
 *
 */
public class SharedCondition {
	
	private String name;
	private String description;
	private String libraryId;
	private ConditionBase condition;

	public SharedCondition(String name, String description, String libraryId, ConditionBase condition) {
		this.name = name;
		this.description = description;
		this.libraryId = libraryId;
		this.condition = condition;
	}
	
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public String getLibraryId() {
		return libraryId;
	}
	public ConditionBase getCondition() {
		return condition;
	}

}
