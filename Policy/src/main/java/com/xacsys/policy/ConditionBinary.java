package com.xacsys.policy;

import java.util.Set;

/**
 * ConditionBinary.
 * 
 * The regular lhs OP rhs item.
 *
 */
public class ConditionBinary  extends ConditionBase {
	
	/**
	 * Binary Condition OPERATOR.
	 * 
	 * Ideally we should use this single definition for all places
	 * in the CPP codebase.
	 *
	 */
	public enum OPERATOR {
		EQUALS,
		MATCHES,
		IN,
		STARTS_WITH,
		ENDS_WITH,
		CONTAINS
	}
	
	/** lhs. */
	private transient final String lhs;
	
	/** rhs. */
	private transient final String rhs;
	
	/** operator. */
	private transient final OPERATOR operator;

	/**
	 * ConditionBinary.
	 * 
	 * The constructor for the Binary Condition Object.
	 * 
	 * @param lhs
	 * @param operator
	 * @param rhs
	 */
	public ConditionBinary(
			final String lhs,
			final OPERATOR operator,
			final String rhs) {
		super();
		this.lhs = lhs;
		this.operator = operator;
		this.rhs = rhs;
	}

	/* (non-Javadoc)
	 * @see core.ConditionBase#evaluate(core.EvaluationContext, java.lang.String)
	 */
	@Override
	public boolean evaluate (
			final EvaluationContext evaluationContext,
			final String path) {
		boolean isTrue = false;
		
		if (null != operator) {
			//We make the assumption that a value may have multiple values
			//so we will test all of them,
			final Set <String> lhsValues = evaluationContext.getAttributeValues(lhs);
			final String rhsValue = rhs;
	
			if (lhsValues != null) {
				for (String lhsValue : lhsValues) {
					if  (evaluateSingleValue(rhsValue, lhsValue)) {
						isTrue = true;
						break;
					}
				}
			}
		
			//Add to the trace info in the context the 
			//description of what happened.
			trace(evaluationContext, path, isTrue, lhsValues);
		}
		
		return isTrue;
	}

	/**
	 * evaluateSingleValue.
	 * 
	 * @param rhsValue
	 * @param lhsValue
	 * @return true if matches according to the operator.
	 */
	private boolean evaluateSingleValue(
			final String rhsValue,
			final String lhsValue) {
		boolean match = false;
		if (lhsValue != null) {
			switch (operator) {
				case EQUALS:
					match = compareEquals(lhsValue, rhsValue);
					break;
				case MATCHES:
					match = compareMatches(lhsValue, rhsValue);
					break;
				case IN:
					match = compareIn(lhsValue, rhsValue);
					break;
				case STARTS_WITH:
					match = compareStartsWith(lhsValue, rhsValue);
					break;
				case ENDS_WITH:
					match = compareEndsWith(lhsValue, rhsValue);
					break;
				case CONTAINS:
					match = compareContains(lhsValue, rhsValue);
					break;
				default :
					break;
			}
		}
		return match;
	}


	
	/**
	 * @param lhsValues
	 * @param rhsValue
	 * @return
	 */
	private boolean compareEquals (
			final String lhsValue,
			final String rhsValue) {
		return lhsValue.equals(rhsValue);
	}
	
	/**
	 * @param lhsValues
	 * @param rhsValue
	 * @return
	 */
	private boolean compareMatches (
			final String lhsValue,
			final String rhsValue) {
		return lhsValue.matches(rhsValue);
	}
	
	/**
	 * @param lhsValues
	 * @param rhsValue
	 * @return
	 */
	private boolean compareIn (
			final String lhsValue,
			final String rhsValue) {
		
		String regexPattern = "^";

	    for (int index = 0; index< rhsValue.length(); index++) {
	    	char c = rhsValue.charAt(index);
	        switch (c)
	        {
	            case '*':
	                regexPattern += ".*";
	                break;
	            case '?':
	                regexPattern += ".";
	                break;
	            default:
	                regexPattern += "[" + c + "]";
	                break;
	        }
	    }
	    return lhsValue.matches(regexPattern);
	}
	
	/**
	 * @param lhsValues
	 * @param rhsValue
	 * @return
	 */
	private boolean compareStartsWith (
			final String lhsValue,
			final String rhsValue) {
		return lhsValue.startsWith(rhsValue);
	}
	
	/**
	 * @param lhsValues
	 * @param rhsValue
	 * @return
	 */
	private boolean compareEndsWith (
			final String lhsValue,
			final String rhsValue) {
		return lhsValue.endsWith(rhsValue);
	}
	
	/**
	 * @param lhsValues
	 * @param rhsValue
	 * @return
	 */
	private boolean compareContains (
			final String lhsValue,
			final String rhsValue) {
		return lhsValue.contains(rhsValue);
	}
	
	/**
	 * trace.
	 * 
	 * A slightly messy function used to provide the 
	 * trace information for the context.
	 * 
	 * @param evaluationContext
	 * @param path
	 * @param isTrue
	 * @param lhsValues
	 */
	private void trace(
			final EvaluationContext evaluationContext,
			final String path,
			final boolean isTrue,
			final Set<String> lhsValues) {
		
		final StringBuffer buffer = new StringBuffer();
		
		buffer.append(lhs);
		buffer.append(' ');
		buffer.append(operator);
		buffer.append(' ');
		buffer.append(rhs);
		buffer.append(" [");
		
		boolean first = true;
		if (null == lhsValues) {
			buffer.append("null");
		} else {
			for (String value : lhsValues) {
				if (first) {
					first = false;
				} else {
					buffer.append(',');
				}
				buffer.append(value);
			}
		}

		buffer.append(']');
		
		
		evaluationContext.addTrace(path, buffer.toString(), isTrue);
	}
}
