package com.xacsys.policy;

import java.util.HashMap;
import java.util.Map;

/**
 * ConditionLibrary.
 * 
 * The idea of the condition library uses three elements:
 * 
 * 1) The Reference Condition. This is simply a condition
 * that can be referred by another condition and links to an 
 * encapsulated Shared Condition
 * 
 * 2) The Shared Condition. Simply a named object which contains any condition
 * 
 * 3) This Store of Shared Condiitons, here: The Condition Library
 *
 */
public class ConditionLibrary {

	/** library */
	private final transient Map <String, SharedCondition> library = new HashMap <> ();
	
	/**
	 * loadShared. Add a Shared condition to the store
	 * 
	 * @param condition
	 */
	public void loadShared(final SharedCondition condition) {
		library.put(condition.getLibraryId(), condition);
	}
	
	/**
	 * removeShared. 
	 * @param id
	 */
	public void removeShared(final String id) {
		if (library.containsKey(id)) {
			library.remove(id);
		}
	}
	
	/**
	 * getShared.
	 * 
	 * @param id
	 * @return
	 */
	public SharedCondition getShared(final String id) {
		SharedCondition item = null;
		if (library.containsKey(id)) {
			item = library.get(id);
		}
		return item;
	}
}
