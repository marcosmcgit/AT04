package br.ufc.mdcc.AT04.client.protobuffer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.List;

import br.ufc.mdcc.AT04.client.AbstractCalculadoraClient;
import br.ufc.mdcc.AT04.shared.model.Element;
import br.ufc.mdcc.AT04.shared.model.Number;
import br.ufc.mdcc.AT04.shared.model.Operator;
import br.ufc.mdcc.AT04.shared.protobuffer.RPNProto.MElement.EnumOperator;

/**
 * Cliente para o serviço de calculadora disponibilizado via sockets.
 */
public class CalculadoraClientProtobuffer extends AbstractCalculadoraClient {

	public CalculadoraClientProtobuffer(String serverAddr, int serverPort, String expression) {
		super(serverAddr, serverPort, expression);
	}

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

	protected double receiveResultData(Socket clientSocket) throws IOException {
		InputStream inputStream = clientSocket.getInputStream();
		br.ufc.mdcc.AT04.shared.protobuffer.ResultProto.Result messageResult = br.ufc.mdcc.AT04.shared.protobuffer.ResultProto.Result
				.parseDelimitedFrom(inputStream);
		return messageResult.getTotal();
	}

	protected void sendRpnData(Socket clientSocket, List<Element> elements) throws IOException {
		DataOutputStream socketSaidaServer = new DataOutputStream(clientSocket.getOutputStream());
		// generates the protobuffer message
		br.ufc.mdcc.AT04.shared.protobuffer.RPNProto.MExpression messageExpression = generateProtobufMessage(elements);
		messageExpression.writeDelimitedTo(socketSaidaServer);
		socketSaidaServer.flush();
	}

	public static void main(String[] args) {
		String serverAddr = "127.0.0.1";
		int serverPort = 9090;
		String expression = "5-3/8+5*9";

		CalculadoraClientProtobuffer clientProtobuffer = new CalculadoraClientProtobuffer(serverAddr, serverPort,
				expression);
		clientProtobuffer.execute();
	}
}
