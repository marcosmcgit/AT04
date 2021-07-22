package br.ufc.mdcc.AT04.client.xml;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.Socket;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import br.ufc.mdcc.AT04.client.AbstractCalculadoraClient;
import br.ufc.mdcc.AT04.shared.model.Element;
import br.ufc.mdcc.AT04.shared.model.Number;
import br.ufc.mdcc.AT04.shared.model.Operator;
import br.ufc.mdcc.AT04.shared.protobuffer.RPNProto.MElement.EnumOperator;

/**
 * Cliente para o serviço de calculadora disponibilizado via sockets.
 */
public class CalculadoraClientXML extends AbstractCalculadoraClient {
	
	public CalculadoraClientXML(String serverAddr, int serverPort, String expression) {
		super(serverAddr, serverPort, expression);
	}
	
	/*
	 * Given a List of Elements, generates an XML of expression
	 */
	private static String generateXMLMessage(
			List<Element> elements) {
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		 
        DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = documentFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

        Document document = documentBuilder.newDocument();

        // root element
        org.w3c.dom.Element root = document.createElement("expression");
        document.appendChild(root);

		for (Element element : elements) {

			if (element.isNumber()) {
				Number number = (Number) element;

				org.w3c.dom.Element numberXML = document.createElement("number");
	            numberXML.appendChild(document.createTextNode(String.valueOf(number.getValue())));
	            root.appendChild(numberXML);
				
			} else if (element.isOperator()) {
				Operator operator = (Operator) element;
				org.w3c.dom.Element operatorXML = document.createElement("operator");
	            
	            
				if (operator.isPlusSign()) {
					operatorXML.appendChild(document.createTextNode(String.valueOf(EnumOperator.ADDITION)));
				} else if (operator.isMinusSign()) {
					operatorXML.appendChild(document.createTextNode(String.valueOf(EnumOperator.SUBTRACTION)));
				} else if (operator.isTimesSign()) {
					operatorXML.appendChild(document.createTextNode(String.valueOf(EnumOperator.MULTIPLICATION)));
				} else if (operator.isDividedBySign()) {
					operatorXML.appendChild(document.createTextNode(String.valueOf(EnumOperator.DIVISION)));
				}
				root.appendChild(operatorXML);
			}

		}
		// create the xml file
        //transform the DOM Object to an XML File
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e1) {
			e1.printStackTrace();
		}
        DOMSource domSource = new DOMSource(document);

        StringWriter writer = new StringWriter();
        try {
			transformer.transform(domSource, new StreamResult(writer));
		} catch (TransformerException e) {
			e.printStackTrace();
		} 
        // If you use
        // StreamResult result = new StreamResult(System.out);
        // the output will be pushed to the standard output ...
        // You can use that for debugging 
        
        String xmlString = writer.getBuffer().toString();
        return xmlString;
	}

	protected double receiveResultData(Socket clientSocket) throws IOException {
		// TODO
		return 0.0;
	}

	protected void sendRpnData(Socket clientSocket, List<Element> elements) throws IOException {
		DataOutputStream socketSaidaServer = new DataOutputStream(clientSocket.getOutputStream());
		// generates the xml message
		String xmlMessage = generateXMLMessage(elements);
		socketSaidaServer.writeUTF(xmlMessage);
		socketSaidaServer.flush();
	}

	public static void main(String[] args) {
		String serverAddr = "127.0.0.1";
		int serverPort = 9091;
		String expression = "5-3/8+5*9";

		CalculadoraClientXML clientXML = new CalculadoraClientXML(serverAddr, serverPort, expression);
		clientXML.execute();
	}
}
