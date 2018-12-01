//This file is deprecated.  Use the high level voip system instead:
// https://developer.oculus.com/documentation/platform/latest/concepts/dg-cc-voip/
//
// NOTE for android developers: The existence of UnityEngine.Microphone causes Unity to insert the 
// android.permission.RECORD_AUDIO permission into the AndroidManifest.xml generated at build time

#if false
using UnityEngine;

namespace Oculus.Platform
{

  public class MicrophoneInput : IMicrophone
  {
    AudioClip microphoneClip;
    int lastMicrophoneSample;
    int micBufferSizeSamples;

    public MicrophoneInput()
    {
      int bufferLenSeconds = 1; //minimum size unity allows
      int inputFreq = 48000; //this frequency is fixed throughout the voip system atm
      microphoneClip = Microphone.Start(null, true, bufferLenSeconds, inputFreq);
      micBufferSizeSamples = bufferLenSeconds * inputFreq;
    }

    public void Start()
    {

    }

    public void Stop()
    {
    }

    public float[] Update()
    {
      int pos = Microphone.GetPosition(null);
      int copySize = 0;
      if (pos < lastMicrophoneSample)
      {
        int endOfBufferSize = micBufferSizeSamples - lastMicrophoneSample;
        copySize = endOfBufferSize + pos;
      }
      else
      {
        copySize = pos - lastMicrophoneSample;
      }

      if (copySize == 0) {
        return null;
      }

      float[] samples = new float[copySize]; //TODO 10376403 - pool this
      microphoneClip.GetData(samples, lastMicrophoneSample);
      lastMicrophoneSample = pos;
      return samples;

    }
  }
}
#endif
