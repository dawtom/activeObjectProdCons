package main;


import Proxy.Proxy;
import main.FTest;

import java.sql.Time;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import java.util.concurrent.*;

public class Main {
    public static List<FTest> staticListInMain = new LinkedList<>();
    public static void main(String[] args) {

        Proxy proxy = new Proxy(10);

        ExecutorService es = Executors.newFixedThreadPool(1);
        Future<Integer> resulcik = es.submit(proxy.produce);

        try {
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

        Integer temp = new Integer(-555);



        try {
            temp = resulcik.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("Producer has received result " + temp);
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

    public static Future<Integer> myTotallyExternalMethod(){
        ExecutorService es = Executors.newFixedThreadPool(1);

        return es.submit(subTask);
    }

}
