import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Created by woute on 11/28/2016.
 */
public class Opdr2A {

    public static void main(String[] args) {
        String test = "2\n" +
                "7 6\n" +
                "5 9";
        InputStream stream = new ByteArrayInputStream(test.getBytes(StandardCharsets.UTF_8));
        System.out.println(run(stream));
    }

    static class Cake{
        int preprocess, paralell;

        public Cake(int pre, int par){
            preprocess = pre;
            paralell = par;
        }
    }

    public static String run(InputStream in) {
        Scanner sc = new Scanner(in);
        int endTime = 0;
        int jobs = sc.nextInt();
        if (jobs == 0)
            return Integer.toString(endTime);

        Comparator<Cake> comparator = new Comparator<Cake>() {
            @Override
            public int compare(Cake p1, Cake p2) {
                if (p1.paralell == p2.paralell) {
                    return p2.preprocess - p1.preprocess;
                }
                return p2.paralell - p1.paralell;
            }
        };
        PriorityQueue<Cake> pqueue = new PriorityQueue<Cake>(jobs, comparator);
        while (sc.hasNextLine()) {
            int preprocessing = sc.nextInt();
            int parallel = sc.nextInt();
            pqueue.add(new Cake(preprocessing, parallel));
        }

        Cake nextCake = pqueue.remove();
        endTime += nextCake.preprocess;
        int paralelltime = nextCake.paralell;

        while (!pqueue.isEmpty()) {
            nextCake = pqueue.remove();
            endTime += nextCake.preprocess;
            paralelltime -= nextCake.preprocess;
            if (nextCake.paralell > paralelltime) {
                paralelltime = nextCake.paralell;
            }
        }

        if (paralelltime > 0) {
            endTime += paralelltime;
        }

        return Integer.toString(endTime);
    }
}
