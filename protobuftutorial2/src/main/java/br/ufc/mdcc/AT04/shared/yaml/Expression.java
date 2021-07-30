package br.ufc.mdcc.AT04.shared.yaml;

import java.util.List;

public class Expression {
	private List<String> expressioElements;

	public Expression(List<String> expressioElements) {
		this.expressioElements = expressioElements;
	}

	public List<String> getExpressioElements() {
		return expressioElements;
	}

	public void setExpressioElements(List<String> expressioElements) {
		this.expressioElements = expressioElements;
	}

	// Without a default constructor, Jackson will throw an exception
    public Expression() {}
}
