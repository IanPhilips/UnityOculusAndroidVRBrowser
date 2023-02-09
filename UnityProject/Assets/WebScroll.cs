using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class WebScroll : MonoBehaviour
{
    public BrowserView browserView;
    OVRInput.Axis2D axis2D = OVRInput.Axis2D.PrimaryThumbstick;

    // get y axis input from ovr thumbstick 
    private void Update() {
        var thumbstickInput = OVRInput.Get(axis2D);
    
        // if thumbstick is not in neutral position
        if (thumbstickInput.y != 0) {
            // scroll up or down
            browserView.InvokeScrollUp(thumbstickInput.y);
        }
    }
}
