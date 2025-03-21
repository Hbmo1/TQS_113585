package com.example.project;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Stack {

	private List<Integer> elements;

	public Stack() {
		elements = new ArrayList<>();
	}

	public void push(int i) {
		elements.add(i);
	}

	public int pop() {
		if (isEmpty()) {
			throw new NoSuchElementException("Can't pop from empty stack");
		}
		return elements.remove(elements.size() - 1);
	}

	public int peek() {
		if (isEmpty()) {
			throw new NoSuchElementException("Can't peek at empty stack");
		}
		return elements.get(elements.size() - 1);
	}

	public int size() {
		return elements.size();
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}

	public int popTopN(int n) {
		if (n > elements.size()) {
			throw new NoSuchElementException("Can't pop more elements than the stack contains");
		}

		int top = 0;
		for (int i = 0; i < n; i++) {
			top = elements.remove(elements.size() - 1);
		}

		return top;
	}
}
