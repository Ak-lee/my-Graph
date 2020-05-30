import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Prim {

    private WeightedGraph G;
    private ArrayList<WeightedEdge> mst;

    public Prim(WeightedGraph G) {
        this.G= G;
        mst = new ArrayList<>();

        CC cc = new CC(G);
        if(cc.count() > 1) return;

        // Prime 算法
        // 这里使用 visited 来表示切分。visited 为true或false就对应了1个切分
        boolean[] visited = new boolean[G.V()];
        visited[0] = true;

        // 声明1个优先队列。java中的优先队列默认就是1个最小堆
        // pq 中的边不1定是合法的横切边
        Queue pq = new PriorityQueue<WeightedEdge>();
        for(int w : G.adj(0)) {
            pq.add(new WeightedEdge(0, w, G.getWeight(0, w)));
        }
        while(!pq.isEmpty()) {
            WeightedEdge minEdge = (WeightedEdge) pq.remove();
            if(visited[minEdge.getV()] && visited[minEdge.getW()])
                continue;
            mst.add(minEdge);
            int newv= visited[minEdge.getV()] ? minEdge.getW() : minEdge.getV();
            visited[newv] = true;
            for(int w : G.adj(newv)) {
                if(!visited[w]) {
                    pq.add(new WeightedEdge(newv, w, G.getWeight(newv, w)));
                }
            }
        }
    }

    public ArrayList<WeightedEdge> result() {
        return mst;
    }

    public static void main(String[] args) {
        WeightedGraph g = new WeightedGraph("g.txt");
        Prim prim = new Prim(g);
        System.out.println(prim.result());
    }
}

