# UnityAndroidVRBrowser


![screenshot](https://raw.githubusercontent.com/IanPhilips/UnityAndroidVRBrowser/master/webview.png)


![video](https://raw.githubusercontent.com/IanPhilips/UnityAndroidVRBrowser/master/output.gif)

This is in `Beta` mode. It's been tested and works on the Oculus Go. Requires Android API 25. If you can answer [this](https://stackoverflow.com/questions/54224025/surfacetexture-in-android-plugin-doesnt-work-in-unity-eth-reward/54311593) question you'd be extremely helpful to the improvement of this repo.

## Usage
Open the `GazePointerWebviewScene` and use your head to aim and the controller's trigger to click. Make sure the browser has pivot points set to (0.5,0.5) so click coordinates translate correctly to the Android's `WebView` coordinates.

If you want to input text, connect your Oculus via usb and use the command: `adb shell input text 'yourtexthere'`


## Importing into your own project
Copy `BrowserView.cs` and  `UnityThread.cs` to your scripts directory, and `unitylibrary-debug.aar`([precompiled](https://github.com/IanPhilips/UnityAndroidVRBrowser/releases/)) to your Plugins/Android/ directory. Fill `BrowserView.cs`'s public fields appropriately. You must set your Android API version to 25 (in player settings).


## Notes
Youtube does not work, see [this.](https://stackoverflow.com/questions/19273437/android-draw-youtube-video-on-surfacetexture)

The CPU draws an Android webview onto a bitmap, [converts it to a png](https://stackoverflow.com/questions/52101948/android-bitmap-image-to-unity-c-sharp-texture), then passes that onto a Unity `RawImage`. This drawing code should be transferred to the GPU as in [here]( https://github.com/ArtemBogush/AndroidViewToGLRendering). You'll see the android project is based on that but I don't know opengl and there are some difficulties I didn't have time to overcome on passing graphics contexts to/from Unity. ( i.e. [this](https://stackoverflow.com/questions/52088859/opengl-drawing-on-android-combining-with-unity-to-transfer-texture-through-frame))

The plugin uses `WebView.enableSlowWholeDocumentDraw()` before inflating the view. This slows down the loading process for a webpage but it's the [only way](https://stackoverflow.com/questions/52782166/programmatic-scroll-of-webview-isnt-reflected-when-drawing-from-canvas) I could scroll the view programmatically and still be able to see the webpage beyond the initial dimensions of the screen.

Please feel free to improve/fix anything and submit a PR. There's lots of work to do!


## How to edit and compile your own Java plugin
The code is in the unitylibrary module. The MainGL class is where most of the work is done, and uses the BitmapWebView's overrided draw method to get the webpage images. 

After modifying, use the gradle menu to build unitylibrary. Take the new `unitylibrary-debug.aar` from ` ⁨UnityAndroidVRBrowser⁩/⁨AndroidViewToGLRendering-master⁩/⁨unitylibrary⁩/⁨build/⁨outputs/⁨aar⁩` and move it to `Assets/Plugins/Android/`

## Debugging
use the `AndroidUnity` tag in logcat to see the plugin's log output, i.e.:
`./adb logcat -s  ActivityManager PackageManager DEBUG AndroidUnity Unity` 


## TODO:
Add VR keyboard  
Generalize input module for demo project  
Test Javascript scrolling to avoid `WebView.enableSlowWholeDocumentDraw()`  
Add trackpad scrolling  



