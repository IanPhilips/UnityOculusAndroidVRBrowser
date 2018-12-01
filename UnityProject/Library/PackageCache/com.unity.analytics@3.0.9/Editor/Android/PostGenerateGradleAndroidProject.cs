#if UNITY_ANDROID
using UnityEditor.Android;

namespace UnityEditor.Analytics
{
	public class PostGenerateGradleAndroidProject : IPostGenerateGradleAndroidProject
	{
		public int callbackOrder { get { return 0; } }

		public void OnPostGenerateGradleAndroidProject(string path)
		{
			PatchAndroidManifest.PatchManifest(path);
		}
	}
}
#endif