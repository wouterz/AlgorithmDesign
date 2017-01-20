import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.ArrayList;
import java.util.Collection;
/**
 * Created by woute on 1/20/2017.
 */
public class Opdr4_6 {

    public static void main(String[] args) {
        String sample = "4 1.0\n" +
                "1.0 2.0 2.4 3.5\n" +
                "2.0 2.1 2.0 3.2";
        InputStream is = new ByteArrayInputStream(sample.getBytes());
        try {
            System.out.println(new Opdr4_6().solve(is));
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("CATCH IO EXCEPTION");
        }
    }

    public String solve(InputStream in) throws IOException{

        //TODO build graph and run max flow
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = reader.readLine();
        String[] tokens = line.split(" ");
        return "0";
    }

    public static String run (InputStream in) throws IOException{
        return new Opdr4_6().solve(in);
    }
}
}

// The classes below implement a graph structure and the Ford-Fulkerson algorithm.
// You can use these classes in your solution.
class MaxFlow {

    static public List<Edge> findPath(Graph g, Node start, Node end) {

        Map<Node, Edge> mapPath = new HashMap<Node, Edge>();
        Queue<Node> sQueue = new LinkedList<Node>();

        Node currentNode = start;
        sQueue.add(currentNode);

        while (!sQueue.isEmpty() && currentNode != end) {

            currentNode = sQueue.remove();

            for (Edge e : currentNode.getEdges()) {
                Node to = e.getTo();
                if (to != start && mapPath.get(to) == null
                        && e.getResidual() > 0) {
                    sQueue.add(e.getTo());
                    mapPath.put(to, e);
                }
            }
        }

        if (sQueue.isEmpty() && currentNode != end)
            return null;

        LinkedList<Edge> path = new LinkedList<Edge>();
        Node current = end;
        while (mapPath.get(current) != null) {
            Edge e = mapPath.get(current);
            path.addFirst(e);
            current = e.getFrom();
        }

        return path;
    }

    static public void maximizeFlow(Graph g) {

        Node sink = g.getSink();
        Node source = g.getSource();
        List<Edge> path;

        while ((path = findPath(g, source, sink)) != null) {

            int r = Integer.MAX_VALUE;
            for (Edge e : path) {
                r = Math.min(r, e.getResidual());
            }

            for (Edge e : path) {
                e.augmentFlow(r);
            }
        }
    }

}

class Graph {

    protected Collection<Node> nodes;
    protected Node source;
    protected Node sink;

    public Graph(Collection<Node> nodes, Node source, Node sink) {
        this.nodes = nodes;
        this.source = source;
        this.sink = sink;
    }

    public Node getSink() {
        return sink;
    }

    public Node getSource() {
        return source;
    }

}

class Node {

    protected String label;
    protected Collection<Edge> edges;

    public Node(String label) {
        this.label = label;
        this.edges = new ArrayList<Edge>();
    }

    public void addEdge(Node to, int capacity) {
        Edge e = new Edge(capacity, this, to);
        edges.add(e);
        to.getEdges().add(e.getBackwards());
    }

    public Collection<Edge> getEdges() {
        return edges;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}

class Edge {

    protected int capacity;
    protected int flow;
    protected Node from;
    protected Node to;
    protected Edge backwards;

    private Edge(Edge e) {
        this.flow = e.getCapacity();
        this.capacity = e.getCapacity();
        this.from = e.getTo();
        this.to = e.getFrom();
        this.backwards = e;
    }

    protected Edge(int capacity, Node from, Node to) {
        this.capacity = capacity;
        this.from = from;
        this.to = to;
        this.flow = 0;
        this.backwards = new Edge(this);
    }

    public void augmentFlow(int add) {

        assert (flow + add <= capacity);
        flow += add;
        backwards.setFlow(getResidual());

    }

    public Edge getBackwards() {
        return backwards;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getFlow() {
        return flow;
    }

    public Node getFrom() {
        return from;
    }

    public int getResidual() {
        return capacity - flow;
    }

    public Node getTo() {
        return to;
    }

    private void setFlow(int f) {
        assert (f <= capacity);
        this.flow = f;
    }

