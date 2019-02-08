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
const float VCC = 4.98;
const float R_DIV = 47500.0;
String b = "Hello Christina";
const float STRAIGHT_RESISTANCE = 37300.0; // resistance when straight
const float BEND_RESISTANCE = 90000.0; // resistance at 90 deg
int f = 454;
 
void setup() 
{
    Serial.begin(9600);
    //Serial.println("Enter AT commands:");
    //pinMode(2, INPUT);
    //pinMode(3, OUTPUT);
    pinMode(FLEX_PIN, INPUT);
    // HC-06 default serial speed for communcation mode is 9600
    BTserial.begin(9600);  
}
 
void loop() 
{
  int flex = analogRead(FLEX_PIN);
  //float flexV = flex * VCC / 1023.0;
  //float flexR = R_DIV * (VCC / flexV - 1.0);
  //float angle = map(flexR, STRAIGHT_RESISTANCE, BEND_RESISTANCE,
  //                 0, 90.0);
  //Serial.println(flex);
  //angle = 456.4;
  BTserial.println(String(flex) + "#");           // Write the character to Bluetooth  
  delay(10); 
}
