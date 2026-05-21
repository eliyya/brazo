/*
 * gate.ino - Control de 4 servos via Serial.
 *
 * Formato JSON esperado (una linea por comando, terminado en \n):
 *   {"servos":[90,45,180,0]}
 *   {"servos":[0,90,180,45]}
 *
 * Cada valor es el angulo en grados (0-180) para el servo en esa posicion.
 * Pines: 0->9, 1->10, 2->11, 3->12.
 *
 * Respuestas:
 *   {"status":"ok"}
 *   {"error":"expected 4 servos"}
 *   {"error":"<deserialization error>"}
 */

#include <ArduinoJson.h>
#include <Servo.h>

#define NUM_SERVOS 4
#define BUFFER_SIZE 256

Servo servos[NUM_SERVOS];
const int pins[NUM_SERVOS] = {9, 10, 11, 12};

char buffer[BUFFER_SIZE];
int index = 0;

void setup() {
    Serial.begin(115200);

    for (int i = 0; i < NUM_SERVOS; i++) {
        servos[i].attach(pins[i]);
        servos[i].write(90);
    }
}

void loop() {
    while (Serial.available() > 0) {
        char c = Serial.read();

        if (c == '\n') {
            buffer[index] = '\0';
            processCommand(buffer);
            index = 0;
        } else if (index < BUFFER_SIZE - 1) {
            buffer[index++] = c;
        }
    }
}

void processCommand(char* json) {
    JsonDocument doc;
    DeserializationError err = deserializeJson(doc, json);

    if (err) {
        Serial.print("{\"error\":\"");
        Serial.print(err.c_str());
        Serial.println("\"}");
        return;
    }

    JsonArray angles = doc["servos"];

    if (!angles || angles.size() != NUM_SERVOS) {
        Serial.println("{\"error\":\"expected 4 servos\"}");
        return;
    }

    for (int i = 0; i < NUM_SERVOS; i++) {
        int angle = angles[i].as<int>();
        angle = constrain(angle, 0, 180);
        servos[i].write(angle);
    }

    Serial.println("{\"status\":\"ok\"}");
}
