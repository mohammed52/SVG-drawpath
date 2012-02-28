package com.svg.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.svg.test.model.Route;

public class MyImageView extends ImageView implements OnTouchListener{
	private static String TAG = "SG Malls ImageView";
	private float scale = 1.f;
	private float totalScale = 1;
	private float[] f;
	
    static final int NONE = 0;
    static final int ZOOM = 1;
    static final int DRAG =2;
    int mode = NONE;

    float oldDist = 1f;

	PointF mid = new PointF();
	
	private float startX;
	private float startY;

	private float midX;
	private float midY;
	
	private Matrix matrix;
	private Matrix savedMatrix;
	///////Routing
	private ArrayList<Route> routes; 
	Integer imageWidth;
	Integer imageHeight;
	
	public MyImageView(Context context) {

		super(context);
		Log.d(TAG, "ImageView Constructor");
        init(context);
		this.setBackgroundColor(Color.WHITE);
		setOnTouchListener(this);
	}

	public MyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
	protected void init(Context context){
		Log.d(TAG, "init");
		matrix = new Matrix();
		savedMatrix = new Matrix();
		
		f = new float[9];
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.d(TAG, "onDraw");
		super.onDraw(canvas);
//		addRoutesInSvg();
		try {
			FileInputStream fstream = new FileInputStream("/sdcard/FileWriterTest.svg");
		    // Parse the SVG file from the resource
//		    SVG svg = SVGParser.getSVGFromResource(getResources(), R.raw.example_map);
		    
		    SVG svg = SVGParser.getSVGFromInputStream(fstream);
		    // Get the picture
		    Picture picture = svg.getPicture();
//		    
//		    this.imageHeight=picture.getHeight();
//		    this.imageWidth=picture.getWidth();
//		    Log.d(TAG, "Width Heigth: "+this.imageHeight +" "+this.imageWidth);
		    canvas.setMatrix(matrix);
//			Log.d(TAG, "onDraw->setMatrix");
		    canvas.drawPicture(picture);
//			Log.d(TAG, "onDraw->drawPicture");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		drawRoutes(canvas); 
	}
//	private void drawRoutes(Canvas canvas) {
//		if(routes!=null){
//	    	
//	    	Paint p = new Paint();
//		    p.setColor (Color.BLUE);
//		    p.setStrokeWidth(3);
//	    	for(int i=0; i<routes.size(); i++){
//			    Route r = routes.get(i);
//			    float fromX = (float) (r.fromX*this.imageWidth);
//			    float fromY = (float) (r.fromY*this.imageHeight);
//			    
//			    float toX = (float) (r.toX*this.imageWidth);
//			    float toY = (float) (r.toY*this.imageHeight);
//			    canvas.drawLine(fromX, fromY, toX, toY, p);
//	    	}
//		}
//		
//	}

	@Override
	public boolean isInEditMode() {
		Log.d(TAG, "isInitMode");
		return super.isInEditMode();
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.d(TAG, "onTouch");
		 switch (event.getAction() & MotionEvent.ACTION_MASK) {
		 case MotionEvent.ACTION_DOWN:
			 Log.d(TAG, "mode=DRAG");
			 savedMatrix.set(matrix);
			 startX = event.getX();
			 startY = event.getY();
			 mode=DRAG;
			 break;
		 case MotionEvent.ACTION_POINTER_DOWN:
			 oldDist = spacing(event);
			 if (oldDist > 10f) {
				 savedMatrix.set(matrix);
			     midX = ( event.getX(0) + event.getX(1)) /2;
			     midY = ( event.getY(0) + event.getY(1)) /2;
				 mode = ZOOM;
				 
			 }
			 break;
		 case MotionEvent.ACTION_UP:
		 case MotionEvent.ACTION_POINTER_UP:
			 mode = NONE;
			 break;
			case MotionEvent.ACTION_MOVE:
				if (mode == DRAG) {
					matrix.set(savedMatrix);
					matrix.postTranslate(event.getX() - startX,
							event.getY() - startY);
				}
				else if (mode == ZOOM) {
					float newDist = spacing(event);
					if (newDist > 10f) {
						matrix.set(savedMatrix);
						scale = newDist / oldDist;
						matrix.postScale(scale, scale, midX, midY);
						matrix.getValues(f);
						totalScale = f[Matrix.MSCALE_X];
					}
				}
				invalidate();
			 break;
		 }

		 return true;
	}
	
	private float spacing(MotionEvent event) {
		Log.d(TAG, "spacing");
		   float x = event.getX(0) - event.getX(1);
		   float y = event.getY(0) - event.getY(1);
		   return FloatMath.sqrt(x * x + y * y);
		}

	public void setRoute (ArrayList<Route> routes)
	{   
	    this.routes = routes;
	}


	
}
