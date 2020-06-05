/**
 *  Author: Fernando Candelario Herrero
 *
 *  Comments:
 *       This app it's part of my final project of my university studies,
 *       degree of Computer engineering, UCM Madrid
 *
 */
package com.example.myappsoc;

import java.util.UUID;

public class MyConstants {

    public static final int NUM_KEYS = 12;

    public static final int DO_INDEX = 0;
    public static final int DO_SHARP_INDEX = 1;
    public static final int RE_INDEX = 2;
    public static final int RE_SHARP_INDEX = 3;

    public static final int MI_INDEX = 4;
    public static final int FA_INDEX = 5;
    public static final int FA_SHARP_INDEX = 6;
    public static final int SOL_INDEX = 7;

    public static final int SOL_SHARP_INDEX = 8;
    public static final int LA_INDEX = 9;
    public static final int LA_SHARP_INDEX = 10;
    public static final int SI_INDEX = 11;

    public static final boolean WHITE_KEY = true;
    public static final boolean BLACK_KEY = false;

    public static final String[] COMBO_VALUES= {"Very soft", "Soft", "Normal", "Hard", "Very hard"};


    public static final String[] STATUS_MSG = { "BLUETOOTH ON", "BLUETOOTH OFF", "CONNECTING...", "CONNECTED",
                                                "CONNECTION FAILED", "CONNECTION LOST", "UNKNOWN STATUS" };

    public static final int STATE_BL_ON = 0;
    public static final int STATE_BL_OFF = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;
    public static final int STATE_CONNECTION_FAILED = 4;
    public static final int STATE_CONNECTION_LOST = 5;
    public static final int UNKNOW_STATE = 6;

    public static final int THREAD_SLEEP_TIME = 1000; // ms

    // To check txt of nº of midi tracks
    public static final String REG_X = "[0-9]+";

    // For OS services request
    public static final int REQ_CODE_ENGABLE_BL = 1;
    public static final int REQ_CODE_FILE_MANAGER = 2;

    // For keyboard octave
    public static final int MAX_OCTAVE = 8;
    public static final int MIN_OCTAVE = 0;

    // UUID found in
    //https://stackoverflow.com/questions/36785985/buetooth-connection-failed-read-failed-socket-might-closed-or-timeout-read-re?answertab=active
    public  static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    public static final byte ON_CMD = 0x02;
    public static final byte OFF_CMD = 0x01;
    public static final byte RECV_FILE_MODE_ON_OFF = 0x67;
    public static final byte ON_OFF_SONG = 0x7e;
    public static final byte REVERB_ON_OFF = 0x5F;

    public static final byte[] VEL = {0x01, 0x02, 0x00, 0x04, 0x08};

    public static final byte[] KEYBOARD_CODES = {   0x15, 0x16, 0x17, // Octava 0
            0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f, 0x20, 0x21, 0x22, 0x23, // Octava 1
            0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2a, 0x2b, 0x2c, 0x2d, 0x2e, 0x2f, // Octava 2
            0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x3a, 0x3b, // Octava 3
            0x3c, 0x3d, 0x3e, 0x3f, 0x40, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, // Octava 4
            0x48, 0x49, 0x4a, 0x4b, 0x4c, 0x4d, 0x4e, 0x4f, 0x50, 0x51, 0x52, 0x53, // Octava 5
            0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 0x5a, 0x5b, 0x5c, 0x5d, 0x5e, 0x5f, // Octava 6
            0x60, 0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6a, 0x6b, // Octava 7
            0x6c // Octava 8
    };


}
