package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPClient {
	
	private static final String SERVER_IP = "192.168.1.23";
	private static final int SERVER_PORT = 5000;
	
	public static void main (String[] args) {
		Socket socket = null;
		try {
			// 1. Creating socket
			socket = new Socket();
			
			// 2. Connecting to server
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			
			//3. Receiving I/O
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			
			//4. Reading and writing
			String data = "hello";
			os.write(data.getBytes("utf-8"));
			
			byte[] buffer = new byte[256];
			int readByteCount = is.read(buffer);
			if (readByteCount <= -1) {
				System.out.println("[client] disconnected by server");
				return;
			}
			data = new String(buffer, 0, readByteCount, "utf-8");
			System.out.println("[client] received:" + data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
}
