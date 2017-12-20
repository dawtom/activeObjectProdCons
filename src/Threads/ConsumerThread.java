package Threads;
import Proxy.Proxy;
import main.Main;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ConsumerThread extends Thread {
    private Integer operationsCounter;
    private String name;
    private Proxy proxy;
    private boolean doOperation = true;

    private Random r;

    public ConsumerThread(String name, Proxy proxy){
        this.name = name;
        this.proxy = proxy;
        r = new Random();
        this.operationsCounter = 0;
    }

//    private Random r = new Random();

    public void run(){
        while(Main.threadsAreRunning){
            int howManyElements = r.nextInt(99999);
            Future<List<Integer>> f = proxy.consume(howManyElements);

            if (doOperation) {
                this.veryDifficultOperationForHalfASecond();
                doOperation = false;
            }
            doOperation = true;
            while (!f.isDone() && Main.threadsAreRunning){

            }
        }
        //System.out.println("Consumer finishing");
    }

    public void veryDifficultOperationForHalfASecond(){
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Main.operationsNumber.addAndGet(1);
    }
}
