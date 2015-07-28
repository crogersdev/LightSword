package crogersdev.lightsword;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class AccelerometerIfc implements SensorEventListener {

    LightSwordState m_swordState;

    private SensorManager m_sensorMgr;
    private Sensor m_accelerometer;
    private final float m_noise = (float) 5.0;
    private final float m_clashThreshold = (float) 25.0;
    private final float m_swooshThreshold = (float) 8.0;
    private float[] m_lastX;
    private float[] m_lastY;
    private float[] m_lastZ;
    boolean m_xyzInitialized;

    public interface EventCB {
        void swooshEvent();
        void clashEvent();
    }

    EventCB m_callback;

    AccelerometerIfc(Context cxt, LightSwordState curState) {
        Log.d("CROGERSDEV", "Inside getAccelerometer");
        m_swordState = curState;

        // setup accelerometer stuff
        m_lastX = new float[3];
        m_lastY = new float[3];
        m_lastZ = new float[3];
        m_xyzInitialized = false;
        m_sensorMgr = (SensorManager) cxt.getSystemService(cxt.SENSOR_SERVICE);
        m_accelerometer = m_sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        m_sensorMgr.registerListener(this, m_accelerometer, SensorManager.SENSOR_DELAY_NORMAL);


        try {
            m_callback = (EventCB) cxt;
        } catch (ClassCastException e) {
            throw new ClassCastException("AccelerometerIfc class must implement callback interface");
        }

    }

    @Override
    // todo: consider moving all accelerometer data to its own class
    public synchronized void onSensorChanged(SensorEvent ev) {
        if (ev.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(ev);
        }
    }

    private void getAccelerometer(SensorEvent ev) {
        Log.d("CROGERSDEV", "Inside getAccelerometer");
        float[] aValues = ev.values;
        float currentX = aValues[0];
        float currentY = aValues[1];
        float currentZ = aValues[2];

        float deltaX = 0;
        float deltaY = 0;
        float deltaZ = 0;

        if (!m_xyzInitialized) {
            m_lastX[0] = currentX;
            m_lastX[1] = currentX;
            m_lastX[2] = currentX;

            m_lastY[0] = currentY;
            m_lastY[1] = currentY;
            m_lastY[2] = currentY;

            m_lastZ[0] = currentZ;
            m_lastZ[1] = currentZ;
            m_lastZ[2] = currentZ;

            m_xyzInitialized = true;
        } else {
            deltaX = Math.abs(m_lastX[0] - currentX);
            deltaY = Math.abs(m_lastY[0] - currentY);
            deltaZ = Math.abs(m_lastZ[0] - currentZ);

            if (deltaX < m_noise) deltaX = (float) 0.0;
            if (deltaY < m_noise) deltaY = (float) 0.0;
            if (deltaZ < m_noise) deltaZ = (float) 0.0;

            m_lastX[0] = currentX;
            m_lastY[0] = currentY;
            m_lastZ[0] = currentZ;

            m_lastX[1] = m_lastX[0];
            m_lastY[1] = m_lastY[0];
            m_lastZ[1] = m_lastZ[0];

            m_lastX[2] = m_lastX[1];
            m_lastY[2] = m_lastY[1];
            m_lastZ[2] = m_lastZ[1];

            double avgX = (m_lastX[0] + m_lastX[1] + m_lastX[2]) / 3.0;
            double avgY = (m_lastY[0] + m_lastY[1] + m_lastY[2]) / 3.0;
            double avgZ = (m_lastZ[0] + m_lastZ[1] + m_lastZ[2]) / 3.0;

            /** Conditions for sound playing */

            // High average value for all three last values means swinging, play swoosh
            if ((deltaX > m_noise || deltaY > m_noise || deltaZ > m_noise) && m_swordState.m_isOn) {
                // TODO: randomize the swing sound so it doesn't sound exactly the same each time
                m_callback.swooshEvent();
            } else {
                return;
            }

            // High negative delta from previous to current means you're swinging and then stopping, play clash
            if ((currentX < m_noise && deltaX > m_clashThreshold || currentY < m_noise && deltaY > m_clashThreshold || currentY < m_noise && deltaZ > m_clashThreshold) && m_swordState.m_isOn) {
                // TODO: light up LED for split second
                m_callback.clashEvent();
            } else {
                return;
            }

            // Sudden change in direction means a clash
            if ((Math.abs(currentX - m_lastX[2]) > 15.0 || Math.abs(currentY - m_lastY[2]) > 15.0 || Math.abs(currentY - m_lastZ[2]) > 15.0) && m_swordState.m_isOn) {
                //todo: play clash sound
                m_callback.clashEvent();
            } else {
                return;
            }
        }
        // TODO: customize sensitivity - allow the user to figure it out themselves

        // TODO: block the laser fire?

        // TODO: make sure you get the "stab the ground" action from lego star wars

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // ignore
    }

    void registerListener() {
        m_sensorMgr.registerListener(this, m_accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    void unregisterListener() {
        m_sensorMgr.unregisterListener(this);
    }
}
