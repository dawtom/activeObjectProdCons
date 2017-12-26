package Proxy;


import Request.ConsumeRequest;
import Request.ProduceRequest;
import Scheduler.Scheduler;
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


    }


    public Future<List<Integer>> consume(int howManyElements) {

        ConsumeRequest request = new ConsumeRequest(howManyElements, this.buffer);

        Future<List<Integer>> consumeRequestFuture = scheduler.enqueue(request);

        return consumeRequestFuture;
    }
}
