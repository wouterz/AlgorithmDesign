import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Created by woute on 11/28/2016.
 */
public class Opdr2B {

    public static void main(String[] args) {
        String test = "5 10\n" +
                "2 6\n" +
                "1 2\n" +
                "17 7\n" +
                "3 9\n" +
                "15 6";
        InputStream stream = new ByteArrayInputStream(test.getBytes(StandardCharsets.UTF_8));
        System.out.println(run(stream));
    }

    static class Researcher {
        int arrive, stay;

        public Researcher(int a, int s){
            arrive = a;
            stay = s;
        }
    }
    public static String run(InputStream in) {
        Scanner sc = new Scanner(in);
        if (!sc.hasNextInt()){
            sc.close();
            return Integer.toString(0);
        }
        int researchers = sc.nextInt();
        int inactivityLimit = sc.nextInt();
        if (researchers == 0) {
            sc.close();
            return Integer.toString(0);
        }

        Comparator<Researcher> comparator = new Comparator<Researcher>() {
            @Override
            public int compare(Researcher p1, Researcher p2) {
                if (p1.arrive == p2.arrive) {
                    return p1.stay - p2.stay;
                }
                return p1.arrive - p2.arrive;
            }
        };
        PriorityQueue<Researcher> pqueue = new PriorityQueue<Researcher>(researchers, comparator);

        while (sc.hasNextInt()) {
            int arriveTime = sc.nextInt();
            int stayTime = sc.nextInt();
            pqueue.add(new Researcher(arriveTime, stayTime));
        }

        PriorityQueue<Integer> workstations = new PriorityQueue<Integer>();

        Researcher researcher = pqueue.remove();
        workstations.offer(researcher.arrive + researcher.stay);

        int locked = 0;
        while (!pqueue.isEmpty()) {
            researcher = pqueue.remove();
            if (!workstations.isEmpty()) {
                while (!workstations.isEmpty() && (workstations.peek() + inactivityLimit < researcher.arrive)) {
                    workstations.poll();
                    locked++;
                }
                if (workstations.peek() <= researcher.arrive) {
                    workstations.poll();
                }
            }
            workstations.add(researcher.arrive + researcher.stay);
        }

        return Integer.toString(researchers - (workstations.size() + locked));
    }
}
