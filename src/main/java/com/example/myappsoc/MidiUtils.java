/*
 *  Author: Fernando Candelario Herrero
 *
 *  Comments:
 *       This app it's part of my final project of my university studies,
 *       degree of Computer engineering, UCM Madrid
 *
 *       This app sends data of midi files by a Bluetooth connection
 *
 * */
package com.example.myappsoc;

import java.io.IOException;
import java.io.InputStream;

public class MidiUtils {



    public static final byte[] HEADER_CHUNK_MARK = {0x4d, 0x54, 0x68, 0x64};
    public static final byte[] LENGTH_OF_HEADER_CHUNK = {0x00, 0x00, 0x00, 0x06};

    public MidiUtils(){
    }

    public String checkFileHeader(InputStream streamIn, int maxNumTracksAllowed) throws IOException {
        int i;
        int data;
        byte c;
        boolean formatZeroFlag = false;

        // Check Header chunk mark
        if(!checkFourBytes(streamIn, HEADER_CHUNK_MARK))
            return "header chunk mark doesn't match";


        // Check Lenght of header data
        if(!checkFourBytes(streamIn, LENGTH_OF_HEADER_CHUNK))
            return "lenght of header chunk doesn't match";


        // Check file format
        if( (byte)streamIn.read() != 0x00 )
            return "not valid format";


        c=(byte)streamIn.read();
        if( c != 0x00 && c != 0x01)
            return "not valid format, only format 0 and format 1 are allowed";

        if(c==0x00)
            formatZeroFlag = true;

        // Check ntrks, num of file tracks
        data= 0;

        c=(byte)streamIn.read();
        if((int)c==-1)
            return "end of file reached, this file is not a midi file";

        data |= c;

        c=(byte)streamIn.read();
        if((int)c==-1)
            return "end of file reached, this file is not a midi file";

        data <<=8;
        data |= c;

        if( !((formatZeroFlag && data == 1) || (!formatZeroFlag && data<=maxNumTracksAllowed && data > 0)) )
            return "number of tracks is not correct, nº of tracks in the file: " + data;


        // Read division
        c=(byte)streamIn.read();
        if((int)c==-1)
            return "end of file reached, this file is not a midi file";

        if( (c & 0x80) != 0x00)
           return "division format is not valid";

        return null;
    }


    private boolean checkFourBytes(InputStream streamIn, byte[] targetData) throws IOException {
        int i = 0;
        byte c;
        boolean ok = true;

        while(ok && i < 4){
            c = (byte)streamIn.read();
            ok = !( (int)c ==-1 || c != targetData[i++]);
        }

        return ok;
    }

}// Class
