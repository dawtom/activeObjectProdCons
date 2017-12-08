package Request;

import Buffer.Buffer;

import java.util.List;
import java.util.concurrent.*;

public class ConsumeRequest implements IRequest {
    private int howMany;
    public Future<List<Integer>> reesult;
    public Buffer buffer;

    public ConsumeRequest(int howMany, Buffer buffer) {
        this.howMany = howMany;
        // FIXME czy to poniżej będzie ok?
        this.reesult = new CompletableFuture<>();
        this.buffer = buffer;
    }

    @Override
    public boolean guard() {
        return (this.buffer.getCount() >= this.howMany);
    }

    @Override
    public void call() {
        ExecutorService es = Executors.newFixedThreadPool(1);

        this.reesult = es.submit(this.subCallConsume);

    }

    private Callable<List<Integer>> subCallConsume = () -> this.buffer.consume(this.howMany);

}
