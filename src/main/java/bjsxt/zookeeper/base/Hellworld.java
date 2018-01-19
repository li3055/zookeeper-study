package bjsxt.zookeeper.base;

import java.util.Date;

public class Hellworld {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("hello ");

        Date d = new Date(  );
        Thread.sleep(100000   );
        System.out.println(d.before(new Date()));
    }
}
