Author: Chris Ward

Version: 08/31/2013

TranTracker
===========

An Android-based GPS Transmitter applicaiton. Uses a BroadcastReceiver to retreive the location of an android device, then
sends the location to an external database server. The app also runs as a service to allow the program to run in the 
background.

TODO:
=====
1] Secure MySQL Communications by implementing a REST or SOAP system to send data to the server.
2] Start application on phone's boot. 
3] Maybe implement a personallizable VehicleID field within the application.
