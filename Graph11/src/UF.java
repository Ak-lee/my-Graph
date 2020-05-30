public class UF {
    private int[] parent;
    private int[] sz;   // sz[i] 表示以 i 为该根的集合中元素个数

    public UF(int n) {
        parent = new int[n];
        sz = new int[n];
        for(int i = 0; i < n; i ++) {
            parent[i] = i;
            sz[i] = 1;
        }
    }

    // 查找元素 p 对应的集合编号
    public int find(int p) {
        if(p != parent[p]) {
            parent[p] = find(parent[p]);
        }
        return parent[p];
    }

    public boolean isConnected(int p, int q) {return find(p) == find(q);}
    public void unionElements(int p, int q) {
        int pRoot = find(p);
        int qRoot = find(q);
        if(pRoot == qRoot) return;

        parent[pRoot] = qRoot;
    }
}
