package com.xacsys.policy;

import java.util.ArrayList;
import java.util.List;

/**
 * EvaluationTrace
 *
 */
public class EvaluationTrace {

	/** trace. */
	private final List <String> trace = new ArrayList<> ();
	
	
	/**
	 * Obtain the trace.
	 * 
	 * @return
	 */
	public List<String> getTrace() {
		return trace;
	}

	/**
	 * Add a trace item with no param.
	 * 
	 * @param id
	 * @param result
	 */
	public void addTrace(
			final String id,
			final boolean result) {
		trace.add(id
				+ ": "
				+ result);
	}
	
	/**
	 * Add a trace item with no param.
	 * 
	 * We assume the caller will have a better idea on
	 * how to format the parameters for a trace, rather
	 * than making the trace generic.
	 * 
	 * @param id
	 * @param param
	 * @param result
	 */
	public void addTrace(
			final String id,
			final String param,
			final boolean result) {
		trace.add(id
				+ " (" + param + ") "
				+ ": "
				+ result);
	}
}
