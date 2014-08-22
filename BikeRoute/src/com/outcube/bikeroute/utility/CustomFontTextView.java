package com.outcube.bikeroute.utility;

import com.outcube.bikeroute.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomFontTextView extends TextView {

	public CustomFontTextView(Context context) {
		super(context);
	}
	public CustomFontTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(attrs);
	}
	public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(attrs);
	}
	
	private void initialize(AttributeSet attrs){
		if(attrs != null){
			TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);
			String fontName = a.getString(R.styleable.CustomFontTextView_fontName);
			if (fontName!=null) {
				 Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/"+fontName);
				 setTypeface(myTypeface);
			}
			a.recycle();
		}
	}

}
