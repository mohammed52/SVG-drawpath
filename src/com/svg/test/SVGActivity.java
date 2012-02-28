package com.svg.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Picture;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.svg.test.model.Route;

public class SVGActivity extends Activity {

	private final String TAG = "SG Malls";
	private int imageHeight;
	private int imageWidth;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ArrayList<Route> r1  = new ArrayList<Route>();
        r1.add(new Route(.2, .2, .3, .5));
      	r1.add(new Route(.3, .5, .7, .6));
      	r1.add(new Route(.7, .6, .8, .2));
      	r1.add(new Route(0, 0, 1, 0));
      	r1.add(new Route(1, 0, 1, 1));
      	r1.add(new Route(1, 1, 0, 1));
      	r1.add(new Route(0, 1, 0, 0));
      	
      	addRoutesInSvg(r1);
//        setContentView(R.layout.main);
//        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        //container.addView(i);
        MyImageView imageView =new MyImageView(this);
        setContentView(imageView);

    }
	public void addRoutesInSvg(ArrayList<Route> routes) {
		SVG svg1 = SVGParser.getSVGFromResource(getResources(), R.raw.example_map);
		
		Picture picture1 = svg1.getPicture();
	    
	    this.imageHeight=picture1.getHeight();
	    this.imageWidth=picture1.getWidth();
		InputStream inputStream = this.getResources().openRawResource(R.raw.example_map);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
         String line;
         StringBuilder text = new StringBuilder();
         try {
           while (( line = buffreader.readLine()) != null) {
        	   
//        		Log.d(TAG, "Line: "+count++ +": "+line);
	           if(line.contains("</svg>")){
		           Log.d(TAG, "found closing tag");
		           String pathSvgString="";
		           for(int i=0; i<routes.size(); i++){
		        	   Route r = routes.get(i);
	       			    float fromX = (float) (r.fromX*imageWidth);
	       			    float fromY = (float) (r.fromY*imageHeight);
	       			    
	       			    float toX = (float) (r.toX*imageWidth);
	       			    float toY = (float) (r.toY*imageHeight);
	       			    pathSvgString = pathSvgString+"<line x1=\""+fromX+"\" y1=\""+fromY+"\" x2=\""+toX+"\" y2=\""+toY+"\" style=\"stroke-dasharray: 0;stroke:#003399;stroke-width:3;\"/>\n";
	       			 
//	       			    Log.d(TAG, "Path Added: "+"<line x1=\""+fromX+"\" y1=\""+fromY+"\" x2=\""+toX+"\" y2=\""+toY+"\" style=\"stroke-width:1;stroke:#003399;\"/>\n");
		           }
		           Log.d(TAG, "line before editing: "+line);
		           
		           line = line.replace("</svg>", pathSvgString+"</svg>");
		           Log.d(TAG, "line after editing: "+line);
               }
        	   text.append(line+'\n');
             }
       } catch (IOException e) {
       }
         
		File file = new File("/sdcard/FileWriterTest.svg");
	     try {
	    	 if(file.exists())file.delete();
	       FileWriter writer = new FileWriter(file ,true);
	       writer.write(text.toString());
	       writer.flush();
	       writer.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
		
	}
}