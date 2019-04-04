// Basic Bluetooth sketch HC-05_01
// Sends "Bluetooth Test" to the serial monitor and the software serial once every second.
//
// Connect the HC-05 module and data over Bluetooth
//
// The HC-05 defaults to commincation mode when first powered on.
// The default baud rate for communication is 9600
 
#include <SoftwareSerial.h>
#include "Wire.h" // This library allows you to communicate with I2C devices.
SoftwareSerial BTserial(2, 3); // RX | TX
// Connect the HC-05 TX to Arduino pin 2 RX. 
// Connect the HC-05 RX to Arduino pin 3 TX through a voltage divider.
// 
const int ring = A0;
const int pinky = A1;
const int middle = A2;
const int index = A3;
const int thumb = A6;

void setup() 
{
    Serial.begin(9600);
    pinMode(ring, INPUT);
    pinMode(pinky, INPUT);
    pinMode(middle, INPUT);
    pinMode(index, INPUT);
    pinMode(thumb, INPUT);
    // HC-06 default serial speed for communcation mode is 9600
    BTserial.begin(9600); 

}
 
void loop() 
{
  int rg = analogRead(ring);
  int pk = analogRead(pinky);
  int md = analogRead(middle);
  int in = analogRead(index);
  int tb = analogRead(thumb);
  Serial.println(tb);
  delay(1000);
}
