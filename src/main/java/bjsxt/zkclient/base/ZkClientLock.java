package bjsxt.zkclient.base;
 
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;
 
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
 
public class ZkClientLock {
 
    /** zookeeper地址 */
    public static final String CONNECT_ADDR = "127.0.0.1:2181";
    /** session超时时间 */
    static final int SESSION_OUTTIME = 5000;// ms
 
    ZkClient zkc;
 
    static String PATH = "/basePath";
    static String SUB_PATH = "/basePath/sub";
 
    static int count = 10;
 
    private int threadId;
 
    private String threadName = "";
 
    private String selfCreatePath;
 
    final CountDownLatch countdown = new CountDownLatch(1);
 
    public ZkClientLock(int threadId, String threadName) {
       super();
       this.zkc = new ZkClient(new ZkConnection(CONNECT_ADDR), 5000);
       this.threadId = threadId;
       this.threadName = threadName;
       genarBasePath();
       createSeq("该节点由线程" + threadId + "创建");
      
    }
   
        void genarBasePath(){
       try {
           if(!zkc.exists(PATH)){
              zkc.createPersistent("/basePath", true);
           }
       }catch( Exception e){
           System.out.println("最基路径创建失败");
       }
       finally {
           if(!zkc.exists(PATH)){
              genarBasePath();
           }
       }
    }
 
    public static void main(String[] args) throws Exception {
 
       // final ReentrantLock reentrantLock = new ReentrantLock();
       final CountDownLatch countdown = new CountDownLatch(1);
 
       for (int i = 0; i < 10; i++) {
           final int threadId = i + 1;
           new Thread(new Runnable() {
              @Override
              public void run() {
                  try {
                     countdown.await();
                     ZkClientLock zkLock = new ZkClientLock(threadId, " 第" + threadId + "个线程 ");
                     // 加锁
                     zkLock.tryGetLock();
                     // if (zkLock.tryGetLock()) {
                     // zkLock.doBusiness();
                     // }
                     // -------------业务处理结束
                  } catch (Exception e) {
                     e.printStackTrace();
                  } finally {
                     try {
                         // 释放
                         // reentrantLock.unlock();
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                  }
              }
           }, "t" + i).start();
       }
       Thread.sleep(100);
       countdown.countDown();
       Thread.sleep(Long.MAX_VALUE);
    }
 
    void createSeq(String data) {
       this.selfCreatePath = zkc.createEphemeralSequential(SUB_PATH, data);
    }
 
    boolean tryGetLock() {
        
       List<String> childrens = zkc.getChildren(PATH);
       Collections.sort(childrens);
       int selfIndex = childrens.indexOf(this.selfCreatePath.substring(PATH.length() + 1));
       if (selfIndex == -1) {
           System.out.println(threadName + selfCreatePath + "节点失效，不存在 " + selfCreatePath);
           return false;
       } else if (selfIndex == 0) {
           System.out.println(threadName + selfCreatePath + "节点排名第一，获得锁 ");
           countdown.countDown();
           return true;
       } else {
           System.out.println(threadName + selfCreatePath + "节点排名第 " + (selfIndex + 1) + " 监控前一个节点" + childrens.get(selfIndex - 1));
          zkc.subscribeDataChanges(PATH + "/" + childrens.get(selfIndex - 1), new IZkDataListener() {
 
              @Override
              public void handleDataDeleted(String arg0) throws Exception {
                  System.out.println(threadName + arg0 + "节点已经删除，被我监听到了，尝试获得锁");
                 
                  //doBusiness();
                   tryGetLock();
              }
 
              @Override
              public void handleDataChange(String arg0, Object arg1) throws Exception {
 
              }
           });
           try {
              countdown.await();
           } catch (InterruptedException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
           }
           return false;
       }
    }
 
    void releaseLock() {
           System.out.println(threadName + "释放锁 "  +selfCreatePath);
           zkc.delete(this.selfCreatePath);
        
    }
 
   
}