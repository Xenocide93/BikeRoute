package com.outcube.bikeroute.utility;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.Toast;

public class SquareImageButton extends ImageButton {
	private int size = -1;
	
	
	public SquareImageButton(Context context) {
		super(context);
	}
	public SquareImageButton(Context context, int imageResourceId) {
		super(context);
		this.setImageResource(imageResourceId);
	}
	public SquareImageButton(Context context, int imageResourceId, int size) {
		super(context);
		this.setImageResource(imageResourceId);
		this.size = size;
	}
	public SquareImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setSize(int size){
		this.size = size;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		if(size != -1){widthMeasureSpec = size; heightMeasureSpec = size;}
		widthMeasureSpec -= (getPaddingLeft()+getPaddingRight());
		heightMeasureSpec -= (getPaddingBottom()+getPaddingTop());
		if(widthMeasureSpec <= heightMeasureSpec){
			super.onMeasure(widthMeasureSpec, widthMeasureSpec);
		} else super.onMeasure(heightMeasureSpec, heightMeasureSpec);
	}
}
