package com.eyeflite.ian.geckoviewplugin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.CursorAnchorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.widget.RelativeLayout;
import com.unity3d.player.UnityPlayer;


import org.mozilla.geckoview.AllowOrDeny;
import org.mozilla.geckoview.GeckoResult;
import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoRuntimeSettings;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.GeckoSessionSettings;
import org.mozilla.geckoview.ScreenLength;
import org.mozilla.geckoview.WebRequestError;

import java.util.HashMap;

public class GeckoViewPLugin extends Fragment implements GeckoSession.NavigationDelegate, GeckoSession.ContentDelegate, GeckoSession.HistoryDelegate, GeckoSession.TextInputDelegate, GeckoSession.ProgressDelegate, GeckoRuntime.Delegate {
    static String FRAGMENT_TAG = "AndroidUnityMainGL";
    static int Width;
    static int Height;
    static int DefaultUserAgent;
    static GeckoViewPLugin Instance;

    private String LOG_TAG = "AndroidUnity";
    private OVRSurfaceGeckoView mWebView;
    private KeyCharacterMap CharMap;
    private GeckoRuntime mRuntime;

    private UnityInterface UnityCallback;
    private static AudioManager mAudioManager;

    enum SessionTypes{
        YOUTUBE, BROWSER,
    }
    private HashMap<SessionTypes, GeckoSession> mSessions;


    public BytesBuffer GetSurfaceBytesBuffer(int width, int height, int quality){
        return mWebView.GetSurfaceBytesBuffer(width, height, quality);
    }


    public void AddTap(final float x, final float y){

        if (mWebView == null) {
            Log.d("AndroidUnity","Webview is null, not adding tap");
            return;
        }
        long downTime = SystemClock.uptimeMillis();

        DispatchTouchEventToWebView(x,y, MotionEvent.ACTION_DOWN, downTime);
        DispatchTouchEventToWebView(x,y, MotionEvent.ACTION_UP, downTime);
//        Log.d("AndroidUnity","Add tap called! at x:" + x + " y: "+y);

    }


    /**
     * Changes the current open GeckoSession. Loads the first url if the gecko session hasn't been opened before.
     * @param sessionType
     * @param firstURL
     */
    public void ChangeSessionAndLoadUrlIfUnopened(String sessionType, String firstURL){
        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(() -> {
            GeckoSession currentSession = mWebView.getSession();
            if (currentSession != null) {
                mWebView.releaseSession();
                currentSession.setActive(false);
            }

            GeckoSession newSession = ParseSessionForType(sessionType);

            boolean firstOpen = !newSession.isOpen();
            if (firstOpen) {
                newSession.open(mRuntime);
            }

            newSession.setActive(true);
            mWebView.setSession(newSession, mRuntime);
            if (firstOpen)
                newSession.loadUri(firstURL);

        });


    }

    private GeckoSession ParseSessionForType(String sessionType){
        GeckoSession newSession;

        switch (sessionType) {
            case "youtube":
                newSession = mSessions.get(SessionTypes.YOUTUBE);
                break;
            case "browser":
            default:
                newSession = mSessions.get(SessionTypes.BROWSER);
                break;
        }
        return newSession;
    }


