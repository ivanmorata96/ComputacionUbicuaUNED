package com.example.misensor;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private int deadband = 0;
    SensorManager sensorManager;

    double vFinal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager != null) {
            Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, deadband);
            } else
                Toast.makeText(this, "El sensor de acelerómetro no está operativo.", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "El servicio de sensor no está operativo.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){

            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];
            double velocidad = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
            vFinal = Math.round(velocidad*100.0) / 100.0;

            if (vFinal >= 0 && vFinal < 1) {
                ((TextView) findViewById(R.id.estado)).setText("Parado");
                deadband = 0;
            }
            if (vFinal >= 1 && vFinal < 4) {
                ((TextView) findViewById(R.id.estado)).setText("Caminando");
                deadband = 1;
            }
            if (vFinal >= 4 && vFinal < 6) {
                ((TextView) findViewById(R.id.estado)).setText("Marchando");
                deadband = 2;
            }
            if (vFinal >= 6 && vFinal < 12) {
                ((TextView) findViewById(R.id.estado)).setText("Corriendo");
                deadband = 3;
            }
            if (vFinal >= 12 && vFinal < 25) {
                ((TextView) findViewById(R.id.estado)).setText("Sprint");
                deadband = 4;
            }
            if (vFinal >= 25 && vFinal < 170) {
                ((TextView) findViewById(R.id.estado)).setText("Vehículo terrestre");
                deadband = 5;
            }
            if (vFinal >= 170) {
                ((TextView) findViewById(R.id.estado)).setText("Vehiculo aéreo");
                deadband = 6;
            }

            ((TextView)findViewById(R.id.txtValues)).setText("" + vFinal);

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), deadband);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

}