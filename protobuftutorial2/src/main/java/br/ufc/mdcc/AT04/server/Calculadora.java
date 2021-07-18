package br.ufc.mdcc.AT04.server;
/**
 * Classe representando uma calculadora extremamente simples, que conta apenas
 * com as quatro operações fundamentais. Cada operação é executada sobre dois
 * valores double e seu resultado também é um double.
 *
 */
public class Calculadora {
	public double soma(double oper1, double oper2) {
		return oper1 + oper2;
	}

	public double subtrai(double oper1, double oper2) {
		return oper1 - oper2;
	}

	public double multiplica(double oper1, double oper2) {
		return oper1 * oper2;
	}

	public double divide(double oper1, double oper2) {
		return oper1 / oper2;
	}
}