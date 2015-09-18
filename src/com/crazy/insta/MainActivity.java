//Umang Agarwal

package com.crazy.insta;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

   
	private static final int SELECTED_PICTURE = 1;
	ImageView iv;
	boolean flagg = false;
	Bitmap yourSelectedImage;
	Bitmap newimg;
	Bitmap resized;
	private Context TheThis;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        iv = (ImageView)findViewById(R.id.imageView1);
        iv.setImageResource(R.drawable.imagickback);
        flagg=false;
	


        iv.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent mev) {
                DecodeActionDownEvent(v, mev, newimg);
                return false;
            }

        });
     }

     private void DecodeActionDownEvent(View v, MotionEvent ev,Bitmap newimg )
     {
        Matrix inverse = new Matrix();
     //v.getMatrix().invert(inverse);
     float[] touchPoint = new float[] {ev.getX(), ev.getY()};
     //inverse.mapPoints(touchPoint);
     int xCoord = Integer.valueOf((int)touchPoint[0]);
     int yCoord = Integer.valueOf((int)touchPoint[1]);
     //Toast.makeText(TheThis, "touched"+xCoord+" "+yCoord, Toast.LENGTH_SHORT).show();
     drawonimage(v,newimg,xCoord,yCoord);
//        try {
//            
//        	btnClick6( v);
//        	Toast.makeText(TheThis, "touched"+xCoord+" "+yCoord, Toast.LENGTH_SHORT).show();
//        } catch (IllegalArgumentException e) {
//        	Toast.makeText(TheThis, "Open Image First", Toast.LENGTH_SHORT).show();
//        }
     }

     public void drawonimage(View v,Bitmap img,int a,int b){
 		//Bitmap wwimage;
 		newimg = draw_im(newimg,a,b);
 		iv.setImageBitmap(newimg);
 		//scaleImage(iv,300);
 	}
     
     
     public Bitmap draw_im(Bitmap img,int a,int b)
     {
    	 newimg = Bitmap.createBitmap(img.getWidth(),img.getHeight(),img.getConfig());
 		int A,R,G,B;
 		int pixelColor;
 		int height = img.getHeight();
 		int width = img.getWidth();
 		for(int y =0; y<height;y++)
 		{
 			for(int x=0;x<width;x++)
 			{ if ((x==a)&&(y==b)||(x==a+1)&&(y==b+1)||(x==a-1)&&(y==b-1)||(x==a+1)&&(y==b-1)||(x==a-1)&&(y==b+1)||(x==a)&&(y==b-1)||(x==a-1)&&(y==b)||(x==a+1)&&(y==b)||(x==a)&&(y==b+1))
 			{
 				pixelColor = img.getPixel(x,y);
 			A= Color.alpha(pixelColor);
 			R = 0;
 			G = 0;
 			B = 0;
 			newimg.setPixel(x,y,Color.argb(A,R,G,B));
 			}
 			else 
 			{pixelColor = img.getPixel(x,y);
 			A= Color.alpha(pixelColor);
 			R = Color.red(pixelColor);
 			G = Color.green(pixelColor);
 			B = Color.blue(pixelColor);
 			newimg.setPixel(x,y,Color.argb(A,R,G,B));}
 			
 			}
 		}
 	return newimg; 
     
     }
     
