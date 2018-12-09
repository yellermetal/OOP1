package homework1;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A Route is a path that traverses arbitrary GeoSegments, regardless
 * of their names.
 * <p>
 * Routes are immutable. New Routes can be constructed by adding a segment 
 * to the end of a Route. An added segment must be properly oriented; that 
 * is, its p1 field must correspond to the end of the original Route, and
 * its p2 field corresponds to the end of the new Route.
 * <p>
 * Because a Route is not necessarily straight, its length - the distance
 * traveled by following the path from start to end - is not necessarily
 * the same as the distance along a straight line between its endpoints.
 * <p>
 * Lastly, a Route may be viewed as a sequence of geographical features,
 * using the <tt>getGeoFeatures()</tt> method which returns an Iterator of
 * GeoFeature objects.
 * <p>
 * <b>The following fields are used in the specification:</b>
 * <pre>
 *   start : GeoPoint            // location of the start of the route
 *   end : GeoPoint              // location of the end of the route
 *   startHeading : angle        // direction of travel at the start of the route, in degrees
 *   endHeading : angle          // direction of travel at the end of the route, in degrees
 *   geoFeatures : sequence      // a sequence of geographic features that make up this Route
 *   geoSegments : sequence      // a sequence of segments that make up this Route
 *   length : real               // total length of the route, in kilometers
 *   endingGeoSegment : GeoSegment  // last GeoSegment of the route
 * </pre>
 **/
public class Route {

	
  	private final GeoPoint start_;
  	private final GeoPoint end_;
  	private final double startHeading_;
  	private final double endHeading_;
  	private final ArrayList<GeoFeature> geoFeatures_;
  	private final ArrayList<GeoSegment> geoSegments_;
  	private final double length_;
  	private final GeoSegment lastGeoSegment_;
	
  	
	// Abstraction Function:
	// Represents a route that starts at geographical point start_ with heading startHeading_, ends at point end_ with heading endHeading_ and stretches length length_.
  	// The route is constructed from a series of segments geoSegments_ which end with segment lastGeoSegment_ that also could be seen as series of geographical features geoFeatures_/
	
	// Representation invariant for every GeoFeature g:
	// startHeading_ and endHeading_ numbers in range [0,360) and equal to geoSegments_.get(0).getHeading() and geoSegments_.get(geoSegments_.size()-1).getHeading() accordinly
  	// start_ equals to geoSegments_.get(0).getP1() and end_ equals to geoSegments_.get(geoSegments.size()-1).getP2()
  	// length_ is the sum of all lengths of segments inside geoSegments_ list
  	// geoSegments_ and geoFeatures_ a nonempty lists of GeoSegments/GeoFeatures
  	// lastGeoSegment_ is the last segment in geoSegments_ list


  	/**
  	 * Constructs a new Route.
     * @requires gs != null
     * @effects Constructs a new Route, r, such that
     *	        r.startHeading = gs.heading &&
     *          r.endHeading = gs.heading &&
     *          r.start = gs.p1 &&
     *          r.end = gs.p2
     **/
  	public Route(GeoSegment gs) {
  		startHeading_ = gs.getHeading();
  		endHeading_ = gs.getHeading();
  		start_ = gs.getP1();
  		end_ = gs.getP2();
  		geoSegments_ = new ArrayList<GeoSegment>();
  		GeoPoint tmpPoint1 = gs.getP1();
  		GeoPoint tmpPoint2 = gs.getP2();
  		GeoSegment gsTmp = new GeoSegment(gs.getName(), tmpPoint1, tmpPoint2);
  		geoSegments_.add(gsTmp);
  		lastGeoSegment_ = gsTmp;
  		geoFeatures_ = new ArrayList<GeoFeature>();
  		GeoFeature gfTmp = new GeoFeature(gsTmp);
  		geoFeatures_.add(gfTmp);
  		length_ = gs.getLength();
  		checkRep();
  	}
  	
