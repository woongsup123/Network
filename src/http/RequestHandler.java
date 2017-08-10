package http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;

public class RequestHandler extends Thread{
	private static final String DOCUMENT_ROOT = "./webapp";
	private Socket socket;
	
	public RequestHandler(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			InetSocketAddress inetSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
			consoleLog( "connected from " + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort() );
			
			InputStreamReader isr = new InputStreamReader(socket.getInputStream(), "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			
			OutputStream os = socket.getOutputStream();
			
			String request = null;
			
			while(true) {
				String line = br.readLine();
				if (line == null || line.equals("")) {
					break;
				}
				
				if (request == null) { // reading only the first line
					request = line;
					break;
				}
			}
			
			consoleLog(request);
			
			String[] tokens = request.split(" ");
			if (tokens[0].equals("GET")) {
				responseStaticResource(os, tokens[1], tokens[2]);
			} else {
				response400Error(os, tokens[2]);
				consoleLog("Bad Request");
			}
			
		} catch( Exception ex ) {
			consoleLog( "error:" + ex );
		} finally {
			// clean-up
			try{
				if( socket != null && socket.isClosed() == false ) {
					socket.close();
				}
			} catch( IOException ex ) {
				consoleLog( "error:" + ex );
			}
		}	
	}
	
	private void response400Error(OutputStream os, String protocol) throws IOException {
		File file = new File (DOCUMENT_ROOT + "/error/400.html");
		byte[] body = Files.readAllBytes(file.toPath());
		String mimeType = Files.probeContentType(file.toPath());
		
		os.write((protocol + " 400 Bad Request\r\n").getBytes("UTF-8"));
		os.write(("Content-Type:" + mimeType + "; charset=utf-8\r\n").getBytes("UTF-8") );
		os.write("\r\n".getBytes());
		//sending body
		os.write(body);
	}
	
	private void response404Error(OutputStream os, String protocol) throws IOException {
		
		File file = new File (DOCUMENT_ROOT + "/error/404.html");
		
		byte[] body = Files.readAllBytes(file.toPath());
		String mimeType = Files.probeContentType(file.toPath());
		
		os.write((protocol + " 404 Not Found\r\n").getBytes("UTF-8"));
		os.write(("Content-Type:" + mimeType + "; charset=utf-8\r\n").getBytes("UTF-8") );
		os.write("\r\n".getBytes());
		//sending body
		os.write(body);
	}
	
	private void responseStaticResource(OutputStream os, String url, String protocol) throws IOException {
		if (url.equals("/")) {
			url = "/index.html";
		}
		
		File file = new File (DOCUMENT_ROOT + url);
		if (!file.exists()) {
			response404Error(os, protocol);
			return;
		}
		
		byte[] body = Files.readAllBytes(file.toPath());
		String mimeType = Files.probeContentType(file.toPath());
		
		//sending header
		os.write((protocol + " 200 OK\r\n").getBytes("UTF-8"));
		os.write(("Content-Type:" + mimeType + "; charset=utf-8\r\n").getBytes("UTF-8") );
		os.write("\r\n".getBytes());
		//sending body
		os.write(body);
	}

	private void consoleLog(String message) {
		System.out.println("[RequestHandler#" + getId() + "] " + message);
	}
}






















