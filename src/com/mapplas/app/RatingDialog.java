package com.mapplas.app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import app.mapplas.com.R;

public class RatingDialog extends Dialog {

    public interface ReadyListener {
        public void ready(String name);
    }

    private String name;
    private ReadyListener readyListener;
    RatingBar mRatingBar;
    float mRate = 0;
    String mtxtComment = "";
    
    public static EditText mComment = null;

    public RatingDialog(Context context, String name, ReadyListener readyListener, float rate, String comment) {
        super(context);
        this.name = name;
        this.readyListener = readyListener;
        this.mtxtComment = comment;
        this.mRate = rate;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rating_dialog);
        
        setTitle(R.string.rating_rate);
        
        EditText txt = (EditText) findViewById(R.id.txtComment);
        
        final RatingBar rbRating = (RatingBar) findViewById(R.id.rbRating);
		final TextView lblRating = (TextView) findViewById(R.id.lblRating);
		
		
		rbRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				if(rating == 0)
				{
					lblRating.setText(R.string.unRated);
				}else
				{
					int auxCase = (int) Math.ceil(rating);
					switch(auxCase)
					{
					case 1:
						lblRating.setText(R.string.poor);
						break;
						
					case 2:
						lblRating.setText(R.string.belowAvg);
						break;
						
					case 3: 
						lblRating.setText(R.string.average);
						break;
						
					case 4:
						lblRating.setText(R.string.aboveAvg);
						break;
						
					case 5:
						lblRating.setText(R.string.excellent);
						break;
					}
				}
			}
		});
		
        
        RatingDialog.mComment = txt;
        txt.setText(this.mtxtComment);
        
        Button buttonOK = (Button) findViewById(R.id.btnAccept);
        buttonOK.setOnClickListener(new OKListener());
        
        Button buttonKO = (Button) findViewById(R.id.btnCancel);
        buttonKO.setOnClickListener(new KOListener());
        
        mRatingBar = (RatingBar) findViewById(R.id.rbRating);
        mRatingBar.setRating(this.mRate);
    }

    private class OKListener implements android.view.View.OnClickListener {
        @Override
        public void onClick(View v) {

            readyListener.ready(String.valueOf(mRatingBar.getRating()) + "|" + RatingDialog.mComment.getText());
            
            RatingDialog.this.dismiss();
        }
    }
    
    private class KOListener implements android.view.View.OnClickListener {
        @Override
        public void onClick(View v) {
            readyListener.ready("CANCEL");
            
            RatingDialog.this.dismiss();
        }
    }

}