  	/**
  	 * Constructs a new Route that equals to rt with appended gs in the end.
     * @requires gs != null && tr != null
     * @effects Constructs a new Route, r, such that
     *	        r.startHeading = rt.startHeading &&
     *          r.endHeading = gs.heading &&
     *          r.start = rt.getStart() &&
     *          r.end = gs.getP2() &&
     *          r.geoSegments = rt.geoSegments.append(gs) &&
     *          r.length = rt.length + gs.length &&
     *          r.lastGeoSegment = gs &&
     *          r.geoFeatures = rt.geoFeatures with new geoFeature appended that contains gs 
     *          				if it is different from last geoFeature in the list or adding 
     *          				gs to the last geoFeature in the list otherwise
     *          
     **/
  	private Route(Route rt, GeoSegment gs) { 		
  	  	start_ = new GeoPoint(rt.getStart().getLatitude(), rt.getStart().getLongitude());
  	  	end_ = new GeoPoint(gs.getP2().getLatitude(), gs.getP2().getLongitude());
  	  	startHeading_ = rt.startHeading_;
  	  	endHeading_ = gs.getHeading();
  	  	length_ = rt.length_ + gs.getLength();
  	    geoSegments_ = new ArrayList<GeoSegment>();
  	    geoFeatures_ = new ArrayList<GeoFeature>();
  	    
  	    // Copying the segment list from original feature to constructed feature with addition of gs
  		for(GeoSegment segment : rt.geoSegments_)
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

  	    // Copying the feature list from original route to constructed route with addition or change to last feature
  		String prevName = geoSegments_.get(0).getName();
  		GeoFeature gf = new GeoFeature(geoSegments_.get(0));
  		if(geoSegments_.size() > 1 )
  		{	
  			for(GeoSegment segment : this.geoSegments_.subList(1, geoSegments_.size()))
  	  		{
  	  	  		if(segment.getName() == prevName)
  	  	  		{
  	  	  			gf = gf.addSegment(segment);
  	  	  		}
  	  	  		else
  	  	  		{
  	  	  			geoFeatures_.add(gf);
  	  	  			gf = new GeoFeature(segment);
  	  	  		}
  	  	  		prevName = segment.getName();
  	  		}
  	  		if(geoFeatures_.size() == 0 || geoFeatures_.get(geoFeatures_.size()-1).equals(gf))
  	  		{
  	  			geoFeatures_.add(gf);
  	  		}
  		}
  		else
  		{
  			geoFeatures_.add(gf);
  		}
  		lastGeoSegment_ = new GeoSegment(gs.getName(), gs.getP1(), gs.getP2());
  		checkRep();
  	}


    /**
     * Returns location of the start of the route.
     * @return location of the start of the route.
     **/
  	public GeoPoint getStart() {
  		checkRep();
  		GeoPoint gp = new GeoPoint(geoSegments_.get(0).getP1().getLatitude(), geoSegments_.get(0).getP1().getLongitude());
  		checkRep();
  		return gp;
  	}


  	/**
  	 * Returns location of the end of the route.
     * @return location of the end of the route.
     **/
  	public GeoPoint getEnd() {
  		checkRep();
  		GeoPoint gp = new GeoPoint(lastGeoSegment_.getP2().getLatitude(), lastGeoSegment_.getP2().getLongitude());
  		checkRep();
  		return gp;
  	}


  	/**
  	 * Returns direction of travel at the start of the route, in degrees.
   	 * @return direction (in compass heading) of travel at the start of the
   	 *         route, in degrees.
   	 **/
  	public double getStartHeading() {
  		checkRep();
  		return startHeading_;
  	}


  	/**
  	 * Returns direction of travel at the end of the route, in degrees.
     * @return direction (in compass heading) of travel at the end of the
     *         route, in degrees.
     **/
  	public double getEndHeading() {
  		checkRep();
  		return endHeading_;
  	}


  	/**
  	 * Returns total length of the route.
     * @return total length of the route, in kilometers.  NOTE: this is NOT
     *         as-the-crow-flies, but rather the total distance required to
     *         traverse the route. These values are not necessarily equal.
   	 **/
  	public double getLength() {
  		checkRep();
  		return length_;
  	}


  	/**
     * Creates a new route that is equal to this route with gs appended to
     * its end.
   	 * @requires gs != null && (gs.p1 == this.end || gs.p2 == this.end)
     * @return a new Route r such that
     *         r.end = gs.p2 &&
     *         r.endHeading = gs.heading &&
     *         r.length = this.length + gs.length
     **/
  	public Route addSegment(GeoSegment gs) {
  		checkRep();
  		Route newRoute;
  		if(gs.getP1().equals(end_))
  		{
  			newRoute = new Route(this, gs);
  		}
  		else
  		{
  			newRoute = new Route(this, gs.reverse());
  		}
  		
  		checkRep();
  		return newRoute;
  	}


