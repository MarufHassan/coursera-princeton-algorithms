import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private final List<LineSegment> lines;

    // finds all line segments containing 4 points
    public FastCollinearPoints(Point[] points) {
        if (points == null || isNull(points))
            throw new IllegalArgumentException();

        int n = points.length, i, j;
        lines = new ArrayList<>();
        double[] slopes = new double[n];

        Point[] pointsCopy = new Point[n];
        for (i = 0; i < n; i++) {
            pointsCopy[i] = points[i];
        }

        Arrays.sort(pointsCopy);
        if (hasDuplicates(pointsCopy))
            throw new IllegalArgumentException();

        for (i = 0; i < n; i++) {
            Arrays.sort(pointsCopy, points[i].slopeOrder());

            for (j = 0; j < n; j++) {
                slopes[j] = points[i].slopeTo(pointsCopy[j]);
            }

            int maxLength = 0;
            j = 0;
            while (j + maxLength < n) {
                int start = 0, end = 0;
                if (slopes[j] == slopes[j + maxLength]) {
                    while (j + maxLength < n && slopes[j] == slopes[j + maxLength]) {
                        maxLength += 1;
                        start = j;
                        end = j + maxLength;
                    }
                }

                if (maxLength >= 3) {
                    Arrays.sort(pointsCopy, start, end);
                    if (points[i].compareTo(pointsCopy[start]) <= 0) {
                        lines.add(new LineSegment(points[i], pointsCopy[end - 1]));
                    }
                }
                j += maxLength;
                maxLength = 0;
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
