package bjsxt.zkclient.base;
 
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
 
public class Test {
 
    static int count = 50;
    public static void genarNo(){
       try {
           count--;
           System.out.println(count);
       } finally {
      
       }
    }
    public static void main(String[] args) throws InterruptedException {
      
      
   
      
       final CountDownLatch countdown = new CountDownLatch(1);
       for(int i = 0; i < 40; i++){
           final int threadId = i;
           new Thread(new Runnable() {
              @Override
              public void run() {
                  ZkClientLock zkLock = new ZkClientLock(threadId, " 第" + threadId + "个线程 ");
                  try {
                     zkLock.tryGetLock();
                     System.out.println(" 第" + threadId + "个线程 可以做我的工作了");
                     genarNo();
                      
                  } catch (Exception e) {
                     e.printStackTrace();
                  } finally {
                     zkLock.releaseLock();
                  }
              }
           },"t" + i).start();
       }
       Thread.sleep(50);
       countdown.countDown();
       //Thread.sleep(Long.MAX_VALUE);
      
    }
 
   
   
}