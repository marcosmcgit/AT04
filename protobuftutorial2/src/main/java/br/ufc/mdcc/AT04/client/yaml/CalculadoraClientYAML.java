package br.ufc.mdcc.AT04.client.yaml;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import br.ufc.mdcc.AT04.client.AbstractCalculadoraClient;
import br.ufc.mdcc.AT04.shared.model.Element;

/**
 * Cliente para o servi�o de calculadora disponibilizado via sockets.
 */
public class CalculadoraClientYAML extends AbstractCalculadoraClient {

	public CalculadoraClientYAML(String serverAddr, int serverPort, String expression) {
		super(serverAddr, serverPort, expression);
	}

	public void generateYAML(List<Element> elements) {
		
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
		int serverPort = 9092;
		String expression = "5-3/8+5*9";

		CalculadoraClientYAML clientYAML = new CalculadoraClientYAML(serverAddr, serverPort, expression);
		clientYAML.execute();
	}
}
