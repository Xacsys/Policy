package com.xacsys.policy;

import java.util.ArrayList;
import java.util.List;

/**
 * The Policy
 * 
 * Essentially a named set of rules that may be sequentially evaluated.
 * 
 * Note that Rules/Policy do not have ranks, rather we utilise the simple
 * power of a Java list.
 *
 */
public class Policy {

	private final String name;
	private final String description;
	private List <Rule> rules = new ArrayList <> ();
	private Object defaultResult;
	
	public Policy(
			final String name,
			final String description,
			final List<Rule> rules,
			final Object defaultResult) {
		this.name = name;
		this.description = description;	
		this.rules = rules;
		this.defaultResult = defaultResult;
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
	public boolean isValid() {
		return true;
	}

	/**
	 * @return
	 */
	public List<Rule> getRules() {
		return rules;
	}
	
	/**
	 * @return
	 */
	public Object getDefaultResult() {
		return defaultResult;
	}

	
	/**
	 * evaluate.
	 * 
	 * Run through the list until a Rule matches,
	 * Then stop and return the rule.
	 * 
	 * @param evaluationContext
	 * @return
	 */
	public Object evaluate (final EvaluationContext evaluationContext) {
		Object selected = null;
		
		for (Rule rule : rules) {
			if (rule.evaluate (evaluationContext)) {
				selected = rule.getResult();
				break;
			}
		}
		
		if (selected == null) {
			selected = defaultResult;
		}
		
		return selected;
	}
}
