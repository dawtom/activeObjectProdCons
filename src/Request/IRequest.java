package Request;

import java.util.concurrent.Future;

public interface IRequest {
    public Future<Integer> reesult = null;
    public boolean guard();
    public void call();
}
