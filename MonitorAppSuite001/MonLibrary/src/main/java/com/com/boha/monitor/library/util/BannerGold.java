package com.com.boha.monitor.library.util;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.boha.monitor.library.R;

public class BannerGold extends View {

	  private final Drawable logo;

	 
	public BannerGold(Context context, int id) {
	    super(context);
	    logo = context.getResources().getDrawable(R.drawable.banner_gold2);
	    setBackground(logo);
	  }

	 
	public BannerGold(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    logo = context.getResources().getDrawable(R.drawable.banner_gold2);
        setBackground(logo);
	  }

	 
	public BannerGold(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	    logo = context.getResources().getDrawable(R.drawable.banner_gold2);
        setBackground(logo);
	  }

	  @Override protected void onMeasure(int widthMeasureSpec,
	      int heightMeasureSpec) {
	    int width = MeasureSpec.getSize(widthMeasureSpec);
	    int height = width * logo.getIntrinsicHeight() / logo.getIntrinsicWidth();
	    setMeasuredDimension(width, height);
	  }
	}