package com.retor.busloader.lib;

/**
 * Created by retor on 29.12.2014.
 */
public interface TaskListener {

    final String LOAD = "Loading";
    final String FINISH = "Finish";
    final String ERROR = "Error";

    public void onLoadingStarted(String msg);

    public void onLoadingFinish(String msg);

    public void onLoadingError(String msg);

}
