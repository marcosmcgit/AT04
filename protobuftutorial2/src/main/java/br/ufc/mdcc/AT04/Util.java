package br.ufc.mdcc.AT04;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Util {

	private static int precedence(char operator1, char operator2) {
		if (operator1 == '(') {
			return 2;
		}
		if (operator1 == '+' || operator1 == '-') {
			if (operator2 == '*' || operator2 == '/') {
				return 2;
			}
		}
		return 1;
	}

	public static List<Character> Exp2Rpn(String expression) {
		List<Character> rpn = new LinkedList<Character>();

		Stack<Character> stack = new Stack<Character>();

		for (int i = 0; i < expression.length(); i++) {
			char currentChar = expression.charAt(i);

			switch (currentChar) {
			case '+':
			case '-':
			case '*':
			case '/':
				while (!stack.empty() && precedence(stack.peek(), currentChar) == 1) {
					rpn.add(stack.pop());
				}
				stack.push(currentChar);
				break;
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				rpn.add(currentChar);
				break;
			case '(':
				stack.push(currentChar);
				break;
			case ')':
				while (stack.peek() != '(') {
					rpn.add(stack.pop());
				}
				stack.pop(); // pops '('
				break;
			default:
				break;
			}
		}

		while (!stack.empty()) {
			rpn.add(stack.pop());
		}

		return rpn;
	}

}
