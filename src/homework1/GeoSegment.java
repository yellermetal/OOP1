package homework1;

import java.text.DecimalFormat;

/**
 * A GeoSegment models a straight line segment on the earth. GeoSegments 
 * are immutable.
 * <p>
 * A compass heading is a nonnegative real number less than 360. In compass
 * headings, north = 0, east = 90, south = 180, and west = 270.
 * <p>
 * When used in a map, a GeoSegment might represent part of a street,
 * boundary, or other feature.
 * As an example usage, this map
 * <pre>
 *  Trumpeldor   a
 *  Avenue       |
 *               i--j--k  Hanita
 *               |
 *               z
 * </pre>
 * could be represented by the following GeoSegments:
 * ("Trumpeldor Avenue", a, i), ("Trumpeldor Avenue", z, i),
 * ("Hanita", i, j), and ("Hanita", j, k).
 * </p>
 * 
 * </p>
 * A name is given to all GeoSegment objects so that it is possible to
 * differentiate between two GeoSegment objects with identical
 * GeoPoint endpoints. Equality between GeoSegment objects requires
 * that the names be equal String objects and the end points be equal
 * GeoPoint objects.
 * </p>
 *
 * <b>The following fields are used in the specification:</b>
 * <pre>
 *   name : String       // name of the geographic feature identified
 *   p1 : GeoPoint       // first endpoint of the segment
 *   p2 : GeoPoint       // second endpoint of the segment
 *   length : real       // straight-line distance between p1 and p2, in kilometers
 *   heading : angle     // compass heading from p1 to p2, in degrees
 * </pre>
 **/
public class GeoSegment  {

	private final String name_;
  	private final GeoPoint p1_;
  	private final GeoPoint p2_;
  	private final double length_;
  	private final double heading_;	
	
  	
	// Abstraction Function:
	// Represents a straight line segment on the earth that starts at point p1_, heading in direction of heading_ and ends at point p2_, named name_and stretches over length length_.
	
	// Representation invariant for every GeoSegment s:
	// heading_ is a number in range [0,360) 
  	// name_ is nonempty string
  	// length_ > 0
	
	
  	/**
     * Constructs a new GeoSegment with the specified name and endpoints.
     * @requires name != null && p1 != null && p2 != null
     * @effects constructs a new GeoSegment with the specified name and endpoints.
     **/
  	public GeoSegment(String name, GeoPoint p1, GeoPoint p2) {
  		name_ = name;
  		p1_ = new GeoPoint(p1.getLatitude(), p1.getLongitude());
  		p2_ = new GeoPoint(p2.getLatitude(), p2.getLongitude());
  		length_ = p1.distanceTo(p2);
  		heading_ = p1.headingTo(p2);
  		checkRep();
  	}


  	/**
     * Returns a new GeoSegment like this one, but with its endpoints reversed.
     * @return a new GeoSegment gs such that gs.name = this.name
     *         && gs.p1 = this.p2 && gs.p2 = this.p1
     **/
  	public GeoSegment reverse() {
  		checkRep();
  		GeoPoint tmpP1 = new GeoPoint(p1_.getLatitude(), p1_.getLongitude());
  		GeoPoint tmpP2 = new GeoPoint(p2_.getLatitude(), p2_.getLongitude());		
  		GeoSegment reversedSegment = new GeoSegment(name_, tmpP2, tmpP1);
  		checkRep();
  		return reversedSegment;
  	}


  	/**
  	 * Returns the name of this GeoSegment.
     * @return the name of this GeoSegment.
     */
  	public String getName() {
  		checkRep();
  		return name_;
  	}


  	/**
  	 * Returns first endpoint of the segment.
     * @return first endpoint of the segment.
     */
  	public GeoPoint getP1() {
  		checkRep();
  		GeoPoint tmpP1 = new GeoPoint(p1_.getLatitude(), p1_.getLongitude());
  		checkRep();
  		return tmpP1;
  	}


  	/**
  	 * Returns second endpoint of the segment.
     * @return second endpoint of the segment.
     */
  	public GeoPoint getP2() {
  		checkRep();
  		GeoPoint tmpP2 = new GeoPoint(p2_.getLatitude(), p2_.getLongitude());
  		checkRep();
  		return tmpP2;
  	}


  	/**
  	 * Returns the length of the segment.
     * @return the length of the segment, using the flat-surface, near the
     *         Technion approximation.
     */
  	public double getLength() {
  		checkRep();
  		return length_;
  	}


  	/**
  	 * Returns the compass heading from p1 to p2.
     * @requires this.length != 0
     * @return the compass heading from p1 to p2, in degrees, using the
     *         flat-surface, near the Technion approximation.
     **/
  	public double getHeading() {
  		checkRep();
  		return heading_;
  	}


  	/**
     * Compares the specified Object with this GeoSegment for equality.
     * @return gs != null && (gs instanceof GeoSegment)
     *         && gs.name = this.name && gs.p1 = this.p1 && gs.p2 = this.p2
   	 **/
  	public boolean equals(Object gs) {
  		checkRep();
  		if(gs == null || !(gs instanceof GeoSegment))
  		{
  			return false;
  		}
  		GeoSegment geoSegment = (GeoSegment)gs;
  		boolean result = ( geoSegment.getName() == name_ && p1_.equals(geoSegment.getP1()) && p2_.equals(geoSegment.getP2())) ? true : false;
  		checkRep();
  		return result;
  	}


  	/**
  	 * Returns a hash code value for this.
     * @return a hash code value for this.
     **/
  	public int hashCode() {
    	// This implementation will work, but you may want to modify it 
    	// for improved performance. 

    	return 1;
  	}


  	/**
  	 * Returns a string representation of this.
     * @return a string representation of this.
     **/
  	public String toString() {
  		checkRep();
  		String shortLength = new DecimalFormat("##.#").format(length_);
  		String geoFeatureString = "Georaphical segment " + name_ + " starts at point: " + p1_.toString() + " and heading in " + heading_ + " to end point: " + p2_.toString() + ". The length of the feature is " + shortLength + " KM.";
  		checkRep();
  		return geoFeatureString;
  	}
  	
	/**
	 * Checks to see if the representation invariant is being violated.
	 * @throws AssertionError if representation invariant is violated.
	 **/
  	private void checkRep() {
		assert (heading_ >= 0 && heading_ < 360) :
			"Wrong heading direction.";
		assert (name_.length() >= 1) :
			"Not viable feature name.";
}

}