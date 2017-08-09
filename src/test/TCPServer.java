package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer {
	private static final int SERVER_PORT = 5000;
	public static void main(String[] args) {

		ServerSocket serverSocket = null;
		try {
			// 1. Create server socket
			serverSocket = new ServerSocket();

			// 2. Binding
			InetAddress inetAddress = InetAddress.getLocalHost();
			String localhostAddress = inetAddress.getHostAddress();

			serverSocket.bind(new InetSocketAddress(localhostAddress, SERVER_PORT));
			System.out.println("[server] binding " + localhostAddress + ":" + SERVER_PORT);

			// 3. Accept
			Socket socket = serverSocket.accept(); // blocking

			// 4. When successfully connected
			InetSocketAddress remoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();

			int remoteHostPort = remoteSocketAddress.getPort();
			String remoteHostAddress = remoteSocketAddress.getAddress().toString();
			System.out.println("[server] connected from " + remoteHostAddress + ":" + remoteHostPort);

			try {
				// 5. Receiving I/O Stream
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				while (true) {
					// 6. Reading data
					byte[] buffer = new byte[256];
					int readByteCount = is.read(buffer);

					if (readByteCount <= -1) {
						System.out.println("[server] disconnected");
						break;
					}

					String data = new String(buffer, 0, readByteCount, "utf-8");
					System.out.println("[server] received: " + data);
					
					// 7. Writing data
					os.write(data.getBytes("utf-8"));
				}

			} catch (SocketException e) {
				//client closed the socket unexpectedly
				System.out.println("[server] closed by client");
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
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (serverSocket != null && !serverSocket.isClosed()) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();

			}
		}
	}

}
