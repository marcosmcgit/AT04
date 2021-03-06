package br.ufc.mdcc.AT04.shared.model;

public interface Element {

	boolean isNumber();

	boolean isOperator();

	boolean isParenthesis();

	boolean isPlusSign();

	boolean isMinusSign();

	boolean isTimesSign();

	boolean isDividedBySign();

	boolean isLeftParenthesis();

	boolean isRightParenthesis();

}
