using UnityEngine;
using UnityEditorInternal;

namespace UnityEditor.PackageManager.UI
{
    internal class PackageManagerPrefs
    {
        private const string showPreviewPackagesPrefs = "PackageManager.ShowPreviewPackages";
        private const string showPreviewPackagesWarningPrefs = "PackageManager.ShowPreviewPackagesWarning";

        public static bool ShowPreviewPackages
        {
            get { return EditorPrefs.GetBool(showPreviewPackagesPrefs, false); }
            set { EditorPrefs.SetBool(showPreviewPackagesPrefs, value); }
        }

        public static bool ShowPreviewPackagesWarning
        {
            get { return EditorPrefs.GetBool(showPreviewPackagesWarningPrefs, true); }
            set { EditorPrefs.SetBool(showPreviewPackagesWarningPrefs, value); }
        }
    }
}
