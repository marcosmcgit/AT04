package br.ufc.mdcc.AT04.server.yaml;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import br.ufc.mdcc.AT04.server.AbstractCalculadoraServer;
import br.ufc.mdcc.AT04.shared.model.Element;

public class CalculadoraServerYAML extends AbstractCalculadoraServer {

	public CalculadoraServerYAML(int serverPort, String name) {
		super(serverPort, name);
	}

	protected void sendResultData(Socket connectionSocket, double result) throws IOException {
		// TODO
		return;
	}

	protected List<Element> receiveRpnData(Socket connectionSocket) throws IOException {
		// TODO
		return null;
	}

}
