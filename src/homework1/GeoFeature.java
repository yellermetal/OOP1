package homework1;

import java.util.Iterator;
import java.util.ArrayList;
/**
 * A GeoFeature represents a route from one location to another along a
 * single geographic feature. GeoFeatures are immutable.
 * <p>
 * GeoFeature abstracts over a sequence of GeoSegments, all of which have
 * the same name, thus providing a representation for nonlinear or nonatomic
 * geographic features. As an example, a GeoFeature might represent the
 * course of a winding river, or travel along a road through intersections
 * but remaining on the same road.
 * <p>
 * GeoFeatures are immutable. New GeoFeatures can be constructed by adding
 * a segment to the end of a GeoFeature. An added segment must be properly
 * oriented; that is, its p1 field must correspond to the end of the original
 * GeoFeature, and its p2 field corresponds to the end of the new GeoFeature,
 * and the name of the GeoSegment being added must match the name of the
 * existing GeoFeature.
 * <p>
 * Because a GeoFeature is not necessarily straight, its length - the
 * distance traveled by following the path from start to end - is not
 * necessarily the same as the distance along a straight line between
 * its endpoints.
 * <p>
 * <b>The following fields are used in the specification:</b>
 * <pre>
 *   start : GeoPoint       // location of the start of the geographic feature
 *   end : GeoPoint         // location of the end of the geographic feature
 *   startHeading : angle   // direction of travel at the start of the geographic feature, in degrees
 *   endHeading : angle     // direction of travel at the end of the geographic feature, in degrees
 *   geoSegments : sequence	// a sequence of segments that make up this geographic feature
 *   name : String          // name of geographic feature
 *   length : real          // total length of the geographic feature, in kilometers
 * </pre>
 **/
public class GeoFeature {
	
  	private final GeoPoint start_;
  	private final GeoPoint end_;
  	private final double startHeading_;
  	private final double endHeading_;
  	private final ArrayList<GeoSegment> geoSegments_;
  	private final String name_;
  	private final double length_;
	
  	
	// Abstraction Function:
	// Represents a continuous geographical feature that starts at point start_, ends at point end_, named name_, length length_.
  	// to traverse the feature one should head in azimuth startHeading_ and go through all segments in geoSegments_ list and head in endHeading_ azimuth at the last segment
	
	// Representation invariant for every GeoFeature g:
	// startHeading_ and endHeading_ numbers in range [0,360) and equal to geoSegments_.get(0) and geoSegments_.get(geoSegments_.size()-1) accordinly
  	// name_ is nonempty string
  	// length_ is the sum of all lengths of segments inside geoSegments_ list
  	// geoSegments_ a nonempty list of GeoSegments with similar name and p2 of every segment in list equals to p1 of the next segment in list,
  	// p1 of first segment in list equals to start_ and p2 of last segment in list equals to end_
  	
  	
  	
