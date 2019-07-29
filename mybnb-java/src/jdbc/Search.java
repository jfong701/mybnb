package jdbc;

import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import dbobjects.Listing;

public class Search {

	private ArrayList<String> conditions;
	public double inLatitude = Double.NEGATIVE_INFINITY;
	public double inLongitude = Double.NEGATIVE_INFINITY;
	public List<Listing> searchResult;
	
	public Search() {
		this.resetSearch();
	}
	
	public boolean addCondition(String condition) {
		return conditions.add(condition);
	}
	
	// a clause of a where condition, when the search is executed, all of these clauses are AND'd together.
	public String generateLocationSearchCond(double lati, double longi, double searchRadiusKm) {
		double earthRadius = 6378.1;
		this.inLatitude = lati;
		this.inLongitude = longi;
		String clause = "(2 * " + earthRadius
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
		
		return clause;
	}
	
	/*
	 * Postal code search matches any substring that starts with the same givenInput
	 */
	public String generatePostalCodeSearchCond(String postalCodeStarting) {
		String clause = "PostalCode LIKE '" + postalCodeStarting + "%'";
		return clause;
	}
	
	/*
	 * Address search matches exact address only (not case sensitive)
	 */
	public String generateExactAddressSearchCond(String address) {
		String clause = "Address = '" + address + "'";
		return clause;
	}
	
	/*
	 * Address search matches exact City name only (not case sensitive)
	 */
	public String generateCitySearchCond(String cityName) {
		String clause = "City = '" + cityName + "'";
		return clause;
	}
	
	public String generateMaxPriceCond(double maxPrice) {
		String clause = "BasePrice <= " + maxPrice;
		return clause;
	}
	

	public void resetSearch() {
		this.conditions = new ArrayList<String>();
		this.searchResult = new ArrayList<Listing>();
		this.inLatitude = Double.NEGATIVE_INFINITY;
		this.inLongitude = Double.NEGATIVE_INFINITY;
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
