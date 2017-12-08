package main;


import Proxy.Proxy;
import main.FTest;

import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import java.util.concurrent.*;

public class Main {
    public static List<FTest> staticListInMain = new LinkedList<>();
    public static void main(String[] args) {


        ExecutorService es = Executors.newFixedThreadPool(1);

        List<Integer> myList = new ArrayList<Integer>() {{add(1);add(2);add(3);}};

        Proxy proxy = new Proxy(myList.size());
        proxy.setInput(myList);

        Future<Integer> resulcik = es.submit(proxy.produce);



        /*try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println("In main ftest " + Main.staticListInMain.get(0));
//        Main.staticListInMain.get(0).reesult = Main.myTotallyExternalMethod();


        while(!resulcik.isDone()){
            System.out.println("Producer waiting for done");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Integer temp = -5555;



        try {
            temp = resulcik.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("Producer has received result " + temp);*/
    }


    public static Callable<Integer> subTask = () -> {
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
    };

    public static Future<Integer> myTotallyExternalMethod(){
        ExecutorService es = Executors.newFixedThreadPool(1);

        return es.submit(subTask);
    }

    public static Future<Void> myVoidTotallyExternalMethod(){
        ExecutorService es = Executors.newFixedThreadPool(1);

        return es.submit(voidSubTask);
    }

}
