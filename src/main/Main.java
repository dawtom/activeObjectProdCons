package main;


import Proxy.Proxy;
import Threads.ConsumerThread;
import Threads.ProducerThread;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static AtomicInteger alreadyProduced = new AtomicInteger(0);
    public static AtomicInteger alreadyOrdered = new AtomicInteger(0);
    public static AtomicInteger operationsNumber = new AtomicInteger(0);
    public static boolean threadsAreRunning = true;
    public static final int bufferDelayInMilliseconds = 500;
    public static final int generalThreadsNumber = 200;

    public static void main(String[] args) {
        int prodsNumber = Main.generalThreadsNumber;
        int consNumber = Main.generalThreadsNumber;


        Proxy proxy = new Proxy();

        List<ProducerThread> producers = new ArrayList<>();
        List<ConsumerThread> consumers = new ArrayList<>();

        for (int i = 0; i < prodsNumber; i++) {
            producers.add(new ProducerThread("P" + (i + 1), proxy));
        }


        for (int i = 0; i < consNumber; i++) {
            consumers.add(new ConsumerThread("C" + (i + 1), proxy));
        }

        //assume that prodsNumber == consNumber
        for (int i = 0; i < prodsNumber; i++) {
            producers.get(i).start();
            consumers.get(i).start();
        }


        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Main.threadsAreRunning = false;

        System.out.println("Produced after 10 seconds:  " + alreadyProduced + " elements");
        System.out.println("Ordered to produce after 10 seconds:  " + alreadyOrdered + " elements");
        System.out.println("Operations number: " + operationsNumber);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
