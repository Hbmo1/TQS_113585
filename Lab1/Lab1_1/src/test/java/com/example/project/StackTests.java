/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package com.example.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static java.lang.invoke.MethodHandles.lookup;
import static org.junit.platform.commons.logging.LoggerFactory.getLogger;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.platform.commons.logging.Logger;

class StackTests {

	static final Logger log = getLogger(lookup().lookupClass());

	Stack myStack;

	@BeforeEach
	void setup() {
		myStack = new Stack();
	}

	@DisplayName("🧪 Assure stack is empty on initialization")
	@Test
	void newStackIsEmptyTest() {
		// verify
		assertEquals(0, myStack.size());
	}

	@DisplayName("🧪 Assure stack size is zero on initialization")
	@Test
	void newStackSizeIsZeroTest() {
		// verify
		assertEquals(0, myStack.size());
	}

	@DisplayName("🧪 Assure popping from an empty stack throws a NoSuchElementException"
			+ " and does not affect the stack size")
	@Test
	void popEmptyTest() {

		// verify
		assertThrows(NoSuchElementException.class, () -> myStack.pop());
		assertEquals(0, myStack.size());
	}

	@DisplayName("🧪 Assure peeking from an empty stack throws a NoSuchElementException"
			+ " and does not affect the stack size")
	@Test
	void peekEmptyTest() {

		// verify
		assertThrows(NoSuchElementException.class, () -> myStack.peek());
		assertEquals(0, myStack.size());
	}

	@DisplayName("🧪 Test pushing and popping and correct size changes")
	@Test
	void pushThenPopTest() {
		// exercise
		myStack.push(1);
		myStack.push(2);
		myStack.push(3);

		// verify
		assertEquals(3, myStack.size());
		assertEquals(3, myStack.pop());
		assertEquals(2, myStack.pop());
		assertEquals(1, myStack.pop());
		assertEquals(0, myStack.size());
	}

	@DisplayName("🧪 Test pushing n values on empty stack and check if size equals n")
	@Test
	void pushNTest() {

		// setup
		int n = (int) (Math.random() * 100);
		// exercise
		for (int i = 0; i < n; i++) {
			myStack.push(i);
		}

		// verify
		assertEquals(n, myStack.size());

	}

	@DisplayName("🧪 Assure peeking returns the top-most value without afecting stack size")
	@Test
	void pushThenPeekTest() {

		// exercise
		myStack.push(1);
		myStack.push(2);
		myStack.push(3);

		// verify
		assertEquals(3, myStack.size());
		assertEquals(3, myStack.peek());
		assertEquals(3, myStack.size());

	}

	@DisplayName("🧪 Test popping top N values")
	@Test
	void popNTest() {

		// setup
		myStack.push(1);
		myStack.push(2);
		myStack.push(3);
		myStack.push(4);
		myStack.push(5);

		// exercise
		int top = myStack.popTopN(3);

		// verify
		assertEquals(3, top);
		assertEquals(2, myStack.size());
		assertEquals(2, myStack.pop());
		assertEquals(1, myStack.pop());
	}

	@DisplayName("🧪 Test popping top N values, with N bigger than stack size")
	@Test
	void popNBiggerThanSizeTest() {

		// setup
		myStack.push(1);
		myStack.push(2);
		myStack.push(3);

		int n = 5;

		// verify
		assertThrows(NoSuchElementException.class, () -> myStack.popTopN(n));
	}

	// @AfterEach
	// void teardown() {
	// mySut.releaseId();
	// mySut.close();
	// }
}
