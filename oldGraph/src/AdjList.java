import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class AdjList {
    private int V;  // 所表示的图有多少个节点
    private int E;  // 所表示的图有多少条边
    private LinkedList<Integer>[] adj;

    public AdjList(String filename) {
        File file = new File(filename);
        try(Scanner scanner = new Scanner(file)) {
            V = scanner.nextInt();
            if(V < 0) {
                throw new IllegalArgumentException("V must be non-negative");
            }
            adj = new LinkedList[V];
            for(int i=0; i<V; i++) {
                adj[i] = new LinkedList<>();
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

                if(a == b) {    // 判断是否出现子环边
                    throw new IllegalArgumentException("Self Loop is Detected!");
                }
                if(adj[a].contains(b)) {    // 处理出现平行边的情况
                    throw new IllegalArgumentException("Parallel Edges are Detected!");
                }
                adj[a].add(b);
                adj[b].add(a);
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void validateVertex(int v) {    // 判断传入的顶点序号是否合法
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
        return adj[v].contains(w);
    }

    public Iterable<Integer> adj(int v) {  // 返回和顶点ｖ相邻的顶点的数组
        validateVertex(v);
        return adj[v];
    }

    public int degree(int v) {  // 求一个顶点的度
        validateVertex(v);
        return adj[v].size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("V = %d, E = %d\n", V, E));
        for(int v=0; v<V; v ++) {
            sb.append(String.format("%d: ", v));
            for(int w : adj[v]) {
                sb.append(String.format("%d ", w));
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        AdjList adjList = new AdjList("g.txt");
        System.out.println(adjList);
    }
}

