package Threads;
import Proxy.Proxy;
import main.Main;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ProducerThread extends Thread{
    private String name;
    private Proxy proxy;
    private Random r;
    private Integer operationsCounter;
    private boolean doOperation = true;

    public ProducerThread(String name, Proxy proxy){
        this.name = name;
        this.proxy = proxy;
        r = new Random();
        this.operationsCounter = 0;
    }

    public void run(){
        while(Main.threadsAreRunning){
            int howManyElements = (r.nextInt(99999));
            Future<Integer> myResult =  proxy.produce(this.generateRandomList(howManyElements));

            if (doOperation) {
                this.veryDifficultOperationForHalfASecond();
                doOperation = false;
            }
            doOperation = true;

            while (!myResult.isDone() && Main.threadsAreRunning) {
            }
            //System.out.println("PRODUCER BACK");
        }
//        System.out.println("Name: " + this.name + ", operations: " + this.operationsCounter);
        //throw new NullPointerException();
    }

    private List<Integer> generateRandomList(int howManyElements){
        List<Integer> result = new LinkedList<>();
        for (int i = 0; i < howManyElements; i++) {
            result.add(r.nextInt()%100);
        }
        return result;
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
