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
    public Proxy() {
        this.scheduler = new Scheduler();
        this.buffer = new Buffer(200000);
    }

    public Future<Integer> produce(List<Integer> input)
    {
        int inputSize = input.size();
        Main.alreadyOrdered.addAndGet(inputSize);
        ProduceRequest request = new ProduceRequest(inputSize, this.buffer, input);

        Future<Integer> produceRequestFuture = scheduler.enqueue(request);

        return produceRequestFuture;

        /*int i = 0;

        System.out.println("In proxy request " + request);
        while (request.reesult == null) {
            i++;
            System.out.println("Proxy waiting for null " + i + " times.");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/


        /*while (!request.reesult.isDone()) {
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

        return request.reesult.get();*/
    };


    public Future<List<Integer>> consume(int howManyElements) {

        ConsumeRequest request = new ConsumeRequest(howManyElements, this.buffer);

        Future<List<Integer>> consumeRequestFuture = scheduler.enqueue(request);

        return consumeRequestFuture;
    }
}
