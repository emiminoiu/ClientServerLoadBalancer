package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import Model.Elev;
import Model.TabelModelElevi;
import view.UpdateFrame;

public class ClientMain extends JFrame {
	JTextArea areaMesaje;
	Socket socket;
	private BufferedWriter writer;
	private BufferedReader reader;
	TabelModelElevi tabelmodel;
	boolean useJSonFormat = true;
	int clientCount = 0;

	public ClientMain() {
		super("Client");
		tabelmodel = new TabelModelElevi(null);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		JLabel lblMesaj = new JLabel();
		lblMesaj.setText("Mesaj:");
		lblMesaj.setBounds(10, 10, 100, 30);
		panel.add(lblMesaj);

		JTextField txtMesaj = new JTextField();
		txtMesaj.setBounds(120, 10, 200, 30);
		panel.add(txtMesaj);

		JButton btnSend = new JButton("Send");
		btnSend.setBounds(340, 10, 100, 30);
		panel.add(btnSend);
		btnSend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				OperationDb(txtMesaj.getText());
			}
		});

		areaMesaje = new JTextArea();
		areaMesaje.setBounds(10, 50, 440, 200);
		JScrollPane scrollPane = new JScrollPane(areaMesaje);
		scrollPane.setBounds(10, 50, 440, 200);
		panel.add(scrollPane);

		JTable tabel = new JTable(tabelmodel);
		JScrollPane scroll = new JScrollPane(tabel);
		scroll.setBounds(460, 10, 400, 240);
		panel.add(scroll);

		JButton btnRefresh = new JButton("Retrieve");
		btnRefresh.setBounds(460, 270, 90, 30);
		panel.add(btnRefresh);
		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OperationDb("retrieve");
			}
		});

		JButton btnInsert = new JButton("Insert");
		btnInsert.setBounds(560, 270, 90, 30);
		panel.add(btnInsert);
		btnInsert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UpdateFrame updateFrame = new UpdateFrame(ClientMain.this);
				OperationDb("insert", updateFrame.getElev());
			}
		});

		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(660, 270, 90, 30);
		panel.add(btnUpdate);
		btnUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = tabel.getSelectedRow();
				Elev elev = tabelmodel.getElev(index);

				if (elev == null) {
					JOptionPane.showMessageDialog(ClientMain.this, "Nu s-a selectat nimic");
				} else {
					UpdateFrame updateFrame = new UpdateFrame(ClientMain.this, elev);
					OperationDb("update", updateFrame.getElev());
				}

			}
		});

		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(760, 270, 90, 30);
		panel.add(btnDelete);
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = tabel.getSelectedRow();
				Elev elev = tabelmodel.getElev(index);
				if (elev == null) {
					JOptionPane.showMessageDialog(ClientMain.this, "Nu s-a selectat nimic");
				} else {
					OperationDb("delete", elev);
				}
			}
		});

		add(panel);
		pack();
		setSize(900, 350);
		setVisible(true);
		connect(9090, false);
		int port = getServerPort();
		if (port != -1) {
			connect(port, true);
		} else {
			JOptionPane.showMessageDialog(this, "Nu am reusit sa preiau portul!");
		}
	}

	public void addMessage(String message) {
		areaMesaje.append(message);
	}

	public void connect(int port, boolean finala) {
		InetAddress address;
		// int port = 9090;
		try {
			address = InetAddress.getLocalHost();
			socket = new Socket(address, port);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			InputStreamReader dis = new InputStreamReader(socket.getInputStream());
			reader = new BufferedReader(dis);
			if (finala) {
				new Thread(new RetrieveServerResponse(this, reader, true)).start();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void reconnect(int port, boolean finala) {
		InetAddress address;
		// int port = 9090;
		try {
			address = InetAddress.getLocalHost();
			socket = new Socket(address, port);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			InputStreamReader dis = new InputStreamReader(socket.getInputStream());
			reader = new BufferedReader(dis);
			if (finala) {
				new Thread(new RetrieveServerResponse(this, reader, true)).start();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	private int getServerPort() {

		if (writer != null && reader != null) {
			String msg = "da-mi port";
			try {
				writer.write(msg);
				writer.newLine();
				writer.flush();			
				areaMesaje.append("Client: " + msg + "\n");
				msg = reader.readLine();
				areaMesaje.append("Server: " + msg + "\n");
				String[] ss = msg.split("\\:");
				if (ss[0].equalsIgnoreCase("port")) {
					ss[1] = ss[1].trim();
					int port = Integer.parseInt(ss[1]);
					System.out.println(port);
					return port;
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
					writer.close();
					socket.close();
				} catch (Exception e) {
				}
			}
		}
		return -1;
	}

	public void sendMessage(String mesaj) {
		String msg = "<comand><action>" + mesaj + "</action>";
		msg += "</comand>";
		if (writer != null) {
			try {
				writer.write(msg);
				writer.newLine();
				writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			areaMesaje.append("Client: " + msg + "\n");
		}
	}

	public void sendJSonMessage(String mesaj) {

		JSONObject json = new JSONObject();

		json.put("action", mesaj);

		JSONObject jsonObj = new JSONObject();

		jsonObj.put("command", json);

		String message = jsonObj.toJSONString();
		String msg = message;

		if (writer != null) {
			try {
				writer.write(msg);
				writer.newLine();
				writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			areaMesaje.append("Client: " + msg + "\n");
		}
	}

	public void sendMessageWithElev(String mesaj, Elev elev) {
		String msg = "<comand><action>" + mesaj + "</action>";
		if (mesaj.equalsIgnoreCase("update")) {
			msg = msg + "<content>" + elev.toXML() + "</content>";
		} else if (mesaj.equalsIgnoreCase("insert")) {
			msg = msg + "<content>" + elev.toXML() + "</content>";
		} else if (mesaj.equalsIgnoreCase("delete")) {
			msg = msg + "<content>" + elev.toXML() + "</content>";
		}

		msg += "</comand>";
		if (writer != null) {
			try {
				writer.write(msg);
				writer.newLine();
				writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			areaMesaje.append("Client: " + msg + "\n");
		}
	}

	public void sendJsonMessageWithElev(String mesaj, Elev elev) {
		JSONObject json = new JSONObject();
		json.put("action", mesaj);
		JSONObject jsonElev = elev.toJson();
		JSONObject jsonCommand = new JSONObject();
		json.put("content", jsonElev);
		jsonCommand.put("command", json);
		String message = jsonCommand.toJSONString();

		if (writer != null) {
			try {
				writer.write(message);
				writer.newLine();
				writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			areaMesaje.append("Client: " + message + "\n");
		}
	}

	public void OperationDb(String operation) {
		if (useJSonFormat)
			sendJSonMessage(operation);
		else
			sendMessage(operation);
	}

	public void parseResponse(List<Object> response) {

		String action = (String) response.get(0);

		if (action.toLowerCase().equals("retrieve")) {
			List<Elev> elevi = new ArrayList<Elev>();
			for (int i = 1; i < response.size(); i++) {
				Elev el = (Elev) response.get(i);
				elevi.add(el);
			}
			tabelmodel.setElevi(elevi);
		} else if (action.toLowerCase().equals("update") || action.toLowerCase().equals("delete")
				|| action.toLowerCase().equals("insert")
				|| action.toLowerCase().startsWith("baza de date a fost updatata de clientul")) {

			{
				OperationDb("retrieve");
			}
		}
	}

	private void OperationDb(String operation, Elev e) {
		if (useJSonFormat)
			sendJsonMessageWithElev(operation, e);
		else
			sendMessageWithElev(operation, e);
	}

	public static void main(String[] args) {
		try {

			ClientMain clientMain = new ClientMain();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
