package main;


import Proxy.Proxy;
import Threads.ConsumerThread;
import Threads.ProducerThread;
import main.FTest;

import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static List<FTest> staticListInMain = new LinkedList<>();
    public static AtomicInteger alreadyProduced = new AtomicInteger(0);
    public static AtomicInteger alreadyOrdered = new AtomicInteger(0);
    public static AtomicInteger operationsNumber = new AtomicInteger(0);
    public static boolean threadsAreRunning = true;
    public static final int bufferDelayInMilliseconds = 0;
    public static final int generalThreadsNumber = 100;

    public static void main(String[] args) {
        int prodsNumber = Main.generalThreadsNumber;
        int consNumber = Main.generalThreadsNumber;


        Proxy proxy = new Proxy();

        List<ProducerThread> producers = new ArrayList<>();
        List<ConsumerThread> consumers = new ArrayList<>();

        for (int i = 0; i < prodsNumber; i++) {
            producers.add(new ProducerThread("P" + (i+1), proxy));
        }


        for (int i = 0; i < consNumber; i++) {
            consumers.add(new ConsumerThread("C" + (i+1), proxy));
        }

        //assume that prodsNumber == consNumber
        for (int i = 0; i < prodsNumber; i++) {
            producers.get(i).start();
            consumers.get(i).start();
        }
        /*for (ProducerThread p:
                producers) {
            p.start();
        }
        for (ConsumerThread c :
                consumers) {
            c.start();
        }*/

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*for (int i = 0; i < prodsNumber; i++) {
            producers.get(i).stop();
            consumers.get(i).stop();
        }*/

        Main.threadsAreRunning = false;

        System.out.println("Produced after 10 seconds:  " + alreadyProduced + " elements");
        System.out.println("Ordered to produce after 10 seconds:  " + alreadyOrdered + " elements");
        System.out.println("Operations number: " + operationsNumber);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*ExecutorService es = Executors.newFixedThreadPool(1);
        List<Integer> myList = new ArrayList<Integer>() {{add(1);add(2);add(3);}};

        Proxy proxy = new Proxy();

        Future<List<Integer>> consumerResult = proxy.consume(2);
        Future<Integer> producerResult = proxy.produce(myList);



        while (!producerResult.isDone() || !consumerResult.isDone()){
            System.out.println("Is producer done? " + producerResult.isDone());
            System.out.println("Is consumer done? " + consumerResult.isDone());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            System.out.println("Producer result is " + producerResult.get());
            System.out.println("Consumer result is " + consumerResult.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
*/
//*********************************************************************

        /*try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println("In main ftest " + Main.staticListInMain.get(0));
//        Main.staticListInMain.get(0).reesult = Main.myTotallyExternalMethod();


        while(!producerResult.isDone()){
            System.out.println("Producer waiting for done");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Integer temp = -5555;



        try {
            temp = producerResult.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("Producer has received result " + temp);*/
    }


    /*public static Callable<Integer> subTask = () -> {
        try {
            TimeUnit.SECONDS.sleep(3);

            return 234234;
        }
        catch (InterruptedException e) {
            throw new IllegalStateException("task interrupted", e);
        }
    };

    public static Callable<Void> voidSubTask = () -> {
        try {
            TimeUnit.SECONDS.sleep(3);

            return null;
        }
        catch (InterruptedException e) {
            throw new IllegalStateException("task interrupted", e);
        }
    };*/

   /* public static Future<Integer> myTotallyExternalMethod(){
        ExecutorService es = Executors.newFixedThreadPool(1);

        return es.submit(subTask);
    }

    public static Future<Void> myVoidTotallyExternalMethod(){
        ExecutorService es = Executors.newFixedThreadPool(1);

        return es.submit(voidSubTask);
    }*/


}
