package com.mapplas.utils.presenters;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import app.mapplas.com.R;

import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.User;

public class UserFormPresenter {
	
	private Context context;
	
	private User user;

	public UserFormPresenter(Context context, User user) {
		this.context = context;
		this.user = user;
	}

	public void present() {
		// Initialize profile textView
		TextView profileTextView = (TextView)((Activity)this.context).findViewById(R.id.lblProfile);
		profileTextView.setTypeface(((MapplasApplication)this.context.getApplicationContext()).getItalicTypeFace());		

		// Initialize name text view field
		TextView lblName = (TextView)((Activity)this.context).findViewById(R.id.lblName);
		lblName.setTypeface(((MapplasApplication)this.context.getApplicationContext()).getTypeFace());
		lblName.setText(this.user.getName());

		if(this.user.getName().equals("")) {
			lblName.setText(R.string.name_not_set);
		}	
		
		// Initialize email text view field
		TextView lblEmail = (TextView)((Activity)this.context).findViewById(R.id.lblEmail);
		lblEmail.setTypeface(((MapplasApplication)this.context.getApplicationContext()).getTypeFace());
		lblEmail.setText(this.user.getEmail());
		
		if(this.user.getEmail().equals("")) {
			lblEmail.setText(R.string.email_not_set);
		}

		// Initialize name edit text field
		EditText txtName = (EditText)((Activity)this.context).findViewById(R.id.txtName);
		txtName.setTypeface(((MapplasApplication)this.context.getApplicationContext()).getTypeFace());
		txtName.setText(this.user.getName());

		// Initialize email edit text field
		EditText txtEmail = (EditText)((Activity)this.context).findViewById(R.id.txtEmail);
		txtEmail.setTypeface(((MapplasApplication)this.context.getApplicationContext()).getTypeFace());
		txtEmail.setText(this.user.getEmail());
	}

}
