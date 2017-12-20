package Buffer;

import main.Main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Buffer {
    private List<Integer> actualBuffer;

    public int getCapacity() {
        return capacity;
    }

    public int getCount() {
        return count;
    }

    private int capacity;
    private int count;


    public Buffer(int capacity) {
        this.actualBuffer = new ArrayList<>();
        this.capacity = capacity;
        this.count = 0;
    }

    public void produce(List<Integer> input) {

        actualBuffer.addAll(input);
        this.count += input.size();
        Main.alreadyProduced.addAndGet(input.size());

        try {
            TimeUnit.MILLISECONDS.sleep(Main.bufferDelayInMilliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println(this.toString());

    }

    public List<Integer> consume(int howMany){
        List<Integer> result = new LinkedList<>();
        for (int i = 0; i < howMany; i++) {
            result.add(actualBuffer.get(0));
            actualBuffer.remove(0);
            this.count--;
        }
        try {
            TimeUnit.MILLISECONDS.sleep(Main.bufferDelayInMilliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //System.out.println(this.toString());
        return result;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("BUFFER: ");
        builder.append(actualBuffer.toString());
        builder.append(" Count: ");
        builder.append(this.count);
        builder.append(" :: capacity: ");
        builder.append(this.capacity);

        return builder.toString();
    }
}
