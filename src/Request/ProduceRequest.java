package Request;

import Buffer.Buffer;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.*;

public class ProduceRequest implements IRequest {
    private int howMany;
    public Future<Integer> reesult;
    public Buffer buffer;
    List<Integer> input;

    public ProduceRequest(int howMany, Buffer buffer, List<Integer> input) {
        this.howMany = howMany;
        // FIXME czy to poniżej będzie ok?
        this.reesult = new CompletableFuture<>();
        this.buffer = buffer;
        this.input = input;
    }

    @Override
    public boolean guard() {
        return (this.buffer.getCount() + this.howMany <= this.buffer.getCapacity());
    }

    @Override
    public void call() {

        ExecutorService es = Executors.newFixedThreadPool(1);

        this.reesult = es.submit(this.subCallProduce);
        //this.buffer.produce(this.input);

    }
    private Callable<Integer> subCallProduce = () -> {
        this.buffer.produce(this.input);
        return 0;
    };
}
