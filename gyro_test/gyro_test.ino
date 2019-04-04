// (c) Michael Schoeffler 2017, http://www.mschoeffler.de
#include "Wire.h" // This library allows you to communicate with I2C devices.
#include <SoftwareSerial.h>
SoftwareSerial BTserial(2, 3);
const int MPU_ADDR = 0x68; // I2C address of the MPU-6050. If AD0 pin is set to HIGH, the I2C address will be 0x69.
int16_t accelerometer_x, accelerometer_y, accelerometer_z; // variables for accelerometer raw data
int16_t gyro_x, gyro_y, gyro_z; // variables for gyro raw data
int16_t temperature; // variables for temperature data
char tmp_str[7]; // temporary variable used in convert function
char* convert_int16_to_str(int16_t i) { // converts int16 to string. Moreover, resulting strings will have the same length in the debug monitor.
  sprintf(tmp_str, "%6d", i);
  return tmp_str;
}
const int ring = A0;
const int pinky = A1;
const int middle = A2;
const int index = A3;
const int thumb = A6; 
int currButtonState = 0;
int prevButtonState = 0;
int activeState = 0;

const int buttonPin = 5;
const int ledPin = 4;

void setup() {
  Serial.begin(9600);
  Wire.begin();
  Wire.beginTransmission(MPU_ADDR); // Begins a transmission to the I2C slave (GY-521 board)
  Wire.write(0x6B); // PWR_MGMT_1 register
  Wire.write(0); // set to zero (wakes up the MPU-6050)
  Wire.endTransmission(true);

  pinMode(ring, INPUT);
  pinMode(pinky, INPUT);
  pinMode(middle, INPUT);
  pinMode(ring, INPUT);
  pinMode(thumb, INPUT);
  //pinMode(ledPin, OUTPUT);
  //pinMode(buttonPin, INPUT);
  BTserial.begin(9600);
}
void loop() {

  /*currButtonState = digitalRead(buttonPin);
  if(prevButtonState == 0 && currButtonState == 1){
    if(activeState == 0){
      activeState = 1;
      digitalWrite(ledPin, HIGH);
    }
    else{
      activeState = 0;
      digitalWrite(ledPin, LOW);
    }
    
  }
  prevButtonState = currButtonState;

  if(activeState == 1){*/
    Wire.beginTransmission(MPU_ADDR);
    Wire.write(0x3B); // starting with register 0x3B (ACCEL_XOUT_H) [MPU-6000 and MPU-6050 Register Map and Descriptions Revision 4.2, p.40]
    Wire.endTransmission(false); // the parameter indicates that the Arduino will send a restart. As a result, the connection is kept active.
    Wire.requestFrom(MPU_ADDR, 7*2, true); // request a total of 7*2=14 registers
    
    // "Wire.read()<<8 | Wire.read();" means two registers are read and stored in the same variable
    accelerometer_x = Wire.read()<<8 | Wire.read(); // reading registers: 0x3B (ACCEL_XOUT_H) and 0x3C (ACCEL_XOUT_L)
    accelerometer_y = Wire.read()<<8 | Wire.read(); // reading registers: 0x3D (ACCEL_YOUT_H) and 0x3E (ACCEL_YOUT_L)
    accelerometer_z = Wire.read()<<8 | Wire.read(); // reading registers: 0x3F (ACCEL_ZOUT_H) and 0x40 (ACCEL_ZOUT_L)
    temperature = Wire.read()<<8 | Wire.read(); // reading registers: 0x41 (TEMP_OUT_H) and 0x42 (TEMP_OUT_L)
    gyro_x = Wire.read()<<8 | Wire.read(); // reading registers: 0x43 (GYRO_XOUT_H) and 0x44 (GYRO_XOUT_L)
    gyro_y = Wire.read()<<8 | Wire.read(); // reading registers: 0x45 (GYRO_YOUT_H) and 0x46 (GYRO_YOUT_L)
    gyro_z = Wire.read()<<8 | Wire.read(); // reading registers: 0x47 (GYRO_ZOUT_H) and 0x48 (GYRO_ZOUT_L)
  
    int flex1 = analogRead(thumb);
    int flex2 = analogRead(index);
    int flex3 = analogRead(middle);
    int flex4 = analogRead(ring);
    int flex5 = analogRead(pinky);
  
    // print out data
    BTserial.print("R");
    BTserial.print(convert_int16_to_str(accelerometer_x));
    BTserial.print(";"); BTserial.print(convert_int16_to_str(accelerometer_y));
    BTserial.print(";"); BTserial.print(convert_int16_to_str(accelerometer_z));
    // the following equation was taken from the documentation [MPU-6000/MPU-6050 Register Map and Description, p.30]
    //BTserial.print(" | tmp = "); BTserial.print(temperature/340.00+36.53);
    BTserial.print(";"); BTserial.print(convert_int16_to_str(gyro_x));
    BTserial.print(";"); BTserial.print(convert_int16_to_str(gyro_y));
    BTserial.print(";"); BTserial.print(convert_int16_to_str(gyro_z));
    BTserial.print(";"); BTserial.print(flex1);
    BTserial.print(";"); BTserial.print(flex2);
    //BTserial.print(";"); BTserial.print(flex3);
    //BTserial.print(";"); BTserial.print(flex4);
    //BTserial.print(";"); BTserial.print(flex5);
  //  BTBTserial.println(String("L") + convert_int16_to_str(accelerometer_x) + String(";") + convert_int16_to_str(accelerometer_y) + String(";") + convert_int16_to_str(accelerometer_z) + String(";") + 
  //  String(flex1) + String(";") + String(flex2) + String(";") + String(flex3) + String(";") + String(flex4) + String(";") + String(flex5) + String("#"));
    BTserial.println();
    
    // delay
    delay(1000);
  //}
}