    /**
     * Adds a long press at x and y. Doesn't work right now b/c of action dialog box not being 3D.
     * @param x
     * @param y
     */
    public void AddLongTap(final float x, final float y){
        if (mWebView == null) {
            return;
        }
        long downTime = SystemClock.uptimeMillis();

        DispatchTouchEventToWebView(x,y, MotionEvent.ACTION_DOWN, downTime);
        final Handler handler = new Handler();
        final int delay = 300; //milliseconds required for long touch
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DispatchTouchEventToWebView(x,y, MotionEvent.ACTION_UP, downTime);
            }
        }, delay);
        Log.d("AndroidUnity","Add tap called! at x:" + x + " y: "+y);

    }


    /**
     * dispatches the programmatic touch to the webview at x,y with action type and length of action.
     * @param x
     * @param y
     * @param action
     * @param downTime
     */
    private void DispatchTouchEventToWebView(final float x, final float y, int action, long downTime){
        UnityPlayer.currentActivity.runOnUiThread(new Runnable() {public void run() {
            MakeWebviewGetFocus();

            // Obtain MotionEvent object
            long eventTime = SystemClock.uptimeMillis()+50;
            int source = InputDevice.SOURCE_CLASS_POINTER;

            // List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
            int metaState = 0;
            MotionEvent event = MotionEvent.obtain(
                    downTime,
                    eventTime,
                    action,
                    x,
                    y,
                    metaState
            );
            event.setSource(source);

            // Dispatch touch event to view
            mWebView.dispatchTouchEvent(event);
        }
        });
    }

    private void MakeWebviewGetFocus(){
        if (getView().findFocus() != mWebView) {
            mWebView.setFocusable(true);
            mWebView.setFocusableInTouchMode(true);
            mWebView.requestFocus();
            // Log.d("AndroidUnity","view focused");
        }
    }


    // For keys with no desired metaState
    public void AddKeyCode(int code){
        AddKeyCode(code,0);
    }


    /**
     * Adds functional keycodes to the webview. Changed from dispatchKeyEvent to onKey{up,down}
     * which enables esc and enter to work perfectly in youtube.
     * @param code
     * @param metaState
     */
    public void AddKeyCode(int code, int metaState){
        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(() -> {
//            int keyCode = KeyEvent.KEYCODE_HOME
//            MakeWebviewGetFocus();
//            Log.d(LOG_TAG, "keycode is: " + code);
            KeyEvent event = new KeyEvent(SystemClock.uptimeMillis(),
                    SystemClock.uptimeMillis(),
                    KeyEvent.ACTION_DOWN,
                    code,
                    0,
                    metaState);

            mWebView.onKeyDown(code, event);
            mWebView.onKeyUp(code, event);

//            mWebView.dispatchKeyEvent(event);
        });
    }


    /**
     * Converts text to keypresses and sends them to the webview.
     * @param text
     */
    public void AddKeys(String text){
        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(() -> {
            if (mWebView == null)
                return;
            MakeWebviewGetFocus();

            // we are just adding text
            char[] szRes = text.toCharArray(); // Convert String to Char array
            KeyEvent[] events = CharMap.getEvents(szRes);

            for (int i = 0; i < events.length; i++)
                mWebView.dispatchKeyEvent(events[i]); // MainWebView is webview

        });
    }


    public void ScrollByXorY(int horizontalAmount, int verticalAmount){

        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(() -> {
            if (mWebView == null)
                return;
            mWebView.getSession().getPanZoomController().scrollBy(ScreenLength.fromPixels(horizontalAmount),ScreenLength.fromPixels(verticalAmount));

        });
    }



    public void ActivateSession(boolean setActive){
        if (mWebView == null || mWebView.getSession()==null)
            return;

        mWebView.getSession().setActive(setActive);
    }


    GeckoResult<GeckoSession.SessionState> saveState;
    public void SaveState(){
            if (mWebView == null || mWebView.getSession()==null)
            return;
//        saveState = mWebView.getSession().saveState();
    }


    public boolean LoadLastState(){
        if (mWebView.getSession()==null || saveState==null) {
            Log.d(LOG_TAG, "ERROR: no gecko session or session state to restore");
            return false;
        }
        try {
            GeckoSession.SessionState state = saveState.poll();
            mWebView.getSession().restoreState(state);
            return true;
        } catch (Throwable throwable) {
            Log.d(LOG_TAG, "ERROR: no gecko session state to restore");
            return false;
        }

    }



    public void LoadURL(final String url) {
        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new Runnable() {public void run() {
            if (mWebView == null || mWebView.getSession()==null) {
                return;
            }
            mWebView.getSession().loadUri(url);

        }});
    }
    public void Refresh() {
        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new Runnable() {public void run() {
            if (mWebView == null || mWebView.getSession()==null) {
                return;
            }
            mWebView.getSession().reload();

        }});
    }

    public void GoBack() {
        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new Runnable() {public void run() {
            if (mWebView == null || mWebView.getSession()==null) {
                return;
            }
//            if ( mWebView.canGoBack()) {
            mWebView.getSession().goBack();
//            }
        }});
    }

    public void GoForward() {
        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new Runnable() {public void run() {
            if (mWebView == null || mWebView.getSession()==null) {
                return;
            }
//            if (mWebView.canGoForward()) {
            mWebView.getSession().goForward();
//            }
        }});
    }

    public void StopWebview(){
        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new Runnable() {public void run() {
            if (mWebView == null || mWebView.getSession()==null) {
                return;
            }
            mWebView.getSession().stop();
        }});

    }

