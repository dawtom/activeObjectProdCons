package Scheduler;

import Request.ConsumeRequest;
import Request.IRequest;
import Request.ProduceRequest;
import main.Main;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Scheduler {

    ReentrantLock lock;
    Condition dispatchLoopCondition;


    public Scheduler() {
        this.producersQueue = new ConcurrentLinkedQueue<>();
        this.consumersQueue = new ConcurrentLinkedQueue<>();
        lock = new ReentrantLock();
        dispatchLoopCondition = lock.newCondition();
        dispatch();

    }

    private ConcurrentLinkedQueue<ProduceRequest> producersQueue;
    private ConcurrentLinkedQueue<ConsumeRequest> consumersQueue;

    public Future enqueue(IRequest request){
        lock.lock();
        // sprawdź jaki Request przychodzi
        // dodaj na koniec odpowiedniej kolejki

        if (request instanceof ProduceRequest){
            producersQueue.offer((ProduceRequest) request);
        }
        if (request instanceof ConsumeRequest){
            consumersQueue.offer((ConsumeRequest) request);
        }

        dispatchLoopCondition.signal();
        System.out.println("Added to queue");
        System.out.println("ConsumersRequests: " + consumersQueue.toString());
        System.out.println("ProducersRequests: " + producersQueue.toString());
        lock.unlock();
        return null;
    };

    private void dispatch() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    lock.lock();
                    System.out.println("In dispatch loop");
                    if (producersQueue.isEmpty() && consumersQueue.isEmpty()){
                        System.out.println("both empty");
                        try {
                            dispatchLoopCondition.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else{
                        if (producersQueue.isEmpty()){
                            System.out.println("onlly prods empty");
                            ConsumeRequest c = consumersQueue.poll();
                            if (c.guard()){
                                c.call();
                                //c.reesult =  Main.myTotallyExternalMethod();
                            } else{
                                try {
                                    dispatchLoopCondition.await();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else{
                            System.out.println("prods not empty");
                            ProduceRequest p = producersQueue.poll();
                            if (p.guard()){
                                p.call();
                                //p.reesult =  Main.myVoidTotallyExternalMethod();
                            } else{
                                System.out.println("Prods not empty but guard");
                                if (consumersQueue.isEmpty()){
                                    System.out.println("Prods guard, cons empty");
                                    try {
                                        dispatchLoopCondition.await();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else{
                                    System.out.println("Just take consumer, must be");
                                    ConsumeRequest consumeRequest = consumersQueue.poll();
                                    consumeRequest.call();
                                    //consumeRequest.reesult = Main.myTotallyExternalMethod();

                                }
                            }
                        }
                    }

                    System.out.println("Finished choosing request");
                }

                /*while(true) {

                    // here implement getting elements of queue
                    //request.execute();
                }*/
            }

        });
        thread.setDaemon(true);
        thread.start();
    }



}
