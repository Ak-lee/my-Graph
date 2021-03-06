import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.Scanner;

// 支持无向带权图
public class WeightedGraph implements Cloneable{
    private int V;  // 所表示的图有多少个节点
    private int E;  // 所表示的图有多少条边
    private TreeMap<Integer, Integer>[] adj;    // 第一个 Integer 代表节点序号，第二个Integer为权值

    public WeightedGraph(String filename) {
        File file = new File(filename);
        try(Scanner scanner = new Scanner(file)) {
            V = scanner.nextInt();
            if(V < 0) {
                throw new IllegalArgumentException("V must be non-negative");
            }
            adj = new TreeMap[V];
            for(int i=0; i<V; i++) {
                adj[i] = new TreeMap<Integer, Integer>();
            }
            E = scanner.nextInt();
            if(E < 0) {
                throw new IllegalArgumentException("E must be non-negative");
            }
            for(int i=0; i<E; i++) {
                int a = scanner.nextInt();
                validateVertex(a);
                int b = scanner.nextInt();
                validateVertex(b);
                int weight = scanner.nextInt();

                if(a == b) {    // 判断是否出现子环边
                    throw new IllegalArgumentException("Self Loop is Detected!");
                }
                if(adj[a].containsKey(b)) {    // 处理出现平行边的情况
                    throw new IllegalArgumentException("Parallel Edges are Detected!");
                }
                adj[a].put(b, weight);
                adj[b].put(a, weight);
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void validateVertex(int v) {    // 判断传入的顶点序号是否合法
        if(v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + "is invalid");
        }
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public boolean hasEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        return adj[v].containsKey(w);
    }

    public Iterable<Integer> adj(int v) {
        validateVertex(v);
        return adj[v].keySet();
    }

    public int getWeight(int v, int w) {
        if(hasEdge(v, w)) {
            return adj[v].get(w);
        }
        throw new IllegalArgumentException(String.format("No edge %d-%d", v, w));
    }

    public int degree(int v) {  // 求一个顶点的度
        validateVertex(v);
        return adj[v].size();
    }

    public void removeEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        adj[v].remove(w);
        adj[w].remove(v);
    }

    @Override
    public Object clone() {

        try {
            WeightedGraph cloned = (WeightedGraph) super.clone();
            cloned.adj = new TreeMap[V];
            for(int v=0; v < V; v ++) {
                cloned.adj[v] = new TreeMap<>();
                for(Map.Entry<Integer, Integer> entry : adj[v].entrySet()) {
                    cloned.adj[v].put(entry.getKey(), entry.getValue());
                }
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("V = %d, E = %d\n", V, E));
        for(int v=0; v<V; v ++) {
            sb.append(String.format("%d: ", v));
            for(Map.Entry<Integer, Integer> entry : adj[v].entrySet()) {
                sb.append(String.format("(%d: %d)", entry.getKey(), entry.getValue()));
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        WeightedGraph g= new WeightedGraph("g.txt");
        System.out.println(g);
    }
}

