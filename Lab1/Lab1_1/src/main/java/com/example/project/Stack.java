package com.example.project;

import java.util.ArrayList;
import java.util.List;

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
			throw new IllegalStateException("Stack is empty");
		}
		return elements.remove(elements.size() - 1);
	}

	public int peek() {
		if (isEmpty()) {
			throw new IllegalStateException("Stack is empty");
		}
		return elements.get(elements.size() - 1);
	}

	public int size() {
		return elements.size();
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}
}
