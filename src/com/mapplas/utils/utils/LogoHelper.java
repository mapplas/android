package com.mapplas.utils.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import app.mapplas.com.R;

import com.mapplas.model.App;
import com.mapplas.model.Constants;
import com.mapplas.utils.cache.CacheFolderFactory;
import com.mapplas.utils.cache.ImageFileManager;
import com.mapplas.utils.network.async_tasks.LoadImageTask;
import com.mapplas.utils.network.async_tasks.TaskAsyncExecuter;


public class LogoHelper {
	
	private Context context;
	
	public LogoHelper(Context context) {
		this.context = context;
	}

	public void setLogoToApp(App app, ImageView logo_place) {
		ImageFileManager imageFileManager = new ImageFileManager();
		String logoUrl = app.getAppLogo();

		if (app.getAppType() == Constants.MAPPLAS_APPLICATION_TYPE_HTML5) {
			
			Drawable image = this.context.getResources().getDrawable(R.drawable.ic_template);
			Bitmap bitmap = Bitmap.createBitmap(image.getMinimumWidth(), image.getMinimumHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			
			if(existsColor(app.getIcon_color_1())) {
				canvas.drawColor(Color.parseColor(app.getIcon_color_1()));
				this.checkNumberOfColoursAndDraw(app, canvas);
			}
			else {
				canvas.drawColor(Color.TRANSPARENT);
			}
			
			logo_place.setImageBitmap(bitmap);
		}
		else {
			logo_place.setColorFilter(null);
			
			if(!logoUrl.equals("")) {
				if(imageFileManager.exists(new CacheFolderFactory(this.context).create(), logoUrl)) {
					logo_place.setImageBitmap(imageFileManager.load(new CacheFolderFactory(this.context).create(), logoUrl));
				}
				else {
					TaskAsyncExecuter imageRequest = new TaskAsyncExecuter(new LoadImageTask(this.context, logoUrl, logo_place, imageFileManager));
					imageRequest.execute();
				}
			}

		}
	}

	private void checkNumberOfColoursAndDraw(App app, Canvas canvas) {
		
		if(existsColor(app.getIcon_color_2())) {
			
			if(existsColor(app.getIcon_color_3())) {
				
				if(existsColor(app.getIcon_color_4())) {
					this.draw4ColouredCanvas(app, canvas);
				}
				else {
					this.draw3ColouredCanvas(app, canvas);
				}
			}
			else {
				this.draw2ColouredCanvas(app, canvas);
			}
		}
	}
	
	private void draw2ColouredCanvas(App app, Canvas canvas) {
		Paint p = new Paint();
		p.setColor(Color.parseColor(app.getIcon_color_2()));
		canvas.drawRoundRect(new RectF(canvas.getWidth()/2 - 30, canvas.getHeight()/2 - 30, canvas.getWidth()/2 + 30, canvas.getHeight()/2 + 30), 10, 10, p);
	}

	private void draw3ColouredCanvas(App app, Canvas canvas) {
		Paint p = new Paint();
		p.setColor(Color.parseColor(app.getIcon_color_2()));
		p.setAntiAlias(true);
		p.setStrokeWidth(50);
		canvas.drawLine(-canvas.getHeight()/4*3, -canvas.getWidth()/4*3, canvas.getWidth()/4*5, canvas.getHeight()/4*5, p);
	
		Paint p2 = new Paint();
		p2.setColor(Color.parseColor(app.getIcon_color_3()));
		p2.setAntiAlias(true);
		p2.setStrokeWidth(50);
		canvas.drawLine(-canvas.getHeight()/4*3, -(canvas.getWidth()/4*3)+50, canvas.getWidth()/4*5, (canvas.getHeight()/4*5)+50, p2);
	}

	private void draw4ColouredCanvas(App app, Canvas canvas) {
		Paint p = new Paint();
		p.setColor(Color.parseColor(app.getIcon_color_2()));
		p.setAntiAlias(true);					
		canvas.drawCircle(-5, -5, 150, p);
	
		Paint p2 = new Paint();
		p2.setColor(Color.parseColor(app.getIcon_color_3()));
		p2.setAntiAlias(true);					
		canvas.drawCircle(-5, -5, 100, p2);
	
		Paint p3 = new Paint();
		p3.setColor(Color.parseColor(app.getIcon_color_4()));
		p3.setAntiAlias(true);					
		canvas.drawCircle(-5, -5, 50, p3);
	}

	private boolean existsColor(String color) {
		if(!color.equals("") && !color.equals("null")) {
			return true;
		}
		else {
			return false;
		}
	}

}
