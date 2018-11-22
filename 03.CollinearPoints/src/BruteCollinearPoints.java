import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private final List<LineSegment> lines;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null || isNull(points))
            throw new IllegalArgumentException();

        lines = new ArrayList<>();
        int len = points.length;
        Point[] pointsCopy = new Point[len];
        for (int i = 0; i < len; i++) {
            pointsCopy[i] = points[i];
        }
        Arrays.sort(pointsCopy);

        if (hasDuplicates(pointsCopy))
            throw new IllegalArgumentException();

        for (int p = 0; p < len; p++) {
            for (int q = p + 1; q < len; q++) {
                for (int r = q + 1; r < len; r++) {
                    for (int s = r + 1; s < len; s++) {
                        double a = pointsCopy[p].slopeTo(pointsCopy[q]);
                        double b = pointsCopy[p].slopeTo(pointsCopy[r]);
                        double c = pointsCopy[p].slopeTo(pointsCopy[s]);
                        if (a == b && b == c) {
                            lines.add(new LineSegment(pointsCopy[p], pointsCopy[s]));
                        }
                    }
                }
            }
        }
    }

    private boolean isNull(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                return true;
        }
        return false;
    }

    private boolean hasDuplicates(Point[] points) {
        for (int i = 1; i < points.length; i++) {
            if (points[i - 1].compareTo(points[i]) == 0)
                return true;
        }
        return false;
    }

    // the number of line segments
    public int numberOfSegments() {
        return lines.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return lines.toArray(new LineSegment[0]);
    }
}
