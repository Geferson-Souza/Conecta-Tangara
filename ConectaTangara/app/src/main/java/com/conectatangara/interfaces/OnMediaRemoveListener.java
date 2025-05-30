package com.conectatangara.interfaces;

import android.net.Uri;

public interface OnMediaRemoveListener {
    void onMediaRemove(Uri mediaUri, int position);
}