package com.com.boha.monitor.library.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FontUtil 
{

	private static Typeface typeFace;
	
	public static final String REGULAR = "roboto-regular";
	public static final String BOLD = "roboto-bold";
	public static final String ITALIC = "roboto-italic";
	public static final String MONS_BOLD = "mons-bold";
	public static final String MONS_REG = "mons_regular";

	public static void setCustomTypeface(Context context, View view, String fontType) 
	{
		if (typeFace == null) 
		{
			// Normal, Italic and Bold
			if(fontType.equals(REGULAR))
			{
				typeFace = Typeface.createFromAsset(context.getAssets(),
					"fonts/Roboto/Roboto-Regular.ttf");
			}
			else if(fontType.equals(ITALIC))
			{
				typeFace = Typeface.createFromAsset(context.getAssets(),
					"fonts/Roboto/Roboto-Italic.ttf");
			}
			else if(fontType.equals(BOLD))
			{
				typeFace = Typeface.createFromAsset(context.getAssets(),
					"fonts/Roboto/Roboto-Bold.ttf");
			}
			else if(fontType.equals(MONS_BOLD))
			{
				typeFace = Typeface.createFromAsset(context.getAssets(),
					"fonts/Montserrat/Montserrat-Bold.ttf");
			}
			else if(fontType.equals(MONS_REG))
			{
				typeFace = Typeface.createFromAsset(context.getAssets(),
					"fonts/Montserrat/Montserrat-Regular.ttf");
			}
			
		}
		
		setFont(view, typeFace);
	}

	private static void setFont(View view, Typeface typeFace) {
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				setFont(((ViewGroup) view).getChildAt(i), typeFace);
			}
		} else if (view instanceof TextView) {
			((TextView) view).setTypeface(typeFace);
		}
	}

}
