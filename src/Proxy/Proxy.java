package Proxy;


import Request.IRequest;
import Request.ProduceRequest;
import Scheduler.Scheduler;
import main.FTest;
import main.Main;

import java.util.concurrent.*;

public class Proxy  {

    private Scheduler scheduler;

    private int howManyElements;

    public Proxy(int howManyElements) {
        this.scheduler = new Scheduler();
        this.howManyElements = howManyElements;
    }

    public Callable<Integer> produce = () -> {
//        FTest fTest = new FTest();
//        Main.staticListInMain.add(fTest);

        IRequest request = new ProduceRequest(this.howManyElements);

        Future<Void> produceRequestFuture = scheduler.enqueue(request);



        int i = 0;

        System.out.println("In proxy request " + request);
        while (request.reesult == null) {
            i++;
            System.out.println("Proxy waiting for null " + i + " times.");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        while (!request.reesult.isDone()) {
            System.out.println("Proxy waiting for done");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("future done? " + produceRequestFuture.isDone());
        try {
            System.out.print("result: " + request.reesult.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return request.reesult.get();
    };


    public Future<Integer> consume() {
        return null;
    }

    public int getHowManyElements() {
        return howManyElements;
    }

    public void setHowManyElements(int howManyElements) {
        this.howManyElements = howManyElements;
    }
}