public void btnClick(View v){
	Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	startActivityForResult(i,SELECTED_PICTURE);
	flagg = true;
	}
	
	public void btnClick2(View v){
			if (flagg) {
			Bitmap wwimage;
			wwimage = invertfilter(resized);
			iv.setImageBitmap(wwimage);
			//scaleImage(iv,300);
			}
			else {Toast.makeText(TheThis, "Open Image First", Toast.LENGTH_SHORT).show(); return;}
			}
	
	public void btnClick3(View v){
		
		if (!flagg) {Toast.makeText(TheThis, "Open Image First", Toast.LENGTH_SHORT).show(); return;}
		SaveImage(this,newimg);
		
		
	}
	
	public void btnClick4(View v){
		if (!flagg) {Toast.makeText(TheThis, "Open Image First", Toast.LENGTH_SHORT).show(); return;}
		//flagg =1;
		Bitmap wwimage;
		wwimage = grayfilter(resized);
		iv.setImageBitmap(wwimage);
		//scaleImage(iv,300);
	}
	
	public void btnClick5(View v){
		if (!flagg) {Toast.makeText(TheThis, "Open Image First", Toast.LENGTH_SHORT).show(); return;}
		//flagg =1;
		Bitmap wwimage;
		wwimage = blackwhitefilter(resized);
		iv.setImageBitmap(wwimage);
	}
	
	
	public void btnClick6(View v){
		if (!flagg) {Toast.makeText(TheThis, "Open Image First", Toast.LENGTH_SHORT).show(); return;}
		//flagg =1;
		Bitmap wwimage;
		wwimage = fastblur(resized,12);
		iv.setImageBitmap(wwimage);
	}
	
	public Bitmap grayfilter(Bitmap img){
		
		newimg = Bitmap.createBitmap(img.getWidth(),img.getHeight(),img.getConfig());
		int A,R,G,B;
		int pixelColor;
		int height = img.getHeight();
		int width = img.getWidth();
		for(int y =0; y<height;y++)
		{
			for(int x=0;x<width;x++)
			{pixelColor = img.getPixel(x,y);
			A= Color.alpha(pixelColor);
			R = 255 - Color.red(pixelColor);
			G = 255 - Color.green(pixelColor);
			B = 255 - Color.blue(pixelColor);
			newimg.setPixel(x,y,Color.argb(A,R,G,B));
			}
		}
	return newimg;
	}
	
	public Bitmap invertfilter(Bitmap img){
		newimg = Bitmap.createBitmap(img.getWidth(),img.getHeight(),img.getConfig());
		int A,R,G,B;
		int pixelColor;
		int height = img.getHeight();
		int width = img.getWidth();
		for(int y =0; y<height;y++)
		{
			for(int x=0;x<width;x++)
			{pixelColor = img.getPixel(x,y);
			A = Color.alpha(pixelColor);
			R = Color.red(pixelColor);
			G = Color.green(pixelColor);
			B = Color.blue(pixelColor);
			R = G = B = (int)(0.299 * R + 0.587 * G + 0.114 * B);
			newimg.setPixel(x,y,Color.argb(A,R,G,B));
			}
		}
	return newimg;
	}
	
	public Bitmap blackwhitefilter(Bitmap img){
		newimg = Bitmap.createBitmap(img.getWidth(),img.getHeight(),img.getConfig());
		int A,R,G,B;
		int pixelColor;
		int height = img.getHeight();
		int width = img.getWidth();
		for(int y =0; y<height;y++)
		{
			for(int x=0;x<width;x++)
			{pixelColor = img.getPixel(x,y);
			A= Color.alpha(pixelColor);
			if(Color.red(pixelColor)>124) R = 255; else R = 0;
			if(Color.green(pixelColor)>124) G = 255; else G = 0;
			if(Color.blue(pixelColor)>124) B = 255; else B = 0;
			newimg.setPixel(x,y,Color.argb(A,R,G,B));
			}
		}
	return newimg;
	}
	
	
	public Bitmap fastblur(Bitmap sentBitmap, int radius) {

        
        newimg = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = newimg.getWidth();
        int h = newimg.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        newimg.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        newimg.setPixels(pix, 0, w, 0, 0, w, h);

        return (newimg);

    }
	
	
	private void scaleImage(ImageView view, int boundBoxInDp)
	{
	    // Get the ImageView and its bitmap
	    Drawable drawing = view.getDrawable();
	    Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();
	    
	    // Get current dimensions
	    int width = bitmap.getWidth();
	    int height = bitmap.getHeight();

	    // Determine how much to scale: the dimension requiring less scaling is
	    // closer to the its side. This way the image always stays inside your
	    // bounding box AND either x/y axis touches it.
	    float xScale = ((float) boundBoxInDp) / width;
	    float yScale = ((float) boundBoxInDp) / height;
	    float scale = (xScale <= yScale) ? xScale : yScale;

	    // Create a matrix for the scaling and add the scaling data
	    Matrix matrix = new Matrix();
	    matrix.postScale(scale, scale);

	    // Create a new bitmap and convert it to a format understood by the ImageView
	    Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	    BitmapDrawable result = new BitmapDrawable(scaledBitmap);
	    width = scaledBitmap.getWidth();
	    height = scaledBitmap.getHeight();

	    // Apply the scaled bitmap
	    //resized = Bitmap.createScaledBitmap(yourSelectedImage, width, height, true);
	    view.setImageDrawable(result);

	    // Now change ImageView's dimensions to match the scaled image
	    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
	    params.width = width;
	    params.height = height;
	    view.setLayoutParams(params);
	}

		
	    private String NameOfFolder = "/Imagick";
	    private String NameOfFile   = "Image";
	 
	    public void SaveImage(Context context,Bitmap ImageToSave){
	        TheThis = context;
	        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath()+ NameOfFolder;
	        String CurrentDateAndTime= getCurrentDateAndTime();
	        File dir = new File(file_path);
	         
	        if(!dir.exists()){
	            dir.mkdirs();
	        }
	         
	        File file = new File(dir, NameOfFile +CurrentDateAndTime+ ".jpg");
	         
	        try {
	            FileOutputStream fOut = new FileOutputStream(file);
	            ImageToSave.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
	            fOut.flush();
	            fOut.close();
	            MakeSureFileWasCreatedThenMakeAvabile(file);
	            AbleToSave();
	             
	        } 
	        catch (FileNotFoundException e) {UnableToSave();}
	        catch (IOException e){UnableToSave();}
	         
	         
	          
	    }
	 
	     
	 
	    private void MakeSureFileWasCreatedThenMakeAvabile(File file) {
	        MediaScannerConnection.scanFile(TheThis,
	                new String[] { file.toString() }, null,
	                new MediaScannerConnection.OnScanCompletedListener() {
	            public void onScanCompleted(String path, Uri uri) {
	                Log.e("ExternalStorage", "Scanned " + path + ":");
	                Log.e("ExternalStorage", "-> uri=" + uri);
	                
	            }
	        });
	         
	    }
	 
	 
	 
	    private String getCurrentDateAndTime() {
	        Calendar c = Calendar.getInstance();
	        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	        String formattedDate = df.format(c.getTime());
	        return formattedDate;
	    }
	     
	     
	private void UnableToSave() {
	    Toast.makeText(TheThis, "Picture cannot to gallery", Toast.LENGTH_SHORT).show();
	         
	    }
	 
	private void AbleToSave() {
	    Toast.makeText(TheThis, "Image saved", Toast.LENGTH_SHORT).show();
	         
	    }
	
	private int dpToPx(int dp)
	{
	    float density = getApplicationContext().getResources().getDisplayMetrics().density;
	    return Math.round((float)dp * density);
	}
	
	public static int convertPixelsToDp(float px){
	    DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    return Math.round(dp);
	}
	
	private static Bitmap codec(Bitmap src, Bitmap.CompressFormat format,
			int quality) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		src.compress(format, quality, os);
 
		byte[] array = os.toByteArray();
		return BitmapFactory.decodeByteArray(array, 0, array.length);
	}
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Display display = getWindowManager().getDefaultDisplay();
	    Point size = new Point();
	    display.getSize(size);
	    int swidth = size.x;
	    int sheight = size.y;
	    int sq = convertPixelsToDp(swidth);
	    switch(requestCode){
	    case SELECTED_PICTURE:
	    		if(resultCode==RESULT_OK){
	    			Uri uri=data.getData();
	    			String[]projection={MediaStore.Images.Media.DATA};
	    			Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
	    			cursor.moveToFirst();
	    			int columnIndex = cursor.getColumnIndex(projection[0]);
	    			String filePath = cursor.getString(columnIndex);
	    			cursor.close();
	    			yourSelectedImage=BitmapFactory.decodeFile(filePath);
	    		//	Bitmap bb = codec(yourSelectedImage, Bitmap.CompressFormat.JPEG,10);  //3 to 80
	    			int ratio = 0 ;
	    			if((yourSelectedImage.getWidth()) > (yourSelectedImage.getHeight())) 
	    				
	    			{
	    			  ratio = (yourSelectedImage.getWidth() / sq);
	    			}
	    			else ratio = (yourSelectedImage.getHeight() / sq);
	    			int nwidth = yourSelectedImage.getWidth()/ratio;
	    			int nheight = yourSelectedImage.getHeight()/ratio;
//	    			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv.getLayoutParams();
//    			    params.width = sq;
//    			    params.height = sq;
//    			    iv.setLayoutParams(params);
    			    //scaleImage(iv,sq);
	    			resized = Bitmap.createScaledBitmap(yourSelectedImage, nwidth, nheight, true);
	    			iv.setImageBitmap(resized);
	    			 
	    			    
	    			    
	    			//iv.setImageBitmap(yourSelectedImage);
	    			//
	    		}
	    		break;
	   }
	}    		    
}


