import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Arrays;

public class WordNet {

    private final Map<String, List<Integer>> nouns;
    private final List<String> synsets;
    private final Digraph G;
    private final SAP path;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        validate(synsets);
        validate(hypernyms);

        this.synsets = new ArrayList<String>();
        this.nouns = new TreeMap<String, List<Integer>>();
        parseSynsets(synsets);
        this.G = construct(hypernyms);
        validate(G);

        path = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        validate(word);
        return nouns.containsKey(word);
    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        validate(nounA, nounB);
        Iterable<Integer> v = nouns.get(nounA);
        Iterable<Integer> w = nouns.get(nounB);
        return path.length(v, w);
    }

    // a synset (second field of nouns.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path
    public String sap(String nounA, String nounB) {
        validate(nounA, nounB);
        Iterable<Integer> v = nouns.get(nounA);
        Iterable<Integer> w = nouns.get(nounB);
        int ancestor = path.ancestor(v, w);
        return synsets.get(ancestor);
    }

    // Helper method for input validation
    private void validate(String s) {
        if (s == null)
            throw new IllegalArgumentException();
    }

    private void validate(String s1, String s2) {
        if (s1 == null || s2 == null)
            throw new IllegalArgumentException();
    }

    private void validate(Digraph G) {
        DirectedCycle dc = new DirectedCycle(G);
        if (dc.hasCycle())
            throw new IllegalArgumentException();
        int count = 0;
        for (int v = 0; v < G.V(); v++) {
            if (G.outdegree(v) == 0)
                count++;
        }
        if (count > 1)
            throw new IllegalArgumentException();
    }

    // Helper method for parsing input files
    private Map<String, List<Integer>> parseSynsets(String filename) {
        validate(filename);

        In in = new In(filename);
        String[] lines = in.readAllLines();
        String[] tokens = null;

        List<Integer> list = null;

        for (String line : lines) {
            tokens = line.split(",");
            int id = Integer.parseInt(tokens[0]);
            String synset = tokens[1];

            synsets.add(synset);

            for (String noun : synset.split(" ")) {
                if (nouns.containsKey(noun)) {
                    list = nouns.get(noun);
                    list.add(id);
                } else {
                    list = Arrays.asList(id);
                }
                nouns.put(noun, new ArrayList<Integer>(list));
            }
        }
        return nouns;
    }

    private Digraph construct(String filename) {
        validate(filename);

        In in = new In(filename);
        String[] lines = in.readAllLines();
        String[] tokens = null;

        Digraph G = new Digraph(synsets.size());

        for (String line : lines) {
            tokens = line.split(",");
            int v = Integer.parseInt(tokens[0]);
            for (int i = 1; i < tokens.length; i++) {
                int w = Integer.parseInt(tokens[i]);
                G.addEdge(v, w);
            }
        }
        return G;
    }


    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");

        assert wn.nouns.size() == 119188; // The number of nouns in synsets.txt is 119,188.
        assert wn.G.E() == 84505; // The number of edges in wordnet digraph

        /*
         * (distance = 23) white_marlin, mileage
         * (distance = 33) Black_Plague, black_marlin
         * (distance = 27) American_water_spaniel, histology
         * (distance = 29) Brown_Swiss, barrel_roll
         * */
        assert wn.distance("white_marlin", "mileage") == 23;
        assert wn.distance("Black_Plague", "black_marlin") == 33;
        assert wn.distance("American_water_spaniel", "histology") == 27;
        assert wn.distance("Brown_Swiss", "barrel_roll") == 29;

    }

}
