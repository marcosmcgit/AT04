package br.ufc.mdcc.AT04;

public class Parenthesis implements Element {

	private static final int LEFT = 0;
	private static final int RIGHT = 1;

	private int value;

	private Parenthesis() {
		super();
	}

	private Parenthesis(int value) {
		super();
		this.value = value;
	}

	public static Parenthesis leftParenthesis() {
		return new Parenthesis(LEFT);
	}

	public static Parenthesis rightParenthesis() {
		return new Parenthesis(RIGHT);
	}

	@Override
	public boolean isNumber() {
		return false;
	}

	@Override
	public boolean isOperator() {
		return false;
	}

	@Override
	public boolean isParenthesis() {
		return true;
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
		return value == LEFT;
	}

	@Override
	public boolean isRightParenthesis() {
		return value == RIGHT;
	}

	@Override
	public String toString() {
		switch (value) {
		case LEFT:
			return "(";
		case RIGHT:
			return ")";
		default:
			return "";
		}
	}
}
