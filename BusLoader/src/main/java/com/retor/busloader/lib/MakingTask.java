package com.retor.busloader.lib;

import android.os.AsyncTask;
import com.retor.busloader.lib.interfaces.TaskListener;

/**
 * Created by retor on 29.12.2014.
 */
public class MakingTask extends AsyncTask<Void,Void,Void> {

    TaskListener listener;
    Loader loader;

    public MakingTask(TaskListener listener, Loader loader) {
        this.listener = listener;
        this.loader = loader;
    }

    @Override
    protected void onPreExecute() {
        listener.onLoadingStarted(TaskListener.LOAD);
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        loader.setBuses(loader.createArrayFromResponse(loader.getResponseString()));
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        listener.onLoadingFinish(TaskListener.FINISH);
        super.onPostExecute(aVoid);
    }
}
