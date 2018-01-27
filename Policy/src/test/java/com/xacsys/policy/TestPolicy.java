package com.xacsys.policy;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;


public class TestPolicy {

	
	@Test
	/**
	 * TestBasic
	 * 
	 * Demonstrate basic operation with a set of simple binary rules.
	 * 
	 * @return ok if passes
	 */
	public void TestBasic() {
	boolean passed = false;
		
		//Set up the policy
		final Policy policy = new Policy ("My Policy","My Policy Description", Arrays.asList(
			new Rule("Rule1", "Rule1 Description", new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.EQUALS, "one"), "SGT1"),
			new Rule("Rule2", "Rule2 Description", new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.EQUALS, "two"), "SGT2"),
			new Rule("Rule3", "Rule3 Description", new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.EQUALS, "three"), "SGT3")
		), null);
		
		//Initialize the context
		final EvaluationContext context = new EvaluationContext(null);
		context.addAttributeValue("tag1", "two");
		
		//Execute the policy
		final Object result = policy.evaluate(context);
		
		
		//Check result is as expected
		if ((result != null) && "SGT2".equals(result)) {
			passed = true;
		}
		
		assertTrue(passed);
	}
	
	@Test
	/**
	 * constantTest
	 * 
	 * Demonstrate basic operation with a set of simple binary rules.
	 * 
	 * @return ok if passes
	 */
	public final void constantTest() {
		boolean passed = false;
		
		//Set up the policy
		final Policy policy = new Policy ("My Policy","My Policy Description", Arrays.asList(
			new Rule("Rule1", "Rule1 Description", new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.EQUALS, "one"), "SGT1"),
			new Rule("Rule2", "Rule2 Description", new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.EQUALS, "two"), "SGT2"),
			new Rule("Rule3", "Rule3 Description", new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.EQUALS, "three"), "SGT3"),
			new Rule("Rule4", "Rule4 Description", new ConditionConstant ( true), "SGT4")
		), null);
		
		//Initialize the context
		final EvaluationContext context = new EvaluationContext(null);
		context.addAttributeValue("tag1", "four");
		
		//Execute the policy
		final Object result = policy.evaluate(context);
		
		
		//Check result is as expected
		if (result != null && "SGT4".equals(result)) {
			passed = true;
		}
		
		assertTrue(passed);
	}
		
	/**
	 * compoundTest
	 * 
	 * Demonstrate compound conditions
	 * 
	 * @return
	 */
	@Test
	public final void compoundTest() {
		boolean passed = false;
		final Policy policy = new Policy ("My Policy","My Policy Description",Arrays.asList(
			new Rule("Rule1", "Rule1 Description", new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.EQUALS, "one"), "SGT1"),
			
			//SGT2 = (tag1==two) AND (tag2==one OR tag2==three))
			new Rule("Rule2", "Rule2 Description", new ConditionBlock (
				ConditionBlock.TYPE.AND,
				Arrays.asList(
					new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.EQUALS, "two"),
					new ConditionBlock ( 
							ConditionBlock.TYPE.OR,
							Arrays.asList(
								(ConditionBase) new ConditionBinary ( "tag2", ConditionBinary.OPERATOR.EQUALS, "one"),
								(ConditionBase) new ConditionBinary ( "tag2", ConditionBinary.OPERATOR.EQUALS, "three")
							)
						)
					)
				), "SGT2"),
			
			//SGT2 = (tag1==two) AND (tag2==two OR tag2==three))
			new Rule("Rule3", "Rule3 Description", new ConditionBlock (
					ConditionBlock.TYPE.AND,
					Arrays.asList(
						new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.EQUALS, "two"),
						new ConditionBlock (
								ConditionBlock.TYPE.OR,
								Arrays.asList(
									(ConditionBase) new ConditionBinary ( "tag2", ConditionBinary.OPERATOR.EQUALS, "two"),
									(ConditionBase) new ConditionBinary ( "tag2", ConditionBinary.OPERATOR.EQUALS, "three")
								)
							)
						)
					), "SGT3"),
			new Rule("Rule4", "Rule4 Description", new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.EQUALS, "three"), "SGT4")
		), null);
		
		final EvaluationContext context = new EvaluationContext(null);
		context.addAttributeValue("tag1", "two");
		context.addAttributeValue("tag2", "two");
		final Object result = policy.evaluate(context);
		
		if (result != null) {
			if (result.equals("SGT3")) {
				passed = true;
			}
		}
		
		assertTrue(passed);
	}
	
	/**
	 * unaryTest.
	 * 
	 * The policy engine supports unary operations such as, does tag1 exist?
	 * 
	 * @return
	 */
	@Test
	public final void unaryTest() {
		boolean passed = false;
		
		final Policy policy = new Policy ("My Policy","My Policy Description",Arrays.asList(
			new Rule("Rule1", "Rule1 Description", new ConditionUnary ( "tag1", ConditionUnary.OPERATOR.EXISTS), "SGT1"),
			new Rule("Rule2", "Rule2 Description", new ConditionUnary ( "tag2", ConditionUnary.OPERATOR.EXISTS), "SGT2"),
			new Rule("Rule3", "Rule3 Description", new ConditionUnary ( "tag3", ConditionUnary.OPERATOR.EXISTS), "SGT3")
		), null);
		
		final EvaluationContext context = new EvaluationContext(null);
		context.addAttributeValue("tag2", "two");
		final Object result = policy.evaluate(context);
		
		if (result != null) {
			if (result.equals("SGT2")) {
				passed = true;
			}
		}
		
		assertTrue(passed);
	}
	
	
	/**
	 * inverterTest.
	 * 
	 * The engine uses a "CondiitonInverter" block to insert NOT where needed
	 * 
	 * @return
	 */
	@Test
	public final void inverterTest() {
		boolean passed = false;
		
		final Policy policy = new Policy ("My Policy","My Policy Description",Arrays.asList(
			new Rule("Rule1", "Rule1 Description", new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.EQUALS, "three"), "SGT1"),
			new Rule("Rule2", "Rule2 Description", new ConditionBlock (
				ConditionBlock.TYPE.AND,
				Arrays.asList(
					//Not the enclosed condition
					new ConditionInverter(new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.EQUALS, "one")),
					new ConditionBlock ( 
							ConditionBlock.TYPE.OR,
							Arrays.asList(
								(ConditionBase) new ConditionBinary ( "tag2", ConditionBinary.OPERATOR.EQUALS, "two"),
								(ConditionBase) new ConditionBinary ( "tag2", ConditionBinary.OPERATOR.EQUALS, "three")
							)
						)
					)
				), "SGT2"),
			new Rule("Rule3", "Rule3 Description", new ConditionBlock (
					ConditionBlock.TYPE.AND,
					Arrays.asList(
						new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.EQUALS, "two"),
						new ConditionBlock (
								ConditionBlock.TYPE.OR,
								Arrays.asList(
									(ConditionBase) new ConditionBinary ( "tag2", ConditionBinary.OPERATOR.EQUALS, "two"),
									(ConditionBase) new ConditionBinary ( "tag2", ConditionBinary.OPERATOR.EQUALS, "three")
								)
							)
						)
					), "SGT3"),
			new Rule("Rule4", "Rule4 Description", new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.EQUALS, "three"), "SGT4")
		), null);
		
		final EvaluationContext context = new EvaluationContext(null);
		context.addAttributeValue("tag1", "two");
		context.addAttributeValue("tag2", "two");
		final Object result = policy.evaluate(context);
		
		if (result != null) {
			if (result.equals("SGT2")) {
				passed = true;
			}
		}
		
		assertTrue(passed);
	}
	
	/**
	 * libraryTest. 
	 * @return
	 */
	@Test
	public final void libraryTest() {
		boolean passed = false;
		
		final Policy policy = new Policy ("My Policy","My Policy Description",Arrays.asList(
			new Rule("", "", new ConditionReference ( "lib1"), "SGT1"),
			new Rule("", "", new ConditionReference ( "lib2"), "SGT2"),
			new Rule("", "", new ConditionReference ( "lib3"), "SGT3")
		), null);
		
		ConditionLibrary library = new ConditionLibrary();
		
		final SharedCondition lib1 = new SharedCondition(
				"MySharedCondition1",
				"tag1 == two",
				"lib1",
				new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.EQUALS, "two")
				);

		final SharedCondition lib2 = new SharedCondition (
				"MySharedCondition2",
				"tag1 == three AND (tag2 == two OR tag2 == three)",
				"lib2",
				new ConditionBlock (
					ConditionBlock.TYPE.AND,
					Arrays.asList(
						new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.EQUALS, "three"),
						new ConditionBlock (
								ConditionBlock.TYPE.OR,
								Arrays.asList(
									(ConditionBase) new ConditionBinary ( "tag2", ConditionBinary.OPERATOR.EQUALS, "two"),
									(ConditionBase) new ConditionBinary ( "tag2", ConditionBinary.OPERATOR.EQUALS, "three")
								)
							)
						)
					)
				);

		final SharedCondition lib3 = new SharedCondition(
				"MySharedCondition3",
				"tag1 EXISTS",
				"lib3",
				new ConditionUnary ( "tag1", ConditionUnary.OPERATOR.EXISTS)
				);

		//Add the library conditions to a library
		library.loadShared(lib1);
		library.loadShared(lib2);
		library.loadShared(lib3);
		
		//Create an evaluation context and utilise the library.
		//Then add the facts.
		final EvaluationContext context = new EvaluationContext(library);
		context.addAttributeValue("tag1", "three");
		context.addAttributeValue("tag2", "three");
		
		//Now run the thing.
		final Object result = policy.evaluate(context);
		
		//Check out the result.
		if (result != null) {
			if (result.equals("SGT2")) {
				passed = true;
			}
		}
		
		assertTrue(passed);
	}
	
	
	
	/**
	 * breakItTest1
	 * 
	 * Try to cause a Null Pointer Exception.
	 * 
	 * @return ok if passes
	 */
	@Test
	public final void breakItTest1() {
		boolean passed = false;
		
		//Set up the policy
		final Policy policy = new Policy ("My Policy","My Policy Description", Arrays.asList(
			new Rule(null, null, new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.EQUALS, "one"), "SGT1"),
			new Rule("Rule1", "Rule1 Description", new ConditionBinary ( null, ConditionBinary.OPERATOR.EQUALS, "two"), null),
			new Rule("Rule2", "Rule2 Description", new ConditionBinary ( null, ConditionBinary.OPERATOR.EQUALS, "two"), "SGT2"),
			new Rule("Rule3", "Rule3 Description", new ConditionBinary ( "tag1", null, "three"), "SGT3"),
			new Rule("Rule4", "Rule4 Description", new ConditionBinary ( "tag1", null, null), "SGT3"),
			new Rule("Rule5", "Rule5 Description", new ConditionBlock (
					null,
					Arrays.asList(
							new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.EQUALS, "two"),
							new ConditionBlock (
									ConditionBlock.TYPE.OR,
									Arrays.asList(
										(ConditionBase) new ConditionBinary ( "tag2", ConditionBinary.OPERATOR.EQUALS, "two"),
										(ConditionBase) new ConditionBinary ( "tag2", ConditionBinary.OPERATOR.EQUALS, "three")
									)
								)
							)
						), "SGT3"),
			new Rule("Rule6", "Rule6 Description", new ConditionBlock (
					ConditionBlock.TYPE.AND,
					Arrays.asList(
						new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.EQUALS, "two"),
						new ConditionBlock (
								null,
								Arrays.asList(
									(ConditionBase) new ConditionBinary ( "tag2", ConditionBinary.OPERATOR.EQUALS, "two"),
									(ConditionBase) new ConditionBinary ( "tag2", ConditionBinary.OPERATOR.EQUALS, "three")
								)
							)
						)
					), "SGT3"),
			new Rule("Rule7", "Rule7 Description", new ConditionBlock (
					ConditionBlock.TYPE.AND,
					Arrays.asList(
						new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.EQUALS, "two"),
						new ConditionBlock (
								ConditionBlock.TYPE.OR,
								null
							)
						)
					), "SGT3"),
			new Rule("Rule8", "Rule8 Description", new ConditionReference (null), "SGT1"),
			new Rule("Rule9", "Rule9 Description", new ConditionConstant (true), "SGT9")
		), null);
		
		//Initialize the context
		final EvaluationContext context = new EvaluationContext(null);
		context.addAttributeValue("tag1", "ten");
		context.addAttributeValue(null, "ten");
		context.addAttributeValue("tag1", null);
		
		//Execute the policy
		final Object result = policy.evaluate(context);
		
		
		//Check result is as expected
		if ((result != null) && "SGT9".equals(result)) {
			passed = true;
		}
		
		assertTrue(passed);
	}
	
	/**
	 * operandsTest
	 * 
	 * Demonstrate basic operation with a set of simple binary rules.
	 * 
	 * To make the test quick, we put a NOT in front of the test we expect to pass,
	 * and just add the condition for those we expect to fail.
	 * Hence the test passes if all the rules fail until the last, a constant.
	 * 
	 * @return ok if passes
	 */
	@Test
	public final void operandsTest() {
		boolean passed = false;
		
		//Set up the policy
		final Policy policy = new Policy ("My Policy","My Policy Description", Arrays.asList(
			new Rule("Rule1", "Rule1 Description", new ConditionInverter(new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.EQUALS, "1234567654321")), "SGT1"),
			new Rule("Rule2", "Rule2 Description", new ConditionInverter(new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.CONTAINS, "345676543")), "SGT2"),
			new Rule("Rule3", "Rule3 Description", new ConditionInverter(new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.ENDS_WITH, "7654321")), "SGT3"),
			new Rule("Rule4", "Rule4 Description", new ConditionInverter(new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.STARTS_WITH, "1234567")), "SGT1"),
			new Rule("Rule5", "Rule5 Description", new ConditionInverter(new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.MATCHES, ".*")), "SGT2"),
			new Rule("Rule6", "Rule6 Description", new ConditionInverter(new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.IN, "?2?4?6*")), "SGT3"),
			new Rule("Rule7", "Rule7 Description", new ConditionInverter(new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.IN, "*6*")), "SGT3"),
			new Rule("Rule8", "Rule8 Description", (new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.IN, "?22?4?6*")), "SGT3"),
			new Rule("Rule9", "Rule9 Description", (new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.IN, "?12?4?6*")), "SGT3"),
			new Rule("Rule10", "Rule10 Description", (new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.IN, "2?4?6*")), "SGT3"),
			new Rule("Rule11", "Rule11 Description", (new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.IN, "?2?4?6")), "SGT3"),
			new Rule("Rule12", "Rule12 Description", (new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.IN, "6*")), "SGT3"),
			new Rule("Rule13", "Rule13 Description", (new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.EQUALS, "234567654321")), "SGT1"),
			new Rule("Rule14", "Rule14 Description", (new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.CONTAINS, "3445676543")), "SGT2"),
			new Rule("Rule15", "Rule15 Description", (new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.ENDS_WITH, "765432")), "SGT3"),
			new Rule("Rule16", "Rule16 Description", (new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.STARTS_WITH, "234567")), "SGT1"),
			new Rule("Rule17", "Rule17 Description", (new ConditionBinary ( "tag1", ConditionBinary.OPERATOR.MATCHES, "21.*")), "SGT2")
		), "SGT999");
		
		//Initialize the context
		final EvaluationContext context = new EvaluationContext(null);
		context.addAttributeValue("tag1", "1234567654321");
		
		//Execute the policy
		final Object result = policy.evaluate(context);
		
		
		//Check result is as expected
		if ((result != null) && "SGT999".equals(result)) {
			passed = true;
		}
		
		assertTrue(passed);
	}
}
