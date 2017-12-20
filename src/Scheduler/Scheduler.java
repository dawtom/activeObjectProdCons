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

    //DispatchQueue queue;
    private ConcurrentLinkedQueue<ProduceRequest> producersQueue;
    private ConcurrentLinkedQueue<ConsumeRequest> consumersQueue;
    ReentrantLock lock;
    Condition dispatchLoopCondition;


    public Scheduler() {
        //this.queue = new DispatchQueue();
        this.producersQueue = new ConcurrentLinkedQueue<>();
        this.consumersQueue = new ConcurrentLinkedQueue<>();
        this.lock = new ReentrantLock();
        this.dispatchLoopCondition = lock.newCondition();
        dispatch();

    }



    public Future enqueue(IRequest request){
        //lock.lock();
        // sprawdź jaki Request przychodzi
        // dodaj na koniec odpowiedniej kolejki

        //Future tmp = null;
        if (request instanceof ConsumeRequest){
            consumersQueue.offer((ConsumeRequest) request);
            lock.lock();
            dispatchLoopCondition.signal();
            lock.unlock();
            return ((ConsumeRequest) request).reesult;

        }
        if (request instanceof ProduceRequest){
            producersQueue.offer((ProduceRequest) request);
            lock.lock();
            dispatchLoopCondition.signal();
            lock.unlock();
            return ((ProduceRequest) request).reesult;
        }


        //return tmp;
        return null;
    }

    private void dispatch() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (Main.threadsAreRunning) {
                    lock.lock();
                    if (producersQueue.isEmpty() && consumersQueue.isEmpty()){
//                        System.out.println("both empty");
                        try {
                            dispatchLoopCondition.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else{
                        if (producersQueue.isEmpty()){
//                            System.out.println("onlly prods empty");
                            ConsumeRequest c = consumersQueue.poll();
                            if (c.guard()){
                                //result = c;
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
//                            System.out.println("prods not empty");
                            ProduceRequest p = producersQueue.poll();
                            if (p.guard()){
//                                result = p;
                                p.call();
                                //p.reesult =  Main.myVoidTotallyExternalMethod();
                            } else{
//                                System.out.println("Prods not empty but guard");
                                if (consumersQueue.isEmpty()){
//                                    System.out.println("Prods guard, cons empty");
                                    try {
                                        dispatchLoopCondition.await();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else{
//                                    System.out.println("Just take consumer, must be");
                                    ConsumeRequest consumeRequest = consumersQueue.poll();
                                    consumeRequest.call();
//                                    result = consumeRequest;
                                    //consumeRequest.reesult = Main.myTotallyExternalMethod();

                                }
                            }
                        }
                    }
                    lock.unlock();
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
