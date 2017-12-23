import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class AddThread implements Runnable {
    private List<Double> list;
 
    public AddThread(List<Double> list) {
        this.list = list;
    }
    
    @Override
    public void run() {
        for(int i = 0; i < 10000; ++i) {
        		double t = Math.random();
            list.add(t);
//            System.out.println(t);
            System.out.println(list.size());
        }
    }
}
 
public class TestCopyOnWrite {
    private static final int THREAD_POOL_SIZE = 2;
 
    public static void main(String[] args) {
        List<Double> list = new CopyOnWriteArrayList<>();
        ExecutorService es = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        es.execute(new AddThread(list));
        es.execute(new AddThread(list));
        es.shutdown();
    }
}