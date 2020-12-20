package rmi;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import produtorconsumidor.TrataProdutorConsumidor;

public class RmiServer extends java.rmi.server.UnicastRemoteObject implements ReceiveMessageInterface {
	private static final long serialVersionUID = -1476830291005476913L;
	int thisPort;
	String thisAddress;
	Registry registry; // rmi registry for lookup the remote objects.
	TrataProdutorConsumidor produtorConsumidor;

	// This method is called from the remote client by the RMI.
	// This is the implementation of the �ReceiveMessageInterface�.
	public void receiveMessage(String mensagemDoCliente) throws RemoteException {
		if (mensagemDoCliente.contains("Produtor".toLowerCase())) this.produtorConsumidor.executarProdutor(mensagemDoCliente);
		else this.produtorConsumidor.executarConsumidor();
	}

	public RmiServer() throws RemoteException {
		try {
			this.produtorConsumidor = TrataProdutorConsumidor.getInstance();
			// get the address of this host.
			thisAddress = (InetAddress.getLocalHost()).toString();
		} catch (Exception e) {
			throw new RemoteException("can't get inet address.");
		}
		thisPort = 3232; // this port(registry�s port)
		System.out.println("this address=" + thisAddress + ",port=" + thisPort);
		try {
			// create the registry and bind the name and object.
			registry = LocateRegistry.createRegistry(thisPort);
			registry.rebind("rmiServer", this);
		} catch (RemoteException e) {
			throw e;
		}
	}

	static public void main(String args[]) {
		try {
			new RmiServer();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}