//    public void RestartBrowser(String url){
//        final Activity a = UnityPlayer.currentActivity;
//        a.runOnUiThread(new Runnable() {public void run() {
//            if (mWebView == null) {
//                Log.d(LOG_TAG, "Error, no webview found to restart!");
//                return;
//            }
//            Log.d(LOG_TAG, "restarting browser!");
//
//            // close current session
//            if (mSession!= null && mSession.isOpen())
//                mSession.close();
//
//            RestartSession(mSession, url,false);
//            // TODO: it looks like there is no way to restart the runtime once it has been created
//            // shutting down the runtime doesn't seem to crash unity
////            if (mRuntime!= null)
////                mRuntime.shutdown();
////            mRuntime = null;
//
//        }});
//    }
//
//
//    // TODO: this is not updating the surface after restart
//    public void RestartSession(GeckoSession session, String url, boolean useLastState){
//        if (session.isOpen()) session.close();
//
//         // if we want to start a fresh session
//        if (!useLastState) {
//            session.open(mRuntime);
//            session.loadUri(url);
//            return;
//        }
//         // try and load our last saved state
//        if (LoadLastState())
//            return;
//
//        // we couldn't load our last saved state, load a fresh one
//        session.open(mRuntime);
//
//        // refresh the surface
//        Log.i(LOG_TAG, "WARNING WE ARE TOUCHING THE MDISPLAY VARIABLE");
//        if (mWebView.mSurface!=null)
//            mWebView.mDisplay.DisplaySurfaceAvailable(mWebView.mSurface,Width, Height);
//
//        session.loadUri(url);
//   }



    // Unity method
  public static void PassStaticSurface(final Surface surface ){
//      Instance.mWebView.UnityCallback = Instance.UnityCallback;
      Instance.mWebView.mWidth=Width;
      Instance.mWebView.mHeight=Height;
      Instance.mWebView.mSurface = surface;
  }

    // Unity method
    public void SetUnityBitmapCallback(UnityInterface callback){
        Log.d(LOG_TAG,"My unity callback is set");
        UnityCallback = callback;
    }


    public void OverrideSessionUserAgent(String sessionType, String agent){
        GeckoSession session = ParseSessionForType(sessionType);
        session.getSettings().setUserAgentOverride(agent);
    }

    public void SetBrowserUserAgent(String agent, boolean reload){

        int agentVal = ParseUserAgentString(agent);
        GeckoSession session = mSessions.get(SessionTypes.BROWSER);
        session.getSettings().setUserAgentMode(agentVal);

        if (reload)
            mWebView.getSession().reload();

    }


    public static int ParseUserAgentString(String agent){
        int agentVal;

        switch (agent){
            case "mobile":
                agentVal = GeckoSessionSettings.USER_AGENT_MODE_MOBILE;
                break;
            case "desktop":
                agentVal = GeckoSessionSettings.USER_AGENT_MODE_DESKTOP;
                break;
            case "vr":
                agentVal = GeckoSessionSettings.USER_AGENT_MODE_VR;
                break;
            default:
                agentVal = GeckoSessionSettings.USER_AGENT_MODE_DESKTOP;
                break;
        }
        return agentVal;
    }


    void initViews(View view){
        // useful for adding keys from unity to the webview
        CharMap = KeyCharacterMap.load(KeyCharacterMap.ALPHA);

        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        // create our geckoview
        mWebView = new OVRSurfaceGeckoView(view.getContext());

        // set the webview parameters to 1x1 so on startup the user doesn't see a big white rectangle
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams( 1,1);//Width/100,  Height/100);
        mWebView.setLayoutParams(layoutParams);
        relativeLayout.addView (mWebView);
        mWebView.setVisibility(View.VISIBLE);

        Log.i(LOG_TAG, "CREATING runtime");
        InitNewRuntime(view);

        Log.i(LOG_TAG, "CREATING sessions hashmap");
        mSessions = new HashMap<SessionTypes, GeckoSession>() {{
            put(SessionTypes.BROWSER, InitNewSession(mRuntime));
            put(SessionTypes.YOUTUBE, InitNewSession(mRuntime));
        }};

    }

    private void InitNewRuntime(View view){
        GeckoRuntimeSettings.Builder runtimeSettings = new GeckoRuntimeSettings.Builder();
        runtimeSettings.inputAutoZoomEnabled(false);
        mRuntime = GeckoRuntime.create(view.getContext(),runtimeSettings.build());
        mRuntime.setDelegate(this);

    }

    private GeckoSession InitNewSession(GeckoRuntime runtime){

        GeckoSessionSettings.Builder builder = new GeckoSessionSettings.Builder();
//        builder.useMultiprocess(false);
        builder.suspendMediaWhenInactive(true);
        builder.userAgentMode(DefaultUserAgent);
        builder.viewportMode(GeckoSessionSettings.VIEWPORT_MODE_MOBILE);
        builder.displayMode(GeckoSessionSettings.DISPLAY_MODE_STANDALONE);
        GeckoSession session = new GeckoSession(builder.build());
        session.setNavigationDelegate(this);
        session.setContentDelegate(this);
        session.setHistoryDelegate(this);
        session.getTextInput().setDelegate(this);
        session.setProgressDelegate( this);
        return session;

    }
