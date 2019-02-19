// Basic Bluetooth sketch HC-05_01
// Sends "Bluetooth Test" to the serial monitor and the software serial once every second.
//
// Connect the HC-05 module and data over Bluetooth
//
// The HC-05 defaults to commincation mode when first powered on.
// The default baud rate for communication is 9600
 
#include <SoftwareSerial.h>
SoftwareSerial BTserial(2, 3); // RX | TX
SoftwareSerial BTserial2(4, 5); //RX | TX
// Connect the HC-05 TX to Arduino pin 2 RX. 
// Connect the HC-05 RX to Arduino pin 3 TX through a voltage divider.
// 

const int FLEX_THUMB = A0; 
const int FLEX_INDEX = A1; 
const int FLEX_MIDDLE = A2;
const int FLEX_RING = A3;
const int FLEX_PINKY = A4;

void setup() 
{
    Serial.begin(9600);
    pinMode(FLEX_THUMB, INPUT);
    pinMode(FLEX_INDEX, INPUT);
    pinMode(FLEX_MIDDLE, INPUT);
    pinMode(FLEX_RING, INPUT);
    pinMode(FLEX_PINKY, INPUT);    
    // HC-06 default serial speed for communcation mode is 9600
    BTserial.begin(9600); 
    BTserial2.begin(9600); 
}
 
void loop() 
{
  int flex1 = analogRead(FLEX_THUMB);
  int flex2 = analogRead(FLEX_INDEX);
  int flex3 = analogRead(FLEX_MIDDLE);
  int flex4 = analogRead(FLEX_RING);
  int flex5 = analogRead(FLEX_PINKY);
  BTserial.println("L123;456;789#");
  delay(1000);
  BTserial2.println("R234;567;890#");
  //BTserial.println(String(flex1)+ ";" + String(flex2)+ ";" + String(flex3)+ ";" + String(flex4)+ ";" + String(flex5)+ "#");           // Write the character to Bluetooth  
  delay(1000); 
}
