package bjsxt.zookeeper.pt;

import bjsxt.zookeeper.watcher.FileUtils;
import bjsxt.zookeeper.watcher.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChangePairsAll {

    static String filePath = "D:/ProfitTrailer/trading/PAIRS.properties";



    public static void main(String[] args) {
        PairsHigh.high();
        PairsMiddle.middle();
        PairsLow.low();
    }




}
