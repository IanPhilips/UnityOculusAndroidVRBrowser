# UnityAndroidVRBrowser


![screenshot](https://raw.githubusercontent.com/IanPhilips/UnityAndroidVRBrowser/master/Screen%20Shot%202018-12-01%20at%2010.02.13%20AM.png)

This is in `Beta` mode. It's been tested and works on the Oculus Go.
Youtube does not work, see: https://stackoverflow.com/questions/19273437/android-draw-youtube-video-on-surfacetexture

This uses the CPU to draw an Android webview onto a bitmap which is passed onto a Unity `RawImage`. This drawing code should be transferred to the GPU as in https://github.com/ArtemBogush/AndroidViewToGLRendering. You'll see the android project is based on that but I don't know opengl and there are some difficulties I didn't have time to overcome on passing graphics contexts to/from Unity. ( i.e. https://stackoverflow.com/questions/52088859/opengl-drawing-on-android-combining-with-unity-to-transfer-texture-through-frame)


Please feel free to improve/fix anything and submit a PR. There's lots of work to do!


## Clicking
If/when input is implemented, make sure the browser has pivot points set to (0.5,0.5) so the coordinates will translate correctly to the Android's `Webview` coordinates.


## How to edit the Java plugin
The code is in the unitylibrary module. The MainGL class is where most of the work is done, and uses the BitmapWebView's overrided draw method to get the webpage images. 

Then using the gradle menu, build unitylibrary. Take the new `unitylibrary-debug.aar` from ` ⁨UnityAndroidVRBrowser⁩/⁨AndroidViewToGLRendering-master⁩/⁨unitylibrary⁩/⁨build/⁨outputs/⁨aar⁩` and move it to `Assets/Plugins/Android/`


## TODO:
Improve this readme

Add oculus go controller interactions

Add trackpad and/or button scrolling

General debugging (some webpages don't work)