	/**
     * Constructs a new GeoFeature.
     * @requires gs != null
     * @effects Constructs a new GeoFeature, r, such that
     *	        r.name = gs.name &&
     *          r.startHeading = gs.heading &&
     *          r.endHeading = gs.heading &&
     *          r.start = gs.p1 &&
     *          r.end = gs.p2
     **/
  	public GeoFeature(GeoSegment gs) {
  		name_ = gs.getName();
  		startHeading_ = gs.getHeading();
  		endHeading_ = gs.getHeading();
  		start_ = new GeoPoint(gs.getP1().getLatitude() ,gs.getP1().getLongitude());
  		end_ = new GeoPoint(gs.getP2().getLatitude() ,gs.getP2().getLongitude());
  		length_ = gs.getLength();
  		geoSegments_ = new ArrayList<GeoSegment>();
  		GeoPoint tmpPoint1 = new GeoPoint(gs.getP1().getLatitude() ,gs.getP1().getLongitude());
  		GeoPoint tmpPoint2 = new GeoPoint(gs.getP2().getLatitude() ,gs.getP2().getLongitude());
  		GeoSegment gsTmp = new GeoSegment(gs.getName(), tmpPoint1, tmpPoint2);
  		geoSegments_.add(gsTmp);
  		checkRep();
  	}
  	
  	
  	/**
     * Constructs a new GeoFeature.
     * @requires gf != null && gs != null
     * @effects Constructs a new GeoFeature, r, such that
     *	        r.name = gf.name &&
     *          r.startHeading = gf.startHeading &&
     *          r.endHeading = gs.heading &&
     *          r.start = gf.start &&
     *          r.end = gs.p2 &&
     *          r.geoSegments = gf.geoSegments.append(gs)
     **/
  	private GeoFeature(GeoFeature gf, GeoSegment gs) {
  	  	start_ = gf.start_;
  	  	end_ = gf.end_;
  	  	startHeading_ = gf.startHeading_;
  	  	endHeading_ = gf.endHeading_;
  	  	name_ = gf.name_;
  	  	length_ = gf.length_ + gs.getLength();
  	    geoSegments_ = new ArrayList<GeoSegment>();
  	    
  	    // Copying the segment list from original feature to constructed feature with addition of gs
  		for(GeoSegment segment : gf.geoSegments_.subList(1, gf.geoSegments_.size()))
  		{
  	  		GeoPoint tmpPoint1 = new GeoPoint(segment.getP1().getLatitude() ,segment.getP1().getLongitude());
  	  		GeoPoint tmpPoint2 = new GeoPoint(segment.getP2().getLatitude() ,segment.getP2().getLongitude());
  	  		GeoSegment gsTmp = new GeoSegment(segment.getName(), tmpPoint1, tmpPoint2);
  	  		geoSegments_.add(gsTmp);
  		}
	  	GeoPoint tmpPoint1 = new GeoPoint(gs.getP1().getLatitude() ,gs.getP1().getLongitude());
	  	GeoPoint tmpPoint2 = new GeoPoint(gs.getP2().getLatitude() ,gs.getP2().getLongitude());
	  	GeoSegment gsTmp = new GeoSegment(gs.getName(), tmpPoint1, tmpPoint2);
  		geoSegments_.add(gsTmp);
  		checkRep();
  	}
  

 	/**
 	 * Returns name of geographic feature.
     * @return name of geographic feature
     */
  	public String getName() {
  		checkRep();
  		return name_;
  	}


  	/**
  	 * Returns location of the start of the geographic feature.
     * @return location of the start of the geographic feature.
     */
  	public GeoPoint getStart() {
  		checkRep();
  		GeoPoint gpTmp = new GeoPoint(start_.getLatitude() ,start_.getLongitude());
  		checkRep();
  		return gpTmp;
  	}


  	/**
  	 * Returns location of the end of the geographic feature.
     * @return location of the end of the geographic feature.
     */
  	public GeoPoint getEnd() {
  		checkRep();
  		GeoPoint gpTmp = new GeoPoint(end_.getLatitude() ,end_.getLongitude());
  		checkRep();
  		return gpTmp;
  	}


  	/**
  	 * Returns direction of travel at the start of the geographic feature.
     * @return direction (in standard heading) of travel at the start of the
     *         geographic feature, in degrees.
     */
  	public double getStartHeading() {
  		checkRep();
  		return startHeading_;
  	}


  	/**
  	 * Returns direction of travel at the end of the geographic feature.
     * @return direction (in standard heading) of travel at the end of the
     *         geographic feature, in degrees.
     */
  	public double getEndHeading() {
  		checkRep();
  		return endHeading_;
  	}


  	/**
  	 * Returns total length of the geographic feature, in kilometers.
     * @return total length of the geographic feature, in kilometers.
     *         NOTE: this is NOT as-the-crow-flies, but rather the total
     *         distance required to traverse the geographic feature. These
     *         values are not necessarily equal.
     */
  	public double getLength() {
  		checkRep();
  		return length_;
  	}


  	/**
   	 * Creates a new GeoFeature that is equal to this GeoFeature with gs
   	 * appended to its end.
     * @requires gs != null && gs.p1 = this.end && gs.name = this.name.
     * @return a new GeoFeature r such that
     *         r.end = gs.p2 &&
     *         r.endHeading = gs.heading &&
     *    	   r.length = this.length + gs.length
     **/
  	public GeoFeature addSegment(GeoSegment gs) {
  		checkRep();
  		GeoFeature newGeoFeature = new GeoFeature(this, gs);
  		checkRep();
  		return newGeoFeature;
  	}


