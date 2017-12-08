package Proxy;


import Request.ConsumeRequest;
import Request.IRequest;
import Request.ProduceRequest;
import Scheduler.Scheduler;
import main.FTest;
import main.Main;
import Buffer.Buffer;

import java.util.List;
import java.util.concurrent.*;

public class Proxy  {

    private Scheduler scheduler;
    private Buffer buffer;

    private int howManyElements;
    private List<Integer> input;

    public void setInput(List<Integer> input) {
        this.input = input;
    }

    public Proxy(int howManyElements) {
        this.scheduler = new Scheduler();
        this.howManyElements = howManyElements;
        this.buffer = new Buffer(20);
    }

    public Callable<Integer> produce = () -> {

        ProduceRequest request = new ProduceRequest(this.howManyElements, this.buffer, this.input);

        Future<Void> produceRequestFuture = scheduler.enqueue(request);

        Future<Integer> x = new CompletableFuture<>();

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
            System.out.println("Proxy producer waiting for done");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        System.out.println("future done? " + produceRequestFuture.isDone());
        try {
            System.out.print("result: " + request.reesult.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return request.reesult.get();
    };


    public Callable<List<Integer>> consume = () -> {

        ConsumeRequest request = new ConsumeRequest(this.howManyElements, this.buffer);

        Future<Void> produceRequestFuture = scheduler.enqueue(request);


        int i = 0;

        System.out.println("In proxy request " + request);
        while (request.reesult == null) {
            i++;
            System.out.println("Proxy consumer waiting for null " + i + " times.");
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

//        System.out.println("future done? " + produceRequestFuture.isDone());
        try {
            System.out.print("result: " + request.reesult.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        List<Integer> result = null;
        try {
            result = request.reesult.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    };
}
