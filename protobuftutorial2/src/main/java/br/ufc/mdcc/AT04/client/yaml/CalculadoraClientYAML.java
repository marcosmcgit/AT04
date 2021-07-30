package br.ufc.mdcc.AT04.client.yaml;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import br.ufc.mdcc.AT04.client.AbstractCalculadoraClient;
import br.ufc.mdcc.AT04.shared.model.Element;
import br.ufc.mdcc.AT04.shared.model.Number;
import br.ufc.mdcc.AT04.shared.model.Operator;
import br.ufc.mdcc.AT04.shared.protobuffer.RPNProto.MElement.EnumOperator;
import br.ufc.mdcc.AT04.shared.yaml.Expression;
import br.ufc.mdcc.AT04.shared.yaml.Result;

/**
 * Cliente para o servi�o de calculadora disponibilizado via sockets.
 */
public class CalculadoraClientYAML extends AbstractCalculadoraClient {

	public CalculadoraClientYAML(String serverAddr, int serverPort, String expression) {
		super(serverAddr, serverPort, expression);
	}

	private String generateYAML(List<Element> elements) throws JsonProcessingException {
		ObjectMapper om = new ObjectMapper(new YAMLFactory());
		List<String> expressionElements = new ArrayList<String>();
		
		for (Element element : elements) {
			if (element.isNumber()) {
				Number number = (Number) element;
				expressionElements.add(String.valueOf(number.getValue()));
			} else if (element.isOperator()) {
				Operator operator = (Operator) element;
				
				if (operator.isPlusSign()) {
					expressionElements.add(String.valueOf(EnumOperator.ADDITION));
				} else if (operator.isMinusSign()) {
					expressionElements.add(String.valueOf(EnumOperator.SUBTRACTION));
				} else if (operator.isTimesSign()) {
					expressionElements.add(String.valueOf(EnumOperator.MULTIPLICATION));
				} else if (operator.isDividedBySign()) {
					expressionElements.add(String.valueOf(EnumOperator.DIVISION));
				}

			}
		}
		
		Expression expression = new Expression(expressionElements);
		String yamlString = om.writeValueAsString(expression);
		return yamlString;
			
	}
	
	
	private Object convert2YamlObject(String yamlString) throws JsonMappingException, JsonProcessingException {
		ObjectMapper om = new ObjectMapper(new YAMLFactory());
		Result resultYamlObject = om.readValue(yamlString, Result.class);
		return resultYamlObject;
	}
	
	protected double receiveResultData(Socket clientSocket) throws IOException {
		DataInputStream socketInput = new DataInputStream(clientSocket.getInputStream());
		String yamlString = socketInput.readUTF();
		Result result = (Result) convert2YamlObject(yamlString);
		double resultDouble = result.getResult();
		return resultDouble;
	}

	protected void sendRpnData(Socket clientSocket, List<Element> elements) throws IOException {
		DataOutputStream socketSaidaServer = new DataOutputStream(clientSocket.getOutputStream());
		String yamlMessage = generateYAML(elements);
		socketSaidaServer.writeUTF(yamlMessage);
		socketSaidaServer.flush();
	}

	public static void main(String[] args) {
		String serverAddr = "127.0.0.1";
		int serverPort = 9092;
		String expression = "5-3/8+5*9";

		CalculadoraClientYAML clientYAML = new CalculadoraClientYAML(serverAddr, serverPort, expression);
		clientYAML.execute();
		
	}
}
