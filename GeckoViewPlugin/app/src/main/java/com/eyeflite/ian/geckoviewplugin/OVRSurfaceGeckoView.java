package com.eyeflite.ian.geckoviewplugin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Build;
import android.os.Handler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.PixelCopy;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import org.mozilla.gecko.util.ActivityUtils;
import org.mozilla.geckoview.BasicSelectionActionDelegate;
import org.mozilla.geckoview.GeckoDisplay;
import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.GeckoView;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;


public class OVRSurfaceGeckoView extends GeckoView {
    private String LOG_TAG = "AndroidUnity";
    protected final Display mDisplay = new Display();

    private GeckoSession.SelectionActionDelegate mSelectionActionDelegate;
    Surface mSurface;
    int mWidth;
    int mHeight;

    boolean IS_APPLICATION;


    public class Display  implements SurfaceHolder.Callback {
        private final int[] mOrigin = new int[2];

        private GeckoDisplay mDisplay;
        private boolean mValid;

        public void acquire(final GeckoDisplay display) {
            Log.i(LOG_TAG, "display acquired");
            mDisplay = display;

            if (!mValid) {
                return;
            }


            // Tell display there is already a surface.
            onGlobalLayout();
            if (OVRSurfaceGeckoView.this.mSurface != null) {
//                final SurfaceHolder holder = SurfaceTextureGeckoView.this.mTextureView.getHolder();
//                final Rect frame = holder.getSurfaceFrame();
                mDisplay.surfaceChanged(mSurface, mWidth, mHeight);
                OVRSurfaceGeckoView.this.setActive(true);
            }
        }

        public GeckoDisplay release() {
            if (mValid) {
                mDisplay.surfaceDestroyed();
                OVRSurfaceGeckoView.this.setActive(false);
            }

            final GeckoDisplay display = mDisplay;
            mDisplay = null;
            return display;
        }


        public void onGlobalLayout() {
            if (mDisplay == null) {
                return;
            }
            if (OVRSurfaceGeckoView.this.mSurface != null) {
                mOrigin[0] = 0;
                mOrigin[1] = mHeight;
//                SurfaceTextureGeckoView.this.mTextureView.getLocationOnScreen(mOrigin);
                Log.i(LOG_TAG, "on global layout: " + mOrigin[0]+" "+ mOrigin[1]);
                mDisplay.screenOriginChanged(mOrigin[0], mOrigin[1]);
            }
        }

        public boolean shouldPinOnScreen() {
            return mDisplay != null ? mDisplay.shouldPinOnScreen() : false;
        }

        public void ChangeScreenOrigin(int left, int top){
            mDisplay.screenOriginChanged(left, top);

        }


        // We can create a TextureView here for debugging purposes, though now unnecessary
        // We can also set out display surface for debugging + making sure we're drawing to it
        public void DisplaySurfaceAvailable(Surface surface, int width, int height) {
            mSurface = surface;
            Log.i(LOG_TAG, "surface available: " + width + " " + height);

            mWidth = width;
            mHeight = height;
            if (mDisplay != null) {

                mDisplay.surfaceChanged(mSurface, width, height);
                if (!mValid) {
                    OVRSurfaceGeckoView.this.setActive(true);
                }
                mDisplay.shouldPinOnScreen();
            }
            mValid = true;
        }
        /**
         * Old interface for when GeckoView used a SurfaceHolder to handle its surface
         */
        @Override // SurfaceHolder.Callback
        public void surfaceCreated(final SurfaceHolder holder) {
        }

        @Override // SurfaceHolder.Callback
        public void surfaceChanged(final SurfaceHolder holder, final int format,
                                   final int width, final int height) {
            if (mDisplay != null) {
                mDisplay.surfaceChanged(holder.getSurface(), width, height);
                if (!mValid) {
                    OVRSurfaceGeckoView.this.setActive(true);
                }
            }
            mValid = true;
        }

        @Override // SurfaceHolder.Callback
        public void surfaceDestroyed(final SurfaceHolder holder) {
            if (mDisplay != null) {
                mDisplay.surfaceDestroyed();
                OVRSurfaceGeckoView.this.setActive(false);
            }
            mValid = false;
        }
    }

