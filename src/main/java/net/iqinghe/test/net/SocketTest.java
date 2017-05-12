package net.iqinghe.test.net;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketTest implements Runnable {

	public static void main(String[] args) {
		SocketTest st = new SocketTest();
		new Thread(st, "test-cg").start();
	}

	@Override
	public void run() {
		try {
			Socket s = new Socket("127.0.0.1", 31111);
			OutputStream output = s.getOutputStream();
			output.write("test".getBytes());
			output.flush();
			// output.close();
			while (true) {
				InputStream in = s.getInputStream();
				byte[] result = new byte[1024];
				int offset = 0;
				offset = in.read(result);
				if (offset > 0) {
					System.out.println("result is::::" + new String(result, 0, offset));
				}
			}
			// s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
