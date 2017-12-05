package Request;

import java.util.concurrent.Future;

public class ConsumeRequest implements IRequest {
    private int howMany;

    public ConsumeRequest(int howMany) {
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
