package com.eyeflite.ian.geckoviewplugin;

public interface UnityInterface {
//    public void updateTextureId(int texId);
//    public void onFrameUpdate(ReadData bytes, int width, int height, boolean canGoBack, boolean canGoForward );
    public void updateURL(String url);
    public void updateProgress(int progress );
    public void CanGoBack(boolean able);
    public void CanGoForward(boolean able);
    public void OnPageVisited(String url, String lastUrl);
    public void ChangeKeyboardVisiblity(boolean show);
    public void RestartInput();
    public void OnSessionCrash();
//    public void OnReadyForSurface();
    public void OnRuntimeShutdown();
    public void OnFullScreenRequestChange(boolean fullScreen);
    //    // SetKeyboardVisibility does not work well:
////    public void SetKeyboardVisibility(String visibile);
//    public void DownloadFileRequestedAtURL(String path, String directory, String fileName, String url);
//    public void FileDownloadComplete();
}

