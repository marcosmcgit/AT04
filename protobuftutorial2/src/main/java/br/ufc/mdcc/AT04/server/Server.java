package br.ufc.mdcc.AT04.server;

import br.ufc.mdcc.AT04.server.protobuffer.CalculadoraServerProtobuffer;
import br.ufc.mdcc.AT04.server.xml.CalculadoraServerXML;
import br.ufc.mdcc.AT04.server.yaml.CalculadoraServerYAML;

public class Server {

	public static void main(String[] args) {
		int protobufferPort = 9090;
		String protobufferName = "Protobuffer";

		int xmlPort = 9091;
		String xmlName = "XML";

		int yamlPort = 9092;
		String yamlName = "YAML";

		CalculadoraServerProtobuffer calculadoraServerProtobuffer = new CalculadoraServerProtobuffer(protobufferPort,
				protobufferName);
		new Thread(calculadoraServerProtobuffer).start();

		CalculadoraServerXML calculadoraServerXML = new CalculadoraServerXML(xmlPort, xmlName);
		new Thread(calculadoraServerXML).start();

		CalculadoraServerYAML calculadoraServerYAML = new CalculadoraServerYAML(yamlPort, yamlName);
		new Thread(calculadoraServerYAML).start();
	}

}
