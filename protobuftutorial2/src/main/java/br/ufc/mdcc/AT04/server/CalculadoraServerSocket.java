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
 * Um serviço de calculadora implementado através de sockets. Ele é extremamente
 * simples e capaz de realizar apenas as quatro operações fundamentais sobre
 * valores double.
 * 
 * Para usá-lo, o cliente deve se conectar à porta 9090 no IP em um dos
 * endereços IP do servidor via socket, e conteúdo de sua requisição deve ser
 * operador, operando 1 e operando 2, separados por uma quebra de linha (\n), em
 * que o operador pode ser um dos seguintes valores: 1-Soma; 2-Subtração;
 * 3-Multiplicação; 4-Divisão.
 */
public class CalculadoraServerSocket {

	public static void main(String[] args) {
		// Criação de objetos que serão utilizados mais adiante
		ServerSocket welcomeSocket;
		DataOutputStream socketOutput;
		BufferedReader socketEntrada;
		Calculadora calc = new Calculadora();

		try {
			// Criação do socket servidor, no endereço local e na porta 9090
			welcomeSocket = new ServerSocket(9090);
			int i = 0; // número de clientes
			System.out.println("Servidor no ar");

			// O programa aguardará conexões eternamente...
			while (true) {

				/*
				 * Aguarda a próxima conexão de um cliente, bloqueando a execução. Quando houver
				 * uma conexão, aceitará os dados e a execução prosseguirá
				 */
				Socket connectionSocket = welcomeSocket.accept();

				/*
				 * incrementa o contador de conexões de clientes, pois se passou da chamada
				 * acima é porque chegou uma nova conexão. Em seguida, imprime uma informação de
				 * quantas conexões houve até então
				 */
				i++;
				System.out.println("Nova requisição. Total de requisições até o momento: " + i);

				// Interpretando dados do servidor
				/*
				 * Trata os dados recebidos pelo socket. Os dados (operação, operando1 e
				 * operando2) encontram-se separados por uma quebra de linha (\n), portanto é
				 * conveniente instanciar um BufferedReader a partir da InputStream do socket
				 * para, logo em seguida, utilizar o método readLine para obter cada um desses
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
			 * Qualquer IOException que ocorrer cairá aqui. Nesse caso, o cliente não
			 * receberá nenhuma resposta.
			 */
			e.printStackTrace();
		}

	}

}
