/**
 *  Author: Fernando Candelario Herrero
 *
 *  Comments:
 *       This app it's part of my final project of my university studies,
 *       degree of Computer engineering, UCM Madrid
 *
 */
package com.example.myappsoc;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myappsoc.ui.BlConnection.BlConnectionFragment;
import com.example.myappsoc.ui.Keyboard.KeyboardFragment;
import com.example.myappsoc.ui.MidiInteraction.MidiInteractionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.content.Intent;

import android.os.Message;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements
        BlConnectionFragment.OnBlConnectionListener, KeyboardFragment.OnKeyboardListener,
        MidiInteractionFragment.OnMidiInteractionListener, BottomNavigationView.OnNavigationItemSelectedListener {

    // CONSTANTS


    // GLOBAL VARIABLES
    // BL communication
    private BluetoothAdapter myBluetoohAdapter;
    private Set<BluetoothDevice> btDevices;
    protected MyBlClient cl;

    // For OS services request
    private Intent btEnablingIntent, fileManagerIntent;

    private MidiUtils midiUtils;
    protected MyAppStatus appStatus;
    protected Uri fileUri;
    protected int selectedVel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);

        BlConnectionFragment fragment = new BlConnectionFragment();
        loadFragment(fragment);

        setup();

    }// onCreate()


    /**************************************************************************************************************************/
    /************************** INTERFACES METHODS IMPLEMENTATION *************************************************************/
    /**************************************************************************************************************************/


    // Register this instance like listener of OnBlConnectionListener, OnKeyboardListener and OnMidiInteractionListener
    @Override
    public  void onAttachFragment(Fragment fragment){
        if(fragment instanceof BlConnectionFragment)
            ((BlConnectionFragment) fragment).setOnBlConnectionListener(this);
        else if(fragment instanceof KeyboardFragment)
            ((KeyboardFragment) fragment).setOnKeyboardListener(this);
        else if(fragment instanceof MidiInteractionFragment)
            ((MidiInteractionFragment) fragment).setOnMidiInteractionListener(this);
    }

    /************************** METHODS OF OnBlConnectionListener *************************************************************/

    @Override
    public void showPairedDevicesOnClick() {
        if(myBluetoohAdapter==null)
            myPrintMsg("This device dosen't support bluetooth");
        else if(!myBluetoohAdapter.isEnabled())
            myPrintMsg("Enable bluetooth to show paired devices");
        else{
            btDevices = myBluetoohAdapter.getBondedDevices();

            if(btDevices.size() > 0){
                String[] btDevicesNames = new String[btDevices.size()];
                int i = 0;

                for(BluetoothDevice dev : btDevices)
                    btDevicesNames[i++] = dev.getName();

                Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if(frag!=null && (frag instanceof BlConnectionFragment))
                    ((BlConnectionFragment)frag).updateListPairedDevices(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, btDevicesNames));
            }
        }
    }// showPairedDevicesOnClick()


    @Override
    public void selectDeviceOnItemClick(int pos) {
        if(appStatus.statusCode==MyConstants.STATE_CONNECTED || appStatus.statusCode==MyConstants.STATE_BL_ON || appStatus.statusCode==MyConstants.STATE_CONNECTION_FAILED){

            if(appStatus.statusCode==MyConstants.STATE_CONNECTED)
                cl.kill();

            appStatus.updateStatusBl(MyConstants.STATE_CONNECTING);
            cl = new MyBlClient((BluetoothDevice)btDevices.toArray()[pos]);
            Thread th = new Thread(cl);
            th.start();

        }
        else{
            if(appStatus.statusCode==MyConstants.STATE_BL_OFF)
                myPrintMsg("Turn on Bluetooth");
            else if(appStatus.statusCode==MyConstants.STATE_CONNECTING)
                myPrintMsg("Trying to connect to other device, wait until state changes");
        }
    }// selectDeviceOnItemClick()


    @Override
    public void disconnetOnClick() {
        if(appStatus.statusCode==MyConstants.STATE_CONNECTED){
            cl.kill();
            appStatus.updateStatusBl(MyConstants.STATE_BL_ON);
        }
        else
            myPrintMsg("No connection established with external device");

    }// disconnetOnClick()


    /************************** METHODS OF OnMidiInteractionListener *************************************************************/

    @Override
    public void pickFileOnClick() {
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(frag!=null && (frag instanceof MidiInteractionFragment)) {
            if (((MidiInteractionFragment) frag).getMaxAllowedTracks() != "")
                startActivityForResult(fileManagerIntent, MyConstants.REQ_CODE_FILE_MANAGER);
            else
                myPrintMsg("Enter a nº of max tracks allowed for the file");
        }
    }// pickFileOnClick()


    @Override
    public void sendDataOnClik() {
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(frag!=null && (frag instanceof MidiInteractionFragment)) {
            if (appStatus.statusCode != MyConstants.STATE_CONNECTED)
                myPrintMsg("No connected device, select a device from the paired devices list");
            else if(fileUri == null)
                myPrintMsg("No file is selected");
            else{
                try {
                    sendSetRecvModeCMD();
                    //Wait, the device must change state before send data.
                    Thread.sleep(MyConstants.THREAD_SLEEP_TIME/2);
                    sendRawFileData();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                    myPrintMsg("Error sending file...");
                }
            }

        }
    }// sendDataOnClik()

    @Override
    public void startStopSongOnClick() {
        if(appStatus.statusCode==MyConstants.STATE_CONNECTED){
            sendOnOffSongCMD();
        }
        else
            myPrintMsg("No connection established with external device");

    }// startStopSongOnClick()


    @Override
    public void sendReverbOnOff() {
        if(appStatus.statusCode==MyConstants.STATE_CONNECTED){
            sendOnOffReverbCMD();
        }
        else
            myPrintMsg("No connection established with external device");

    }// startStopSongOnClick()

    /************************** METHODS OF OnKeyboardListener *************************************************************/

    @Override
    public void decraseOctaveOnClick(){
        int keyboardOctave = appStatus.keyboardOctave;
        if(keyboardOctave > MyConstants.MIN_OCTAVE)
            appStatus.updateStatusKeyboardOctave(--keyboardOctave);

    }// decraseOctaveOnClick()


    @Override
    public void increaseOctaveOnClick(){
        int keyboardOctave = appStatus.keyboardOctave;
        if(keyboardOctave < MyConstants.MAX_OCTAVE)
            appStatus.updateStatusKeyboardOctave(++keyboardOctave);

    }// increaseOctaveOnClick()


    @Override
    public void onPressRelease(boolean b, int keyIndex){
        if(appStatus.statusCode==MyConstants.STATE_CONNECTED){

            int n = appStatus.keyboardOctave;
            byte onOffCmd = b ? MyConstants.ON_CMD : MyConstants.OFF_CMD;

            if(n==0) {
                if (keyIndex >= MyConstants.LA_INDEX)
                    sendOnOffCMD(onOffCmd, keyIndex - MyConstants.LA_INDEX);

            }else if(n < 8 || (n==8 && keyIndex == MyConstants.DO_INDEX) )
                    sendOnOffCMD(onOffCmd,(n-1)*MyConstants.NUM_KEYS+keyIndex+3);
        }
        else
            myPrintMsg("No connection established, go to connection view to start a connection request with other device");

    }// onPressRelease()

    @Override
    public void onItemSelected(int pos){
        if(pos>=0 && pos<=4)
            selectedVel = pos;
    }// onItemSelected()


    /************************** METHODS OF OnNavigationItemSelectedListener *************************************************************/

    // Manage which fragment display depending of the item selected
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment frag = null;
        boolean res;

        switch (menuItem.getItemId()){

            case R.id.navigation_bl_connection:
                frag = new BlConnectionFragment();
                break;

            case R.id.navigation_keyboard:
                frag = new KeyboardFragment();
                break;

            case R.id.navigation_midi_interaction:
                frag = new MidiInteractionFragment();
                break;
        }

        if( (res=loadFragment(frag)) )
            appStatus.refreshState();

        return res;

    }// onNavigationItemSelected()

    /**************************************************************************************************************************/
    /************************** FOR MANAGE OS ANSWERS *************************************************************************/
    /**************************************************************************************************************************/

    @Override
    protected void onActivityResult(int requestedCode, int resultCode, Intent data){
        switch (requestedCode){
            case MyConstants.REQ_CODE_ENGABLE_BL:
                if(resultCode==RESULT_OK)
                    appStatus.updateStatusBl(MyConstants.STATE_BL_ON);
                else if(resultCode==RESULT_CANCELED)
                    myPrintMsg("Bluetooth enabling canceled");
                break;

            case MyConstants.REQ_CODE_FILE_MANAGER:
                if(resultCode==RESULT_OK && data!=null) {
                    Uri uri = data.getData();
                    Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                    if (frag != null && (frag instanceof MidiInteractionFragment))
                        fileUri = checkFile(((MidiInteractionFragment)frag).getMaxAllowedTracks(), uri) ? uri : null;

                } else if (resultCode == RESULT_CANCELED)
                    myPrintMsg("File manger search canceled");

                break;

            default:
                myPrintMsg("Unexpected value: " + requestedCode);
        }

    }//onActivityResult()

    /**************************************************************************************************************************/
    /************************** COMMON USE METHODS ****************************************************************************/
    /**************************************************************************************************************************/

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

            return true;
        }
        return false;
    }// loadFragment()

    private void sendOnOffCMD(byte onOffCmd, int keyIndex) {
        cl.send(onOffCmd);
        cl.send(MyConstants.KEYBOARD_CODES[keyIndex]);
        cl.send(MyConstants.VEL[selectedVel]);
    }// sendOnOffCMD()


    private boolean checkFile(String maxAllowedTracks, Uri uri){
        boolean ok = false;
        if (maxAllowedTracks != null && maxAllowedTracks.matches(MyConstants.REG_X) &&
                Integer.valueOf(maxAllowedTracks) > 0) {
            try {
                String msg = "";
                InputStream streamIn = getContentResolver().openInputStream(uri);
                if ((msg = midiUtils.checkFileHeader(streamIn, Integer.valueOf(maxAllowedTracks))) == null){
                    ok = true;
                    myPrintMsg(uri.toString());
                }
                else
                    myPrintMsg("Not valid file, " + msg);

                streamIn.close();
            } catch (IOException e) {
                e.printStackTrace();
                myPrintMsg("Error while checking file format");
            }
        } else
            myPrintMsg("Enter a nº of max allowed tracks of the file, only positive numbers are supported, 0 is not allowed");

        return ok;
    }// checkFile()



    private void sendSetRecvModeCMD(){
        cl.send(MyConstants.RECV_FILE_MODE_ON_OFF);
    }// sendSetRecvModeCMD()

    private void sendRawFileData() {
        try{
            int c;
            Integer n = 0;
            String auxS = "";
            InputStream streamIn = getContentResolver().openInputStream(fileUri);
            myPrintMsg("Sending file...");
            while((c=streamIn.read())!=-1){
                cl.send((byte)c);
                if(n < Integer.MAX_VALUE)
                    n++;
                else
                    auxS = ", more than";
            }
            streamIn.close();
            myPrintMsg("File sent, nº of bytes sent" + auxS + ": " + n.toString());
        }
        catch (IOException e){
            e.printStackTrace();
            myPrintMsg("Error while sending the midi file");
        }
    }//sendRawFileData()


    private void sendOnOffSongCMD(){
        cl.send(MyConstants.ON_OFF_SONG);
    }// sendOnOffSongCMD()

    private void sendOnOffReverbCMD(){
        cl.send(MyConstants.REVERB_ON_OFF);
    }// sendOnOffSongCMD()


    private void setup() {

        myBluetoohAdapter = BluetoothAdapter.getDefaultAdapter();
        btEnablingIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

        fileManagerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        fileManagerIntent.addCategory(Intent.CATEGORY_OPENABLE);
        fileManagerIntent.setType("*/*"); // Select extension of file

        int iniStat = myBluetoohAdapter.isEnabled() ? MyConstants.STATE_BL_ON : MyConstants.STATE_BL_OFF;
        appStatus = new MyAppStatus(MyConstants.STATUS_MSG[iniStat], iniStat,4);
        Thread th = new Thread(appStatus);
        th.start();

        midiUtils = new MidiUtils();
        cl = null;
        fileUri = null;
        selectedVel = 2;

    }//setup()

    protected void myPrintMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }// myPrintMsg()

    /**************************************************************************************************************************/
    /************************************** INNER CLASSES *********************************************************************/
    /**************************************************************************************************************************/

    private class MyAppStatus implements Runnable {
        private int statusCode;
        boolean updateFlag;
        private Handler handlerBl, handlerKey;
        private int keyboardOctave;

        public MyAppStatus(String s, int status, int n ){
            keyboardOctave = n;
            updateBl(s,status);
            handlerBl = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    if(msg.what >= MyConstants.STATE_BL_ON && msg.what <= MyConstants.STATE_CONNECTION_LOST){
                        appStatus.updateBl(MyConstants.STATUS_MSG[msg.what], msg.what);
                        return false;
                    }
                    appStatus.updateBl(MyConstants.STATUS_MSG[MyConstants.UNKNOW_STATE], MyConstants.UNKNOW_STATE);
                    return false;
                }
            });

            handlerKey = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    updateKeyboardOctave(msg.what);
                    return false;
                }
            });
        }

        @Override
        public void run(){
            try {
                while(true){
                    Thread.sleep(MyConstants.THREAD_SLEEP_TIME/2);
                    if(updateFlag){
                        updateFlag = false;
                        updateStatusBl(statusCode);
                    }

                    if(!myBluetoohAdapter.isEnabled() && statusCode!=MyConstants.STATE_BL_OFF){
                        if(statusCode==MyConstants.STATE_CONNECTED || statusCode==MyConstants.STATE_CONNECTING)
                            cl.kill();
                        updateStatusBl(MyConstants.STATE_BL_OFF);
                    }

                    if(myBluetoohAdapter.isEnabled() && statusCode==MyConstants.STATE_BL_OFF)
                        updateStatusBl(MyConstants.STATE_BL_ON);

                }// while
            }
            catch (InterruptedException e){ e.printStackTrace(); }
        }// run()

        public void updateStatusBl(int status){
            Message msg = Message.obtain();
            msg.what = status;
            handlerBl.sendMessage(msg);
        }

        public void updateStatusKeyboardOctave(int status){
            Message msg = Message.obtain();
            msg.what = status;
            handlerKey.sendMessage(msg);
        }

        public void refreshState(){
            updateStatusBl(statusCode);
            updateStatusKeyboardOctave(keyboardOctave);
        }

        private void updateBl(String s, int statCode){
            Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            updateFlag = ( frag == null ||
                        !(frag instanceof BlConnectionFragment || frag instanceof KeyboardFragment || frag instanceof MidiInteractionFragment) );
            if(frag != null ){
                if( frag instanceof BlConnectionFragment )
                    ((BlConnectionFragment) frag).updateStatusTxt(s);
                else if( frag instanceof KeyboardFragment )
                    ((KeyboardFragment) frag).updateStatusTxt(s);
                else if( frag instanceof MidiInteractionFragment )
                    ((MidiInteractionFragment) frag).updateStatusTxt(s);

                statusCode = statCode;
            }
        }

        private void updateKeyboardOctave(int n){
            Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (frag != null && (frag instanceof KeyboardFragment)){
                keyboardOctave = n;
                ((KeyboardFragment) frag).updateKeyboardOctaveTxt(n);
            }
        }

    }// Class MyAppStatus


    private class MyBlClient implements Runnable {

        private BluetoothSocket socket;
        private OutputStream outputStream;
        private boolean kill;

        public MyBlClient(BluetoothDevice targetDevice){
            kill = false;
            try{ socket = targetDevice.createRfcommSocketToServiceRecord(MyConstants.MY_UUID); }
            catch (IOException e){ e.printStackTrace();}
        }

        @Override
        public void run(){
            try {
                socket.connect();
                appStatus.updateStatusBl(MyConstants.STATE_CONNECTED);
                outputStream = socket.getOutputStream();
                while(!kill) {
                    Thread.sleep(MyConstants.THREAD_SLEEP_TIME);
                    if(!socket.isConnected()){
                        kill = true;
                        appStatus.updateStatusBl(MyConstants.STATE_CONNECTION_LOST);
                    }
                }
                socket.close();
            }
            catch (IOException | InterruptedException e){
                e.printStackTrace();
                appStatus.updateStatusBl(MyConstants.STATE_CONNECTION_FAILED);
            }
        }

        // Used to send data
        public void send(byte b){
            try{
                if(socket.isConnected())
                    outputStream.write(b);
            }
            catch (IOException e){ e.printStackTrace();}
        }

        public void kill(){
            kill = true;
        }
    }// Class MyBlClient

}// Class
