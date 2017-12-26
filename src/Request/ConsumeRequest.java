package Request;

import Buffer.Buffer;

import java.util.List;
import java.util.concurrent.*;

public class ConsumeRequest implements IRequest {
    private int howMany;
    public CompletableFuture<List<Integer>> reesult;
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

        this.reesult.complete(this.buffer.consume(this.howMany));


    }


}