    /**
     * Returns an Iterator of GeoFeature objects. The concatenation
     * of the GeoFeatures, in order, is equivalent to this route. No two
     * consecutive GeoFeature objects have the same name.
     * @return an Iterator of GeoFeatures such that
     * <pre>
     *      this.start        = a[0].start &&
     *      this.startHeading = a[0].startHeading &&
     *      this.end          = a[a.length - 1].end &&
     *      this.endHeading   = a[a.length - 1].endHeading &&
     *      this.length       = sum(0 <= i < a.length) . a[i].length &&
     *      for all integers i
     *          (0 <= i < a.length - 1 => (a[i].name != a[i+1].name &&
     *                                     a[i].end  == a[i+1].start))
     * </pre>
     * where <code>a[n]</code> denotes the nth element of the Iterator.
     * @see homework1.GeoFeature
     **/
  	public Iterator<GeoFeature> getGeoFeatures() {
  		checkRep();
  		ArrayList<GeoFeature> newFeatureList = new ArrayList<GeoFeature>();
  		// Copying the Feature list to prevent rep exposure
  		
  	    // Copying the feature list from original route to constructed route with addition or change to last feature
  		String prevName = geoSegments_.get(0).getName();
  		GeoFeature gf = new GeoFeature(geoSegments_.get(0));
  		if(geoSegments_.size() > 1 )
  		{	
  			for(GeoSegment segment : this.geoSegments_.subList(1, geoSegments_.size()))
  	  		{
  	  	  		if(segment.getName() == prevName)
  	  	  		{
  	  	  			gf = gf.addSegment(segment);
  	  	  		}
  	  	  		else
  	  	  		{
  	  	  		newFeatureList.add(gf);
  	  	  			gf = new GeoFeature(segment);
  	  	  		}
  	  	  		prevName = segment.getName();
  	  		}
  	  		if(newFeatureList.size() == 0 || newFeatureList.get(newFeatureList.size()-1).equals(gf))
  	  		{
  	  			newFeatureList.add(gf);
  	  		}
  		}
  		else
  		{
  			newFeatureList.add(gf);
  		}
  		checkRep();
  		return newFeatureList.iterator();
  	}


  	/**
     * Returns an Iterator of GeoSegment objects. The concatenation of the
     * GeoSegments, in order, is equivalent to this route.
     * @return an Iterator of GeoSegments such that
     * <pre>
     *      this.start        = a[0].p1 &&
     *      this.startHeading = a[0].heading &&
     *      this.end          = a[a.length - 1].p2 &&
     *      this.endHeading   = a[a.length - 1].heading &&
     *      this.length       = sum (0 <= i < a.length) . a[i].length
     * </pre>
     * where <code>a[n]</code> denotes the nth element of the Iterator.
     * @see homework1.GeoSegment
     **/
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
  		checkRep();
  		return newSegmentList.iterator();
  	}


  	/**
     * Compares the specified Object with this Route for equality.
     * @return true iff (o instanceof Route) &&
     *         (o.geoFeatures and this.geoFeatures contain
     *          the same elements in the same order).
     **/
  	public boolean equals(Object o) {
  		checkRep();
  		if(o == null || !(o instanceof Route))
  		{
  			return false;
  		}
  		GeoFeature thisGeoFeature;
		GeoFeature inputGeoFeature;
		boolean result = false;
  		Route rt = (Route)o;
  		Iterator<GeoFeature> thisIterator = geoFeatures_.iterator();
  		Iterator<GeoFeature> rtIterator = rt.getGeoFeatures();
  		while (thisIterator.hasNext() && rtIterator.hasNext())
  		{
  			thisGeoFeature = thisIterator.next();
  			inputGeoFeature = rtIterator.next();
  			result = ( thisGeoFeature.equals(inputGeoFeature) ) ? true : false;
  			result = ( (geoFeatures_.iterator().hasNext() && rt.getGeoFeatures().hasNext()) || (!geoFeatures_.iterator().hasNext() && !rt.getGeoFeatures().hasNext()) ) ? true : false;
  		}
  		checkRep();
  		return result;
  	}


    /**
     * Returns a hash code for this.
     * @return a hash code for this.
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
  		String geoFeatureString = "Route starts at: " + start_.toString() + " and ends at: " + end_.toString() + ". The length of the route is " + shortLength + " KM.";
  		geoFeatureString += " The route contains the next features: ";
  		for(GeoFeature feature : geoFeatures_)
  		{
  			geoFeatureString += feature.getName() + ", ";
  		}
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
		assert (lastGeoSegment_.equals(geoSegments_.get(geoSegments_.size()-1)) ) :
			"Last segments do not match.";
		double tmpLength = geoSegments_.get(0).getLength();
		GeoPoint prevPoint = geoSegments_.get(0).getP2();
		for(GeoSegment segment : geoSegments_.subList(1, geoSegments_.size()))
  		{
			assert (prevPoint.equals(segment.getP1())) :
				"Wrong segment sequence.";
  	  		tmpLength += segment.getLength();
  	  		prevPoint = segment.getP2();
  		}
		prevPoint = geoFeatures_.get(0).getEnd();
		for(GeoFeature feature : geoFeatures_.subList(1, geoFeatures_.size()))
  		{
			assert (prevPoint.equals(feature.getStart())) :
				"Wrong feature sequence.";
  	  		prevPoint = feature.getEnd();
  		}
		assert (tmpLength == length_) :
			"Incorrect feature length.";
		}
  	
}