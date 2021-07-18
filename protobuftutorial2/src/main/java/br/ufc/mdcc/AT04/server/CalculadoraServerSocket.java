package br.ufc.mdcc.AT04.server;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import br.ufc.mdcc.AT04.shared.model.Element;
import br.ufc.mdcc.AT04.shared.model.Number;
import br.ufc.mdcc.AT04.shared.model.Operator;
import br.ufc.mdcc.AT04.shared.protobuffer.RPNProto.MElement.EnumOperator;

/**
 * Um servi�o de calculadora de express�es implementado atrav�s de sockets. Ele
 * espera receber uma express�o em nota��o polonesa reversa.
 * 
 * Para us�-lo, o cliente deve se conectar � porta 9090 no IP em um dos
 * endere�os IP do servidor via socket, e o conte�do de sua requisi��o deve ser
 * uma mensagem com a express�o em nota��o polonesa reversa, descrita utilizando
 * o protobuffer MExpression.
 */
public class CalculadoraServerSocket {

	/*
	 * Reads the protobuffer message with the expression from an InputStream,
	 * returning a List of Element.
	 */
	private static List<Element> receiveExpression(InputStream inputStream) throws IOException {
		// reads the protobuffer message
		br.ufc.mdcc.AT04.shared.protobuffer.RPNProto.MExpression messageExpression = br.ufc.mdcc.AT04.shared.protobuffer.RPNProto.MExpression
				.parseDelimitedFrom(inputStream);

		// with the message, gets the List of MElement
		List<br.ufc.mdcc.AT04.shared.protobuffer.RPNProto.MElement> messageElements = messageExpression
				.getElementList();

		// with the List of MElement, constructs the List of Element
		List<Element> rpn = new LinkedList<Element>();
		for (br.ufc.mdcc.AT04.shared.protobuffer.RPNProto.MElement mElement : messageElements) {
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

		return rpn;
	}

	public static void main(String[] args) {
		// Cria��o de objetos que ser�o utilizados mais adiante
		ServerSocket welcomeSocket;
		DataOutputStream socketOutput;

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
				 * Trata os dados recebidos pelo socket. Os dados ser�o recebidos como
				 * protobuffer
				 */

				try {
					InputStream inputStream = connectionSocket.getInputStream();

					List<Element> rpn = receiveExpression(inputStream);

					ICalculator calculator = new Calculadora();
					double result = ServerUtil.solveRpn(rpn, calculator);

					System.out.println(String.valueOf(result));

					socketOutput = new DataOutputStream(connectionSocket.getOutputStream());
					socketOutput.writeBytes(String.valueOf(result) + '\n');

					socketOutput.flush();
					socketOutput.close();
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
