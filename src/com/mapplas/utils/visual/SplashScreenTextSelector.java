package com.mapplas.utils.visual;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import android.content.Context;
import app.mapplas.com.R;

import com.mapplas.model.Constants;
import com.mapplas.utils.language.LanguageSetter;
import com.mapplas.utils.visual.custom_views.RobotoTextView;

public class SplashScreenTextSelector {

	private RobotoTextView mainText;

	private RobotoTextView authorText;

	private Context context;

	public SplashScreenTextSelector(RobotoTextView mainText, RobotoTextView authorText, Context context) {
		this.mainText = mainText;
		this.authorText = authorText;
		this.context = context;
	}

	public void setRandomText() {
		String language_constant = new LanguageSetter(this.context).getLanguageConstantFromPhone();
		String line = "";
		if(language_constant.equals(Constants.SPANISH) || language_constant.equals(Constants.BASQUE)) {
			InputStream rawFile = this.context.getResources().openRawResource(R.raw.cites_es);
			line = this.randomInFile(rawFile);
		}
		else {
			InputStream rawFile = this.context.getResources().openRawResource(R.raw.cites_en);
			line = this.randomInFile(rawFile);
		}

		String[] splittedLine = line.split("\\|");
		this.mainText.setText(splittedLine[0]);
		this.authorText.setText(splittedLine[1]);
	}

	private String randomInFile(InputStream file) {

		InputStreamReader inputreader = new InputStreamReader(file);
		BufferedReader buffreader = new BufferedReader(inputreader);

		Random random = new Random();
		int lines = this.numberOfLines(inputreader);
		int randomNum = random.nextInt(lines);

		// Reset buffer reader
		try {
			file.reset();
			buffreader = new BufferedReader(new InputStreamReader(file));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String line;
		int i = 0;

		try {
			while ((line = buffreader.readLine()) != null) {
				if(i != randomNum) {
					i++;
				}
				else {
					return line;
				}
			}
		} catch (IOException e) {
			return "";
		}
		return "";
	}

	private int numberOfLines(InputStreamReader isr) {
		int lineCtr = 0;
		try {
			BufferedReader br = new BufferedReader(isr);

			@SuppressWarnings("unused")
			String theLine = "";
			lineCtr = 0;
			while ((theLine = br.readLine()) != null) {
				lineCtr++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lineCtr;
	}
}
