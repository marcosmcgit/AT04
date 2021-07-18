package br.ufc.mdcc.AT04.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.List;

import br.ufc.mdcc.AT04.shared.model.Element;
import br.ufc.mdcc.AT04.shared.model.Number;
import br.ufc.mdcc.AT04.shared.model.Operator;
import br.ufc.mdcc.AT04.shared.protobuffer.RPNProto.MElement.EnumOperator;

/**
 * Cliente para o serviço de calculadora disponibilizado via sockets.
 */
public class CalculadoraClientSocket {

	/*
	 * Given a List of Elements, generates an object of MExpression class
	 * (protobuffer representation).
	 */
	private static br.ufc.mdcc.AT04.shared.protobuffer.RPNProto.MExpression generateProtobufMessage(
			List<Element> elements) {
		br.ufc.mdcc.AT04.shared.protobuffer.RPNProto.MExpression.Builder expressionBuilder = br.ufc.mdcc.AT04.shared.protobuffer.RPNProto.MExpression
				.newBuilder();

		for (Element element : elements) {
			br.ufc.mdcc.AT04.shared.protobuffer.RPNProto.MElement.Builder elementBuilder = br.ufc.mdcc.AT04.shared.protobuffer.RPNProto.MElement
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

		br.ufc.mdcc.AT04.shared.protobuffer.RPNProto.MExpression messageExpression = expressionBuilder.build();
		return messageExpression;
	}

	public static void main(String[] args) {
		String serverAddr = "127.0.0.1";
		int serverPort = 9090;
		String expression = "5-3/8+5*9";

		// converts the expression to a List of Element containing the reverse polish
		// notation form of the expression
		List<Element> elements = ClientUtil.Exp2Rpn(expression);

		// generates the protobuffer message
		br.ufc.mdcc.AT04.shared.protobuffer.RPNProto.MExpression messageExpression = generateProtobufMessage(elements);

		try {
			// sending...
			Socket clientSocket = new Socket(serverAddr, serverPort);
			DataOutputStream socketSaidaServer = new DataOutputStream(clientSocket.getOutputStream());
			messageExpression.writeDelimitedTo(socketSaidaServer);
			socketSaidaServer.flush();

			// receiving...
			InputStream inputStream = clientSocket.getInputStream();
			br.ufc.mdcc.AT04.shared.protobuffer.ResultProto.Result messageResult = br.ufc.mdcc.AT04.shared.protobuffer.ResultProto.Result
					.parseDelimitedFrom(inputStream);

			double result = messageResult.getTotal();

			System.out.println("resultado=" + result);

			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
