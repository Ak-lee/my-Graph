import java.util.ArrayList;
import java.util.Collections;

public class TopoSort2 {
    private Graph G;

    private ArrayList<Integer> res;
    private boolean hasCycle = false;

    public TopoSort2(Graph G) {
        if(!G.isDirected()) {
            // 拓扑排序只适用于有向图
            throw new IllegalArgumentException("TopoSort only works in directed graph.");
        }
        this.G = G;

        res = new ArrayList<>();
        hasCycle = (new DirectedCycleDetection(G)).hasCycle();
        if(hasCycle) return;

        GraphDFS dfs = new GraphDFS(G);
        for(int v : dfs.post()) {
            res.add(v);
        }
        Collections.reverse(res);
    }

    public boolean hasCycle() {
        return hasCycle;
    }

    public ArrayList<Integer> result() {
        return res;
    }

    public static void main(String[] args) {
        Graph g = new Graph("ug.txt", true);
        TopoSort2 topoSort = new TopoSort2(g);
        System.out.println(topoSort.result());
    }
}