//
//
//    private void StartMemoryCheckEvery(int seconds){
//
//        final Handler handler = new Handler();
//        final int delay = seconds*1000; //milliseconds
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                long nativeHeapSize = Debug.getNativeHeapSize();
//                long nativeHeapFreeSize = Debug.getNativeHeapFreeSize();
//                long usedMemInBytes = nativeHeapSize - nativeHeapFreeSize;
//                long usedMemInPercentage = usedMemInBytes * 100 / nativeHeapSize;
//                Log.i(LOG_TAG, "free native heap size: " + nativeHeapFreeSize);
//                Log.i(LOG_TAG, "used mem in MB: " + usedMemInBytes/1000000);
//                Log.i(LOG_TAG, "used mem in percent: " + usedMemInPercentage + "%");
//                handler.postDelayed(this, delay);
//
//            }
//        }, delay);


//    }


    /**
     * ***********************
     * RUNTIME SHUTDOWN DELEGATE
     * ***********************
     * */

    @Override
    public void onShutdown() {
        Log.i(LOG_TAG, "runtime has been shutdown!!");
        UnityCallback.OnRuntimeShutdown();
    }


    /**
     * ***********************
     * PROGRESS DELEGATE
     * ***********************
     * */


    @Override
    public void onPageStart(GeckoSession session, String url) {
        Log.i(LOG_TAG, "page start");

    }

    @Override
    public void onPageStop(GeckoSession session, boolean success) {
        Log.i(LOG_TAG, "page stop");

    }

    @Override
    public void onProgressChange(GeckoSession session, int progress) {
        Log.i(LOG_TAG, "progress change");
        Log.i(LOG_TAG, "progress: " + progress);
        if (UnityCallback==null)
            return;
        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new Runnable() {public void run() {
            UnityCallback.updateProgress(progress);
        }});
    }

    @Override
    public void onSecurityChange(GeckoSession session, SecurityInformation securityInfo) {

    }




    /**
     * ***********************
     * INPUT DELEGATE
     * ***********************
     * */


    @Override
    public void restartInput(@NonNull GeckoSession session, int reason) {
        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new Runnable() {public void run() {
            UnityCallback.RestartInput();
        }});
    }

    @Override
    public void showSoftInput(@NonNull GeckoSession session) {
//        Log.i(LOG_TAG, "SHOW SOFT INPUT");
        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new Runnable() {public void run() {
            UnityCallback.ChangeKeyboardVisiblity(true);
        }});

    }

    @Override
    public void hideSoftInput(@NonNull GeckoSession session) {
//        Log.i(LOG_TAG, "HIDE SOFT INPUT");
        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new Runnable() {public void run() {
            UnityCallback.ChangeKeyboardVisiblity(false);
        }});

    }

//    @Override
//    public void updateSelection(@NonNull GeckoSession session, int selStart, int selEnd, int compositionStart, int compositionEnd) {
////        Log.i(LOG_TAG, "UPDATE SELECTION INPUT" + selStart);
//
//    }
//
//    @Override
//    public void updateExtractedText(@NonNull GeckoSession session, @NonNull ExtractedTextRequest request, @NonNull ExtractedText text) {
////        Log.i(LOG_TAG, "UPDATE EXTRACTED INPUT " + text.text);
//
//    }
//
//    @Override
//    public void updateCursorAnchorInfo(@NonNull GeckoSession session, @NonNull CursorAnchorInfo info) {
//
//    }

