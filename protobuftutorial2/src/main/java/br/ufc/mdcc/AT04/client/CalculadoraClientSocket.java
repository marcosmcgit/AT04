package br.ufc.mdcc.AT04.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

import br.ufc.mdcc.AT04.model.Element;
import br.ufc.mdcc.AT04.model.Number;
import br.ufc.mdcc.AT04.model.Operator;
import br.ufc.mdcc.AT04.protobuffer.RPNProto.MElement.EnumOperator;
import br.ufc.mdcc.AT04.util.Util;

/**
 * Cliente para o serviço de calculadora disponibilizado via sockets.
 */
public class CalculadoraClientSocket {

	public static void main(String[] args) {

		String expression = "5-3/8+5*9";
		List<Element> elements = Util.Exp2Rpn(expression);

		br.ufc.mdcc.AT04.protobuffer.RPNProto.MExpression.Builder expressionBuilder = br.ufc.mdcc.AT04.protobuffer.RPNProto.MExpression
				.newBuilder();

		for (Element element : elements) {
			br.ufc.mdcc.AT04.protobuffer.RPNProto.MElement.Builder elementBuilder = br.ufc.mdcc.AT04.protobuffer.RPNProto.MElement
					.newBuilder();

			if (element.isNumber()) {
				Number number = (Number) element;
				elementBuilder.setNumber(number.getValue());
			} else if (element.isOperator()) {
				Operator operator = (Operator) element;
				if (operator.isPlusSign()) {
					elementBuilder.setOperator(EnumOperator.ADDITION);
				} else if (operator.isMinusSign()) {
					elementBuilder.setOperator(EnumOperator.SUBTRACTION);
				} else if (operator.isTimesSign()) {
					elementBuilder.setOperator(EnumOperator.MULTIPLICATION);
				} else if (operator.isDividedBySign()) {
					elementBuilder.setOperator(EnumOperator.DIVISION);
				}
			}

			expressionBuilder.addElement(elementBuilder.build());
		}

		try {
			br.ufc.mdcc.AT04.protobuffer.RPNProto.MExpression messageExpression = expressionBuilder.build();

			Socket clientSocket = new Socket("127.0.0.1", 9090);

			DataOutputStream socketSaidaServer = new DataOutputStream(clientSocket.getOutputStream());

			messageExpression.writeTo(socketSaidaServer);

			socketSaidaServer.flush();

//			BufferedReader messageFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//			String result = messageFromServer.readLine();
//
//			System.out.println("resultado=" + result);

			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
