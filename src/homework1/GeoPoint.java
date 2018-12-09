package homework1;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * A GeoPoint is a point on the earth. GeoPoints are immutable.
 * <p>
 * North latitudes and east longitudes are represented by positive numbers.
 * South latitudes and west longitudes are represented by negative numbers.
 * <p>
 * The code may assume that the represented points are nearby the Technion.
 * <p>
 * <b>Implementation direction</b>:<br>
 * The Ziv square is at approximately 32 deg. 46 min. 59 sec. N
 * latitude and 35 deg. 0 min. 52 sec. E longitude. There are 60 minutes
 * per degree, and 60 seconds per minute. So, in decimal, these correspond
 * to 32.783098 North latitude and 35.014528 East longitude. The 
 * constructor takes integers in millionths of degrees. To create a new
 * GeoPoint located in the the Ziv square, use:
 * 
 * <tt>GeoPoint zivCrossroad = new GeoPoint(32783098,35014528);</tt>
 * <p>
 * Near the Technion, there are approximately 110.901 kilometers per degree
 * of latitude and 93.681 kilometers per degree of longitude. An
 * implementation may use these values when determining distances and
 * headings.
 * <p>
 * <b>The following fields are used in the specification:</b>
 * <pre>
 *   latitude :  real        // latitude measured in degrees
 *   longitude : real        // longitude measured in degrees
 * </pre>
 **/
public class GeoPoint {
	private final int latitude_;
	private final int longitude_;



	/** Minimum value the latitude field can have in this class. **/
	public static final int MIN_LATITUDE  =  -90 * 1000000;
	    
	/** Maximum value the latitude field can have in this class. **/
	public static final int MAX_LATITUDE  =   90 * 1000000;
	    
	/** Minimum value the longitude field can have in this class. **/
	public static final int MIN_LONGITUDE = -180 * 1000000;
	    
	/** Maximum value the longitude field can have in this class. **/
	public static final int MAX_LONGITUDE =  180 * 1000000;

  	/**
   	 * Approximation used to determine distances and headings using a
     * "flat earth" simplification.
     */
  	public static final double KM_PER_DEGREE_LATITUDE = 110.901;

  	/**
     * Approximation used to determine distances and headings using a
     * "flat earth" simplification.
     */
  	public static final double KM_PER_DEGREE_LONGITUDE = 93.681;
  	
  	public static final int MILLION = 1000000;
  	

	// Abstraction Function:
	// The latitude and longitude degree is latitude_/10^6 and longitude_/10^6, the minutes are
	// first two digits of (latitude_%10^6)*60 and (longitude_%10^6)*60.
	// Seconds are the first two digits of [(latitude_%10^6)-(minutes of latitude)*(10^6)/60]*3600
  	// and the first two digits of [(longitude_%10^6)-(minutes of longitude)*(10^6)/60]*3600
	
	// Representation invariant for every GeoPoint p:
	// (MIN_LATITUDE <= p.latitude_ <= MAX_LATITUDE)
	// (MIN_LONGITUDE <= p.longitude_ <= MAX_LONGITUDE)

  	
  	/**
  	 * Constructs GeoPoint from a latitude and longitude.
     * @requires the point given by (latitude, longitude) in millionths
   	 *           of a degree is valid such that:
   	 *           (MIN_LATITUDE <= latitude <= MAX_LATITUDE) and
     * 	 		 (MIN_LONGITUDE <= longitude <= MAX_LONGITUDE)
   	 * @effects constructs a GeoPoint from a latitude and longitude
     *          given in millionths of degrees.
   	 **/
  	public GeoPoint(int latitude, int longitude) {
  		latitude_ = latitude;
  		longitude_ = longitude;
  		checkRep();
  	}

  	 
  	/**
     * Returns the latitude of this.
     * @return the latitude of this in millionths of degrees.
     */
  	public int getLatitude() {
  		checkRep();
  		return latitude_;
  	}


  	/**
     * Returns the longitude of this.
     * @return the latitude of this in millionths of degrees.
     */
  	public int getLongitude() {
  		checkRep();
  		return longitude_;
  	}


