import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Created by woute on 11/18/2016.
 */
public class Opdr1B {

    public static void main(String[] args) throws FileNotFoundException {
        String str = "7 7 1 5\n" +
                "1 2 2\n" +
                "2 3 100\n" +
                "3 4 10\n" +
                "4 5 10\n" +
                "2 6 10\n" +
                "6 7 10\n" +
                "7 4 80";
//        InputStream stream = new FileInputStream("1C-test.txt");
      InputStream stream= new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
        System.out.println(solve3(stream));
    }

    public static String solve3(InputStream in) {
        Scanner sc = new Scanner(in);
        int amountVert = sc.nextInt();
        //int amountEdge =
        sc.nextInt();
        int start = sc.nextInt();
        int exit = sc.nextInt();

        if (start == exit) {
            sc.close();
            return "0";
        }

        TreeMap<Integer, Node> graph = new TreeMap<Integer, Node>();
        TreeMap<Integer, Node> tree = new TreeMap<Integer, Node>();
         Comparator<Node> minDist = new Comparator<Node>() {
             @Override
             public int compare(Node d1, Node d2) {
                 return (d1.distance - d2.distance);
             }
         };
        PriorityQueue<Node> pqueue = new PriorityQueue<Node>(amountVert, minDist);

        for (int v = 1; v < amountVert + 1; v++) {
            Node node = new Node(v);
            graph.put(v, node);
        }
        while (sc.hasNext()) {
            int from = sc.nextInt();
            int to = sc.nextInt();
            int cost = sc.nextInt();
            Node fromNode = graph.get(from);
            fromNode.neighbours.add(to);
            fromNode.costs.add(cost);
        }

        graph.get(start).distance = graph.get(start).neighbours.size();
//        tree.put(graph.get(start).distance, graph.get(start));
        pqueue.offer(graph.get(start));
//        while (!tree.isEmpty()) {
//            Map.Entry<Integer, Node> entry = tree.firstEntry();
//            tree.remove(entry.getKey());
//            Node min = entry.getValue();
        while (!pqueue.isEmpty()) {
            Node min = pqueue.remove();
            if (min.vertex == exit) {
                sc.close();
                return String.valueOf(min.distance);
            }
            for (int n = 0; n < min.neighbours.size(); n++) {
                Node neighbourNode = graph.get(min.neighbours.get(n));
                int newDistance = min.distance + min.costs.get(n) + neighbourNode.neighbours.size();
                if ((!neighbourNode.visited) && (newDistance < neighbourNode.distance)) {
                    neighbourNode.visited = true;
                    neighbourNode.distance = newDistance;
//                    tree.put(newDistance, neighbourNode);
                    pqueue.offer(neighbourNode);
                }
            }
        }

        sc.close();
        return "-1";
    }

    static class Node {
        public boolean visited;
        public int distance;
        public int vertex;
        public ArrayList<Integer> neighbours;
        public ArrayList<Integer> costs;

        public Node(int vert1) {
            vertex = vert1;
            distance = Integer.MAX_VALUE;
            neighbours = new ArrayList<Integer>();
            costs = new ArrayList<Integer>();
        }
    }

    public static String solve2(InputStream in) {
        Scanner sc = new Scanner(in);
        int amountVert = sc.nextInt();
        //int amountEdge =
        sc.nextInt();
        int start = sc.nextInt();
        int exit = sc.nextInt();

        if (start == exit) {
            sc.close();
            return "0";
        }

        TreeMap<Integer, Node> graph = new TreeMap<Integer, Node>();
        TreeMap<Integer, Node> tree = new TreeMap<Integer, Node>();


        for (int v = 1; v < amountVert + 1; v++) {
            Node node = new Node(v);
            graph.put(v, node);
        }
        while (sc.hasNext()) {
            int from = sc.nextInt();
            int to = sc.nextInt();
            int cost = sc.nextInt();
            Node fromNode = graph.get(from);
            fromNode.neighbours.add(to);
            fromNode.costs.add(cost);
            tree.put(fromNode.distance, fromNode);
        }

        graph.get(start).distance = graph.get(start).neighbours.size();
        tree.put(graph.get(start).distance, graph.get(start));
        while (!tree.isEmpty()) {
            Map.Entry<Integer, Node> entry = tree.firstEntry();
            tree.remove(entry.getKey());
            Node min = entry.getValue();
            if (min.vertex == exit) {
                sc.close();
                return String.valueOf(min.distance);
            }
            for (int n = 0; n < min.neighbours.size(); n++) {
                Node neighbourNode = graph.get(min.neighbours.get(n));
                int newDistance = min.distance + min.costs.get(n) + neighbourNode.neighbours.size();
                if ((!neighbourNode.visited) && (newDistance < neighbourNode.distance)) {
                    neighbourNode.visited = true;
                    neighbourNode.distance = newDistance;
                    tree.put(newDistance, neighbourNode);
                }
            }
        }

        sc.close();
        return "-1";
    }

    public static String solve(InputStream in) {
        Scanner sc = new Scanner(in);
        int amountVert = sc.nextInt();
        /*int amountEdge =*/ sc.nextInt();
        int start = sc.nextInt();
        int exit = sc.nextInt();
        int[][] graph = new int[amountVert + 1][amountVert + 1];
        int[] distance = new int[amountVert + 1];
        // Comparator<Integer> minDist = new Comparator<Integer>() {
        //     @Override
        //     public int compare(Integer d1, Integer d2) {
        //         return (distance[d1] - distance[d2]);
        //     }
        // };
        // PriorityQueue<Integer> queue = new PriorityQueue<Integer>(amountVert + 1, minDist);
        LinkedList<Integer> queue = new LinkedList<Integer>();

        while (sc.hasNext()) {
            graph[sc.nextInt()][sc.nextInt()] = sc.nextInt();
        }
        for(int v = 1; v < amountVert + 1; v++) {
            distance[v] = Integer.MAX_VALUE;
            queue.offer(v);
        }
        distance[start] = 0;

        // queue.offer(start);

        if (start == exit) {
            sc.close();
            return "0";
        }

        System.out.println("hoi");
        while (queue.size() > 0) {

            int min = queue.peek();
            for(int v : queue) {
                if (distance[v] < distance[min])
                    min = v;
            }
            min = queue.remove(queue.indexOf(min));
            // System.out.println(Arrays.toString(distance));
            for (int i = 1; i < amountVert + 1; i++) {
                if (graph[min][i] != 0) {
                    int newDistance = graph[min][i] + distance[min] + 1;
                    if (newDistance > distance[i])
                        break;
                    distance[i] = distance[min] + graph[min][i] + 1;
                    if (i == exit && queue.size() <= 1) {
                        sc.close();
                        return String.valueOf(distance[exit] + 1);
                    }
                    // queue.add(i);
                }
            }
        }
        if (distance[exit] < Integer.MAX_VALUE) {
            sc.close();
            return String.valueOf(distance[exit] + 1);
        }
        sc.close();
        return String.valueOf(-1);
    }
}
