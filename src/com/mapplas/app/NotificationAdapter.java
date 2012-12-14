package com.mapplas.app;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import app.mapplas.com.R;

import com.mapplas.app.activities.AppDetail;
import com.mapplas.app.activities.MapplasActivity;
import com.mapplas.app.application.MapplasApplication;
import com.mapplas.model.AppNotification;
import com.mapplas.model.Constants;
import com.mapplas.utils.DateUtils;

public class NotificationAdapter extends ArrayAdapter<AppNotification> {

	private ArrayList<AppNotification> items;

	private Context context = null;

	public NotificationAdapter(Context context, int textViewResourceId, ArrayList<AppNotification> items) {
		super(context, textViewResourceId, items);
		this.items = items;

		this.context = context;
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.rownot, null);
                
            }
            
            final AppNotification o = items.get(position);
            if (o != null) {
            	int posList = MapplasActivity.getLocalizationAdapter().getPosition(o.getAuxLocalization());
            	v.setTag(posList);
            	
            	Typeface normalTypeface = ((MapplasApplication)getContext().getApplicationContext()).getTypeFace();
            	
            	TextView tt = (TextView) v.findViewById(R.id.lblTitle);
            	tt.setText(o.getName());
            	tt.setTypeface(normalTypeface);
            	
            	tt = (TextView) v.findViewById(R.id.lblDescription);
            	tt.setTypeface(normalTypeface);
            	tt.setText(o.getDescription());
	           
            	tt = (TextView) v.findViewById(R.id.lblDate);
            	tt.setTypeface(normalTypeface);
            	tt.setText(DateUtils.FormatSinceDate(o.getDate(), o.getHour(), this.context));
            	
        		ImageView iv = (ImageView) v.findViewById(R.id.imgLogo);

        		Bitmap bmp = null;
        		
        		MapplasActivity.getDbd().loadDrawable(o.getAuxLocalization().getAppLogo(), iv, this.context.getResources().getDrawable(R.drawable.ic_refresh));
        		
        		
        		v.setOnClickListener(new View.OnClickListener() {
        			
        			@Override
        			public void onClick(View v) {

        				Intent intent = new Intent(context, AppDetail.class);
        				intent.putExtra(Constants.MAPPLAS_DETAIL_APP, (int)((Integer) v.getTag()));
        				((MapplasActivity)context).startActivityForResult(intent, Constants.SYNESTH_DETAILS_ID);
        			}
        		});
        		
        		
        		// TODO: Carga asincrona de las im�genes en un hilo
        		
        		
        		
        		
            }
            
            /*
            ViewGroup.LayoutParams params = v.getLayoutParams();
            TextView tv = (TextView) v.findViewById(R.id.lblDescription);
            ViewGroup.LayoutParams params2 = tv.getLayoutParams();
            v.setLayoutParams(new ViewGroup.LayoutParams(params2.width, 20 + params2.height));
            */
            return v;
    }
}
