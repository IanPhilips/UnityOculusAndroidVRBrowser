//package com.eyeflite.ian.geckoviewplugin;
//
//import android.app.Activity;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.SystemClock;
//import android.support.annotation.NonNull;
//import android.util.Log;
//import android.view.InputDevice;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//
//import android.widget.RelativeLayout;
//
//
//import org.mozilla.geckoview.GeckoRuntime;
//import org.mozilla.geckoview.GeckoRuntimeSettings;
//import org.mozilla.geckoview.GeckoSession;
//import org.mozilla.geckoview.GeckoSessionSettings;
//
//public class BasicActivity extends Activity implements GeckoSession.ScrollDelegate, GeckoSession.ContentDelegate {
//
//    public static OVRSurfaceGeckoView mWebView;
//    private static final String LOG_TAG = "AndroidUnity";
//    RelativeLayout relativeLayout;
//    @Override
//    public void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
//
//        initViews(relativeLayout);
//        // register listener for download complete status
////        getContext().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//    }
//
//    @Override
//    protected void onPause(){
//        super.onPause();
//
//
//    }
//    @Override
//    protected void onResume(){
//        super.onResume();
////        mWebView.mTextureView.scrollBy(0,-200);
////        mWebView.setVisibility(View.VISIBLE);
//    }
//
//    GeckoSession mSession;
//    GeckoRuntime mRuntime;
//
//    void initViews(View view){
//        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
//        Log.i("AndroidUnity", "CREATING view");
//
//        // create the geckoview
//         mWebView = new OVRSurfaceGeckoView(view.getContext());
//
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                1400);
//        mWebView.setBackgroundColor(Color.TRANSPARENT);
//        mWebView.setVisibility(View.VISIBLE);
//        mWebView.setLayoutParams(layoutParams);
//        relativeLayout.addView (mWebView);
//
//
//        Log.i("AndroidUnity", "CREATING runtime");
////        mRuntime = GeckoRuntime.create(view.getContext());
//        GeckoRuntimeSettings.Builder runtimeSettings = new GeckoRuntimeSettings.Builder();
//        runtimeSettings.inputAutoZoomEnabled(false);
//        mRuntime = GeckoRuntime.create(view.getContext(),runtimeSettings.build());
//
//        Log.i("AndroidUnity", "CREATING session");
//        SetSessionSettings();
//
//        Log.i("AndroidUnity", "opening runtime");
//        mSession.open(mRuntime);
//
//        mSession.setProgressDelegate(new GeckoSession.ProgressDelegate() {
//            @Override
//            public void onPageStart(GeckoSession session, String url) {
//                Log.i("AndroidUnity", "page start");
//
//            }
//
//            @Override
//            public void onPageStop(GeckoSession session, boolean success) {
//                Log.i("AndroidUnity", "page stop");
//
//            }
//
//            @Override
//            public void onProgressChange(GeckoSession session, int progress) {
//                Log.i("AndroidUnity", "progress change");
//                Log.i("AndroidUnity", "progress: " + progress);
//                if (progress>=100){
//                    if (mWebView.findFocus() != mWebView) {
//                        mWebView.setFocusable(true);
//                        mWebView.setFocusableInTouchMode(true);
//                        mWebView.requestFocus();
//                         Log.d("AndroidUnity","view focused");
//                    }
////                    DelayedMethod();
//
//                }
//
//            }
//
//            @Override
//            public void onSecurityChange(GeckoSession session, SecurityInformation securityInfo) {
//
//            }
//        });
//
////        mWebView.dispatchKeyEvent(KeyEvent.KEY)
////        mSession.getSettings()
//        mWebView.setSession(mSession);
//        mSession.loadUri("https://www.google.com");
////        mSession.loadUri("https://www.youtube.com/watch?v=qisi0PsDTI8"); // Or any other URL... // "https://www.youtube.com/tv#"); //
//    }
//
//
//    private void SetSessionSettings(){
//        GeckoSessionSettings.Builder builder = new GeckoSessionSettings.Builder();
//        builder.useMultiprocess(false);
//        builder.suspendMediaWhenInactive(true);
//        builder.userAgentMode(GeckoSessionSettings.USER_AGENT_MODE_MOBILE);
//        builder.viewportMode(GeckoSessionSettings.VIEWPORT_MODE_MOBILE);
//        builder.displayMode(GeckoSessionSettings.DISPLAY_MODE_STANDALONE);
//
//        mSession = new GeckoSession(builder.build());
//        mSession.setContentDelegate(this);
//    }
//
//
//    private void DelayedMethod() {
//        BasicActivity.this.
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        SetUserAgent("trash", true);
//                    }});
//
//    }
//    private void SingleTap() {
//        BasicActivity.this.
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        AddTap(x,y,0,0,0);
//                    }});
//    }
//
//    private void DelayedMethodTap(){
//        final Handler handler = new Handler();
//        final int delay = 50; //milliseconds
//
//        SingleTap();
//        SingleTap();
//
//
////        handler.postDelayed(new Runnable() {
////            public void run() {
////                SingleTap();
////            }},delay );
//    }
//
//
//    int user = -1;
//
//    public void SetUserAgent(String agent, boolean reload){
//
//        if ( user >= GeckoSessionSettings.USER_AGENT_MODE_DESKTOP){
//            DelayedMethodTap();
//            return;
//        }
//
//        user += 1;
//
//        mWebView.getSession().getSettings().setUserAgentMode(GeckoSessionSettings.USER_AGENT_MODE_DESKTOP);
//        if (reload) {
//            BasicActivity.this.runOnUiThread(new Runnable() {public void run() {
//                if (mWebView == null || mWebView.getSession()==null) {
//                    return;
//                }
////                Configuration configuration = new Configuration();
////                configuration.orientation = Configuration.ORIENTATION_LANDSCAPE;
////                mWebView.dispatchConfigurationChanged(configuration);
//                mWebView.getSession().reload();
//            }});
//
//        }
//    }
//
//
//
//
//    float x =  (float) 0;
//    float y = (float) 0;
//    private void simulateTapDown(){
////        simulateTapEventWithHistory(MotionEvent.ACTION_DOWN, x, y);
//        BasicActivity.this.
//                runOnUiThread(() -> simulateTapEventWithHistory(MotionEvent.ACTION_DOWN, x, y));
//    }
//
//    private void simulateTapUp(){
////        simulateTapEventWithHistory(MotionEvent.ACTION_UP, x, y);
//        BasicActivity.this.
//                runOnUiThread(() -> simulateTapEventWithHistory(MotionEvent.ACTION_UP, x, y));
//    }
//
//    private void simulateMoveTap2(){
//        y-=30;
////        simulateTapEventWithHistory(MotionEvent.ACTION_SCROLL, x, y);
//        BasicActivity.this.
//                runOnUiThread(() -> simulateTapEventWithHistory(MotionEvent.ACTION_MOVE, x, y));
//    }
//
//
//
//
//    MotionEvent lastMotionEvent;
//    private void simulateTapEventWithHistory(int action, float x, float y)
//    {
//        long downTime = SystemClock.uptimeMillis();
//        long eventTime = SystemClock.uptimeMillis();
//        // List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
//        int metaState = 0;
//        // sets tool type to finger
//        MotionEvent.PointerProperties pointerProperties = new MotionEvent.PointerProperties();
//        pointerProperties.toolType = 1;
//        pointerProperties.id = 0;
//
//        MotionEvent.PointerCoords pointerCoords = new MotionEvent.PointerCoords();
//        pointerCoords.x=x;
//        pointerCoords.y=y;
//        pointerCoords.pressure =1;
//        pointerCoords.size=1;
//        int source = InputDevice.SOURCE_TOUCHSCREEN;
//
//        MotionEvent me = MotionEvent.obtain(downTime,
//                                            eventTime,
//                                            action,
//                                            1,
//                                            new MotionEvent.PointerProperties[]{pointerProperties},
//                                            new MotionEvent.PointerCoords[]{pointerCoords},metaState,
//                                            0,1,1,0,0,source,0);
//
//        // adds move events to history
//        if (lastMotionEvent != null && me.getAction() == MotionEvent.ACTION_MOVE && lastMotionEvent.getAction() == MotionEvent.ACTION_MOVE){
//            lastMotionEvent.addBatch(eventTime, new MotionEvent.PointerCoords[]{pointerCoords},metaState);
//            return;
//        }
//        // triggers last move event with filled history
//        else if(lastMotionEvent != null && me.getAction() == MotionEvent.ACTION_UP && lastMotionEvent.getAction() == MotionEvent.ACTION_MOVE) {
//            boolean moved = mWebView.getRootView().dispatchTouchEvent(lastMotionEvent);
//            Log.d(LOG_TAG, "scrolled: " + moved);
//        }
//
//        boolean scrolled = mWebView.getRootView().dispatchTouchEvent(me);
//        Log.d(LOG_TAG, "scrolled: " + scrolled);
//        lastMotionEvent = me;
//    }
//
//
//
//    // accounts for the webview's scroll Y
//    public void AddTap(final float x, float y, final float diffX, final float diffY, final int timeDiff)
//    {
//
////        Log.d("AndroidUnity","Add tap called! at x:" + x + " y: "+y);
//
//        if (mWebView == null)
//            return;
//        // going down is negative in scroll but positive in coordinate system
//    //            y-= mWebView.getScrollY();
//        final float finalY = y;
//        Log.d("AndroidUnity","Add tap called! at x:" + x + " y: "+finalY);
//
//        if (this.mWebView.findFocus() != mWebView) {
//            mWebView.setFocusable(true);
//            mWebView.setFocusableInTouchMode(true);
//            mWebView.requestFocus();
//            // Log.d("AndroidUnity","view focused");
//        }
//        BasicActivity.this.runOnUiThread(new Runnable() {public void run() {
//
//            // Obtain MotionEvent object
//            long downTime = SystemClock.uptimeMillis();
//            long eventTime = SystemClock.uptimeMillis() + timeDiff;
//            int source = InputDevice.SOURCE_CLASS_POINTER;
//
//    // List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
//            int metaState = 0;
//            MotionEvent event = MotionEvent.obtain(
//                    downTime,
//                    eventTime,
//                    MotionEvent.ACTION_DOWN,
//                    x,
//                    finalY,
//                    metaState
//            );
//            event.setSource(source);
//
//    // Dispatch touch event to view
//            mWebView.dispatchTouchEvent(event);
//
//            event = MotionEvent.obtain(
//                    downTime,
//                    eventTime,
//                    MotionEvent.ACTION_UP,
//                    x + diffX,
//                    finalY + diffY,
//                    metaState
//            );
//            event.setSource(source);
//            mWebView.dispatchTouchEvent(event);
//        }
//        });
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        DelayedMethod();
//    }
//
//
//    @Override
//    public void onScrollChanged(GeckoSession session, int scrollX, int scrollY) {
////        Log.d("AndroidUnity","scrolling!");
//
//    }
//
//
//    /**
//     * ***********************
//     * CONTENT DELEGATE
//     * ***********************
//     * */
//
//    @Override
//    public void onTitleChange(GeckoSession session, String title) {
//
//    }
//
//    @Override
//    public void onFocusRequest(GeckoSession session) {
//
//    }
//
//    @Override
//    public void onCloseRequest(GeckoSession session) {
//
//    }
//
//    @Override
//    public void onFullScreen(GeckoSession session, boolean fullScreen) {
//
//    }
//
//    @Override
//    public void onContextMenu(@NonNull GeckoSession session, int screenX, int screenY, @NonNull GeckoSession.ContentDelegate.ContextElement element) {
//        Log.d(LOG_TAG, "on context menu: " + element.linkUri);
//        Log.d(LOG_TAG, "element type menu: " + element.type);
//        Log.d(LOG_TAG, "element alt text: " + element.altText);
//
//    }
//
//
//    @Override
//    public void onExternalResponse(GeckoSession session, GeckoSession.WebResponseInfo response) {
//        Log.d(LOG_TAG, "on external response, type: " + response.contentType);
//        Log.d(LOG_TAG, "on external response, filename: " + response.filename);
//        Log.d(LOG_TAG, "on external response, uri: " + response.uri);
//    }
//
//    @Override
//    public void onCrash(@NonNull GeckoSession session) {
//        Log.d(LOG_TAG, "on crash!");
//
//    }
//
//    @Override
//    public void onFirstComposite(@NonNull GeckoSession session) {
//
//    }
//
//
//}
//
////    public static int count = 0;
////    public static MotionEvent[] events = new MotionEvent[4];
////
////    public static void AddEvent(MotionEvent e){
////        if (count<4)
////            events[BasicActivity.count] = e;
////        else if (count ==5 ){
////            count=7;
////            Log.d(LOG_TAG,"TRIGGERING SAVED EVENTS" );
////
////            for (int i =0; i<4; i++){
////                mWebView.dispatchTouchEvent(events[i]);
////            }
////        }
////        count += 1;
////
////    }