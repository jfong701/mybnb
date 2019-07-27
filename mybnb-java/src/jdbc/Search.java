package jdbc;

import java.util.ArrayList;

import dbobjects.Listing;

public class Search {

	private ArrayList<String> conditions;
	public double inLatitude = Double.MIN_VALUE;
	public double inLongitude = Double.MIN_VALUE;
	public ArrayList<Listing> searchResult;
	
	public Search() {
		this.resetSearch();
	}
	
	public String generateLocationSearchCond(double lati, double longi, double searchRadiusKm) {
		double earthRadius = 6378.1;
		this.inLatitude = lati;
		this.inLongitude = longi;
		return "(2 * " + earthRadius
				+ " * ASIN( SQRT( POWER( SIN( "
				+ "(RADIANS(" + lati + ") - "
				+ "RADIANS(Latitude))/2)"
				+ ",2)"
				+ "+ COS(RADIANS(Latitude))"
				+ "* COS(RADIANS(" + lati + "))"
				+ "* POWER( SIN( "
				+ "(RADIANS( " + longi + ") - "
				+ "RADIANS(Longitude))/2)"
				+ ",2)"
				+ "))) < " + searchRadiusKm;
	}
	
	public boolean addCondition(String condition) {
		return conditions.add(condition);
	}
	
	public void resetSearch() {
		this.conditions = new ArrayList<String>();
		this.searchResult = new ArrayList<Listing>();
		this.inLatitude = Double.MIN_VALUE;
		this.inLongitude = Double.MIN_VALUE;
	}
	
	public String getAllConditionsString() {
		String out = "";
		for (int i = 0; i < conditions.size(); i++) {
			out += conditions.get(i);
			if (i < conditions.size() - 1) {
				out += " AND ";
			}
		}
		return out;
	}
}
