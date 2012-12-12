package com.synesth.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import app.synesth.com.R;

public class DateUtils {
	
	public static int Hours(String hour)
	{
		int ret = 0;
		
		try
		{
			ret = Integer.parseInt(hour.substring(0, hour.indexOf(':') - 1));
		}catch (Exception exc)
		{
			
		}
		
		return ret;
	}
	
	public static int Minutes(String hour)
	{
		int ret = 0;
		
		try
		{
			ret = Integer.parseInt(hour.substring(hour.indexOf(':') + 1));
		}catch (Exception exc)
		{
			
		}
		
		return ret;
	}
	
	public static String FormatSinceDate(String date, String hour, Context context)
	{
		String ret = date + " " + hour; // Fallback
		int aux = 0;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		Date now = new Date();
		Date past;
		try {
			 past = df.parse(date + "T" + hour);
			 
			 if(now.getYear() > past.getYear())
			 {
				 // Hace X años
				 aux = now.getYear() - past.getYear();
				 if(aux == 1)
				 {
					 ret = aux + " " + context.getString(R.string.year);
				 }else
				 {
					 ret = aux + " " + context.getString(R.string.years);
				 }
			 }else if(now.getMonth() > past.getMonth())
			 {
				 // Hace X meses
				 aux = now.getMonth() - past.getMonth();
				 if(aux == 1)
				 {
					 ret = aux + " " + context.getString(R.string.month);
				 }else
				 {
					 ret = aux + " " + context.getString(R.string.months);
				 }
			 }else if(now.getDate() > past.getDate())
			 {
				 // Hace X dias
				 aux = now.getDate() - past.getDate();
				 if(aux == 1)
				 {
					 ret = aux + " " + context.getString(R.string.day);
				 }else
				 {
					 ret = aux + " " + context.getString(R.string.days);
				 }
			 }else if(now.getHours() > past.getHours())
			 {
				 // Hace X horas
				 aux = now.getHours() - past.getHours();
				 if(aux == 1)
				 {
					 ret = aux + " " + context.getString(R.string.hour);
				 }else
				 {
					 ret = aux + " " + context.getString(R.string.hours);
				 }
			 }else
			 {
				 // Hace X minutos
				 aux = now.getMinutes() - past.getMinutes();
				 if(aux == 1)
				 {
					 ret = aux + " " + context.getString(R.string.minute);
				 }else
				 {
					 ret = aux + " " + context.getString(R.string.minutes);
				 }
			 }
				 
			 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int m = DateUtils.Minutes(hour);
		int h = DateUtils.Hours(hour);
		
		
		
		return ret;
	}
}
