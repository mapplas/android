package com.synesth.app;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import app.synesth.com.R;

import com.synesth.model.Comment;
import com.synesth.utils.DateUtils;

public class CommentAdapter extends ArrayAdapter<Comment> {

    private ArrayList<Comment> items;
    private Context mContext = null;
    
    
    public CommentAdapter(Context context, int textViewResourceId, ArrayList<Comment> items) {
            super(context, textViewResourceId, items);
            this.items = items;
            
            this.mContext = context;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.rowcom, null);
                
            }
            
            final Comment o = items.get(position);
            if (o != null) {
            	v.setTag(position);
            	
            	TextView tt = (TextView) v.findViewById(R.id.lblDate);
            	tt.setText(DateUtils.FormatSinceDate(o.getDate(), o.getHour(), this.mContext));
            	tt.setTypeface(SynesthActivity.getTypeFace());
            	
            	tt = (TextView) v.findViewById(R.id.lblComment);
            	tt.setTypeface(SynesthActivity.getTypeFace());
            	tt.setText(o.getComment());
            	
            	// Modificamos las estrellas y los iconos
            	/*
                float r = o.getRate();
                ImageView img1 = (ImageView) v.findViewById(R.id.imgStar1);
                ImageView img2 = (ImageView) v.findViewById(R.id.imgStar2);
                ImageView img3 = (ImageView) v.findViewById(R.id.imgStar3);
                ImageView img4 = (ImageView) v.findViewById(R.id.imgStar4);
                ImageView img5 = (ImageView) v.findViewById(R.id.imgStar5);
                if(r >= 0.5f)
                {
                	img1.setBackgroundResource(R.drawable.icon_half_important);
                	img2.setBackgroundResource(R.drawable.icon_not_important);
                	img3.setBackgroundResource(R.drawable.icon_not_important);
                	img4.setBackgroundResource(R.drawable.icon_not_important);
                	img5.setBackgroundResource(R.drawable.icon_not_important);
                }
                if(r >= 1.0f)
                {
                	img1.setBackgroundResource(R.drawable.icon_important);
                	img2.setBackgroundResource(R.drawable.icon_not_important);
                	img3.setBackgroundResource(R.drawable.icon_not_important);
                	img4.setBackgroundResource(R.drawable.icon_not_important);
                	img5.setBackgroundResource(R.drawable.icon_not_important);
                }
                if(r >= 1.5f)
                {
                	img1.setBackgroundResource(R.drawable.icon_important);
                	img2.setBackgroundResource(R.drawable.icon_half_important);
                	img3.setBackgroundResource(R.drawable.icon_not_important);
                	img4.setBackgroundResource(R.drawable.icon_not_important);
                	img5.setBackgroundResource(R.drawable.icon_not_important);
                }
                if(r >= 2.0f)
                {
                	img1.setBackgroundResource(R.drawable.icon_important);
                	img2.setBackgroundResource(R.drawable.icon_important);
                	img3.setBackgroundResource(R.drawable.icon_not_important);
                	img4.setBackgroundResource(R.drawable.icon_not_important);
                	img5.setBackgroundResource(R.drawable.icon_not_important);
                }
                if(r >= 2.5f)
                {
                	img1.setBackgroundResource(R.drawable.icon_important);
                	img2.setBackgroundResource(R.drawable.icon_important);
                	img3.setBackgroundResource(R.drawable.icon_half_important);
                	img4.setBackgroundResource(R.drawable.icon_not_important);
                	img5.setBackgroundResource(R.drawable.icon_not_important);
                }
                if(r >= 3.0f)
                {
                	img1.setBackgroundResource(R.drawable.icon_important);
                	img2.setBackgroundResource(R.drawable.icon_important);
                	img3.setBackgroundResource(R.drawable.icon_important);
                	img4.setBackgroundResource(R.drawable.icon_not_important);
                	img5.setBackgroundResource(R.drawable.icon_not_important);
                }
                if(r >= 3.5f)
                {
                	img1.setBackgroundResource(R.drawable.icon_important);
                	img2.setBackgroundResource(R.drawable.icon_important);
                	img3.setBackgroundResource(R.drawable.icon_important);
                	img4.setBackgroundResource(R.drawable.icon_half_important);
                	img5.setBackgroundResource(R.drawable.icon_not_important);
                }
                if(r >= 4.0f)
                {
                	img1.setBackgroundResource(R.drawable.icon_important);
                	img2.setBackgroundResource(R.drawable.icon_important);
                	img3.setBackgroundResource(R.drawable.icon_important);
                	img4.setBackgroundResource(R.drawable.icon_important);
                	img5.setBackgroundResource(R.drawable.icon_not_important);
                }
                if(r >= 4.5f)
                {
                	img1.setBackgroundResource(R.drawable.icon_important);
                	img2.setBackgroundResource(R.drawable.icon_important);
                	img3.setBackgroundResource(R.drawable.icon_important);
                	img4.setBackgroundResource(R.drawable.icon_important);
                	img5.setBackgroundResource(R.drawable.icon_half_important);
                }
                if(r >= 5.0f)
                {
                	img1.setBackgroundResource(R.drawable.icon_important);
                	img2.setBackgroundResource(R.drawable.icon_important);
                	img3.setBackgroundResource(R.drawable.icon_important);
                	img4.setBackgroundResource(R.drawable.icon_important);
                	img5.setBackgroundResource(R.drawable.icon_important);
                }
	         
        		ImageView iv = (ImageView) v.findViewById(R.id.imgLogo);

        		Bitmap bmp = null;
        		
        		if(!o.getAuxLocalization().isInternalLogoLoaded() && o.getAuxLocalization().getAppLogo() != "")
        		{
        			String strUrl = o.getAuxLocalization().getAppLogo();
        			bmp = ImageRepository.Find(strUrl);
        			
        			if(bmp == null)
        			{
        				try
        				{
			            	URL url = new URL(strUrl);
			            	bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			            	ImageRepository.Store(strUrl, bmp);
        				}catch (Exception e)
        				{
        					// no hacemos nada
        				}
        			}
	            	iv.setImageBitmap(bmp);
	            	o.getAuxLocalization().setInternalLogoLoaded(true);
	            	o.getAuxLocalization().setInternalLogo(bmp);
        		}else
        		{
        			bmp = o.getAuxLocalization().getInternalLogo();
        			if(bmp != null)
        			{
        				iv.setImageBitmap(bmp);
        			}else
        			{
        				o.getAuxLocalization().setInternalLogoLoaded(false);
        			}
        		}
        		
        		  */
            }
            
            //v.setlayoutparams(new listview.setlayoutparams(width, height));
            
            return v;
    }
}
