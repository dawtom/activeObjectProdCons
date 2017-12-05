package Request;

import java.util.concurrent.Future;

public class ProduceRequest implements IRequest {
    private int howMany;

    public ProduceRequest(int howMany) {
        this.howMany = howMany;
    }
    @Override
    public boolean guard() {
        return false;
    }

    @Override
    public void call() {

    }
}
