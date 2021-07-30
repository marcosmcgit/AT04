package br.ufc.mdcc.AT04.server.xml;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.w3c.dom.Node;

import br.ufc.mdcc.AT04.server.AbstractCalculadoraServer;
import br.ufc.mdcc.AT04.shared.model.Element;
import br.ufc.mdcc.AT04.shared.model.Number;
import br.ufc.mdcc.AT04.shared.model.Operator;

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
	
	//transform result on xml message to send to client
	private String convertResult2XML(double result) {
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		 
        DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = documentFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

        Document document = documentBuilder.newDocument();

        // root element
        org.w3c.dom.Element root = document.createElement("result");
        root.appendChild(document.createTextNode(String.valueOf(result)));
        document.appendChild(root);

		
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
        
        String xmlString = writer.getBuffer().toString();
        return xmlString;
	}

	protected void sendResultData(Socket connectionSocket, double result) throws IOException {
		String xmlResult = convertResult2XML(result);
		DataOutputStream socketOutput = new DataOutputStream(connectionSocket.getOutputStream());
		socketOutput.writeUTF(xmlResult);
		socketOutput.flush();
		socketOutput.close();
	}

	protected List<Element> receiveRpnData(Socket connectionSocket) throws IOException {
		DataInputStream socketInput = new DataInputStream(connectionSocket.getInputStream());
		String xmlExpression = socketInput.readUTF();
		org.w3c.dom.Document xmlDoc = convertStringToXMLDocument(xmlExpression);
		List<Element> rpn = convert2ListOfElement(xmlDoc);
		return rpn;
	}

}
