# UnityAndroidVRBrowser
This is a fully functioning Android plugin in-game 3D web browser based on the GeckoView browser engine from Mozilla and works on the Oculus Go. It's adapted from Eyeflite's hands-free browser and you'll find lots of API calls intended for hands-free use. The demo project does not work with controller buttons other than the trigger, i.e. no scrolling with the controller. It should work on any android device, though you'll have to adapt/add in the Unity input module to support your device.

![video](https://raw.githubusercontent.com/IanPhilips/UnityAndroidVRBrowser/master/output.gif)


![screenshot](https://raw.githubusercontent.com/IanPhilips/UnityAndroidVRBrowser/master/webview.png)




## Usage
Open the `GazePointerWebviewScene_01` and use your head to aim and the controller's trigger to click. 

If you want to input text, connect your Oculus via usb and use the command: `adb shell input text 'yourtexthere'`

The demo supports two 'tabs' at the moment, also known as sessions in GeckoView lingo. You can switch between youtube and browser sessions which are essentially tabs in a browser. The demo and plugin are kind of hard-wired for these two special tabs atm though the wires are not very hard.

## In your own project
Currently you need: `UnityProject/Assets/Oculus` and `UnityProject/Assets/Scripts/BrowserView.cs` and `UnityProject/Assets/Scripts/UnityThread.cs` though a bit of configuration is needed for the `BrowserView.cs` to work properly, see the demo scene for guidance. Hopefully we can update the plugin from the Oculus folder to the most recent plugin and you won't have to use this older version package (1.30.0).
 
## Debugging
use the `AndroidUnity` tag in logcat to see the plugin's log output, i.e.:
`./adb logcat -s  ActivityManager PackageManager AndroidUnity Unity` 

## Technical Notes:

### Updating GeckoView  
1. Download the latest `.aar` from https://maven.mozilla.org/maven2/?prefix=maven2/org/mozilla/geckoview/geckoview-armeabi-v7a/ and place it in Unity's Plugin/Android/  
2. Update the .gradle file with the new version number: `geckoviewVersion = "65.0.20190201231734"`  
3. If pushing to the oculus store note: To remove the speech api reference: Change `.aar` to zip and unzip. Then change the classes.jar to classes.zip and unzip that. Remove the `InputOptionsUtil.class` file from the classes directory and re-jar and re-aar both via `jar cvf newLib.{jar, aar} -C libDirectory/ .`

### How to edit and compile your own Java plugin
Open the GeckoViewPlugin in android studio.

## TODO:
Generalize sessions for any #, not just youtube and browser  
Add VR keyboard  
Generalize input module for demo project  
Add trackpad scrolling  




