using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class SwitchUserSession : MonoBehaviour
{
    public BrowserView BrowserView;

    public BrowserView.BrowserHistoryType SessionType;
    // Start is called before the first frame update
    void Start()
    {
        GetComponent<Button>().onClick.AddListener(()=> BrowserView.SwitchSessionTo(SessionType));
    }

}
