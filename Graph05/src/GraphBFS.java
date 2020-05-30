import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

public class GraphBFS {
    private Graph G;
    private boolean[] visited;

    private ArrayList<Integer> order = new ArrayList<>();

    public GraphBFS(Graph G) {
        this.G = G;
        visited = new boolean[G.V()];

        for(int v=0; v<G.V(); v ++) {
            if(!visited[v]) {
                bfs(v);
            }
        }
    }

    // 从某个顶点出发，进行广度优先遍历
    private void bfs(int s) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(s);
        visited[s] = true;
        while(!queue.isEmpty()) {
            int v = queue.remove();
            order.add(v);

            for(int w : G.adj(v)) {
                if(!visited[w]) {
                    queue.add(w);
                    visited[w] = true;
                }
            }
        }
    }

    public Iterable<Integer> order() {
        return order;
    }

    public static void main(String[] args) {
        Graph g= new Graph("g.txt");
        GraphBFS graphBFS = new GraphBFS(g);
        System.out.println("BFS Order: " + graphBFS.order());
    }
}
