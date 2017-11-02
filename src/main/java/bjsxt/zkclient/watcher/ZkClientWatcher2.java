package bjsxt.zkclient.watcher;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

public class ZkClientWatcher2 {

	/** zookeeper地址 */
	static final String CONNECT_ADDR = "127.0.0.1:2181";
	/** session超时时间 */
	static final int SESSION_OUTTIME = 5000;//ms 
	
	
	public static void main(String[] args) throws Exception {
		ZkClient zkc = new ZkClient(new ZkConnection(CONNECT_ADDR), 5000);
		
		zkc.createPersistent("/super2", "1234");
		
		//对父节点添加监听子节点变化。
		zkc.subscribeDataChanges("/super2", new IZkDataListener() {
			@Override
			public void handleDataDeleted(String path) throws Exception {
				System.out.println("删除的节点为:" + path);
			}
			
			@Override
			public void handleDataChange(String path, Object data) throws Exception {
				System.out.println("变更的节点为:" + path + ", 变更内容为:" + data);
			}
		});
		
		Thread.sleep(3000);
		zkc.writeData("/super2", "456", -1);
		Thread.sleep(1000);

		zkc.delete("/super2");
		//Thread.sleep(Integer.MAX_VALUE);
		
		
	}
}
