package com.self.viewtoglrendering;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.LinearLayout;

/**
 * Created by user on 3/15/15.
 */
public class GLLinearLayout extends LinearLayout implements GLRenderable {

    private ViewToGLRenderer mViewToGLRenderer;

    // default constructors

    public GLLinearLayout(Context context) {
        super(context);
    }

    public GLLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public GLLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // drawing magic
    @Override
    public void draw(Canvas canvas) {
        Canvas glAttachedCanvas = mViewToGLRenderer.onDrawViewBegin();
        if(glAttachedCanvas != null) {
            //prescale canvas to make sure content fits
            float xScale = glAttachedCanvas.getWidth() / (float)canvas.getWidth();
            glAttachedCanvas.scale(xScale, xScale);
            //draw the view to provided canvas
            super.draw(glAttachedCanvas);
        }
        // notify the canvas is updated
        mViewToGLRenderer.onDrawViewEnd();
    }

    public void setViewToGLRenderer(ViewToGLRenderer viewToGLRenderer){
        mViewToGLRenderer = viewToGLRenderer;
    }
}
