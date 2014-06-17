package com.mapplas.model;

public class SearchValue {

	private int id;
	
	private String country;

	private String name1;
	
	private String name1_clean;

	private String name2;
	
	private String name2_clean;
	
	private String population;

	// SearchValues table name
	public static final String TABLE_SEARCHVALUES = "search_values";

	// SearchValues Table Columns names
	public static final String KEY_ID = "id";
	
	public static final String KEY_COUNTRY = "country";

	public static final String KEY_NAME1 = "name1";

	public static final String KEY_NAME2 = "name2";
	
	public static final String KEY_NAME1_CLEAN = "name1_clean";
	
	public static final String KEY_NAME2_CLEAN = "name2_clean";
	
	public static final String KEY_POPULATION = "population";

	public static final String[] COLUMNS = { KEY_ID, KEY_POPULATION, KEY_COUNTRY, KEY_NAME1, KEY_NAME1_CLEAN, KEY_NAME2, KEY_NAME2_CLEAN };

	public SearchValue() {
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getCountry() {
		return this.country;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public String getName1() {
		return this.name1;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getName2() {
		return this.name2;
	}
	
	public void setName1Clean(String name1_clean) {
		this.name1_clean = name1_clean;
	}

	public String getName1Clean() {
		return this.name1_clean;
	}
	
	public void setName2Clean(String name2_clean) {
		this.name2_clean = name2_clean;
	}

	public String getName2Clean() {
		return this.name2_clean;
	}

}
