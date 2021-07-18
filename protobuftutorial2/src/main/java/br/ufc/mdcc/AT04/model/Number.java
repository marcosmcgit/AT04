package br.ufc.mdcc.AT04.model;

public class Number implements Element {

	private double value;

	public Number(double value) {
		super();
		this.value = value;
	}

	@Override
	public boolean isNumber() {
		return true;
	}

	@Override
	public boolean isOperator() {
		return false;
	}

	@Override
	public boolean isParenthesis() {
		return false;
	}

	@Override
	public boolean isPlusSign() {
		return false;
	}

	@Override
	public boolean isMinusSign() {
		return false;
	}

	@Override
	public boolean isTimesSign() {
		return false;
	}

	@Override
	public boolean isDividedBySign() {
		return false;
	}

	@Override
	public boolean isLeftParenthesis() {
		return false;
	}

	@Override
	public boolean isRightParenthesis() {
		return false;
	}

	public double getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
