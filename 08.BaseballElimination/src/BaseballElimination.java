import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


public class BaseballElimination {

    private Map<String, Integer> map; // map team name to vertices
    private String[] names; // all teams
    private int[] w;
    private int[] l;
    private int[] r;
    private int[][] g;
    private int n; // number of teams

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        if (filename == null)
            throw new IllegalArgumentException("No file specified");

        In in = new In(filename);
        initialize(in);
        store(in);
    }

    // initialize all the instance variable
    private void initialize(In in) {
        n = in.readInt();

        map = new HashMap<String, Integer>();
        names = new String[n];
        w = new int[n];
        l = new int[n];
        r = new int[n];
        g = new int[n][n];
    }

    // store the data from the input stream
    private void store(In in) {
        for (int idx = 0; idx < n; idx++) {
            String name = in.readString();
            map.put(name, idx);
            names[idx] = name;
            w[idx] = in.readInt();
            l[idx] = in.readInt();
            r[idx] = in.readInt();

            for (int j = 0; j < n; j++) {
                g[idx][j] = in.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return n;
    }

    // all teams
    public Iterable<String> teams() {
        return Arrays.asList(names);
    }

    // number of wins for given team
    public int wins(String team) {
        validate(team);
        return w[map.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        validate(team);
        return l[map.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        validate(team);
        return r[map.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        validate(team1);
        validate(team2);

        int idx1 = map.get(team1);
        int idx2 = map.get(team2);

        return g[idx1][idx2];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        return certificateOfElimination(team) != null;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        validate(team);
        int x = map.get(team); // team id

        // check for trivial elimination
        List<String> winners = new ArrayList<String>();
        for (int v = 0; v < n; v++) {
            if (v == x) continue;
            if (w[x] + r[x] < w[v]) {
                winners.add(names[v]);
            }
        }
        return winners.size() != 0 ? winners : eliminates(x);
    }

    private Iterable<String> eliminates(int x) {
        int games = ((n - 2) * (n - 1)) / 2; // number of games
        int V = 2 + n + games; // number of vertices in the network

        int src = V - 2, sink = V - 1; // arbitrarily selected

        FlowNetwork network = network(V, x, src, sink);
        FordFulkerson ff = new FordFulkerson(network, src, sink);

        List<String> winners = new ArrayList<String>();
        for (int v = 0; v < n; v++) {
            if (v == x) continue;
            if (ff.inCut(v))
                winners.add(names[v]);
        }
        return winners.size() == 0 ? null : winners;
    }

    // make a flow network graph with source and sink node specified
    private FlowNetwork network(int V, int x, int src, int sink) {
        FlowNetwork network = new FlowNetwork(V);
        double infinity = Double.POSITIVE_INFINITY;

        // 0 to (n - 1) is reserved for team vertices, team x remain unused
        int offset = n; // game vertex offset

        for (int v = 0; v < n; v++) {
            if (v == x) continue;
            for (int w = v + 1; w < n; w++) {
                if (w == x) continue;
                // edges from source to game vertices
                network.addEdge(new FlowEdge(src, offset, g[v][w]));
                // edges from game vertices to team vertices
                network.addEdge(new FlowEdge(offset, v, infinity));
                network.addEdge(new FlowEdge(offset, w, infinity));
                offset++;
            }
            // edges from team vertices to target
            int capacity = Math.max(0, w[x] + r[x] - w[v]);
            network.addEdge(new FlowEdge(v, sink, capacity));
        }
        return network;
    }

    // team name validation
    private void validate(String name) {
        if (name == null || !map.containsKey(name))
            throw new IllegalArgumentException("invalid team name");
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