  	/**
     * Returns an Iterator of GeoSegment objects. The concatenation of the
     * GeoSegments, in order, is equivalent to this GeoFeature. All the
     * GeoSegments have the same name.
     * @return an Iterator of GeoSegments such that
     * <pre>
     *      this.start        = a[0].p1 &&
     *      this.startHeading = a[0].heading &&
     *      this.end          = a[a.length - 1].p2 &&
     *      this.endHeading   = a[a.length - 1].heading &&
     *      this.length       = sum(0 <= i < a.length) . a[i].length &&
     *      for all integers i
     *          (0 <= i < a.length-1 => (a[i].name == a[i+1].name &&
     *                                   a[i].p2d  == a[i+1].p1))
     * </pre>
     * where <code>a[n]</code> denotes the nth element of the Iterator.
     * @see homework1.GeoSegment
     */
  	public Iterator<GeoSegment> getGeoSegments() {
  		checkRep();
  		ArrayList<GeoSegment> newSegmentList = new ArrayList<GeoSegment>();
  		// Copying the segment list to prevent rep exposure
  		for(GeoSegment segment : geoSegments_)
  		{
  	  		GeoPoint tmpPoint1 = new GeoPoint(segment.getP1().getLatitude() ,segment.getP1().getLongitude());
  	  		GeoPoint tmpPoint2 = new GeoPoint(segment.getP2().getLatitude() ,segment.getP2().getLongitude());
  	  		GeoSegment gsTmp = new GeoSegment(segment.getName(), tmpPoint1, tmpPoint2);
  	  		newSegmentList.add(gsTmp);
  		}
  		checkRep();
  		return newSegmentList.iterator();
  	}


  	/**
     * Compares the argument with this GeoFeature for equality.
     * @return o != null && (o instanceof GeoFeature) &&
     *         (o.geoSegments and this.geoSegments contain
     *          the same elements in the same order).
     **/
  	public boolean equals(Object o) {
  		checkRep();
  		if(o == null || !(o instanceof GeoFeature))
  		{
  			return false;
  		}
  		GeoFeature gf = (GeoFeature)o;
  		boolean result = ( geoSegments_.equals(gf) ) ? true : false;
  		checkRep();
  		return result;
  	}


  	/**
     * Returns a hash code for this.
     * @return a hash code for this.
     **/
  	public int hashCode() {
  		
    	
    	return 1;
  	}


  	/**
  	 * Returns a string representation of this.
   	 * @return a string representation of this.
     **/
  	public String toString() {
  		checkRep();
  		String geoFeatureString = "Georaphical feature " + name_ + " starts at: " + start_.toString() + " and ends at: " + end_.toString() + ". The length of the feature is " + length_ + " KM.";
  		checkRep();
  		return geoFeatureString;
  	}



	/**
	 * Checks to see if the representation invariant is being violated.
	 * @throws AssertionError if representation invariant is violated.
	 **/
	private void checkRep() {
		assert (startHeading_ >= 0 && startHeading_ < 360 && geoSegments_.get(0).getHeading() == startHeading_) :
			"Wrong start heading direction.";
		assert (endHeading_ >= 0 && endHeading_ < 360 && geoSegments_.get(geoSegments_.size()-1).getHeading() == endHeading_) :
			"Wrong end heading direction.";
		assert (name_.length() >= 1) :
			"Not viable feature name.";
		double tmpLength = 0;
		GeoPoint prevPoint = geoSegments_.get(0).getP1();
		for(GeoSegment segment : geoSegments_)
  		{
			assert (prevPoint.equals(segment.getP1())) :
				"Wrong segment sequence.";
  	  		tmpLength += segment.getLength();
  	  		prevPoint = segment.getP2();
  		}
		assert (tmpLength == length_) :
			"Incorrect feature length.";
		}

}
