package rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class RmiClient {
	static public void main(String args[]) {
		ReceiveMessageInterface rmiServer;
		Registry registry;
		String serverAddress = "127.0.0.1";
		String serverPort = "3232";
		String mensagemParaServidor;
		Scanner teclado = new Scanner(System.in);
		String tipo = "";

		try {
			while (!tipo.equalsIgnoreCase("Produtor") && !tipo.equalsIgnoreCase("Consumidor")) {
				System.out.println("Produtor ou Consumidor?");
				tipo = teclado.nextLine();
			}

			boolean ehProdutor = tipo.equalsIgnoreCase("Produtor");
			System.out.println("Para sair digite /quit");
			if (ehProdutor)System.out.println("Digite algo para produzir");
			// get the �registry�
			registry = LocateRegistry.getRegistry(serverAddress, (new Integer(serverPort)).intValue());
			// look up the remote object
			rmiServer = (ReceiveMessageInterface) (registry.lookup("rmiServer"));
			// call the remote method
			while (teclado.hasNextLine()) {
				mensagemParaServidor = teclado.nextLine();
				if (mensagemParaServidor.startsWith("/quit"))
					break;
				rmiServer.receiveMessage("<" + tipo + ">: " + mensagemParaServidor);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		teclado.close();
	}
}
