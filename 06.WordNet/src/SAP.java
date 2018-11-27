import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.List;

public class SAP {
    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        validate(G);
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w;
    // -1 if no such path
    public int length(int v, int w) {
        validate(v, w);

        if (v == w)
            return 0;

        BreadthFirstDirectedPaths left = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths right = new BreadthFirstDirectedPaths(this.G, w);

        int minDistance = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            for (int vertex : G.adj(i)) {
                if (left.hasPathTo(vertex) && right.hasPathTo(vertex)) {
                    int distance = left.distTo(vertex) + right.distTo(vertex);
                    minDistance = Math.min(distance, minDistance);
                }
            }
        }

        return minDistance < Integer.MAX_VALUE ? minDistance : -1;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {
        validate(v, w);

        if (v == w)
            return v;

        BreadthFirstDirectedPaths left = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths right = new BreadthFirstDirectedPaths(this.G, w);

        int minDistance = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i = 0; i < G.V(); i++) {
            for (int vertex : G.adj(i)) {
                if (left.hasPathTo(vertex) && right.hasPathTo(vertex)) {
                    int distance = left.distTo(vertex) + right.distTo(vertex);
                    if (distance < minDistance) {
                        minDistance = distance;
                        ancestor = vertex;
                    }
                }
            }
        }
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w;
    // -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validate(v);
        validate(w);

        int minDistance = Integer.MAX_VALUE;
        for (int a: v) {
            for (int b: w) {
                int distance = length(a, b);
                minDistance = Math.min(distance, minDistance);
            }
        }

        return minDistance < Integer.MAX_VALUE ? minDistance : -1;
    }

    // a common ancestor that participates in shortest ancestral path;
    // -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validate(v);
        validate(w);

        int minDistance = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int a: v) {
            for (int b: w) {
                int distance = length(a, b);
                if (distance < minDistance) {
                    minDistance = distance;
                    ancestor = ancestor(a, b);
                }
            }
        }
        return ancestor;
    }

    // private Helper method
    private void validate(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException();
    }

    private void validate(int v, int w) {
        validate(v);
        validate(w);
    }

    private void validate(int vertex) {
        if (vertex >= G.V())
            throw new IllegalArgumentException();
    }

    private void validate(Iterable<Integer> vertices) {
        if (vertices == null)
            throw new IllegalArgumentException();
        for (Integer v : vertices) {
            if (v == null)
                throw new IllegalArgumentException();
            validate(v);
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        Digraph G = new Digraph(new In("digraph25.txt"));
        SAP path = new SAP(G);

        assert path.length(13, 16) == 4;
        assert path.ancestor(13, 16) == 3;

        assert path.length(21, 22) == 2;
        assert path.ancestor(21, 22) == 16;

        assert path.length(18, 19) == 4;
        assert path.ancestor(18, 19) == 5;

        assert path.length(1, 2) == 2;
        assert path.ancestor(1, 2) == 0;

        assert path.length(1, 0) == 1;
        assert path.ancestor(1, 0) == 0;


        List<Integer> A = Arrays.asList(13, 23, 24);
        List<Integer> B = Arrays.asList(6, 16, 17);

        assert path.length(A, B) == 4;
        assert path.ancestor(A, B) == 3;
    }
}
