import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class DirectedEulerLoop {
    private Graph G;

    public DirectedEulerLoop(Graph G) {
        if(!G.isDirected()) {
            // 若图为无向图，抛出异常
            throw new IllegalArgumentException("DirectedEulerLoop only works in directed graph");
        }
        this.G = G;
    }

    public boolean hasEulerLoop() {
//        // 首先判断图的联通性
//        CC cc = new CC(G);
//        if(cc.count() > 1) return false;

        // 有向图的连通性和无向图不同，我们会后面再来处理这个问题。
        for(int v=0; v < G.V(); v ++) {
            if(G.indegree(v) != G.outdegree(v)) return false;
        }
        return true;
    }

    public ArrayList<Integer> result () {
        ArrayList<Integer> res = new ArrayList<>();
        if(!hasEulerLoop()) return res;

        Graph g = (Graph) G.clone();

        Stack<Integer> stack = new Stack<>();

        int curv = 0;
        stack.push(curv);
        while(!stack.empty()) {
            if(g.outdegree(curv) != 0 ) {
                stack.push(curv);
                int w = g.adj(curv).iterator().next();
                g.removeEdge(curv, w);
                curv = w;
            } else {
                res.add(curv);
                curv = stack.pop();
            }
        }
        Collections.reverse(res);
        return res;
    }

    public static void main(String[] args) {
        Graph g = new Graph("ug.txt", true);
        DirectedEulerLoop el = new DirectedEulerLoop(g);
        System.out.println(el.result());

        Graph g2 = new Graph("ug2.txt", true);
        DirectedEulerLoop el2 = new DirectedEulerLoop(g2);
        System.out.println(el2.result());
    }
}