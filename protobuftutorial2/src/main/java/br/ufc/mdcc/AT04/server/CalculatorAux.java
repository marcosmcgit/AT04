package br.ufc.mdcc.AT04.server;

import br.ufc.mdcc.AT04.model.ICalculator;

public class CalculatorAux implements ICalculator {

	@Override
	public double soma(double oper1, double oper2) {
		return oper1 + oper2;
	}

	@Override
	public double subtrai(double oper1, double oper2) {
		return oper1 - oper2;
	}

	@Override
	public double multiplica(double oper1, double oper2) {
		return oper1 * oper2;
	}

	@Override
	public double divide(double oper1, double oper2) {
		return oper1 / oper2;
	}

}
