import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;

public class KdTree {


    private static final boolean VERTICAL = false;
    private static final boolean HORIZONTAL = false;
    private Node root;
    private int N;
    private Point2D champion;
    private double minDistance;

    // construct an empty set of points
    public KdTree() {
        root = null;
        N = 0;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree kt = new KdTree();
        assert kt.isEmpty();

        kt.insert(new Point2D(0.7, 0.2));
        assert kt.size() == 1;
        assert !kt.isEmpty();
        assert kt.contains(new Point2D(0.7, 0.2));

        kt.insert(new Point2D(0.5, 0.4));
        assert kt.size() == 2;
        assert kt.contains(new Point2D(0.5, 0.4));

        kt.insert(new Point2D(0.2, 0.3));
        assert kt.size() == 3;
        assert kt.contains(new Point2D(0.2, 0.3));

        kt.insert(new Point2D(0.9, 0.6));
        assert kt.size() == 4;
        assert kt.contains(new Point2D(0.9, 0.6));

        // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        Point2D query = new Point2D(0.8125, 0.25);
        Point2D nearest = kdtree.nearest(query);
        System.out.println(nearest);
        System.out.println(nearest.distanceTo(query));
    }

    // is the set empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // number of points in the set
    public int size() {
        return N;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        validate(p);
        root = insert(root, p, 0, 0, 1, 1, VERTICAL);
    }

    private Node insert(Node x, Point2D p, double xmin, double ymin, double xmax, double ymax, boolean orientation) {
        if (x == null) {
            N = N + 1;
            return new Node(p, new RectHV(xmin, ymin, xmax, ymax), orientation);
        }
        if (p.compareTo(x.p) == 0)  return x;

        double cmp;
        if (orientation == VERTICAL) {
            cmp = p.x() - x.p.x();
            if (cmp < 0)
                x.lb = insert(x.lb, p, xmin, ymin, x.p.x(), ymax, !orientation);
            else
                x.rt = insert(x.rt, p, x.p.x(), ymin, xmax, ymax, !orientation);
        } else {
            cmp = p.y() - x.p.y();
            if (cmp < 0)
                x.lb = insert(x.lb, p, xmin, ymin, xmax, x.p.y(), !orientation);
            else
                x.rt = insert(x.rt, p, xmin, x.p.y(), xmax, ymax, !orientation);
        }
        return x;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        validate(p);
        return contains(root, p);
    }

    private boolean contains(Node x, Point2D p) {
        if (x == null)      return false;
        if (p.equals(x.p))  return true;

        if (x.orientation == VERTICAL) {
            if (p.x() < x.p.x())    return contains(x.lb, p);
            else                    return contains(x.rt, p);
        } else {
            if (p.y() < x.p.y())    return contains(x.lb, p);
            else                    return contains(x.rt, p);
        }
    }

    // draw all points to standard draw
    public void draw() {
        // TODO
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        validate(rect);
        List<Point2D> points = new ArrayList<Point2D>();
        range(root, rect, points);
        return points;
    }

    private void range(Node x, RectHV rect, List<Point2D> points) {
        if (x != null) {
            if (rect.contains(x.p)) {
                points.add(x.p);
            }

            if (x.orientation == VERTICAL) {
                if (rect.xmax() < x.p.x())
                    range(x.lb, rect, points);
                else if (rect.xmin() >= x.p.x())
                    range(x.rt, rect, points);
                else {
                    range(x.lb, rect, points);
                    range(x.rt, rect, points);
                }
            } else {
                if (rect.ymax() < x.p.y())
                    range(x.lb, rect, points);
                else if (rect.ymin() >= x.p.y())
                    range(x.rt, rect, points);
                else {
                    range(x.lb, rect, points);
                    range(x.rt, rect, points);
                }
            }
        }
    }

    // a champion neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        validate(p);
        champion = null;
        minDistance = Double.POSITIVE_INFINITY;
        nearest(root, p);
        return champion;
    }

    private void nearest(Node x, Point2D query) {
        if (x == null) return;
        double distance = query.distanceSquaredTo(x.p);
        if (distance < minDistance) {
            minDistance = distance;
            champion = x.p;
        }

        double rtDistance = Double.POSITIVE_INFINITY, lbDistance = Double.POSITIVE_INFINITY;

        if (x.lb != null)
            lbDistance = x.lb.rect.distanceSquaredTo(query);
        if (x.rt != null)
            rtDistance = x.rt.rect.distanceSquaredTo(query);

        if (x.orientation == VERTICAL) {
            if (query.x() < x.p.x()) {
                nearest(x.lb, query);
                if (rtDistance < minDistance)
                    nearest(x.rt, query);
            } else {
                nearest(x.rt, query);
                if (lbDistance < minDistance)
                    nearest(x.lb, query);
            }
        } else {
            if (query.y() < x.p.y()) {
                nearest(x.lb, query);
                if (rtDistance < minDistance)
                    nearest(x.rt, query);
            } else {
                nearest(x.rt, query);
                if (lbDistance < minDistance)
                    nearest(x.lb, query);
            }
        }
    }

    private void validate(Object ob) {
        if (ob == null)
            throw new IllegalArgumentException();
    }

    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb; // left/bottom subtree
        private Node rt; // right/top subtree
        private boolean orientation;

        public Node(Point2D p, RectHV rect, boolean orientation) {
            this.p = p;
            this.rect = rect;
            this.orientation = orientation;
        }
    }

}
