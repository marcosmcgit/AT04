package br.ufc.mdcc.AT04.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

import br.ufc.mdcc.AT04.model.Element;
import br.ufc.mdcc.AT04.model.Number;
import br.ufc.mdcc.AT04.model.Operator;
import br.ufc.mdcc.AT04.protobuffer.RPNProto.MElement.EnumOperator;
import br.ufc.mdcc.AT04.util.Util;
import old.protobuftutorial.CountryProto;
import old.protobuftutorial.CountryProto.Country;

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
			FileOutputStream output = new FileOutputStream("c:\\java\\temp\\expression.data");
			messageExpression.writeTo(output);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			FileInputStream inputStream = new FileInputStream("c:\\java\\temp\\expression.data");
			br.ufc.mdcc.AT04.protobuffer.RPNProto.MExpression messageExpression = br.ufc.mdcc.AT04.protobuffer.RPNProto.MExpression.parseFrom(inputStream);
			inputStream.close();
			List<br.ufc.mdcc.AT04.protobuffer.RPNProto.MElement> messageElements = messageExpression.getElementList();
			for (br.ufc.mdcc.AT04.protobuffer.RPNProto.MElement mElement : messageElements) {
				System.out.println(mElement);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
////		try {
////			Socket clientSocket = new Socket("127.0.0.1", 9090);
////			DataOutputStream socketSaidaServer = new DataOutputStream(clientSocket.getOutputStream());
////			
////		}
//
//		// Os dois operandos
//		double oper1 = 10, oper2 = 20;
//		// O indicador da operação
//		int operacao = 1; // 1-somar 2-subtrair 3-dividir 4-multiplicar
//		// Para armazenar o resultado da operação, recebido via socket
//		String result = "";
//		try {
//
//			// Conexão com o Servidor
//			/*
//			 * Vamos criar uma conexão via socket na porta 9090 do servidor no endereço
//			 * 127.0.0.1 (localhost)
//			 */
//			Socket clientSocket = new Socket("127.0.0.1", 9090);
//
//			/*
//			 * O envio dos dados para o servidor será feito através de um outputStream do
//			 * socket. Para isso, será utilizada uma instância da classe concreta
//			 * DataOutputStream
//			 */
//			DataOutputStream socketSaidaServer = new DataOutputStream(clientSocket.getOutputStream());
//
//			// Enviando os dados
//			/*
//			 * A stream enviada será composta por operador, operando 1 e operando 2, nesta
//			 * ordem. Após cada um deles, será incluída uma quebra de linha ("\n"), afim de
//			 * delimitar os dados.
//			 */
//			socketSaidaServer.writeBytes(operacao + "\n");
//			socketSaidaServer.writeBytes(oper1 + "\n");
//			socketSaidaServer.writeBytes(oper2 + "\n");
//
//			/*
//			 * descarrega o buffer na stream, se ainda houver, que será então encaminhado ao
//			 * servidor.
//			 */
//			socketSaidaServer.flush();
//
//			// Recebendo a resposta
//			/*
//			 * O recebimento do resultado do servidor será feito através de um InputStream
//			 * do socket. A stream recebida é formada pelo resultado seguido de uma quebra
//			 * de linha. Como o resultado encontra-se delimitado por quebra de linha ("\n"),
//			 * é conveniente instanciar um BufferedReader a partir da InputStream do socket
//			 * para, logo em seguida, utilizar o método readLine para obtê-lo.
//			 */
//			BufferedReader messageFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//			result = messageFromServer.readLine();
//
//			// Imprime o resultado na tela
//			System.out.println("resultado=" + result);
//
//			// Fecha o socket.
//			clientSocket.close();
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}

}
