//This file is deprecated.  Use the high level voip system instead:
// https://developer.oculus.com/documentation/platform/latest/concepts/dg-cc-voip/

#if false
using UnityEngine;
using System;

namespace Oculus.Platform
{
  public class MicrophoneInputNative : IMicrophone
  {
    IntPtr mic;

    int tempBufferSize = 960 * 10;
    float[] tempBuffer;

    public MicrophoneInputNative()
    {
      mic = CAPI.ovr_Microphone_Create();
      CAPI.ovr_Microphone_Start(mic);
      tempBuffer = new float[tempBufferSize];
      Debug.Log(mic);
    }

    public float[] Update()
    {
      ulong readSize = (ulong)CAPI.ovr_Microphone_ReadData(mic, tempBuffer, (UIntPtr)tempBufferSize);
      if (readSize > 0)
      {

        float[] outBuffer = new float[readSize];
        Array.Copy(tempBuffer, outBuffer, (int)readSize);
        return outBuffer;
      }
      return null;
    }

    public void Start()
    {

    }

    public void Stop()
    {
      CAPI.ovr_Microphone_Stop(mic);
      CAPI.ovr_Microphone_Destroy(mic);
    }
  }
}
#endif
