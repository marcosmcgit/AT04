package br.ufc.mdcc.AT04.server.yaml;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import br.ufc.mdcc.AT04.server.AbstractCalculadoraServer;
import br.ufc.mdcc.AT04.shared.model.Element;
import br.ufc.mdcc.AT04.shared.model.Number;
import br.ufc.mdcc.AT04.shared.model.Operator;
import br.ufc.mdcc.AT04.shared.yaml.Expression;
import br.ufc.mdcc.AT04.shared.yaml.Result;

public class CalculadoraServerYAML extends AbstractCalculadoraServer {

	public CalculadoraServerYAML(int serverPort, String name) {
		super(serverPort, name);
	}
	
	private static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	

	private static List<Element> convert2ListOfElement(String messageElements) throws JsonMappingException, JsonProcessingException {
		List<Element> rpn = new LinkedList<Element>();
		
		// Instantiating a new ObjectMapper as a YAMLFactory
		ObjectMapper om = new ObjectMapper(new YAMLFactory());

		// Mapping the employee from the YAML file to the Employee class
		Expression expression = om.readValue(messageElements, Expression.class);
		int totalElements = (expression.getExpressioElements().size() - 1);
		
		for (int i=0; i<= totalElements; i++) {
			String mElement = expression.getExpressioElements().get(i).toString();			
			if (isNumeric(mElement)) {
				Number number = new Number(Double.parseDouble(mElement));
				rpn.add(number);
			}else {
				Operator operator = null;
				if (mElement.equals("ADDITION")) {
					operator = Operator.plusSign();
				} else if (mElement.equals("SUBTRACTION")) {
					operator = Operator.minusSign();
				} else if (mElement.equals("MULTIPLICATION")) {
					operator = Operator.timesSign();
				} else if (mElement.equals("DIVISION")) {
					operator = Operator.dividedBySign();
				}
				rpn.add(operator);
			}
		}
		return rpn;
	}
	
	private String generateYAML(double result) throws JsonProcessingException {
		ObjectMapper om = new ObjectMapper(new YAMLFactory());
		Result resultObj = new Result(result);
		String yamlResultString = om.writeValueAsString(resultObj);
		return yamlResultString;
		
	}
	
	protected void sendResultData(Socket connectionSocket, double result) throws IOException {
		String yamlResultString = generateYAML(result);
		DataOutputStream socketSaidaServer = new DataOutputStream(connectionSocket.getOutputStream());
		socketSaidaServer.writeUTF(yamlResultString);
		socketSaidaServer.flush();
		socketSaidaServer.close();
	}

	protected List<Element> receiveRpnData(Socket connectionSocket) throws IOException {
		DataInputStream socketInput = new DataInputStream(connectionSocket.getInputStream());
		String yamlExp = socketInput.readUTF();
		List<Element> rpn = convert2ListOfElement(yamlExp);	
		return rpn;
	}

}
