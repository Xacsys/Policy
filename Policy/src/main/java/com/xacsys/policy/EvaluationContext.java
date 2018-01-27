package com.xacsys.policy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * EvaluationContext.
 * 
 * This is the structure that is pre-filled with the facts that the rules are
 * to be evaluated against. Also contains the reference to the 
 * condition library. It is up to the invoker of the engine
 * whether the condition library is a singleton.
 *
 */
public class EvaluationContext extends EvaluationTrace {

	private final Map <String, Set <String>> attributes = new HashMap <> ();
	private final ConditionLibrary library;

	/**
	 * EvaluationContext.
	 * 
	 * @param library pass in a reference to the library,
	 */
	public EvaluationContext(
			final ConditionLibrary library) {
		super();
		this.library = library;
	}

	/**
	 * addAttributeValue.
	 * 
	 * put an attribute value onto the context.
	 * So far the engine uses just strings.
	 * 
	 * More than one value can be added to an attribute
	 * 
	 * @param name
	 * @param value
	 */
	public void addAttributeValue(
			final String name,
			final String value) {
		
		Set <String> values = null;
		if (attributes.containsKey(name)) {
			values = attributes.get(name);
		} else {
			values = new HashSet <String> ();
			attributes.put(name, values);
		}
		
		values.add(value);
	}
	
	/**
	 * getAttributeValues.
	 * 
	 * Get all the values,
	 * 
	 * @param name
	 * @return
	 */
	public Set <String> getAttributeValues(
			final String name) {
		
		Set <String> values = null;
		if (attributes.containsKey(name)) {
			values = attributes.get(name);
		}
		return values;
	}

	/**
	 * Get the library.
	 * 
	 * @return
	 */
	public ConditionLibrary getLibrary() {
		return library;
	}

	
}
