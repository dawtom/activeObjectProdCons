package Scheduler;

import Request.ConsumeRequest;
import Request.IRequest;
import Request.ProduceRequest;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

public class Scheduler {
    public Scheduler() {
        this.producersQueue = new ConcurrentLinkedQueue<>();
        this.consumersQueue = new ConcurrentLinkedQueue<>();
    }

    private ConcurrentLinkedQueue<IRequest> producersQueue;
    private ConcurrentLinkedQueue<IRequest> consumersQueue;

    public Future<Void> enqueue(IRequest request){
        // sprawdź jaki Request przychodzi
        // dodaj na koniec odpowiedniej kolejki

        if (request instanceof ProduceRequest){
            producersQueue.offer(request);
        }
        if (request instanceof ConsumeRequest){
            consumersQueue.offer(request);
        }

        return null;
    };



}
