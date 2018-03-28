package Database;

/**
 * A Polygon geometric object
 * @author DB Team
 * @version 3/28/2018
 */

public class Polygon extends GeomObject {
	
	// a linked list of all the vertices of the polygon
	private LinkedList<Point> points;
	// a linked list for keys for each point
	private LinkedList<Integer> keys=new LinkedList<Integer>();

	/**
	 * Constructor for the polygon
	 * NOTICE!!: WHEN ADDING VERTICES TO THE POINTS ARRAY, ADD THEM IN ORDER!!!!
	 * @param points the linked list of all the vertices of the polygon
	 */
	public Polygon(LinkedList<Point> points) {
		super();
		
		// initialize the point array
		this.points = points;
		// Set the keys of the points in the polygon
		setPointsKeys();	
		
		// Key value for polygon
		type =3;
	}	
	
	/**
	 * Get the keys of the points in the polygon.
	 */
	public void setPointsKeys() {
		// Add keys to the keys linked list in order of the points
		for(int i=0; i<points.size();i++) {
			keys.add(keys.size(), points.get(i).getKey());
		}
	}
	
	/**
	 * A method that returns the keys of the points
	 * @return a linked list containing the keys
	 */
	public LinkedList<Integer> getPointsKeys() {
		return keys;
	}
	
	/** Given three colinear points p, q, r 
	 * the function checks if point q lies on line segment pr
	 * @param p the point being checked
	 * @param q starting point of the edge
	 * @param r ending point of the edge
	 * @return true if q lies on the sigment of pr
	 */
	public static boolean onSegment(Point p, Point q, Point r) {
		if (q.getX() <= Math.max(p.getX(), r.getX()) && q.getX() >= Math.min(p.getX(), r.getX())
                && q.getY() <= Math.max(p.getY(), r.getY()) && q.getY() >= Math.min(p.getY(), r.getY()))
            return true;
        return false;	
        }
	
	/**
	 * To find orientation of ordered triplet (p, q, r).
	 * The function returns following values
	 * 0 --> p, q and r are colinear
	 * 1 --> Clockwise
	 * 2 --> Counterclockwise
	 * @param p the point being checked
	 * @param q one of the points of the edge
	 * @param r one of the points of the edge
	 * @return the type of the orientation
	 */
	public static int orientation(Point p, Point q, Point r)
    {
        int val = (int)((q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY()));
 
        if (val == 0)
            return 0; // colinear
        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }
	
	/**
	 * The function that returns true if line segment 'p1q1' and 'p2q2' intersect.
	 * @param p1 one of the points of the p1q1
	 * @param q1 one of the points of the p1q1
	 * @param p2 one of the points of the p2q2
	 * @param q2 one of the points of the p2q2
	 * @return true if p1q1 and p2q2 intersect
	 */
	public static boolean doIntersect(Point p1, Point q1, Point p2, Point q2)
    {	
 
		// Find the four orientations needed for general and
	    // special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);
 
        // General case
        if (o1 != o2 && o3 != o4)
            return true;
     // Special Cases
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1))
            return true;
 
     // p1, q1 and p2 are colinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1))
            return true;
 
     // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2))
            return true;
 
     // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        if (o4 == 0 && onSegment(p2, q1, q2))
            return true;
        
     // Doesn't fall in any of the above cases
        return false;
    }
	
	/**
	 * Check if one point is inside the polygon.
	 * @param polygon the polygon
	 * @param n the number of vertices
	 * @param p the point being checked
	 * @return true if the point p lies inside the polygon[] with n vertices
	 */
	public boolean isInside(LinkedList polygon, int n, Point p)
    {
        int INF = 10000;
        
     // There must be at least 3 vertices in polygon[]
        if (n < 3)
            return false;
 
     // Create a point for line segment from p to infinite
        Point extreme = new Point(INF, p.getY());
 
     // Count intersections of the above line with sides of polygon
        int count = 0, i = 0;
        do
        {
        	
            int next = (i + 1) % n;
            
         // Check if the line segment from 'p' to 'extreme' intersects
            // with the line segment from 'polygon[i]' to 'polygon[next]'
            if (doIntersect((Point)polygon.get(i), (Point)polygon.get(next), p, extreme))
            {
            	
            	// If the point 'p' is colinear with line segment 'i-next',
                // then check if it lies on segment. If it lies, return true,
                // otherwise false
                if (orientation((Point)polygon.get(i), p, (Point)polygon.get(next)) == 0)
                
                    return onSegment((Point)polygon.get(i), p, (Point)polygon.get(next));
 
                count++;
            }
            i = next;
            
        } while (i != 0);
 
     // Return true if count is odd, false otherwise
        return (count & 1) == 1 ? true : false; // Same as (count%2 == 1)
    }
}