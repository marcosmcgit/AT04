package br.ufc.mdcc.AT04.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import br.ufc.mdcc.AT04.shared.model.Element;
import br.ufc.mdcc.AT04.shared.model.Number;
import br.ufc.mdcc.AT04.shared.model.Operator;
import br.ufc.mdcc.AT04.shared.model.Parenthesis;

public class ClientUtil {

	/*
	 * Returns:
	 * 
	 * 2 - if element2 has higher precedence on an algebraic expression
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

		// not all minus sign is an operator. It can be the signal of a negative number.
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

	/*
	 * Given a String, it will convert to an Element (Number, Operator or
	 * Parenthesis)
	 */
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

	/*
	 * Given a valid algebraic expression as a String, it will construct and return
	 * a List of Element containing the reverse polish notation.
	 */
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

}
