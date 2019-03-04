import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ServerMain extends Thread {

	List<ClientWorker> clients;
	int port = 9090;
	ServerSocket server;

	static DBConnection connection = new DBConnection();
	static int id;

	public ServerMain(DBConnection con, int port) {
		clients = new ArrayList<ClientWorker>();
		this.port = port;
		connection = con;

	}
	public void recalibreaza(int port)
	{
		int index = clients.size()-1;
		ClientWorker client = clients.get(index);
		client.reconecteaza(port);
	}

	public void run() {
		try {
			ServerSocket server = new ServerSocket(port);
			System.out.println("Start server");

			while (!server.isClosed()) {
				try {
					Socket socket = server.accept();
					connection.addClient(socket);
					ClientWorker clientWorker = new ClientWorker(this, socket, connection, ++id);
					new Thread(clientWorker).start();
					clients.add(clientWorker);
					System.out.println("Client: " + id + " with port: " + port);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	public void removeClient(ClientWorker client) {
		clients.remove(client);
	}

	public boolean allowClients() {
		return clients.size() < 3;
	}

	static public int avgServer(List<ServerMain> servers)
	{
		int average;
		int no_Of_Clients = 0;
		int no_Of_Servers = servers.size();
		for (ServerMain srv : servers)
		{
			no_Of_Clients += srv.clients.size();
		}
		if(servers.size() == 0)
		{
			return 0;
		}
		average = no_Of_Clients / no_Of_Servers;
		return average;    	
	}
	public static ServerMain minServer(List<ServerMain> servers)
	{
		int min = 4;
		for (ServerMain srv : servers)
		{
			if(srv.clients.size() <= min)
			{
				min = srv.clients.size();
			}  	
			if(srv.clients.size() == min)
			{
				return srv;
			}  
		}

		return null;    	
	}
	public static ServerMain maxServer(List<ServerMain> servers)
	{
		int max = -1;
		for (ServerMain srv : servers)
		{
			if(srv.clients.size() >= max)
			{
				max = srv.clients.size();
			}  	
			if(srv.clients.size() == max)
			{
				return srv;
			}  
		}

		return null;    	
	}
	public int getPort() {
		return port;
	}

	public static void main(String[] args) {
		try {
			int port = 9090;
			List<ServerMain> servers = new ArrayList<ServerMain>();
			DBConnection conection = new DBConnection();
			ServerSocket server = new ServerSocket(port);
			System.out.println("Start server");
			id = 0;
			port++;
			while (!server.isClosed()) {
				try {
					Socket socket = server.accept();
					PrintWriter pw = new PrintWriter(socket.getOutputStream());
					InputStreamReader dis = new InputStreamReader(socket.getInputStream());
					BufferedReader br = new BufferedReader(dis);
					BufferedWriter writer;
					String s = br.readLine();
					
					if (s.equalsIgnoreCase("da-mi port")) {
						boolean ok = false;						
						int p = port;
						//for (ServerMain srv : servers) {
						ServerMain sm = minServer(servers);
						if(sm == null)
						{
							sm = new ServerMain(conection, p);
							servers.add(sm);
							sm.start();
						}
						
						int min_Server_Port = sm.getPort();
						int avg = avgServer(servers);
						if((sm.clients.size() < avg) || (avg == 0))
						{
							ok = true;
							System.out.println("Reconectam un client la serverul cu nr minim de clienti");
							pw.write("port: " + min_Server_Port);
							pw.write("\n");
							pw.flush();
						}
						if(!ok) {
						for (ServerMain srv : servers) {
							if (srv.allowClients()) {
								System.out.println("iti dau server existent");
								p = srv.getPort();
								ok = true;							
								
							}		
							if (!ok) {
								port++;
								
								p = port;
								System.out.println("creez server nou");
								ServerMain sv = new ServerMain(connection, p);
								sv.start();
								servers.add(sv);
								if (br != null && pw != null) {
									pw.write("port: " + port);
									pw.write("\n");
									pw.flush();
									ok = true;
									
								}
								if(maxServer(servers).clients.size() >= avg)
								{
									maxServer(servers).recalibreaza(port);
								}
								if(ok)
									break;
							}


						}
						}
						pw.write("port: " + p);
						pw.write("\n");
						pw.flush();
					}
         
					socket.close();

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

}