    public BytesBuffer GetSurfaceBytesBuffer(int width, int height, int quality){
        if (mSurface==null)
            return null;

        // copy entire surface to initial bitmap
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        PixelCopy.OnPixelCopyFinishedListener listener = copyResult -> {
        };
        PixelCopy.request(mSurface,bitmap,listener,getHandler());

        // crop the pixel copy bitmap according to the passed width and height
        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);

        // convert cropped bitmap to png
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        croppedBitmap.compress(Bitmap.CompressFormat.PNG, quality, stream);

        return new BytesBuffer(stream.toByteArray());
    }


    public OVRSurfaceGeckoView(final Context context) {
        super(context);
        init();
    }

    public OVRSurfaceGeckoView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // TODO: Requires we comment out BasicActivity to work
    private void CheckIfWeAreApplication(){
        try {
            Class.forName( "com.eyeflite.ian.geckoviewplugin.BasicActivity" );
            Log.i(LOG_TAG, "BasicActivity found! We are an application");
            IS_APPLICATION = true;
        } catch( ClassNotFoundException e ) {
            Log.i(LOG_TAG, "BasicActivity not found! We are a library and not an application");
            IS_APPLICATION = false;
        }
    }


    SurfaceView pubSurfaceView;
    private void init(int... intPointer) {
        CheckIfWeAreApplication();
        setFocusable(false);
        setFocusableInTouchMode(false);
        setWillNotCacheDrawing(false);

        if (IS_APPLICATION)
            SetupApplicationSurfaceView();


        final Activity activity = ActivityUtils.getActivityFromContext(getContext());
        if (activity != null) {
            boolean usingFloatingToolbar = true;
            Log.i(LOG_TAG, "activity is not null, setting basic selection delegate and using floating bar: " + usingFloatingToolbar );
            mSelectionActionDelegate = new BasicSelectionActionDelegate(activity, usingFloatingToolbar);
        }

        // We can't call OnSurfaceTextureAvailable from the GL thread
        // Instead, initGLTextures() will set our surface texture at some point,
        // poll until it happens
        if (!IS_APPLICATION) {
            final Handler handler = new Handler();
            final int delay = 100; //milliseconds
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (mSurface != null) {
                        mDisplay.DisplaySurfaceAvailable(mSurface, mWidth, mHeight);

                    } else {
                        handler.postDelayed(this, delay);
                    }
                }
            }, delay);
        }
    }


    SurfaceView mSurfaceView;
    private void SetupApplicationSurfaceView(){
        setFocusable(true);
        setFocusableInTouchMode(true);
        setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);

        // We are adding descendants to this LayerView, but we don't want the
        // descendants to affect the way LayerView retains its focus.
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);

        // This will stop PropertyAnimator from creating a drawing cache (i.e. a
        // bitmap) from a SurfaceView, which is just not possible (the bitmap will be
        // transparent).
        setWillNotCacheDrawing(false);

        mSurfaceView = new SurfaceView(getContext());

        mSurfaceView.setBackgroundColor(Color.WHITE);
        addView(mSurfaceView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        mSurfaceView.getHolder().addCallback(mDisplay);
        pubSurfaceView = mSurfaceView;
    }


    /**
     * The following methods are GECKOVIEW required methods
     * b/c they were protected or we need to supply our surface
     */

    // have to override this so we are referencing our GeckoDisplay class
    @Override
    public void onDetachedFromWindow() {
        Log.i(LOG_TAG, "onDetachedFromWindow called!");
        if (mSession == null) {
            return;
        }
        Log.i(LOG_TAG, "releasing display");

        // Release the display before we detach from the window.
        mSession.releaseDisplay(mDisplay.release());

        Log.i(LOG_TAG, "calling super (geckoview) onDetached");
        super.onDetachedFromWindow();
    }



    private void setActive(final boolean active) {
        if (mSession != null) {
            mSession.setActive(active);
        }
    }

    public GeckoSession releaseSession() {
        if (mSession == null) {
            return null;
        }

        // Cover the view while we are not drawing to the surface.
        coverUntilFirstPaint(Color.WHITE);

        GeckoSession session = mSession;
        mSession.releaseDisplay(mDisplay.release());
        mSession.getOverscrollEdgeEffect().setInvalidationCallback(null);
        mSession.getCompositorController().setFirstPaintCallback(null);

        if (mSession.getAccessibility().getView() == this) {
            mSession.getAccessibility().setView(null);
        }

        if (mSession.getTextInput().getView() == this) {
            mSession.getTextInput().setView(null);
        }

        if (mSession.getSelectionActionDelegate() == mSelectionActionDelegate) {
            mSession.setSelectionActionDelegate(null);
        }

        if (isFocused()) {
            mSession.setFocused(false);
        }
        mSession = null;
//        mRuntime = null;
        return session;
    }

    /**
     * Attach a session to this view. The session should be opened before
     * attaching or a runtime needs to be provided for automatic opening.
     *
     * @param session The session to be attached.
     * @param runtime The runtime to be used for opening the session.
     */
    public void setSession(@NonNull final GeckoSession session,
                           @Nullable final GeckoRuntime runtime) {
        if (mSession != null && mSession.isOpen()) {
            throw new IllegalStateException("Current session is open");
        }
        Log.i(LOG_TAG, "setting session!");
        releaseSession();

        mSession = session;
//        mRuntime = runtime;

//        if (session.isOpen()) {
//            if (runtime != null && runtime != session.getRuntime()) {
//                throw new IllegalArgumentException("Session was opened with non-matching runtime");
//            }
//            mRuntime = session.getRuntime();
//        } else if (runtime == null) {
//            throw new IllegalArgumentException("Session must be open before attaching");
//        }
        if  ( !session.isOpen() && runtime == null) {
            throw new IllegalArgumentException("Session must be open before attaching");
        }

        // session creates a geckodisplay
        mDisplay.acquire( session.acquireDisplay());

        final Context context = getContext();
        session.getOverscrollEdgeEffect().setTheme(context);
        session.getOverscrollEdgeEffect().setInvalidationCallback(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= 16) {
                    OVRSurfaceGeckoView.this.postInvalidateOnAnimation();
                } else {
                    OVRSurfaceGeckoView.this.postInvalidateDelayed(10);
                }
            }
        });

        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        final TypedValue outValue = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.listPreferredItemHeight,
                outValue, true)) {
            session.getPanZoomController().setScrollFactor(outValue.getDimension(metrics));
        } else {
            session.getPanZoomController().setScrollFactor(0.075f * metrics.densityDpi);
        }

        session.getCompositorController().setFirstPaintCallback(new Runnable() {
            @Override
            public void run() {
                coverUntilFirstPaint(Color.TRANSPARENT);
            }
        });

        if (session.getTextInput().getView() == null) {
            session.getTextInput().setView(this);
        }

        if (session.getAccessibility().getView() == null) {
            session.getAccessibility().setView(this);
        }

        if (session.getSelectionActionDelegate() == null && mSelectionActionDelegate != null) {
            session.setSelectionActionDelegate(mSelectionActionDelegate);
        }

        if (isFocused()) {
            session.setFocused(true);
        }
    }


    @Override
    public boolean gatherTransparentRegion(final Region region) {
        // For detecting changes in SurfaceView layout, we take a shortcut here and
        // override gatherTransparentRegion, instead of registering a layout listener,
        // which is more expensive.
        if (mSurface != null) {
            mDisplay.onGlobalLayout();
        }
        return super.gatherTransparentRegion(region);
    }


    @Override
    public boolean onKeyUp(final int keyCode, final KeyEvent event) {
        if (super.onKeyUp(keyCode, event)) {
            return true;
        }
        return mSession != null &&
                mSession.getTextInput().onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (super.onKeyDown(keyCode, event)) {
            return true;
        }
        return mSession != null &&
                mSession.getTextInput().onKeyDown(keyCode, event);
    }

}
