package br.ufc.mdcc.AT04;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Util {

	/*
	 * Returns:
	 * 
	 * 2 - if element2 has higher precedence
	 * 
	 * 1 - otherwise
	 */
	private static int precedence(Element element1, Element element2) {
		if (element1.isLeftParenthesis()) {
			return 2;
		}
		if (element1.isPlusSign() || element1.isMinusSign()) {
			if (element2.isTimesSign() || element2.isDividedBySign()) {
				return 2;
			}
		}
		return 1;
	}

	/*
	 * Given an expression as parameter, this function will construct a list with
	 * all tokens (numbers, operators and parenthesis) However, this is not the most
	 * efficient way to tokenize the string.
	 */
	private static List<String> tokenize(String expression) {
		String[] arrayTokens = expression.split(
				"((?<=\\+)|(?=\\+))|((?<=\\-)|(?=\\-))|((?<=\\*)|(?=\\*))|((?<=\\/)|(?=\\/))|((?<=\\()|(?=\\())|((?<=\\))|(?=\\)))");

		// not all minus sign is an operator. It can be the signal of a negative number
		// The following loop will "merge" the minus sign with the next number when it
		// is only a signal
		List<String> listTokens = new ArrayList<String>(Arrays.asList(arrayTokens));
		for (int i = 0; i < listTokens.size() - 1; i++) {
			if (listTokens.get(i).equals("-")) {
				if (i == 0 || listTokens.get(i - 1).equals("(")) {
					listTokens.set(i + 1, "-" + listTokens.get(i + 1));
					listTokens.remove(i);
				}
			}
		}

		return listTokens;
	}

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

	private static int precedence(String operator1, String operator2) {
		if (operator1.equals("(")) {
			return 2;
		}
		if (operator1.equals("+") || operator1.equals("-")) {
			if (operator2.equals("*") || operator2.equals("/")) {
				return 2;
			}
		}
		return 1;
	}

	private static Element string2Element(String token) {
		if (token.equals("+")) {
			return Operator.plusSign();
		} else if (token.equals("-")) {
			return Operator.minusSign();
		} else if (token.equals("*")) {
			return Operator.timesSign();
		} else if (token.equals("/")) {
			return Operator.dividedBySign();
		} else if (token.equals("(")) {
			return Parenthesis.leftParenthesis();
		} else if (token.equals(")")) {
			return Parenthesis.rightParenthesis();
		} else {
			return new Number(Double.parseDouble(token));
		}
	}

	public static List<Element> Exp2Rpn(String expression) {
		List<String> tokenList = tokenize(expression);
		List<Element> rpn = new LinkedList<Element>();
		Stack<Element> stack = new Stack<Element>();

		for (String token : tokenList) {
			Element currentElement = string2Element(token);

			if (currentElement.isOperator()) {
				while (!stack.empty() && precedence(stack.peek(), currentElement) == 1) {
					rpn.add(stack.pop());
				}
				stack.push(currentElement);
			} else if (currentElement.isNumber()) {
				rpn.add(currentElement);
			} else if (currentElement.isLeftParenthesis()) {
				stack.push(currentElement);
			} else if (currentElement.isRightParenthesis()) {
				while (!stack.peek().isLeftParenthesis()) {
					rpn.add(stack.pop());
				}
				stack.pop(); // pops '('
			}
		}

		while (!stack.empty()) {
			rpn.add(stack.pop());
		}

		return rpn;
	}

	public static List<Character> Exp2Rpn2(String expression) {
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

	public static double solveRpn(List<Element> rpn, ICalculator calculator) {
		Stack<Element> stack = new Stack<Element>();

		for (Element currentElement : rpn) {
			if (currentElement.isOperator()) {
				Number n2 = (Number) stack.pop();
				Number n1 = (Number) stack.pop();

				double subtotal = 0;
				if (currentElement.isPlusSign()) {
					subtotal = calculator.soma(n1.getValue(), n2.getValue());
				} else if (currentElement.isMinusSign()) {
					subtotal = calculator.subtrai(n1.getValue(), n2.getValue());
				} else if (currentElement.isTimesSign()) {
					subtotal = calculator.multiplica(n1.getValue(), n2.getValue());
				} else if (currentElement.isDividedBySign()) {
					subtotal = calculator.divide(n1.getValue(), n2.getValue());
				}

				Element newElement = new Number(subtotal);
				stack.push(newElement);
			} else {
				stack.push(currentElement);
			}
		}

		return ((Number) stack.pop()).getValue();
	}

	public static void main(String[] args) {
		String expression = "-1*.20/(2-.5/2.0)-(-1+(-2))"; // = -3

		ICalculator calculator = new CalculatorAux();

		List<Element> elements = Exp2Rpn(expression);
		System.out.println(solveRpn(elements, calculator));
	}

}