  	/**
     * Computes the distance between GeoPoints.
     * @requires gp != null
     * @return the distance from this to gp, using the flat-surface, near
     *         the Technion approximation.
     **/
  	public double distanceTo(GeoPoint gp) {
  		checkRep();
  		double gpLatitudeInKm = (((double)( gp.getLatitude())) / MILLION ) * KM_PER_DEGREE_LATITUDE;
  		double gpLongitudeInKm = (((double)( gp.getLongitude())) / MILLION ) * KM_PER_DEGREE_LONGITUDE;
  		double thisLatitudeInKm = (((double)( latitude_)) / MILLION ) * KM_PER_DEGREE_LATITUDE;
  		double thisLongitudeInKm = (((double)( longitude_)) / MILLION ) * KM_PER_DEGREE_LONGITUDE;
  		double distance = Math.sqrt( Math.pow( ( gpLatitudeInKm - thisLatitudeInKm ), 2) + Math.pow( ( gpLongitudeInKm - thisLongitudeInKm ), 2) );
  		checkRep();
  		return distance;
  	}


  	/**
     * Computes the compass heading between GeoPoints.
     * @requires gp != null && !this.equals(gp)
     * @return the compass heading h from this to gp, in degrees, using the
     *         flat-surface, near the Technion approximation, such that
     *         0 <= h < 360. In compass headings, north = 0, east = 90,
     *         south = 180, and west = 270.
     **/
  	public double headingTo(GeoPoint gp) {
  		checkRep();
  		int gpCenteredLatitude = gp.getLatitude() - latitude_;
  		int gpCenteredLongitude = gp.getLongitude() - longitude_;
  		double headingDirection = Math.atan2(gpCenteredLongitude, gpCenteredLatitude) * 180 / Math.PI;
  		double nonNegativeDirection = (headingDirection >= 0 ) ? headingDirection : headingDirection + 360;
  		checkRep();
  		return nonNegativeDirection;
  	}


  	/**
     * Compares the specified Object with this GeoPoint for equality.
     * @return gp != null && (gp instanceof GeoPoint) &&
     * 		   gp.latitude = this.latitude && gp.longitude = this.longitude
     **/
  	public boolean equals(Object gp) {
  		checkRep();
  		if(gp == null || !(gp instanceof GeoPoint))
  		{
  			return false;
  		}
  		GeoPoint gpTmp = (GeoPoint)gp;
  		boolean result = ( gpTmp.getLatitude() == this.latitude_ && gpTmp.getLongitude() == this.longitude_) ? true : false ;
  		checkRep();
  		return result;		 		 
  	}


  	/**
     * Returns a hash code value for this GeoPoint.
     * @return a hash code value for this GeoPoint.
   	 **/
  	public int hashCode() {
  		checkRep();
  		int hashCode = latitude_ + longitude_;
  		checkRep();
  		return hashCode;
  	}


  	/**
     * Returns a string representation of this GeoPoint.
     * @return a string representation of this GeoPoint.
     **/
  	public String toString() {
  		checkRep();
  		int signLatitude = (int) Math.signum(latitude_);
  		int signLongitude = (int) Math.signum(longitude_);

  				
  		String latitudeString = (signLatitude > 0) ? " N" : " S";
  		String longitudeString = (signLongitude > 0) ? " E" : " W";
  		
  		NumberFormat formatter = new DecimalFormat("#00.000000");
  		String geoPointString =  "(" + String.valueOf(formatter.format((double)latitude_/MILLION)) + latitudeString + ", " 
  								     + String.valueOf(formatter.format((double)longitude_/MILLION)) + longitudeString + ")";
  		checkRep();
  		return geoPointString;
  	}
  	/**
     * Checks to see if the representation invariant is being violated.
     * @throws AssertionError if representation invariant is violated.
     **/
  	private void checkRep() {
  		assert (latitude_ >= MIN_LATITUDE && latitude_ <= MAX_LATITUDE) :
  			"Latitude out of bounds.";
  		assert (longitude_ >= MIN_LONGITUDE && longitude_ <= MAX_LONGITUDE) :
  			"Longitude out of bounds.";
  		}
  		

}
