/**
 *  Author: Fernando Candelario Herrero
 *
 *  Comments:
 *       This app it's part of my final project of my university studies,
 *       degree of Computer engineering, UCM Madrid
 *
 */
package com.example.myappsoc.ui.BlConnection;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myappsoc.R;

public class BlConnectionFragment extends Fragment {

    private OnBlConnectionListener callBack;

    private ImageButton showPairedDev, disconnet;
    private TextView txtStatus;
    private ListView listPairedDevs;


    public interface OnBlConnectionListener{
        void showPairedDevicesOnClick();
        void selectDeviceOnItemClick(int pos);
        void disconnetOnClick();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_bl_connection, container, false);

        setupFrag(root);

        return root;
    }


    public void updateStatusTxt(String s){
        if(txtStatus!=null)
            txtStatus.setText(s);
    }

    public void updateListPairedDevices(ArrayAdapter l){
        if(listPairedDevs!=null)
            listPairedDevs.setAdapter(l);
    }


    public void setOnBlConnectionListener(Activity activity){
        try { callBack = (OnBlConnectionListener) activity; }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+
                    " must implement OnBlConnectionListener");
        }
    }


    private void setupFrag(View root){
        txtStatus = root.findViewById(R.id.status);
        showPairedDev = root.findViewById(R.id.showPairedDev);
        listPairedDevs = root.findViewById(R.id.listPairedDevs);
        disconnet = root.findViewById(R.id.disconnet);


        if(showPairedDev != null){
            showPairedDev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.showPairedDevicesOnClick();
                }
            });

            showPairedDev.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
                        // true press, false release
                        boolean pressRelease = event.getAction() == MotionEvent.ACTION_DOWN;
                        if (pressRelease)
                            showPairedDev.setBackgroundResource(R.drawable.ic_showpaireddevices);
                        else
                            showPairedDev.setBackgroundResource(R.drawable.ic_showpaireddevices_color);
                    }

                    return false;
                }
            });

        }

        if(listPairedDevs != null){
            listPairedDevs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    callBack.selectDeviceOnItemClick(position);
                }
            });
        }


        if(disconnet != null){
            disconnet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.disconnetOnClick();
                }
            });

            disconnet.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
                        // true press, false release
                        boolean pressRelease = event.getAction() == MotionEvent.ACTION_DOWN;
                        if (pressRelease)
                            disconnet.setBackgroundResource(R.drawable.ic_discconection_2_white);
                        else
                            disconnet.setBackgroundResource(R.drawable.ic_discconection_2_color);
                    }

                    return false;
                }
            });
        }

    }// setupFrag()


}// Class