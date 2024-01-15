package com.example.nutritracker;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class WebAppInterface {
    Context mContext;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void recordEvent(String videoId, String eventType, long timeStamp) {
        // Handle the event recording here (e.g., logging play/pause events)
    }
}


