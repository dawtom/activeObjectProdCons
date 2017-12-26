package Request;

import Buffer.Buffer;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.*;

public class ProduceRequest implements IRequest {
    private int howMany;
    public CompletableFuture<Integer> reesult;
    public Buffer buffer;
    List<Integer> input;

    public ProduceRequest(int howMany, Buffer buffer, List<Integer> input) {
        this.howMany = howMany;
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
        this.buffer.produce(this.input);
        this.reesult.complete(5);

    }

}
