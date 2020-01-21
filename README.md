# UnityAndroidVRBrowser

## Updating GeckoView  
1. Download the latest `.aar` from https://maven.mozilla.org/maven2/?prefix=maven2/org/mozilla/geckoview/geckoview-armeabi-v7a/ and place it in Unity's Plugin/Android/  
2. Update the .gradle file with the new version number: `geckoviewVersion = "65.0.20190201231734"`  
3. If pushing to the oculus store note: To remove the speech api reference: Change `.aar` to zip and unzip. Then change the classes.jar to classes.zip and unzip that. Remove the `InputOptionsUtil.class` file from the classes directory and re-jar and re-aar both via `jar cvf newLib.{jar, aar} -C libDirectory/ .`

## Usage
Open the `GazePointerWebviewScene` and use your head to aim and the controller's trigger to click. Make sure the browser has pivot points set to (0.5,0.5) so click coordinates translate correctly to the Android's `WebView` coordinates.

If you want to input text, connect your Oculus via usb and use the command: `adb shell input text 'yourtexthere'`

## How to edit and compile your own Java plugin
Check out the GeckoViewPlugin android studio project.

## Debugging
use the `AndroidUnity` tag in logcat to see the plugin's log output, i.e.:
`./adb logcat -s  ActivityManager PackageManager DEBUG AndroidUnity Unity` 

## TODO:
Add VR keyboard  
Generalize input module for demo project  
Add trackpad scrolling  




