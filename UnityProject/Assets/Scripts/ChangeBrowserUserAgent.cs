using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ChangeBrowserUserAgent : MonoBehaviour
{
    public BrowserView BrowserView;

    public BrowserView.UserAgent UserAgent;
    // Start is called before the first frame update
    void Start()
    {
        GetComponent<Button>().onClick.AddListener(()=> BrowserView.InvokeToggleUserAgentThenZoomOut());
    }


}