//    @Override
//    public void notifyAutoFill(@NonNull GeckoSession session, int notification, int virtualId) {
//
//    }


    /**
     * ***********************
     * HISTORY DELEGATE
     * ***********************
     * */

    @Nullable
    @Override
    public GeckoResult<Boolean> onVisited(@NonNull GeckoSession session, @NonNull String url, @Nullable String lastVisitedURL, int flags) {
        if (UnityCallback==null)
            return null;
        String lastVisitedUrl = "";
        if (lastVisitedURL!=null)
            lastVisitedUrl = lastVisitedURL.toString();
        final String lastUrl = lastVisitedUrl;
        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new Runnable() {public void run() {
            UnityCallback.OnPageVisited(url, lastUrl);
        }});

        return null;
    }

    @Nullable
    @Override
    public GeckoResult<boolean[]> getVisited(@NonNull GeckoSession session, @NonNull String[] urls) {
        return null;
    }


    /**
     * ***********************
     * NAVIGATION DELEGATE
     * ***********************
     * */

    @Override
    public void onLocationChange(GeckoSession session, String url) {
        if (UnityCallback==null)
            return;

        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new Runnable() {public void run() {
            UnityCallback.updateURL(url);
        }});

//        Log.d(LOG_TAG, "location change uri: " + url);

    }

    @Override
    public void onCanGoBack(GeckoSession session, boolean canGoBack) {
        if (UnityCallback==null)
            return;
        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new Runnable() {public void run() {
            UnityCallback.CanGoBack(canGoBack);
        }});
    }

    @Override
    public void onCanGoForward(GeckoSession session, boolean canGoForward) {
        if (UnityCallback==null)
            return;
        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new Runnable() {public void run() {
            UnityCallback.CanGoForward(canGoForward);
        }});


    }

    @Nullable
    @Override
    public GeckoResult<AllowOrDeny> onLoadRequest(@NonNull GeckoSession session, @NonNull LoadRequest request) {
//        GeckoResult<AllowOrDeny> geckoResult = new GeckoResult<AllowOrDeny>();
//        geckoResult.complete(AllowOrDeny.ALLOW);
//        Log.d(LOG_TAG, "request uri: " + request.uri);
//        return  geckoResult;
        return null;
    }

    @Nullable
    @Override
    public GeckoResult<GeckoSession> onNewSession(@NonNull GeckoSession session, @NonNull String uri) {
        return null;
    }

    @Override
    public GeckoResult<String> onLoadError(GeckoSession session, String uri, WebRequestError error) {
        Log.d(LOG_TAG, "onload error uri: " + uri);

        return null;
    }

    @Override
    public void onCrash(GeckoSession session) {
        Log.d(LOG_TAG, "********** on crash! we're letting unity handle this ***********" );
        final Activity a = UnityPlayer.currentActivity;
//        Log.d(LOG_TAG, "on crash! fatal: " + session. );
//        mRuntime.getSettings().getCrashHandler()
        a.runOnUiThread(new Runnable() {public void run() {
            UnityCallback.OnSessionCrash();
        }});

    }

    @Override
    public void onFirstComposite(GeckoSession session) {

    }



    /**
     * ***********************
     * CONTENT DELEGATE
     * ***********************
     * */

    @Override
    public void onTitleChange(GeckoSession session, String title) {

    }

    @Override
    public void onFocusRequest(GeckoSession session) {

    }

    @Override
    public void onCloseRequest(GeckoSession session) {

    }

    @Override
    public void onFullScreen(GeckoSession session, boolean fullScreen) {

//        Log.d(LOG_TAG, "on full screen: " + fullScreen);
        final Activity a = UnityPlayer.currentActivity;
//        Log.d(LOG_TAG, "on crash! fatal: " + session. );
//        mRuntime.getSettings().getCrashHandler()
        a.runOnUiThread(new Runnable() {public void run() {
            UnityCallback.OnFullScreenRequestChange(fullScreen);
        }});
    }

    @Override
    public void onContextMenu(@NonNull GeckoSession session, int screenX, int screenY, @NonNull GeckoSession.ContentDelegate.ContextElement element) {
        Log.d(LOG_TAG, "on context menu: " + element.linkUri);
        Log.d(LOG_TAG, "element type menu: " + element.type);
        Log.d(LOG_TAG, "element alt text: " + element.altText);

    }


    @Override
    public void onExternalResponse(GeckoSession session, GeckoSession.WebResponseInfo response) {
        Log.d(LOG_TAG, "on external response, type: " + response.contentType);
        Log.d(LOG_TAG, "on external response, filename: " + response.filename);
        Log.d(LOG_TAG, "on external response, uri: " + response.uri);
    }



    /**
     * ***********************
     * CREATION METHODS
     * ***********************
     * */


    // To Call From Unity at start
    public static GeckoViewPLugin CreateInstance(int width, int height, String defaultUserAgent)
    {
        Log.i("AndroidUnity", "Hi, new instance created");
        // create the new fragment
        GeckoViewPLugin newMainGL = new GeckoViewPLugin();
        GeckoViewPLugin.Width = width;
        GeckoViewPLugin.Height =height;
        GeckoViewPLugin.DefaultUserAgent = GeckoViewPLugin.ParseUserAgentString(defaultUserAgent);
        newMainGL.setRetainInstance(true);
        // add fragment for callbacks
        Log.i("AndroidUnity", "CREATING FRAGMENT");
        AddFragment(newMainGL);

        return newMainGL;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // register listener for download complete status
//        getContext().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        setRetainInstance(true); // Retain between configuration changes (like device rotation)
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        Log.d(LOG_TAG,"On Create View!");
//        WebView.enableSlowWholeDocumentDraw();
//        BitmapWebView.enableSlowWholeDocumentDraw();

        // can use this to not show window
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.ADJUST_NOTHING|     WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        View mView =  inflater.inflate(R.layout.activity_main, parent, false);
        Log.d(LOG_TAG,"On inflate  View!");
        Instance = this;
        initViews(mView);
        Log.d(LOG_TAG,"On finish Create View + initViews!");

        return mView;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        Log.d(LOG_TAG,"On View Created!");
    }

    @SuppressLint("ResourceType")
    private static void AddFragment(Fragment fragment){

//        ViewGroup rootView = (ViewGroup)fragment.getActivity().findViewById(android.R.id.content);
        ViewGroup rootView = (ViewGroup)UnityPlayer.currentActivity.findViewById(android.R.id.content);

        // find the first leaf view (i.e. a view without children)
        // the leaf view represents the topmost view in the view stack
        View topMostView = getLeafView(rootView);

        if (topMostView != null) {

            // let's add a sibling to the leaf view
            ViewGroup leafParent = (ViewGroup)topMostView.getParent();

            if (leafParent != null) {
                leafParent.setId(0x20348);

                // fragment = new WebViewFragment();
//                FragmentManager fragmentManager = fragment.getActivity().getFragmentManager();
                FragmentManager fragmentManager = UnityPlayer.currentActivity.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(leafParent.getId(), fragment, FRAGMENT_TAG);
                fragmentTransaction.commit();
            }
        }
    }

    private static View getLeafView(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup)view;
            for (int i = 0; i < vg.getChildCount(); ++i) {
                View chview = vg.getChildAt(i);
                View result = getLeafView(chview);
                if (result != null)
                    return result;
            }
            return null;
        }
        else {
            return view;
        }
    }


    /*
     ********************************
     * VOLUME AREA
     *********************************
     */

    /**
     * VOLUME CONTROL
     * @param status
     */
    public static void AdjustVolume(int status){
        if (mAudioManager==null)
            mAudioManager = (AudioManager) UnityPlayer.currentActivity.getSystemService(Context.AUDIO_SERVICE);

        if(mAudioManager==null ) {
            Log.d("AndroidUnity", "AUDIO MANAGER IS NULL, NOT CONTROLLING VOLUME");
            return;
        }

        if (status>0)
            mAudioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);

        else if (status<0)
            mAudioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);

        // mute code only follows
        if (status!= 0)
            return;

        // mute off
        if (mAudioManager.isStreamMute(AudioManager.STREAM_MUSIC))
            mAudioManager.adjustVolume(AudioManager.ADJUST_TOGGLE_MUTE, AudioManager.FLAG_PLAY_SOUND);

            // mute on
        else
            mAudioManager.adjustVolume(AudioManager.ADJUST_MUTE, AudioManager.FLAG_PLAY_SOUND);
    }


    public static int GetVolume(){

        if (mAudioManager==null)
            mAudioManager = (AudioManager) UnityPlayer.currentActivity.getSystemService(Context.AUDIO_SERVICE);


        if (mAudioManager.isStreamMute(AudioManager.STREAM_MUSIC))
            return 0;

        return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }



}
