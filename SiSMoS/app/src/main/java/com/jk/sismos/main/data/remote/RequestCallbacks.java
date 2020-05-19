package com.jk.sismos.main.data.remote;

import androidx.annotation.NonNull;

public interface RequestCallbacks {
    void onSuccess(@NonNull String value);

    void onError(@NonNull Throwable throwable);
}
