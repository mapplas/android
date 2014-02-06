package com.mapplas.model;

public class SearchValue {

	private int id;

	private String name1;

	private String name2;

	// SearchValues table name
	public static final String TABLE_SEARCHVALUES = "search_values";

	// SearchValues Table Columns names
	public static final String KEY_ID = "id";

	public static final String KEY_NAME1 = "name1";

	public static final String KEY_NAME2 = "name2";

	public static final String[] COLUMNS = { KEY_ID, KEY_NAME1, KEY_NAME2 };

	public SearchValue() {
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
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

}
