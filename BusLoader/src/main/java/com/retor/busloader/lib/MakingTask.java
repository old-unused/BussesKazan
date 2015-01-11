package com.retor.busloader.lib;

import android.os.AsyncTask;
import com.retor.busloader.lib.interfaces.TaskListener;

import java.util.ArrayList;

/**
 * Created by retor on 29.12.2014.
 */
public class MakingTask extends AsyncTask<Void,Void,ArrayList<Bus>> {

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
    protected ArrayList<Bus> doInBackground(Void... params) {
        return loader.createArrayFromResponse(loader.getResponseString());
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ArrayList<Bus> array) {
        loader.setBuses(array);
        listener.onLoadingFinish(TaskListener.FINISH);
        super.onPostExecute(array);
    }
}
