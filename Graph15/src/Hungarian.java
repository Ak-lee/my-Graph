import java.util.*;

public class Hungarian {
    private Graph G;
    private int maxMatching = 0;
    private int[] matching;

    public Hungarian(Graph G) {
        BipartitionDetection bd = new BipartitionDetection(G);
        if(!bd.isBipartite()) { // 若图不是二分图
            throw new IllegalArgumentException("Hungarian only works for bipartite graph.");
        }
        this.G = G;

        int[] colors = bd.colors();

        matching = new int[G.V()];  // matching[i] 表示与节点i已经匹配的点
        Arrays.fill(matching, -1);  // matching[i] 初始化为-1，表示未匹配
        for(int v=0; v<G.V(); v ++) {
            if(colors[v] == 0 && matching[v] == -1) {    // 我们只遍历左侧的还未匹配的点
                if(bfs(v)) maxMatching ++;  // 如果寻找到增广路径，则maxMatching 增1
            }
        }
    }

    private boolean bfs(int v) {
        Queue<Integer> q = new LinkedList<>();
        int[] pre = new int[G.V()];
        Arrays.fill(pre, -1);

        q.add(v);
        pre[v] = v;
        while(!q.isEmpty()) {
            int cur = q.remove();
            for(int next : G.adj(cur)) {
                if(pre[next] == -1) {   // 如果 next 还没有被广度优先遍历到
                    if(matching[next] != -1) {  // 如果next已经匹配过了
                        pre[next] = cur;
                        pre[matching[next]] = next;
                        q.add(matching[next]);
                    } else {
                        pre[next] = cur;
                        ArrayList<Integer> augPath = getAugPath(pre, v, next);
                        for(int i = 0; i<augPath.size(); i = i + 2) {
                            matching[augPath.get(i)] = augPath.get(i+1);
                            matching[augPath.get(i+1)] = augPath.get(i);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private ArrayList<Integer> getAugPath(int[] pre, int start, int end) {
        // start 为路径的起点，end为路径的终点
        ArrayList<Integer> res = new ArrayList<>();
        int cur = end;
        while(cur != start) {
            res.add(cur);
            cur = pre[cur];
        }
        res.add(start);
        return res;
    }

    public int maxMatching() {
        return maxMatching;
    }
    public boolean isPerfectMatching() {
        return maxMatching * 2 == G.V();
    }

    public static void main(String[] args) {
        Graph g = new Graph("g.txt");
        Hungarian hungarian = new Hungarian(g);
        System.out.println(hungarian.maxMatching());

        Graph g2 = new Graph("g2.txt");
        Hungarian hungarian2 = new Hungarian(g2);
        System.out.println(hungarian2.maxMatching());
    }
}
