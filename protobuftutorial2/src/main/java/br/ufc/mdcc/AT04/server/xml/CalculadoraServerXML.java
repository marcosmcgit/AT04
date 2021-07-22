package br.ufc.mdcc.AT04.server.xml;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.w3c.dom.Node;

import br.ufc.mdcc.AT04.server.AbstractCalculadoraServer;
import br.ufc.mdcc.AT04.shared.model.Element;
import br.ufc.mdcc.AT04.shared.model.Number;
import br.ufc.mdcc.AT04.shared.model.Operator;
import br.ufc.mdcc.AT04.shared.protobuffer.RPNProto.MElement.EnumOperator;

public class CalculadoraServerXML extends AbstractCalculadoraServer {

	public CalculadoraServerXML(int serverPort, String name) {
		super(serverPort, name);
	}
	
	//Convert string into XMLDom
	private org.w3c.dom.Document convertStringToXMLDocument(String xmlExpression) {
		//Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         
        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();
             
            //Parse the content to Document object
            org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(xmlExpression)));
            return doc;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return null;
	}
	
	/*
	 * Reads the XML message with the expression from an InputStream,
	 * returning a List of Element.
	 */
	private static List<Element> convert2ListOfElement(org.w3c.dom.Document xmlExpression) throws IOException {
		List<Element> rpn = new LinkedList<Element>();
		
		NodeList nodeList = xmlExpression.getElementsByTagName("*");
	    for (int i = 0; i < nodeList.getLength(); i++) {
	        Node node = (Node) nodeList.item(i);
	        if(node.getNodeName().toString() == "expression") continue;
	        else if (node.getNodeName().toString() == "number") {
	        	Number number = new Number(Double.parseDouble(node.getFirstChild().getNodeValue()));
				rpn.add(number);
	        } else {
	        	Operator operator = null;
				if (node.getFirstChild().getNodeValue().toString().equals("ADDITION")) {
					operator = Operator.plusSign();
				} else if (node.getFirstChild().getNodeValue().toString().equals("SUBTRACTION")) {
					operator = Operator.minusSign();
				} else if (node.getFirstChild().getNodeValue().toString().equals("MULTIPLICATION")) {
					operator = Operator.timesSign();
				} else if (node.getFirstChild().getNodeValue().toString().equals("DIVISION")) {
					operator = Operator.dividedBySign();
				}
				rpn.add(operator);
	        }
	        
	    }
		return rpn;
	}

	protected void sendResultData(Socket connectionSocket, double result) throws IOException {
		// TODO
		return;
	}

	protected List<Element> receiveRpnData(Socket connectionSocket) throws IOException {
		DataInputStream socketInput = new DataInputStream(connectionSocket.getInputStream());
		String xmlExpression = socketInput.readUTF();
		org.w3c.dom.Document xmlDoc = convertStringToXMLDocument(xmlExpression);
		List<Element> rpn = convert2ListOfElement(xmlDoc);
		return rpn;
	}

}
