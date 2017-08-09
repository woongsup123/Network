package util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NSLookup {

	public static void main(String[] args) {
		String host = "www.youtube.com";
		try {
			InetAddress[] inetAddresses = InetAddress.getAllByName(host);
			
			for (InetAddress inetAddress: inetAddresses) {
				System.out.println(inetAddress.getHostAddress());
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
