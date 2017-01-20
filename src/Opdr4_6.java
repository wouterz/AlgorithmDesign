import java.io.*;
import java.util.*;

/**
 * Created by woute on 1/20/2017.
 */
public class Opdr4_6 {

  public static void main(String[] args) {
    String sample = "3\n" +
        "1 2 10\n" +
        "1 2 1\n" +
        "1 2 10\n" +
        "4\n" +
        "3 2 1\n" +
        "2 3 2\n" +
        "1 3 1\n" +
        "1 2 0\n" +
        "1";
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

    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    String line = reader.readLine();
    String[] tokens = line.split(" ");

    Node source = new Node("source");
    Node sink = new Node("sink");

    int functions = Integer.parseInt(tokens[0]);
    ArrayList<Node> nodes = new ArrayList<Node>(functions);
    for (int i = 0; i < functions; i++) {
      line = reader.readLine();
      // [0] = compiled time, [1] = not compiled time, [2] = compile time
      tokens = line.split(" ");
      Node function = new Node(Integer.toString(i+1));
      function.compiled = Integer.parseInt(tokens[0]);
      function.notCompiled = Integer.parseInt(tokens[1]);
      function.compile = Integer.parseInt(tokens[2]);
      if (function.getLabel().equals("1")) {
        source.addEdge(function, function.compile + function.compiled);
        function.addEdge(sink, function.notCompiled);
      }
      else {
        source.addEdge(function, function.compile);
        function.addEdge(sink, 0);
      }
      nodes.add(function);

    }

    line = reader.readLine();
    int methodcalls = Integer.parseInt(line);
    ArrayList<Edge> overheadEdges = new ArrayList<Edge>();
    for (int i = 0; i < methodcalls; i++) {
      line = reader.readLine();
      tokens = line.split(" ");
      int from = Integer.parseInt(tokens[0]);
      int to = Integer.parseInt(tokens[1]);
      int calls = Integer.parseInt(tokens[2]);
      if (calls > 0) {

        source.addEdge(nodes.get(to-1), calls*nodes.get(to-1).compiled);
        nodes.get(to-1).addEdge(sink, calls*nodes.get(to-1).notCompiled);

        overheadEdges.add(new Edge(calls, nodes.get(from-1), nodes.get(to-1)));
      }
    }

    //multiply extra overhead edges by overhead cost
    line = reader.readLine();
    int overhead = Integer.parseInt(line);
    for (Edge e : overheadEdges) {
      e.from.addEdge(e.to, e.capacity*overhead);
    }

    //max flow = min cut = min runtime
    Graph graph = new Graph(nodes, source, sink);
    MaxFlow.maximizeFlow(graph);
    int runtime = 0;
    for (Edge e : source.edges) {
      int f = e.flow;
      if (f > 0) {
        runtime += f;
      }
    }
    return Integer.toString(runtime);
  }

  public static String run (InputStream in) throws IOException{
    return new Opdr4_6().solve(in);
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
  protected ArrayList<Edge> edges;
  public Integer compiled;
  public Integer notCompiled;
  public Integer compile;

  public Node(String label) {
    this.label = label;
    this.edges = new ArrayList<Edge>();
  }

  public void addEdge(Node to, int capacity) {
    Edge e = new Edge(capacity, this, to);
    edges.add(e);
    to.getEdges().add(e.getBackwards());
  }

  public ArrayList<Edge> getEdges() {
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
}

