package net.iqinghe.test.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;

public class CuratorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String connectString = "192.168.1.105:2181";
		int sessionTimeOut = 60 * 1000;
		int connectTimeOut = 3 * 1000;
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient(
				connectString, sessionTimeOut, connectTimeOut, retryPolicy);
		client.start();
		try {
			client.create().forPath("/testCurator", "test".getBytes());
			client.create().withMode(CreateMode.EPHEMERAL).forPath("/testEP");
			client.getChildren().usingWatcher(new CuratorWatcher() {

				public void process(WatchedEvent arg0) throws Exception {
					System.out.println("event state is ::::" + arg0.getState());
				}
			}).forPath("/test");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
