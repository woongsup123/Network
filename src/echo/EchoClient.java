package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
	
	private static final String SERVER_IP = "192.168.1.23";
	private static final int SERVER_PORT = 6000;
	
	public static void main(String[] args) {
		Socket socket = null;
		Scanner scanner = new Scanner(System.in);
		try {
			// 1. Creating socket
			socket = new Socket();

			// 2. Connecting to server
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));

			// 3. Receiving I/O
			BufferedReader br = new BufferedReader(
									new InputStreamReader(
											socket.getInputStream(), "UTF-8"
									)
								);
			PrintWriter pw = new PrintWriter(
									new OutputStreamWriter(
											socket.getOutputStream()
									), true
								);

			// 4. Reading and writing
			while(true) {
				System.out.print(">>");
				String message = scanner.nextLine();
				
				if (message.equals("exit")) {
					break;
				}
				//Sending message
				pw.println(message);
				
				//Receiving echo message
				String echoMessage = br.readLine();
				if (echoMessage == null) {
					System.out.println("[client] Disconnected by Server");
					break;
				}
				//Printing
				System.out.println("<< " + echoMessage);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
			try {
				if (socket != null && !socket.isClosed()) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
