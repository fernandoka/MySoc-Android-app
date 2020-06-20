MySoc Android app
==============

This respository contains the source code from an androdid app which take part in the development of my final university project, providing an external GUI interface to comunicate with the digital piano (implemented in a FPGA as System On Chip). This communication is done using bluethooth.


This app is divided in three diferents windows: Connection, File transfer and Keyboard. You can acces each of this windows using the navigation menu located at the bottom of the device screen. The source code located in src/main/...

 - **Connection**: This window allow the user to establish a bluethooth connection with the digital piano. The user can also disconnect                       from the device using this window.

 - **File Transfer**: This window allow the user to upload a MIDI file to the digital piano, only if the user have already established a                        bluethooth connection with It. The user can also define the maximun number of tracks for the next MIDI file to be                          uploaded. 

 - **Keyboard**: This window show an octave of a piano, allowing the user to play these notes using the digital piano. To play these notes,                 the android device must be connected and the external interface interaction must be set in the digital piano. You can change the current ocatve between all the others that form a piano keybaord. At the same time, this windows include a combo-box to define the intensity of these notes. 

The [first release ](https://github.com/fernandoka/MySoc-Android-app/releases) 
provides both the installable apk and the Android Studio project which had been used to develop this app.

## Screenshots

<p align="center">
  <img src="https://github.com/fernandoka/MySoc-Android-app/blob/master/screenshots/Connection_2.png" width="250" height="400" title="Connection Window">
  <img src="https://github.com/fernandoka/MySoc-Android-app/blob/master/screenshots/FileTransfer_1.png" width="250" height="400" title="File Transfer Window">
  <img src="https://github.com/fernandoka/MySoc-Android-app/blob/master/screenshots/Keyboard_2.png" width="250" height="400" title="File Transfer Window">
</p>
