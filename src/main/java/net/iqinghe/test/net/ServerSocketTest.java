package net.iqinghe.test.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.IOUtils;

public class ServerSocketTest {
	private static ExecutorService excutor = null;

	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(31111);
			excutor = Executors.newFixedThreadPool(10);
			while (true) {
				Socket s = ss.accept();
				InputStream in = s.getInputStream();
				OutputStream out = s.getOutputStream();
				excutor.execute(new Runnable() {

					@Override
					public void run() {
						byte[] result = new byte[1024];
						int offset = 0;
						try {
							offset = in.read(result);
							out.write("OK".getBytes());
							out.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
						System.out.println("result is::::" + new String(result, 0, offset));
						IOUtils.closeQuietly(in);
						IOUtils.closeQuietly(out);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
