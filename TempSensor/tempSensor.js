/* 
 * Copyright 2017 Mario Contreras <marioc@nazul.net>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var iot = require('aws-iot-device-sdk');
var device = iot.device({
    keyPath: __dirname + '/TempSensor.private.key',
    certPath: __dirname + '/TempSensor.cert.pem',
    caPath: __dirname + '/root-CA.crt',
    clientID: 'TempSensor',
    region: 'us-east-1'
});

device.on('connect', function () {
    console.log('connect');
    device.subscribe('TemperatureReadings', function (error, result) {
        console.log(result);
    });
});

device.on('close', function () {
    console.log('close');
});

device.on('reconnect', function () {
    console.log('reconnect');
});

device.on('offline', function () {
    console.log('offline');
});

device.on('message', function (topic, payload) {
    console.log('message', topic, payload.toString());
});

console.log('Sending new temperature readings...');
setInterval(function () {
    //
    var newTemperature = Math.floor(Math.random() * (500.0 - 0 + 1.0) + 0) / 10.0;
    device.publish('TemperatureReadings', JSON.stringify({sensorId: 'iot1', temperature: newTemperature}));
}, 300000);

// EOF
