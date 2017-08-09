package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class EchoServerReceiveThread extends Thread {

	private Socket socket;
	
	public EchoServerReceiveThread(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		// 4. When successfully connected
		InetSocketAddress remoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();

		int remoteHostPort = remoteSocketAddress.getPort();
		String remoteHostAddress = remoteSocketAddress.getAddress().toString();
		consoleLog("connected from " + remoteHostAddress + ":" + remoteHostPort);

		try {
			// 5. Receiving I/O Stream
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
			while (true) {
				// 6. Reading data
				String message = br.readLine();

				if (message == null) {
					consoleLog("disconnected");
					break;
				}

				consoleLog("received: " + message);

				// 7. Writing data
				pw.println(message);
			}

		} catch (SocketException e) {
			// client closed the socket unexpectedly
			consoleLog("closed by client");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null && !socket.isClosed()) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void consoleLog(String log) {
		System.out.println("[server: " + getId() + "]" + log);
	}
}
