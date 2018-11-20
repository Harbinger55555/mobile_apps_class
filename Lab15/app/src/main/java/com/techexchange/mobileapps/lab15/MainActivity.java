package com.techexchange.mobileapps.lab15;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private TextView lightSensor;
    private TextView luminescenceValue;
    private TextView proximitySensor;
    private TextView proximityValue;
    private TextView stepsSensor;
    private TextView stepsValue;
    private TextView accelerometerSensor;
    private TextView azimuthSensor;
    private TextView azimuthValue;
    private TextView pitchSensor;
    private TextView pitchValue;
    private TextView rollSensor;
    private TextView rollValue;
    private TextView pressureSensor;
    private TextView pressureValue;
    private TextView temperatureSensor;
    private TextView temperatureValue;
    private TextView magneticSensor;
    private TextView magneticValue;

    private Sensor light;
    private Sensor proximity;
    private Sensor rotation;
    private Sensor stepCounter;
    private Sensor magnetic;
    private Sensor pressure;
    private Sensor temperature;

    private SensorManager sensorManager;

    private String sensorList;

    private Button seeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lightSensor = findViewById(R.id.light_sensor);
        luminescenceValue = findViewById(R.id.luminescence_value);
        proximitySensor = findViewById(R.id.proximity_sensor);
        proximityValue = findViewById(R.id.proximity_value);
        stepsSensor = findViewById(R.id.steps_sensor);
        stepsValue = findViewById(R.id.steps_value);
        accelerometerSensor = findViewById(R.id.accelerometer_sensor);
        azimuthSensor = findViewById(R.id.azimuth_sensor);
        azimuthValue = findViewById(R.id.azimuth_value);
        pitchSensor = findViewById(R.id.pitch_sensor);
        pitchValue = findViewById(R.id.pitch_value);
        rollSensor = findViewById(R.id.roll_sensor);
        rollValue = findViewById(R.id.roll_value);
        pressureSensor = findViewById(R.id.pressure_sensor);
        pressureValue = findViewById(R.id.pressure_value);
        temperatureSensor = findViewById(R.id.temperature_sensor);
        temperatureValue = findViewById(R.id.temperature_value);
        magneticSensor = findViewById(R.id.magnetic_sensor);
        magneticValue = findViewById(R.id.magnetic_value);

        seeButton = findViewById(R.id.button_id);
        seeButton.setOnClickListener(v -> onButtonPressed());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        getSensorAvailability();
        sensorList = getSensorList();
    }

    private String getSensorList() {

        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        String sensorInfo = "";
        for (Sensor s : sensorList) {
            sensorInfo = sensorInfo + s.getName() + "\n";
        }
        return sensorInfo;
    }

    private void getSensorAvailability() {
        if ((light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)) == null)
            luminescenceValue.setText("N/A");
        if ((proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)) == null)
            proximityValue.setText("N/A");
        if ((rotation = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)) == null) {
            azimuthValue.setText("N/A");
            pitchValue.setText("N/A");
            rollValue.setText("N/A");
        }
        if ((stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)) == null)
            stepsValue.setText("N/A");
        if ((magnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)) == null)
            magneticValue.setText("N/A");
        if ((pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)) == null)
            pressureValue.setText("N/A");
        if ((temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)) == null)
            temperatureValue.setText("N/A");
    }

    public void onButtonPressed() {
        new AlertDialog.Builder(this).setMessage(sensorList)
                .setTitle(getString(R.string.sensorList))
                .setPositiveButton(getString(R.string.OK), (arg0, arg1) -> {
                }).show();
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public final void onSensorChanged(SensorEvent event) {

        switch (event.sensor.getType()) {
            case Sensor.TYPE_LIGHT:
                float light = event.values[0];
                luminescenceValue.setText(String.format("%.2f", light));
                break;
            case Sensor.TYPE_PROXIMITY:
                float proximity = event.values[0];
                proximityValue.setText(String.format("%.2f", proximity));
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                float[] rotMatrix = new float[9];
                float[] rotVals = new float[3];
                sensorManager.getRotationMatrixFromVector(rotMatrix, event.values);
                sensorManager.remapCoordinateSystem(rotMatrix,
                        sensorManager.AXIS_X, sensorManager.AXIS_Y, rotMatrix);
                sensorManager.getOrientation(rotMatrix, rotVals);
                float azimuth = (float) Math.toDegrees(rotVals[0]);
                float pitch = (float) Math.toDegrees(rotVals[1]);
                float roll = (float) Math.toDegrees(rotVals[2]);
                azimuthValue.setText(String.format("%.3f", azimuth));
                pitchValue.setText(String.format("%.2f", pitch));
                rollValue.setText(String.format("%.2f", roll));
                break;
            case Sensor.TYPE_STEP_COUNTER:
                float steps = event.values[0];
                stepsValue.setText(String.format("%.2f", steps));
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                float magX = event.values[0];
                float magY = event.values[1];
                float magZ = event.values[2];
                magneticValue.setText(String.format("%.2f", Math.sqrt(magX * magX + magY * magY + magZ * magZ)) + " µTesla");
                break;
            case Sensor.TYPE_PRESSURE:
                float pressure = event.values[0];
                pressureValue.setText(String.format("%.2f", pressure) + " mbar");
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                float temperature = event.values[0];
                temperatureValue.setText(String.format("%.2f", temperature) + " C");
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, rotation, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetic, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
