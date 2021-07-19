package br.ufc.mdcc.AT04.server.protobuffer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import br.ufc.mdcc.AT04.server.AbstractCalculadoraServer;
import br.ufc.mdcc.AT04.shared.model.Element;
import br.ufc.mdcc.AT04.shared.model.Number;
import br.ufc.mdcc.AT04.shared.model.Operator;
import br.ufc.mdcc.AT04.shared.protobuffer.RPNProto.MElement.EnumOperator;

/**
 * Um serviço de calculadora de expressões implementado através de sockets. Ele
 * espera receber uma expressão em notação polonesa reversa.
 * 
 * Para usá-lo, o cliente deve se conectar à porta 9090 no IP em um dos
 * endereços IP do servidor via socket, e o conteúdo de sua requisição deve ser
 * uma mensagem com a expressão em notação polonesa reversa, descrita utilizando
 * o protobuffer MExpression.
 */
public class CalculadoraServerProtobuffer extends AbstractCalculadoraServer {

	public CalculadoraServerProtobuffer(int serverPort, String name) {
		super(serverPort, name);
	}

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
		List<Element> rpn = convert2ListOfElement(messageElements);

		return rpn;
	}

	private static List<Element> convert2ListOfElement(
			List<br.ufc.mdcc.AT04.shared.protobuffer.RPNProto.MElement> messageElements) {
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

	protected void sendResultData(Socket connectionSocket, double result) throws IOException {
		br.ufc.mdcc.AT04.shared.protobuffer.ResultProto.Result.Builder resultBuilder = br.ufc.mdcc.AT04.shared.protobuffer.ResultProto.Result
				.newBuilder();
		resultBuilder.setTotal(result);
		br.ufc.mdcc.AT04.shared.protobuffer.ResultProto.Result messageResult = resultBuilder.build();
		DataOutputStream socketOutput = new DataOutputStream(connectionSocket.getOutputStream());
		messageResult.writeDelimitedTo(socketOutput);
		socketOutput.flush();
		socketOutput.close();
	}

	protected List<Element> receiveRpnData(Socket connectionSocket) throws IOException {
		InputStream inputStream = connectionSocket.getInputStream();
		List<Element> rpn = receiveExpression(inputStream);
		return rpn;
	}

}
