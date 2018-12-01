package com.self.viewtoglrendering;

import android.opengl.GLSurfaceView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.self.viewtoglrendering.cuberenerer.CubeGLRenderer;
//import com.unityexport.ian.unitylibrary.Howdy;


public class MainActivity extends ActionBarActivity {

    private GLSurfaceView mGLSurfaceView;
    private GLRenderable mGLLinearLayout;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Howdy.DoSthInAndroid();
        //initViews();
    }

    private void initViews() {
        setContentView(R.layout.activity_main);
//
        ViewToGLRenderer viewToGlRenderer = new CubeGLRenderer(this);
//
        mGLSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
        mGLLinearLayout = (GLRenderable) findViewById(R.id.gl_layout);
        mWebView = (WebView) findViewById(R.id.web_view);
//
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(viewToGlRenderer);

        mGLLinearLayout.setViewToGLRenderer(viewToGlRenderer);

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setVisibility(View.VISIBLE);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        final String url = "https://www.google.com";
        runOnUiThread(new Runnable() {public void run() {
            if (mWebView == null) {
                return;
            }

                mWebView.loadUrl(url);;
            }
        });
       // mWebView.loadUrl("https://google.com"); //"http://stackoverflow.com/questions/12499396/is-it-possible-to-render-an-android-view-to-an-opengl-fbo-or-texture");
    }


}
