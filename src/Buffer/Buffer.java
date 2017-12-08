package Buffer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
        System.out.println(this.toString());

    }

    public List<Integer> consume(int howMany){
        List<Integer> result = new LinkedList<>();
        for (int i = 0; i < howMany; i++) {
            result.add(actualBuffer.get(0));
        }
        return result;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("BUFFER: ");
        builder.append(actualBuffer.toString());
        builder.append("\nCount: ");
        builder.append(this.count);
        builder.append(" :: capacity: ");
        builder.append(this.capacity);

        return builder.toString();
    }
}
