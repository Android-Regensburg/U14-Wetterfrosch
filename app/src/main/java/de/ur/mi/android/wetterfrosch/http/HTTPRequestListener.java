package de.ur.mi.android.wetterfrosch.http;


public interface HTTPRequestListener {

    void onResult(String result);
    void onError(String errorMessage);

}