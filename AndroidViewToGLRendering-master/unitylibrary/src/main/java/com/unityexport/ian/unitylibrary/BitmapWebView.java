package com.unityexport.ian.unitylibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;

/**
 * Created by user on 3/15/15.
 */
public class BitmapWebView extends WebView{//} implements GLRenderable,  View.OnTouchListener{

    private ImageView mImageView;
    public MainGL mainGL;

    boolean shouldBeDrawing = true;

    // default constructors

    public BitmapWebView(Context context) {
        super(context);
    }

    public BitmapWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BitmapWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

// so we can add an on touch listener
    @Override
    public boolean performClick(){
        super.performClick();
//        Log.d("AndroidUnity","performing click!");
        return false;
    }


    public void SetImage(Bitmap bm){
        mImageView.setImageBitmap(bm);

    }

    @Override
    public void draw( Canvas canvas ) {
        Log.d("AndroidUnity", "canvas draw called!");
        Log.d("AndroidUnity", "should be drawing is: "+ shouldBeDrawing);

        if (shouldBeDrawing) {
            super.draw(mainGL.bigcanvas);
            mainGL.convertWebviewToBitmapForUnity();
        }
    }

    //useful for debugging:
    public void setImageViewer(ImageView imageView){
        mImageView = imageView;
    }
}
