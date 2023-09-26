package com.kollectivemobile.euki.networking;

public interface EukiCallback<T> {
    void onSuccess(T t);
    void onError(ServerError serverError);
}
