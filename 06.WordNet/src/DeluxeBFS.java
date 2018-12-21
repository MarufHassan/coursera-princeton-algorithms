import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;
import java.util.List;

public class DeluxeBFS {
    private boolean[] markedV;
    private boolean[] markedW;
    private int[] distToV;
    private int[] distToW;

    private Digraph G;

    private List<Integer> stateV;
    private List<Integer> stateW;

    private int length;
    private int ancestor;

    public DeluxeBFS(Digraph G) {
        this.G = G;
        markedV = new boolean[G.V()];
        markedW = new boolean[G.V()];
        distToV = new int[G.V()];
        distToW = new int[G.V()];
        stateV = new ArrayList<Integer>();
        stateW = new ArrayList<Integer>();
        length = -1; ancestor = -1;
    }

    // BFS from single source
    private void bfs1(int s) {
        Queue<Integer> q = new Queue<Integer>();
        markedV[s] = true;
        distToV[s] = 0;
        stateV.add(s);
        q.enqueue(s);
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!markedV[w]) {
                    distToV[w] = distToV[v] + 1;
                    markedV[w] = true;
                    stateV.add(w);
                    q.enqueue(w);
                }
            }
        }
    }

    // BFS from multiple sources
    private void bfs1(Iterable<Integer> sources) {
        Queue<Integer> q = new Queue<Integer>();
        for (int s : sources) {
            markedV[s] = true;
            distToV[s] = 0;
            stateV.add(s);
            q.enqueue(s);
        }
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!markedV[w]) {
                    distToV[w] = distToV[v] + 1;
                    markedV[w] = true;
                    stateV.add(w);
                    q.enqueue(w);
                }
            }
        }
    }

    // BFS from multiple sources
    private void bfs2(Iterable<Integer> sources) {
        Queue<Integer> q = new Queue<Integer>();
        for (int s : sources) {
            markedW[s] = true;
            distToW[s] = 0;
            stateW.add(s);
            q.enqueue(s);
        }
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!markedW[w]) {
                    distToW[w] = distToW[v] + 1;
                    markedW[w] = true;
                    stateW.add(w);
                    q.enqueue(w);
                }
            }
        }
    }

    private void bfs2(int s) {
        Queue<Integer> q = new Queue<Integer>();
        markedW[s] = true;
        distToW[s] = 0;
        stateW.add(s);
        q.enqueue(s);
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!markedW[w]) {
                    distToW[w] = distToW[v] + 1;
                    markedW[w] = true;
                    stateW.add(w);
                    q.enqueue(w);
                }
            }
        }
    }

    public int length(int v, int w) {
        bfs1(v);
        bfs2(w);
        int len = length();
        revert();
        return len;
    }

    private int length() {
        int min = Integer.MAX_VALUE;
        List<Integer> common = new ArrayList<Integer>(stateV);
        common.retainAll(stateW);
        for (int i : common) {
            int distance = distToV[i] + distToW[i];
            if (distance < min) {
                min =  distance;
                length = distance;
            }
        }
        return length;
    }

    public int ancestor(int v, int w) {
        bfs1(v);
        bfs2(w);
        int a = ancestor();
        revert();
        return a;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        bfs1(v);
        bfs2(w);
        int len = length();
        revert();
        return len;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        bfs1(v);
        bfs2(w);
        int a = ancestor();
        revert();
        return a;
    }

    private int ancestor() {
        int min = Integer.MAX_VALUE;
        List<Integer> common = new ArrayList<Integer>(stateV);
        common.retainAll(stateW);
        for (int i : common) {
            int distance = distToV[i] + distToW[i];
            if (distance < min) {
                min =  distance;
                ancestor= i;
            }
        }
        return ancestor;
    }

    private void revert() {
        for (int i : stateV) {
            markedV[i] = false;
            distToV[i] = 0;
        }
        for (int i : stateW) {
            markedW[i] = false;
            distToW[i] = 0;
        }
        length = -1; ancestor = -1;
        stateV = new ArrayList<Integer>();
        stateW = new ArrayList<Integer>();
    }
}