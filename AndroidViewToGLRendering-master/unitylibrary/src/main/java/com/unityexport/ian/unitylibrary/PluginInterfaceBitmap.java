package com.unityexport.ian.unitylibrary;

public interface PluginInterfaceBitmap {
    public void onFrameUpdate(ReadData bytes, int width, int height, boolean canGoBack, boolean canGoForward );
    public void updateURL(String url);
    public void updateProgress(int progress, boolean canGoBack, boolean canGoForward  );
    // SetKeyboardVisibility does not work well:
    public void SetKeyboardVisibility(String visibile);
    public void DownloadFileRequestedAtURL(String path, String directory, String fileName, String url);
    public void FileDownloadComplete();
//    public void hasUpdatedImage();
//    public void OnPageLoaded();
//    public void canGo(String forward, String back);


}
