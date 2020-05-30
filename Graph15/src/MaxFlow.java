import java.util.*;

// MaxFlow 最大流问题
public class MaxFlow {
    private WeightedGraph network;
    private int s, t;   // s 为网络的源点，t为网络的汇点

    private WeightedGraph rG;   // r- residual rG即残量图
    private int maxFlow = 0;

    public MaxFlow(WeightedGraph network, int s, int t) {
        if(!network.isDirected()) {
            // 若network为无向图，抛出异常
            throw new IllegalArgumentException("MaxFlow only works in directed graph.");
        }
        if(network.V() < 2) {
            throw new IllegalArgumentException("The network should has at least 2 vertices.");
        }
        network.validateVertex(s);
        network.validateVertex(t);
        if(s == t) {
            throw new IllegalArgumentException("s and t should be different.");
        }

        this.network = network;
        this.s = s;
        this.t = t;

        this.rG = new WeightedGraph(network.V(), true);
        for(int v=0; v<network.V(); v ++) {
            for(int w : network.adj(v)) {
                rG.addEdge(v, w, network.getWeight(v, w));
                rG.addEdge(w, v, 0);
            }
        }

        while (true) {
            // augPath 保存某个增广路径都经过哪些顶点。
            ArrayList<Integer> augPath = getAugmentingPath();
            // 如果图中没有增广路径了
            if(augPath.size() == 0) break;

            int f = Integer.MAX_VALUE;
            for(int i=1; i<augPath.size(); i ++) {
                int v = augPath.get(i-1);
                int w = augPath.get(i);
                f = Math.min(rG.getWeight(v, w), f);
            }
            maxFlow += f;

            for(int i=1; i<augPath.size(); i ++) {
                int v = augPath.get(i-1);
                int w = augPath.get(i);

                rG.setWeight(v, w, rG.getWeight(v, w) - f);
                rG.setWeight(w, v, rG.getWeight(w, v) + f);
            }
        }
    }

    private ArrayList<Integer> getAugmentingPath() {
        // 函数功能：在残量图中寻找增广路径，实现一个广度优先遍历
        Queue<Integer> q = new LinkedList<>();
        int[] pre = new int[network.V()];
        Arrays.fill(pre, -1);   // pre有记录节点的上一个节点的功能，还有判断节点是否被遍历过的功能
        q.add(s);
        pre[s] = s;
        while(!q.isEmpty()) {
            int cur = q.remove();
            if(cur == t) break;
            for(int next : rG.adj(cur)) {
                if(pre[next] == -1 && rG.getWeight(cur, next) > 0) {
                    pre[next] = cur;
                    q.add(next);
                }
            }
        }
        ArrayList<Integer> res = new ArrayList<>();
        if(pre[t] == -1) {
            return res;
        }
        int cur = t;
        while(cur != s) {
            res.add(cur);
            cur = pre[cur];
        }
        res.add(s);
        Collections.reverse(res);
        return res;
    }

    public int result() {
        return maxFlow;
    }

    public int flow(int v, int w) {
        if(!network.hasEdge(v, w)) {
            throw new IllegalArgumentException(String.format("No edge %d-%d", v, w));
        }
        return rG.getWeight(w, v);
    }

    public static void main(String[] args) {
        WeightedGraph network = new WeightedGraph("network2.txt", true);
        MaxFlow maxFlow = new MaxFlow(network, 0, 5);
        System.out.println(maxFlow.result());
        for(int v=0; v<network.V(); v ++) {
            for(int w: network.adj(v)) {
                System.out.println(String.format("%d-%d : %d / %d",v, w, maxFlow.flow(v, w), network.getWeight(v, w)));
            }
        }
    }
}
