package net.iqinghe.test.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class ZooKeeperClient {
	private ZooKeeper server;
	private CountDownLatch connected = new CountDownLatch(1);

	public boolean connect(String connectStr, int sessionTimeOut,
			Watcher watcher) {
		try {
			server = new ZooKeeper(connectStr, sessionTimeOut, new Watcher() {
				public void process(WatchedEvent event) {
					if (KeeperState.SyncConnected.equals(event.getState())) {
						connected.countDown();
					}
				}
			});
			try {
				connected.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String create(String path, byte[] data) {
		return this.create(path, data, CreateMode.PERSISTENT);
	}

	public String create(String path, byte[] data, CreateMode createMode) {
		String resultPath = "";
		if (server != null) {
			// List<ACL> acl = new ArrayList<ACL>();
			// acl.add(new ACL())
			try {
				resultPath = server.create(path, data, Ids.OPEN_ACL_UNSAFE,
						createMode);
			} catch (KeeperException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return resultPath;
	}

	public void close() throws InterruptedException {
		if (server != null) {
			server.close();
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public static void main(String[] args) throws IOException, KeeperException,
			InterruptedException {
		String connectString = "192.168.1.105:2181";
		ZooKeeperClient client = new ZooKeeperClient();
		boolean result = client.connect(connectString, 60 * 1000, null);
		System.out.println("connected result is::::" + result);
		// String create = client.create("/test", "testNode".getBytes());
		// System.out.println("create result is::::" + create);
		System.out.println("create emnode is::::"
				+ client.create("/test1", "testNode2".getBytes(),
						CreateMode.EPHEMERAL));
		// List<String> nodes = zk.getChildren("/test", false);
		// System.out.println("nodes is:" + nodes);
	}

}
