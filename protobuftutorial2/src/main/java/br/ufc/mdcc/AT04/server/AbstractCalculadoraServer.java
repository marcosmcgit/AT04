package br.ufc.mdcc.AT04.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import br.ufc.mdcc.AT04.shared.model.Element;

public abstract class AbstractCalculadoraServer implements Runnable {

	protected int serverPort;
	protected String name;

	protected abstract List<Element> receiveRpnData(Socket connectionSocket) throws IOException;

	protected abstract void sendResultData(Socket connectionSocket, double result) throws IOException;

	public AbstractCalculadoraServer(int serverPort, String name) {
		super();
		this.serverPort = serverPort;
		this.name = name;
	}

	@Override
	public void run() {
		ServerSocket welcomeSocket;

		try {
			welcomeSocket = new ServerSocket(serverPort);
			int i = 0; // número de clientes
			System.out.println("Servidor " + name + " no ar");

			// O programa aguardará conexões eternamente...
			while (true) {
				Socket connectionSocket = welcomeSocket.accept();
				i++;
				System.out.println("Nova requisição " + name + ". Total de requisições até o momento: " + i);

				try {
					// receiving...
					List<Element> rpn = receiveRpnData(connectionSocket);

					// solving..
					double result = solveRpn(rpn);
					System.out.println("Resultado " + name + ": " + String.valueOf(result));

					// sending
					sendResultData(connectionSocket, result);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private double solveRpn(List<Element> rpn) {
		ICalculator calculator = new Calculadora();
		double result = ServerUtil.solveRpn(rpn, calculator);
		return result;
	}

}