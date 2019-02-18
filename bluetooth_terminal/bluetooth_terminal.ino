// Basic Bluetooth sketch HC-05_01
// Sends "Bluetooth Test" to the serial monitor and the software serial once every second.
//
// Connect the HC-05 module and data over Bluetooth
//
// The HC-05 defaults to commincation mode when first powered on.
// The default baud rate for communication is 9600
 
#include <SoftwareSerial.h>
SoftwareSerial BTserial(2, 3); // RX | TX
// Connect the HC-05 TX to Arduino pin 2 RX. 
// Connect the HC-05 RX to Arduino pin 3 TX through a voltage divider.
// 

const int FLEX_PIN = A0; 
void setup() 
{
    Serial.begin(9600);
    pinMode(FLEX_PIN, INPUT);
    // HC-06 default serial speed for communcation mode is 9600
    BTserial.begin(9600);  
}
 
void loop() 
{
  int flex = analogRead(FLEX_PIN);
  BTserial.println(String(flex) + "#");           // Write the character to Bluetooth  
  delay(10); 
}
