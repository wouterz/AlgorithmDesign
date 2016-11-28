import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Created by woute on 11/19/2016.
 */
public class Opdr1A {
    class Node {
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

    public String solve2(InputStream in) {
            String result = "no";
            Scanner sc = new Scanner(in);
            int amountVert = sc.nextInt();
        /*int amountEdge =*/
            sc.nextInt();
            int start = sc.nextInt();
            int exit = sc.nextInt();

            if (start == exit) {
                sc.close();
                return "no";
            }

            ArrayList<Node> graph = new ArrayList<Node>();
            for (int v = 0; v < amountVert + 1; v++) {
                Node node = new Node(v);
                graph.add(node);
            }
            while (sc.hasNext()) {
                int from = sc.nextInt();
                int to = sc.nextInt();
                int cost = sc.nextInt();
                Node fromNode = graph.get(from);
                fromNode.neighbours.add(to);
//                fromNode.costs.add(cost);
            }

            Queue<Node> queue = new LinkedList<Node>();

            queue.add(graph.get(start));
            while (!queue.isEmpty()) {
                Node min = queue.remove();
                for (int n = 0; n < min.neighbours.size(); n++) {
                    Node neighbourNode = graph.get(min.neighbours.get(n));
                    if (neighbourNode.vertex == exit) {
                        sc.close();
                        return "yes";
                    }
                    if (!neighbourNode.visited){
                        neighbourNode.visited = true;
                        queue.add(neighbourNode);
                    }
                }
            }
            sc.close();
            return result;
        }

        // Implement the solve method to return the answer to the problem posed by the inputstream.
            public String solve(InputStream in) {
                String result = "no";
                Scanner sc = new Scanner(in);
                int amountVert;
                //int amountEdge;
                int start;
                int exit;
                int[][] graph;

                amountVert = sc.nextInt();

                if (amountVert == 0) {
                    sc.close();
                    return "no";
                }
        /*amountEdge = */sc.nextInt();
                start = sc.nextInt();
                exit = sc.nextInt();
                graph = new int[amountVert + 1][amountVert + 1];
                while (sc.hasNext()) {
                    graph[sc.nextInt()][sc.nextInt()] = sc.nextInt();
                }
                Queue<Integer> queue = new LinkedList<Integer>();
                if (start == exit) {
                    sc.close();
                    return "yes";
                }
                queue.add(start);
                while (queue.size() > 0) {
                    for (int i = 1; i <= amountVert; i++) {
                        if (graph[queue.peek()][i] != 0) {
                            if (i == exit) {
                                sc.close();
                                return "yes";
                            }
                            graph[queue.peek()][i] = 0;
                            queue.add(i);
                        }
                    }
                    queue.remove();
                }
                sc.close();
                return result;
            }

//

    }

