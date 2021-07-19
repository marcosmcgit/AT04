package br.ufc.mdcc.AT04.client;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import br.ufc.mdcc.AT04.shared.model.Element;

public abstract class AbstractCalculadoraClient {

	protected String serverAddr;
	protected int serverPort;
	protected String expression;

	public AbstractCalculadoraClient(String serverAddr, int serverPort, String expression) {
		super();
		this.serverAddr = serverAddr;
		this.serverPort = serverPort;
		this.expression = expression;
	}

	protected abstract double receiveResultData(Socket clientSocket) throws IOException;

	protected abstract void sendRpnData(Socket clientSocket, List<Element> elements) throws IOException;

	public void execute() {
		// converts the expression to a List of Element containing the reverse polish
		// notation form of the expression
		List<Element> elements = ClientUtil.Exp2Rpn(expression);

		try {
			Socket clientSocket = new Socket(serverAddr, serverPort);

			// sending...
			sendRpnData(clientSocket, elements);

			// receiving...
			double result = receiveResultData(clientSocket);

			System.out.println("resultado=" + result);

			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}