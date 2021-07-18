package br.ufc.mdcc.AT04.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import br.ufc.mdcc.AT04.model.Element;
import br.ufc.mdcc.AT04.model.ICalculator;
import br.ufc.mdcc.AT04.model.Number;
import br.ufc.mdcc.AT04.model.Operator;
import br.ufc.mdcc.AT04.protobuffer.RPNProto.MElement.EnumOperator;
import br.ufc.mdcc.AT04.util.Util;

/**
 * Um servi�o de calculadora implementado atrav�s de sockets. Ele � extremamente
 * simples e capaz de realizar apenas as quatro opera��es fundamentais sobre
 * valores double.
 * 
 * Para us�-lo, o cliente deve se conectar � porta 9090 no IP em um dos
 * endere�os IP do servidor via socket, e conte�do de sua requisi��o deve ser
 * operador, operando 1 e operando 2, separados por uma quebra de linha (\n), em
 * que o operador pode ser um dos seguintes valores: 1-Soma; 2-Subtra��o;
 * 3-Multiplica��o; 4-Divis�o.
 */
public class CalculadoraServerSocket {

	public static void main(String[] args) {
		// Cria��o de objetos que ser�o utilizados mais adiante
		ServerSocket welcomeSocket;
		DataOutputStream socketOutput;
		BufferedReader socketEntrada;
		Calculadora calc = new Calculadora();

		try {
			// Cria��o do socket servidor, no endere�o local e na porta 9090
			welcomeSocket = new ServerSocket(9090);
			int i = 0; // n�mero de clientes
			System.out.println("Servidor no ar");

			// O programa aguardar� conex�es eternamente...
			while (true) {

				/*
				 * Aguarda a pr�xima conex�o de um cliente, bloqueando a execu��o. Quando houver
				 * uma conex�o, aceitar� os dados e a execu��o prosseguir�
				 */
				Socket connectionSocket = welcomeSocket.accept();

				/*
				 * incrementa o contador de conex�es de clientes, pois se passou da chamada
				 * acima � porque chegou uma nova conex�o. Em seguida, imprime uma informa��o de
				 * quantas conex�es houve at� ent�o
				 */
				i++;
				System.out.println("Nova requisi��o. Total de requisi��es at� o momento: " + i);

				// Interpretando dados do servidor
				/*
				 * Trata os dados recebidos pelo socket. Os dados (opera��o, operando1 e
				 * operando2) encontram-se separados por uma quebra de linha (\n), portanto �
				 * conveniente instanciar um BufferedReader a partir da InputStream do socket
				 * para, logo em seguida, utilizar o m�todo readLine para obter cada um desses
				 * dados separadamente.
				 */

				try {
					InputStream inputStream = connectionSocket.getInputStream();

					br.ufc.mdcc.AT04.protobuffer.RPNProto.MExpression messageExpression = br.ufc.mdcc.AT04.protobuffer.RPNProto.MExpression
							.parseFrom(inputStream);

					inputStream.close();
					List<br.ufc.mdcc.AT04.protobuffer.RPNProto.MElement> messageElements = messageExpression
							.getElementList();

					List<Element> rpn = new LinkedList<Element>();

					for (br.ufc.mdcc.AT04.protobuffer.RPNProto.MElement mElement : messageElements) {
						if (mElement.hasNumber()) {
							Number number = new Number(mElement.getNumber());
							rpn.add(number);
						} else {
							Operator operator = null;
							if (mElement.getOperator().equals(EnumOperator.ADDITION)) {
								operator = Operator.plusSign();
							} else if (mElement.getOperator().equals(EnumOperator.SUBTRACTION)) {
								operator = Operator.minusSign();
							} else if (mElement.getOperator().equals(EnumOperator.MULTIPLICATION)) {
								operator = Operator.timesSign();
							} else if (mElement.getOperator().equals(EnumOperator.DIVISION)) {
								operator = Operator.dividedBySign();
							}
							rpn.add(operator);
						}
					}

					ICalculator calculator = new CalculatorAux();
					double result = Util.solveRpn(rpn, calculator);

					System.out.println(String.valueOf(result));
					
//					socketOutput = new DataOutputStream(connectionSocket.getOutputStream());
//					socketOutput.writeBytes(String.valueOf(result) + '\n');
//
//					socketOutput.flush();
//					socketOutput.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			/*
			 * Qualquer IOException que ocorrer cair� aqui. Nesse caso, o cliente n�o
			 * receber� nenhuma resposta.
			 */
			e.printStackTrace();
		}

	}

}
