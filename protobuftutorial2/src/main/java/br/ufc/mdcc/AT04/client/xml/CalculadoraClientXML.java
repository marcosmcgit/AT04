package br.ufc.mdcc.AT04.client.xml;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import br.ufc.mdcc.AT04.client.AbstractCalculadoraClient;
import br.ufc.mdcc.AT04.shared.model.Element;

/**
 * Cliente para o serviço de calculadora disponibilizado via sockets.
 */
public class CalculadoraClientXML extends AbstractCalculadoraClient {

	public CalculadoraClientXML(String serverAddr, int serverPort, String expression) {
		super(serverAddr, serverPort, expression);
	}

	protected double receiveResultData(Socket clientSocket) throws IOException {
		// TODO
		return 0.0;
	}

	protected void sendRpnData(Socket clientSocket, List<Element> elements) throws IOException {
		// TODO
		return;
	}

	public static void main(String[] args) {
		String serverAddr = "127.0.0.1";
		int serverPort = 9091;
		String expression = "5-3/8+5*9";

		CalculadoraClientXML clientXML = new CalculadoraClientXML(serverAddr, serverPort, expression);
		clientXML.execute();
	}
}
