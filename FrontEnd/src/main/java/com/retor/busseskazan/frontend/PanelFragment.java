package com.retor.busseskazan.frontend;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.retor.busloader.lib.Bus;
import com.retor.busloader.lib.Loader;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by retor on 30.12.2014.
 */
public class PanelFragment extends Fragment {

    Spinner spinner;
    FragmentActivity parent;
    Loader loader;
    ArrayAdapter<String> adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = getActivity();
/*        loader = Loader.getInstance(getActivity(), "http://data.kzn.ru:8082/api/v0/dynamic_datasets/bus.json");
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, getBusNumbers(loader.getBuses()));*/
    }

    private ArrayList<String> getBusNumbers(ArrayList<Bus> array) {
        ArrayList<String> out = new ArrayList<>();
        if (array != null) {
            for (Bus b : array) {
                if (!out.contains(b.getMarsh()))
                    out.add(b.getMarsh());
            }
        }
        Collections.sort(out);
        if (out.contains("0"))
            out.remove("0");
        return out;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.panel_fragment, container);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        spinner = (Spinner)view.findViewById(R.id.spinnerMarshrut);
        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
