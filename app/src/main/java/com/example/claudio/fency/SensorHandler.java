package com.example.claudio.fency;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Francesco on 25/07/2017.
 */

public class SensorHandler extends FencyHandler implements SensorEventListener {

    private SensorFusion sensorFusion;
    private SensorManager sensorManager;
    private Sensor sensorLinearAcceleration;
    private int state;
    private float yPrevious, yDelta, peak;
    private boolean start;
    private long startingAttackTime;
    private final int SENSOR_DELAY = SensorManager.SENSOR_DELAY_GAME;

    public SensorHandler(FencyActivity context, Player player) {
        super(context, player);
    }

    public void registerSensorManagerListeners() {
        // Register 3 sensors for SensorFusion
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SENSOR_DELAY);

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SENSOR_DELAY);

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SENSOR_DELAY);

        // Register sensorLinearAcc
        sensorManager.registerListener(this, sensorLinearAcceleration, SENSOR_DELAY);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
