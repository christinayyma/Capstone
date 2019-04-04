// constants won't change. They're used here to set pin numbers:
const int buttonPin = 10;     // the number of the pushbutton pin
const int ledPin =  8;      // the number of the LED pin

// variables will change:
int currButtonState = 0;         // variable for reading the pushbutton status
int prevButtonState = 0;
int activeState = 0;

void setup() {
  // initialize the LED pin as an output:
  pinMode(ledPin, OUTPUT);
  // initialize the pushbutton pin as an input:
  pinMode(buttonPin, INPUT);
  Serial.begin(9600);
}

void loop() {
  // read the state of the pushbutton value:
  
  currButtonState = digitalRead(buttonPin);
  if(prevButtonState == 0 && currButtonState == 1){
    if(activeState == 0){
      activeState = 1;
      digitalWrite(ledPin, HIGH);
      Serial.println("ACTIVE");
    }
    else{
      activeState = 0;
      digitalWrite(ledPin, LOW);
      Serial.println("NOT ACTIVE");
    }
    
  }
  prevButtonState = currButtonState;
}
