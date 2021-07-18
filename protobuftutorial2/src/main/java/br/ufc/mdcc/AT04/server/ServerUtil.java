package br.ufc.mdcc.AT04.server;

import java.util.List;
import java.util.Stack;

import br.ufc.mdcc.AT04.shared.model.Element;
import br.ufc.mdcc.AT04.shared.model.Number;

public class ServerUtil {

	/*
	 * This method will solve the expression in reverse polish notation and return
	 * its value. It is necessary to pass an instance of ICalculator to provide the
	 * basic operations: addition, subtraction, multiplication and division.
	 */
	public static double solveRpn(List<Element> rpn, ICalculator calculator) {
		Stack<Element> stack = new Stack<Element>();

		for (Element currentElement : rpn) {
			// if it reached an operator, the last two elements pushed to the stack will be
			// operands
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
	
}
