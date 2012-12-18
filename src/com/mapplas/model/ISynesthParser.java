package com.mapplas.model;

import java.util.ArrayList;


public interface ISynesthParser {

	public void Init();
	
	public void Delete();
	
	public void ParseLocalizations(String input, SuperModel model, boolean append);
	
	public ArrayList<App> SimpleParseLocalizations(String input);
	
	public User ParseUser(String input);
	
	public App ParseLocalization(String input);
	
}
