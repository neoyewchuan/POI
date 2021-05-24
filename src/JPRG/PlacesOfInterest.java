package JPRG;

public class PlacesOfInterest {
	String placeID;
	String placeInterestType;
	String placeName;
	String mustSeeOrDo;
	double rating;
	
	PlacesOfInterest() {
		placeID = "";
		placeInterestType = "";
		placeName = "";
		mustSeeOrDo = "";
		rating = 0;
	}
	
	PlacesOfInterest(String arg0, String arg1, String arg2, String arg3, double arg4) {
		placeID = arg0;
		placeInterestType = arg1;
		placeName = arg2;
		mustSeeOrDo = arg3;
		rating = arg4;
	}
	
	protected String getPlaceID()	{
		return placeID;
	}
	
	protected String getPlaceInterestType()	{
		return placeInterestType;
	}
	
	protected String getPlaceName()	{
		return placeName;
	}
	
		
	protected String getMustSeeOrDo() {
		return mustSeeOrDo;
	}
	
	protected double getRating() {
		return rating;
	}
	
	
	
}