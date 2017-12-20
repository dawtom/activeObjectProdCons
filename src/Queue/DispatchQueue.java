package Queue;

import Request.ConsumeRequest;
import Request.IRequest;
import Request.ProduceRequest;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DispatchQueue {

    private ConcurrentLinkedQueue<ProduceRequest> producersQueue;
    private ConcurrentLinkedQueue<ConsumeRequest> consumersQueue;
    ReentrantLock lock;
    Condition dispatchLoopCondition;

    public DispatchQueue() {
        this.producersQueue = new ConcurrentLinkedQueue<>();
        this.consumersQueue = new ConcurrentLinkedQueue<>();
        this.lock = new ReentrantLock();
        this.dispatchLoopCondition = lock.newCondition();
    }
    //przeciążyć zadanie wykonywane przez schedulera, egzekutor musi się namęczyć (sleep, obliczenie)
    //zrobić dłuższe działanie producenta lub konsumenta

    // producent niech jednak czeka na wynik, jeśli producenta przeciążymy w monitorze to odpadnie
    // bo siedzi w monitorze wykonując ciężkie zadanie i nie ma czasu na nic innego
    // a u nas możemy sobie  ładnie liczyć w międzyczasie

    // najważniejsza jest przy mierzeniu właściwa konstrukcja producenta i konsumenta (dodanie
    // dodatkowego zadania)

    // ogarnąć i rozumieć elementy współbieżności w Erlangu

    public Future enqueue(IRequest request){

        Future tmp = null;
        if (request instanceof ConsumeRequest){
            consumersQueue.offer((ConsumeRequest) request);
            tmp = ((ConsumeRequest) request).reesult;

        }
        if (request instanceof ProduceRequest){
            producersQueue.offer((ProduceRequest) request);
            tmp = ((ProduceRequest) request).reesult;
        }
        int x = 0;

        lock.lock();
        dispatchLoopCondition.signal();
        lock.unlock();

        return tmp;
    }

    public IRequest dequeue(){
        IRequest result = getNextOrNull();
        while (result == null) {
            myAwait();
            result = getNextOrNull();
        }
        return result;
    }

    public IRequest getNextOrNull() {
        //lock.lock();
        IRequest result = null;
        System.out.println("In dispatch loop");
        if (producersQueue.isEmpty() && consumersQueue.isEmpty()){
            System.out.println("both empty");
            //myAwait();
        } else{
            if (producersQueue.isEmpty()){
                System.out.println("onlly prods empty");
                ConsumeRequest c = consumersQueue.poll();
                if (c.guard()){
                    result = c;
                    int x = 0;
                    //c.call();
                    //c.reesult =  Main.myTotallyExternalMethod();
                } else{
                    //myAwait();
                }
            } else{
                System.out.println("prods not empty");
                ProduceRequest p = producersQueue.poll();
                if (p.guard()){
                    result = p;
                    //p.call();
                    //p.reesult =  Main.myVoidTotallyExternalMethod();
                } else{
                    System.out.println("Prods not empty but guard");
                    if (consumersQueue.isEmpty()){
                        System.out.println("Prods guard, cons empty");
                        //myAwait();
                    } else{
                        System.out.println("Just take consumer, must be");
                        ConsumeRequest consumeRequest = consumersQueue.poll();
                        //consumeRequest.call();
                        result = consumeRequest;
                        //consumeRequest.reesult = Main.myTotallyExternalMethod();

                    }
                }
            }
        }
        //lock.unlock();
        return result;
    }

    public void myAwait(){
        lock.lock();
        try {
            dispatchLoopCondition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.unlock();
    }
}
