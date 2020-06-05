package com.example.myappsoc.ui.MidiInteraction;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myappsoc.R;

public class MidiInteractionFragment extends Fragment {
    private OnMidiInteractionListener callBack;


    private ImageButton sendData, pickFile, startStopSong, reverbOnOff;

    private EditText maxAllowedTracks;
    private TextView txtStatus;

    public interface OnMidiInteractionListener{
        void pickFileOnClick();
        void sendDataOnClik();
        void startStopSongOnClick();
        void sendReverbOnOff();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_midi_interaction, container, false);

        setupFrag(root);

        return root;
    }

    public void updateStatusTxt(String s){
        if(txtStatus!=null)
            txtStatus.setText(s);
    }


    public String getMaxAllowedTracks(){
        if(maxAllowedTracks!=null)
            return  maxAllowedTracks.getText().toString();

        return "";
    }

    public void setOnMidiInteractionListener(Activity activity){
        try { callBack = (OnMidiInteractionListener) activity; }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+
                    " must implement OnBlConnectionListener");
        }
    }


    private void setupFrag(View root){
        txtStatus = root.findViewById(R.id.status);
        pickFile = root.findViewById(R.id.pickFile);
        maxAllowedTracks = root.findViewById(R.id.maxAllowedTracks);
        sendData = root.findViewById(R.id.sendData);
        startStopSong = root.findViewById(R.id.onOffSong);
        reverbOnOff =   root.findViewById(R.id.reverbOnOff);

        if(maxAllowedTracks != null)
            maxAllowedTracks.setText("2");


        if(pickFile != null){
            pickFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.pickFileOnClick();
                }
            });

            pickFile.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
                        // true press, false release
                        boolean pressRelease = event.getAction() == MotionEvent.ACTION_DOWN;
                        if (pressRelease)
                            pickFile.setBackgroundResource(R.drawable.ic_selectmidifile);
                        else
                            pickFile.setBackgroundResource(R.drawable.ic_selectmidifile_color);
                    }

                    return false;
                }
            });


        }

        if(sendData != null){
            sendData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.sendDataOnClik();
                }
            });

            sendData.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
                        // true press, false release
                        boolean pressRelease = event.getAction() == MotionEvent.ACTION_DOWN;
                        if (pressRelease)
                            sendData.setBackgroundResource(R.drawable.ic_sendfile_2);
                        else
                            sendData.setBackgroundResource(R.drawable.ic_sendfile_color);
                    }

                    return false;
                }
            });
        }



        if(startStopSong != null){
            startStopSong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.startStopSongOnClick();
                }
            });

            startStopSong.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
                        // true press, false release
                        boolean pressRelease = event.getAction() == MotionEvent.ACTION_DOWN;
                        if (pressRelease)
                            startStopSong.setBackgroundResource(R.drawable.ic_songon_white);
                        else
                            startStopSong.setBackgroundResource(R.drawable.ic_songon_color);
                    }

                    return false;
                }
            });
        }


        if(reverbOnOff != null){
            reverbOnOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.sendReverbOnOff();
                }
            });

            reverbOnOff.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
                        // true press, false release
                        boolean pressRelease = event.getAction() == MotionEvent.ACTION_DOWN;
                        if (pressRelease)
                            reverbOnOff.setBackgroundResource(R.drawable.ic_reverb_white);
                        else
                            reverbOnOff.setBackgroundResource(R.drawable.ic_reverb_color);
                    }

                    return false;
                }
            });


        }



    }// setupFrag()

}// Class
