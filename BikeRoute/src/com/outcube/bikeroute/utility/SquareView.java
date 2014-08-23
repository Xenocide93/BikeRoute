package com.outcube.bikeroute.utility;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class SquareView extends View {

	public SquareView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if(widthMeasureSpec < heightMeasureSpec) super.onMeasure(widthMeasureSpec, widthMeasureSpec);
		else super.onMeasure(heightMeasureSpec, heightMeasureSpec);
	}
}
