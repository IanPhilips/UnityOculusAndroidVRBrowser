//package com.eyeflite.ian.geckoviewplugin;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//
//import android.os.Handler;
//import android.support.annotation.Nullable;
//import android.util.AttributeSet;
//
//public class BitmapGrabberImageView extends android.widget.ImageView {
//    public BitmapGrabberImageView(Context context) {
//        super(context);
//        init();
//    }
//
//    public BitmapGrabberImageView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    public BitmapGrabberImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init();
//    }
//
//    public BitmapGrabberImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init();
//    }
////    public GLES3JNIView.BitmapRenderer bitmapRenderer;
////    public SurfaceGeckoView bitmapRenderer;
////    public TextureViewGeckoView textureViewGeckoView;
////    public OVRSurfaceGeckoView textureGeckoView;
//    private void init(){
//       final Handler handler = new Handler();
//        final int delay = 100; //milliseconds
//
//        handler.postDelayed(new Runnable(){
//            public void run(){
//                DrawBitmap();
//                handler.postDelayed(this, delay);
//            }
//        }, delay);
//    }
//    public Bitmap mBitmap;
//    public void DrawBitmap(){
//
////        if (textureViewGeckoView!= null && textureViewGeckoView.PubTextureView!= null){
////            setImageBitmap(textureViewGeckoView.PubTextureView.getBitmap());
////        }
////        if (textureGeckoView!=null && textureGeckoView.mSurface!=null){
////            Log.i("AndroidUnity", "setting bitmap");
////
////            Bitmap plotBitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
////            PixelCopy.OnPixelCopyFinishedListener listener = copyResult -> {
////
////            };
////
////            PixelCopy.request(textureGeckoView.mSurface,plotBitmap,listener,getHandler());
////            setImageBitmap(plotBitmap);        }
////        else if (textureGeckoView!= null && textureGeckoView.mBitmap!=null){
////            setImageBitmap(textureGeckoView.mBitmap);
////        }
//        if (mBitmap!=null){
//                        setImageBitmap(mBitmap);
//        }
//
//
//
////        if (bitmapRenderer!= null) {
////            Bitmap plotBitmap = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
////            PixelCopy.OnPixelCopyFinishedListener listener = copyResult -> {
////
////            };
////
////            PixelCopy.request(bitmapRenderer.PubSurfaceView,plotBitmap,listener,getHandler());
//            setImageBitmap(plotBitmap);
////            }
//    }
//
//
//
//
////
////    @Override
////    protected void onDraw(Canvas canvas){
////        super.onDraw(canvas);
////        if (bitmapRenderer!= null) {
////            this.setImageBitmap(bitmapRenderer.getBitmap());
////        }
////    }
//}
