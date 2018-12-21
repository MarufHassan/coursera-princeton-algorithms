import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.List;

public class SAP {
    private final Digraph G;
    private DeluxeBFS bfs;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        validate(G);
        this.G = new Digraph(G);
        bfs = new DeluxeBFS(this.G);
    }

    // length of shortest ancestral path between v and w;
    // -1 if no such path
    public int length(int v, int w) {
        validate(v, w);

        if (v == w)
            return 0;
        return bfs.length(v, w);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {
        validate(v, w);

        if (v == w)
            return v;

        return bfs.ancestor(v, w);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w;
    // -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validate(v);
        validate(w);

        return bfs.length(v, w);
    }

    // a common ancestor that participates in shortest ancestral path;
    // -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validate(v);
        validate(w);
        return bfs.ancestor(v, w);
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
        if (vertex >= G.V() || vertex < 0)
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
