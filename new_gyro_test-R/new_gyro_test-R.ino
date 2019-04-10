 // MPU-6050 Short Example Sketch
// By Arduino User JohnChi
// August 17, 2014
// Public Domain
#include<Wire.h>
#include<SoftwareSerial.h>
SoftwareSerial BTSerial(2,3); //RX | TX
const int MPU_addr=0x68;  // I2C address of the MPU-6050
int16_t AcX,AcY,AcZ,Tmp,GyX,GyY,GyZ;


const int ring = A0;
const int pinky = A1;
const int middle = A2;
const int index = A3;
const int thumb = A6;

void setup(){
  Wire.begin();
  Wire.beginTransmission(MPU_addr);
  Wire.write(0x6B);  // PWR_MGMT_1 register
  Wire.write(0);     // set to zero (wakes up the MPU-6050)
  Wire.endTransmission(true);
  pinMode(ring, INPUT);
  pinMode(pinky, INPUT);
  pinMode(middle, INPUT);
  pinMode(ring, INPUT);
  pinMode(thumb, INPUT);

  BTSerial.begin(9600);
}
void loop(){
// read the state of the pushbutton value:

    Wire.beginTransmission(MPU_addr);
    Wire.write(0x3B);  // starting with register 0x3B (ACCEL_XOUT_H)
    Wire.endTransmission(false);
    Wire.requestFrom(MPU_addr,14,true);  // request a total of 14 registers
    AcX=Wire.read()<<8|Wire.read();  // 0x3B (ACCEL_XOUT_H) & 0x3C (ACCEL_XOUT_L)     
    AcY=Wire.read()<<8|Wire.read();  // 0x3D (ACCEL_YOUT_H) & 0x3E (ACCEL_YOUT_L)
    AcZ=Wire.read()<<8|Wire.read();  // 0x3F (ACCEL_ZOUT_H) & 0x40 (ACCEL_ZOUT_L)
    Tmp=Wire.read()<<8|Wire.read();  // 0x41 (TEMP_OUT_H) & 0x42 (TEMP_OUT_L)
    GyX=Wire.read()<<8|Wire.read();  // 0x43 (GYRO_XOUT_H) & 0x44 (GYRO_XOUT_L)
    GyY=Wire.read()<<8|Wire.read();  // 0x45 (GYRO_YOUT_H) & 0x46 (GYRO_YOUT_L)
    GyZ=Wire.read()<<8|Wire.read();  // 0x47 (GYRO_ZOUT_H) & 0x48 (GYRO_ZOUT_L)
  
    int flex1 = analogRead(thumb);
    int flex2 = analogRead(index);
    int flex3 = analogRead(middle);
    int flex4 = analogRead(ring);
    int flex5 = analogRead(pinky);
    BTSerial.print("RSTART");
    BTSerial.print(" "); BTSerial.print(0);
    BTSerial.print(" "); BTSerial.print(0);
    BTSerial.print(" "); BTSerial.print(0);
    BTSerial.print(" "); BTSerial.print(0);
    BTSerial.print(" "); BTSerial.print(0);
    BTSerial.print(" "); BTSerial.print(0);
    BTSerial.print(" "); BTSerial.print(flex1);
    BTSerial.print(" "); BTSerial.print(flex2);
    BTSerial.print(" "); BTSerial.print(flex3);
    BTSerial.print(" "); BTSerial.print(flex4);
    BTSerial.print(" "); BTSerial.print(flex5);
    BTSerial.println(" END");
    delay(10);
  
}
