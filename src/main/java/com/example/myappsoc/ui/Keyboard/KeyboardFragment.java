/**
 *  Author: Fernando Candelario Herrero
 *
 *  Comments:
 *       This app it's part of my final project of my university studies,
 *       degree of Computer engineering, UCM Madrid
 *
 */
package com.example.myappsoc.ui.Keyboard;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myappsoc.MyConstants;
import com.example.myappsoc.R;

import static android.R.layout.simple_spinner_item;

public class KeyboardFragment extends Fragment {

    private OnKeyboardListener callBack;

    private ImageButton  increaseOctave, decreaseOctave;
    private TextView connectionStatus, keyboardStatus;
    private Spinner combo_box;
    private ImageButton keys[];

    public interface OnKeyboardListener{
        void decraseOctaveOnClick();
        void increaseOctaveOnClick();
        void onPressRelease(boolean b, int keyCode);
        void onItemSelected(int pos);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_keyboard, container, false);

        setupFrag(root);

        return root;
    }

    public void updateStatusTxt(String s){
        if(connectionStatus!=null)
            connectionStatus.setText(s);
    }

    public void updateKeyboardOctaveTxt(int n){
        if(keyboardStatus!=null)
            keyboardStatus.setText("Octave " + n);
    }

    public void setOnKeyboardListener(Activity activity){
        try { callBack = (OnKeyboardListener) activity; }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+
                    " must implement OnKeyboardListener");
        }
    }


    private void setupFrag(View root) {
        keys = new ImageButton[MyConstants.NUM_KEYS];

        connectionStatus = root.findViewById(R.id.status);
        keyboardStatus = root.findViewById(R.id.keyboard_octave);
        increaseOctave = root.findViewById(R.id.increase_octave);
        decreaseOctave = root.findViewById(R.id.decrease_octave);
        combo_box = root.findViewById(R.id.combo_box);

        keys[0] = root.findViewById(R.id.doBt);
        keys[1] = root.findViewById(R.id.doSharpBt);
        keys[2] = root.findViewById(R.id.reBt);
        keys[3] = root.findViewById(R.id.reSharpBt);

        keys[4] = root.findViewById(R.id.miBt);
        keys[5] = root.findViewById(R.id.faBt);
        keys[6] = root.findViewById(R.id.faSharpBt);
        keys[7] = root.findViewById(R.id.solBt);

        keys[8] = root.findViewById(R.id.solSharpBt);
        keys[9] = root.findViewById(R.id.laBt);
        keys[10] = root.findViewById(R.id.laSharpBt);
        keys[11] = root.findViewById(R.id.siBt);


        if(increaseOctave != null){
            increaseOctave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.increaseOctaveOnClick();
                }
            });

            increaseOctave.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
                        // true press, false release
                        boolean pressRelease = event.getAction() == MotionEvent.ACTION_DOWN;
                        if (pressRelease)
                            increaseOctave.setBackgroundResource(R.drawable.ic_increase8va_press);
                        else
                            increaseOctave.setBackgroundResource(R.drawable.ic_increase8va);
                    }

                    return false;
                }
            });

        }

        if(decreaseOctave != null){
            decreaseOctave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.decraseOctaveOnClick();
                }
            });

            decreaseOctave.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
                        // true press, false release
                        boolean pressRelease = event.getAction() == MotionEvent.ACTION_DOWN;
                        if (pressRelease)
                            decreaseOctave.setBackgroundResource(R.drawable.ic_decrease8va_press);
                        else
                            decreaseOctave.setBackgroundResource(R.drawable.ic_decrease8va);
                    }

                    return false;
                }
            });

        }

        ArrayAdapter<String> a = new ArrayAdapter<String>(getContext(),simple_spinner_item,MyConstants.COMBO_VALUES);
        combo_box.setDropDownHorizontalOffset(android.R.layout.simple_spinner_dropdown_item);
        combo_box.setAdapter(a);

        combo_box.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                callBack.onItemSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No use
            }
        });

        defineKeysBehaviour();
    }

    private void defineKeysBehaviour() {

        keys[MyConstants.DO_INDEX].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP){
                    // true press, false release
                    boolean pressRelease = event.getAction() == MotionEvent.ACTION_DOWN;
                    if(pressRelease)
                        keys[MyConstants.DO_INDEX].setBackgroundResource(R.drawable.ic_presswhite);
                    else
                        keys[MyConstants.DO_INDEX].setBackgroundResource(R.drawable.ic_whitekey_2);
                    callBack.onPressRelease(pressRelease, MyConstants.DO_INDEX);
                }
                return true;
            }
        });

        keys[MyConstants.DO_SHARP_INDEX].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP){
                    // true press, false release
                    boolean pressRelease = event.getAction() == MotionEvent.ACTION_DOWN;
                    if(pressRelease)
                        keys[MyConstants.DO_SHARP_INDEX].setBackgroundResource(R.drawable.ic_pressblack);
                    else
                        keys[MyConstants.DO_SHARP_INDEX].setBackgroundResource(R.drawable.ic_blackkey);
                    callBack.onPressRelease(pressRelease, MyConstants.DO_SHARP_INDEX);
                }

                return true;
            }
        });

        keys[MyConstants.RE_INDEX].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP){
                    // true press, false release
                    boolean pressRelease = event.getAction() == MotionEvent.ACTION_DOWN;
                    if(pressRelease)
                        keys[MyConstants.RE_INDEX].setBackgroundResource(R.drawable.ic_presswhite);
                    else
                        keys[MyConstants.RE_INDEX].setBackgroundResource(R.drawable.ic_whitekey_2);
                    callBack.onPressRelease(pressRelease, MyConstants.RE_INDEX);
                }

                return true;
            }
        });

        keys[MyConstants.RE_SHARP_INDEX].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP){
                    // true press, false release
                    boolean pressRelease = event.getAction() == MotionEvent.ACTION_DOWN;
                    if(pressRelease)
                        keys[MyConstants.RE_SHARP_INDEX].setBackgroundResource(R.drawable.ic_pressblack);
                    else
                        keys[MyConstants.RE_SHARP_INDEX].setBackgroundResource(R.drawable.ic_blackkey);
                    callBack.onPressRelease(pressRelease, MyConstants.RE_SHARP_INDEX);
                }

                return true;
            }
        });





        keys[MyConstants.MI_INDEX].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP){
                    // true press, false release
                    boolean pressRelease = event.getAction() == MotionEvent.ACTION_DOWN;
                    if(pressRelease)
                        keys[MyConstants.MI_INDEX].setBackgroundResource(R.drawable.ic_presswhite);
                    else
                        keys[MyConstants.MI_INDEX].setBackgroundResource(R.drawable.ic_whitekey_2);
                    callBack.onPressRelease(pressRelease, MyConstants.MI_INDEX);
                }

                return true;
            }
        });

        keys[MyConstants.FA_INDEX].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP){
                    // true press, false release
                    boolean pressRelease = event.getAction() == MotionEvent.ACTION_DOWN;
                    if(pressRelease)
                        keys[MyConstants.FA_INDEX].setBackgroundResource(R.drawable.ic_presswhite);
                    else
                        keys[MyConstants.FA_INDEX].setBackgroundResource(R.drawable.ic_whitekey_2);
                    callBack.onPressRelease(pressRelease, MyConstants.FA_INDEX);
                }

                return true;
            }
        });

        keys[MyConstants.FA_SHARP_INDEX].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP){
                    // true press, false release
                    boolean pressRelease = event.getAction() == MotionEvent.ACTION_DOWN;
                    if(pressRelease)
                        keys[MyConstants.FA_SHARP_INDEX].setBackgroundResource(R.drawable.ic_pressblack);
                    else
                        keys[MyConstants.FA_SHARP_INDEX].setBackgroundResource(R.drawable.ic_blackkey);
                    callBack.onPressRelease(pressRelease, MyConstants.FA_SHARP_INDEX);
                }

                return true;
            }
        });

        keys[MyConstants.SOL_INDEX].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP){
                    // true press, false release
                    boolean pressRelease = event.getAction() == MotionEvent.ACTION_DOWN;
                    if(pressRelease)
                        keys[MyConstants.SOL_INDEX].setBackgroundResource(R.drawable.ic_presswhite);
                    else
                        keys[MyConstants.SOL_INDEX].setBackgroundResource(R.drawable.ic_whitekey_2);
                    callBack.onPressRelease(pressRelease, MyConstants.SOL_INDEX);
                }

                return true;
            }
        });





        keys[MyConstants.SOL_SHARP_INDEX].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP){
                    // true press, false release
                    boolean pressRelease = event.getAction() == MotionEvent.ACTION_DOWN;
                    if(pressRelease)
                        keys[MyConstants.SOL_SHARP_INDEX].setBackgroundResource(R.drawable.ic_pressblack);
                    else
                        keys[MyConstants.SOL_SHARP_INDEX].setBackgroundResource(R.drawable.ic_blackkey);
                    callBack.onPressRelease(pressRelease, MyConstants.SOL_SHARP_INDEX);
                }

                return true;
            }
        });

        keys[MyConstants.LA_INDEX].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP){
                    // true press, false release
                    boolean pressRelease = event.getAction() == MotionEvent.ACTION_DOWN;
                    if(pressRelease)
                        keys[MyConstants.LA_INDEX].setBackgroundResource(R.drawable.ic_presswhite);
                    else
                        keys[MyConstants.LA_INDEX].setBackgroundResource(R.drawable.ic_whitekey_2);
                    callBack.onPressRelease(pressRelease, MyConstants.LA_INDEX);
                }

                return true;
            }
        });

        keys[MyConstants.LA_SHARP_INDEX].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP){
                    // true press, false release
                    boolean pressRelease = event.getAction() == MotionEvent.ACTION_DOWN;
                    if(pressRelease)
                        keys[MyConstants.LA_SHARP_INDEX].setBackgroundResource(R.drawable.ic_pressblack);
                    else
                        keys[MyConstants.LA_SHARP_INDEX].setBackgroundResource(R.drawable.ic_blackkey);
                    callBack.onPressRelease(pressRelease, MyConstants.LA_SHARP_INDEX);
                }

                return true;
            }
        });

        keys[MyConstants.SI_INDEX].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP){
                    // true press, false release
                    boolean pressRelease = event.getAction() == MotionEvent.ACTION_DOWN;
                    if(pressRelease)
                        keys[MyConstants.SI_INDEX].setBackgroundResource(R.drawable.ic_presswhite);
                    else
                        keys[MyConstants.SI_INDEX].setBackgroundResource(R.drawable.ic_whitekey_2);
                    callBack.onPressRelease(pressRelease, MyConstants.SI_INDEX);
                }

                return true;
            }
        });
    }// defineKeysBehaviour()

}