using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEditor;

[CustomEditor(typeof(OVRProjectConfig))]
public class OVRProjectConfigEditor : Editor
{
	override public void OnInspectorGUI()
	{
		OVRProjectConfig projectConfig = (OVRProjectConfig)target;
		DrawTargetDeviceInspector(projectConfig);
		EditorGUILayout.Space();
		DrawProjectConfigInspector(projectConfig);
	}

	public static void DrawTargetDeviceInspector(OVRProjectConfig projectConfig)
	{
		bool hasModified = false;

		// Target Devices
		EditorGUILayout.LabelField("Target Devices", EditorStyles.boldLabel);

		foreach (OVRProjectConfig.DeviceType deviceType in System.Enum.GetValues(typeof(OVRProjectConfig.DeviceType)))
		{
			bool oldSupportsDevice = projectConfig.targetDeviceTypes.Contains(deviceType);
			bool newSupportsDevice = oldSupportsDevice;
			OVREditorUtil.SetupBoolField(projectConfig, ObjectNames.NicifyVariableName(deviceType.ToString()), ref newSupportsDevice, ref hasModified);

			if (newSupportsDevice && !oldSupportsDevice)
			{
				projectConfig.targetDeviceTypes.Add(deviceType);
			}
			else if (oldSupportsDevice && !newSupportsDevice)
			{
				projectConfig.targetDeviceTypes.Remove(deviceType);
			}
		}

		if (hasModified)
		{
			OVRProjectConfig.CommitProjectConfig(projectConfig);
		}
	}

	public static void DrawProjectConfigInspector(OVRProjectConfig projectConfig)
	{
		bool hasModified = false;
		EditorGUI.BeginDisabledGroup(!projectConfig.targetDeviceTypes.Contains(OVRProjectConfig.DeviceType.Quest));
		EditorGUILayout.LabelField("Quest Features", EditorStyles.boldLabel);

		// Show overlay support option
		OVREditorUtil.SetupBoolField(projectConfig, new GUIContent("Focus Aware",
			"If checked, the new overlay will be displayed when the user presses the home button. The game will not be paused, but will now receive InputFocusLost and InputFocusAcquired events."),
			ref projectConfig.focusAware, ref hasModified);

		// Hand Tracking Support
		OVREditorUtil.SetupEnumField(projectConfig, "Hand Tracking Support", ref projectConfig.handTrackingSupport, ref hasModified);

		EditorGUI.EndDisabledGroup();
		EditorGUILayout.Space();

		EditorGUI.BeginDisabledGroup(false);
		EditorGUILayout.LabelField("Android Build Settings", EditorStyles.boldLabel);

		// Show overlay support option
		OVREditorUtil.SetupBoolField(projectConfig, new GUIContent("Skip Unneeded Shaders",
			"If checked, prevent building shaders that are not used by default to reduce time spent when building."),
			ref projectConfig.skipUnneededShaders, ref hasModified);

		EditorGUI.EndDisabledGroup();
		EditorGUILayout.Space();

		EditorGUILayout.LabelField("Security", EditorStyles.boldLabel);
		OVREditorUtil.SetupInputField(projectConfig, "Custom Security XML Path", ref projectConfig.securityXmlPath, ref hasModified);
		OVREditorUtil.SetupBoolField(projectConfig, "Disable Backups", ref projectConfig.disableBackups, ref hasModified);
		OVREditorUtil.SetupBoolField(projectConfig, "Enable NSC Configuration", ref projectConfig.enableNSCConfig, ref hasModified);

		// apply any pending changes to project config
		if (hasModified)
		{
			OVRProjectConfig.CommitProjectConfig(projectConfig);
		}
	}
}
