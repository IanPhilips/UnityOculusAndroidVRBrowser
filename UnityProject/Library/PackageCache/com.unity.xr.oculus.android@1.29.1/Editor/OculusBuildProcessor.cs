#if UNITY_ANDROID
using System.Reflection;
using System.Xml;
using UnityEngine;
using UnityEngine.Rendering;
using UnityEditor.Android;
using UnityEditor.Build;
using UnityEditor.Build.Reporting;

namespace UnityEditor.XR.Oculus
{
    internal class OculusManifest : IPostGenerateGradleAndroidProject
    {
        static readonly string k_AndroidURI = "http://schemas.android.com/apk/res/android";

        static readonly string k_AndroidManifestPath = "/src/main/AndroidManifest.xml";

        void UpdateOrCreateAttributeInTag(XmlDocument doc, string parentPath, string tag, string name, string value)
        {
            var xmlNode = doc.SelectSingleNode(parentPath + "/" + tag);

            if (xmlNode != null)
            {
                ((XmlElement)xmlNode).SetAttribute(name, k_AndroidURI, value);
            }
        }

        void UpdateOrCreateNameValueElementsInTag(XmlDocument doc, string parentPath, string tag,
            string firstName, string firstValue, string secondName, string secondValue)
        {
            var xmlNodeList = doc.SelectNodes(parentPath + "/" + tag);

            foreach (XmlNode node in xmlNodeList)
            {
                var attributeList = ((XmlElement)node).Attributes;

                foreach (XmlAttribute attrib in attributeList)
                {
                    if (attrib.Value == firstValue)
                    {
                        var valueSibling = attrib.NextSibling;
                        valueSibling.Value = secondValue;
                        return;
                    }
                }
            }
            
            // Didn't find any attributes that matched, create both
            XmlElement childElement = doc.CreateElement(tag);
            childElement.SetAttribute(firstName, k_AndroidURI, firstValue);
            childElement.SetAttribute(secondName, k_AndroidURI, secondValue);

            var xmlParentNode = doc.SelectSingleNode(parentPath);

            if (xmlParentNode != null)
            {
                xmlParentNode.AppendChild(childElement);
            }
        }

        public void OnPostGenerateGradleAndroidProject(string path)
        {
            var manifestPath = path + k_AndroidManifestPath;
            var manifestDoc = new XmlDocument();
            manifestDoc.Load(manifestPath);

            var sdkVersion = (int)PlayerSettings.Android.minSdkVersion;

            UpdateOrCreateAttributeInTag(manifestDoc, "/", "manifest", "installLocation", "auto");

            var nodePath = "/manifest/application";
            UpdateOrCreateNameValueElementsInTag(manifestDoc, nodePath, "meta-data", "name", "com.samsung.android.vr.application.mode", "value", "vr_only");

            nodePath = "/manifest/application";
            UpdateOrCreateAttributeInTag(manifestDoc, nodePath, "activity", "screenOrientation", "landscape");
            UpdateOrCreateAttributeInTag(manifestDoc, nodePath, "activity", "theme", "@android:style/Theme.Black.NoTitleBar.Fullscreen");

            var configChangesValue = "keyboard|keyboardHidden|navigation|orientation|screenLayout|screenSize|uiMode";
            configChangesValue = ((sdkVersion >= 24) ? configChangesValue + "|density" : configChangesValue);
            UpdateOrCreateAttributeInTag(manifestDoc, nodePath, "activity", "configChanges", configChangesValue);

            if (sdkVersion >= 24)
            {
                UpdateOrCreateAttributeInTag(manifestDoc, nodePath, "activity", "resizeableActivity", "false");
            }

            UpdateOrCreateAttributeInTag(manifestDoc, nodePath, "activity", "launchMode", "singleTask");

	        // Uncomment the following block to add additional manifest entries required for store release submissions
            
            //nodePath = "/manifest/application";
            //UpdateOrCreateAttributeInTag(manifestDoc, nodePath, "activity", "excludeFromRecents", "true");

            //nodePath = "/manifest/application/activity/intent-filter";
            //UpdateOrCreateAttributeInTag(manifestDoc, nodePath, "action", "name", "android.intent.action.MAIN");
            //UpdateOrCreateAttributeInTag(manifestDoc, nodePath, "category", "name", "android.intent.category.INFO");

            //nodePath = "/manifest/application";
            //UpdateOrCreateNameValueElementsInTag(manifestDoc, nodePath, "meta-data", "name", "unityplayer.SkipPermissionsDialog", "value", "false");
            
            manifestDoc.Save(manifestPath);
        }

        public int callbackOrder { get { return 2; } }

        void DebugPrint(XmlDocument doc)
        {
            var sw = new System.IO.StringWriter();
            var xw = XmlWriter.Create(sw);
            doc.Save(xw);
            Debug.Log(sw);
        }
    }
}
#endif
