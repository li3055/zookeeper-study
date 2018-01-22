package bjsxt.zookeeper.pt;

import bjsxt.zookeeper.watcher.FileUtils;
import bjsxt.zookeeper.watcher.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PairsHigh {

    static String filePath = ChangePairsAll.filePath;
    //要修改的币
    static String[] firstlevel = {"ADA", "BNB","ETH","NEO","BCD","LEND","QTUM"};


    static String[] changeParams = {"max_cost", "trailing_buy", "buy_strategy", "buy_value", "sell_value", "trailing_profit"};
    static String max_cost = "0.01";
    static String trailing_buy = "0.4";
    static String buy_strategy = "EMASPREAD";
    static String buy_value = "-1.08";
    static String sell_value = "2";
    static String trailing_profit = "0.3";
    static String market = "BTC";



    public static void main(String[] args) {

    }


    public  static void high () {
        File dir = new File(filePath);

        List<String> list = FileUtils.readToList(filePath);
        List<String> listNew = new ArrayList<String>();
        for (String lineStr : list) {
            //System.out.println(s.indexOf("liquan"));
            if (lineStr.indexOf("#") >= 0) {
                listNew.add(lineStr);
                continue;
            }
            if (StringUtils.isEmpty(lineStr)) {
                listNew.add(lineStr);
                continue;
            }
            boolean replace = false;
            for (String lev : firstlevel) {
                if (lineStr.indexOf(lev+market) >= 0) {
                    for (String changeParam : changeParams) {
                        if (lineStr.indexOf(changeParam) >= 0) {
                            replace = true;
                            if (changeParam.equals("max_cost")) {
                                listNew.add(lev +market+ "_" + "max_cost = " + max_cost);
                            }
                            if (changeParam.equals("trailing_buy")) {
                                listNew.add(lev +market+ "_" + "trailing_buy = " + trailing_buy);
                            }
                            if (changeParam.equals("buy_strategy")) {
                                listNew.add(lev + market+"_" + "buy_strategy = " + buy_strategy);
                            }
                            if (changeParam.equals("buy_value")) {
                                listNew.add(lev +market+ "_" + "buy_value = " + buy_value);
                            }
                            if (changeParam.equals("sell_value")) {
                                listNew.add(lev +market+ "_" + "sell_value = " + sell_value);
                            }
                            if (changeParam.equals("trailing_profit")) {
                                listNew.add(lev +market+ "_" + "trailing_profit = " + trailing_profit);
                            }
                        }
                    }
                }
            }
            if (!replace) {
                listNew.add(lineStr);
            }


        }
        FileUtils.writeLine(filePath, listNew);
    }
}
