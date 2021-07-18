package br.ufc.mdcc.AT04.shared.model;

public class Operator implements Element {

	private static final int PLUS_SIGN = 0;
	private static final int MINUS_SIGN = 1;
	private static final int TIMES_SIGN = 2;
	private static final int DIVIDED_BY_SIGN = 3;

	private int value;

	private Operator() {
		super();
	}

	private Operator(int value) {
		super();
		this.value = value;
	}

	public static Operator plusSign() {
		return new Operator(PLUS_SIGN);
	}

	public static Operator minusSign() {
		return new Operator(MINUS_SIGN);
	}

	public static Operator timesSign() {
		return new Operator(TIMES_SIGN);
	}

	public static Operator dividedBySign() {
		return new Operator(DIVIDED_BY_SIGN);
	}

	@Override
	public boolean isNumber() {
		return false;
	}

	@Override
	public boolean isOperator() {
		return true;
	}

	@Override
	public boolean isParenthesis() {
		return false;
	}

	@Override
	public boolean isPlusSign() {
		return value == PLUS_SIGN;
	}

	@Override
	public boolean isMinusSign() {
		return value == MINUS_SIGN;
	}

	@Override
	public boolean isTimesSign() {
		return value == TIMES_SIGN;
	}

	@Override
	public boolean isDividedBySign() {
		return value == DIVIDED_BY_SIGN;
	}

	@Override
	public boolean isLeftParenthesis() {
		return false;
	}

	@Override
	public boolean isRightParenthesis() {
		return false;
	}

	@Override
	public String toString() {
		switch (value) {
		case PLUS_SIGN:
			return "+";
		case MINUS_SIGN:
			return "-";
		case TIMES_SIGN:
			return "*";
		case DIVIDED_BY_SIGN:
			return "/";
		default:
			return "";
		}
	}
}
