package crogersdev.lightsword;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.os.Handler;
import android.widget.Toast;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.HashMap;

public class MainActivity extends FragmentActivity implements SensorEventListener {

    /* Accelerometer Fun */
    private SensorManager m_sensorMgr;
    private Sensor m_accelerometer;
    private final float m_noise = (float) 5.0;
    private final float m_clashThreshold = (float) 25.0;
    private final float m_swooshThreshold = (float) 8.0;
    private float[] m_lastX;
    private float[] m_lastY;
    private float[] m_lastZ;
    boolean m_xyzInitialized;

    /* Listeners */
    private View.OnClickListener m_viewListener;

    /* Buttons and Views */
    private ImageButton m_hiltBtn;
    private ImageButton m_bladeBtn;

    /* Sounds */
    private SoundPool m_soundPool;
    private HashMap<Integer, Integer> m_soundMap;
    private int m_humId;
    private boolean m_swooshSound;
    private boolean m_clashSound;
    int m_swordOnSoundId = 1;
    int m_swordOffSoundId = 2;
    int m_swordSwing1SoundId = 3;
    int m_swordSwing2SoundId = 4;
    int m_swordClashSoundId = 5;
    int m_swordHumSoundId = 6;

    /* the Light Sword State class object */
    LightSwordState m_swordState;

    /* Customize Dialog Stuff */
    AlertDialog customizeSwordDlg;

    private View swordOptionsView;
    private ImageButton m_dlgHilt1;
    private ImageButton m_dlgHilt2;
    private ImageButton m_dlgHilt3;
    private ImageButton m_dlgColorBlue;
    private ImageButton m_dlgColorGreen;
    private ImageButton m_dlgColorRed;
    private ImageButton m_dlgColorPurple;

    /* Animation */
    Animation m_animSwordOn;

    protected void onInflateObjects() {
        m_hiltBtn  = (ImageButton) findViewById(R.id.btn_hilt);
        m_bladeBtn = (ImageButton) findViewById(R.id.btn_blade);

        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        swordOptionsView = inflater.inflate(R.layout.sword_options, null);

        m_dlgHilt1       = (ImageButton) swordOptionsView.findViewById(R.id.hilt1Dialog);
        m_dlgHilt2       = (ImageButton) swordOptionsView.findViewById(R.id.hilt2Dialog);
        m_dlgHilt3       = (ImageButton) swordOptionsView.findViewById(R.id.hilt3Dialog);
        m_dlgColorBlue   = (ImageButton) swordOptionsView.findViewById(R.id.blueBladeDialog);
        m_dlgColorGreen  = (ImageButton) swordOptionsView.findViewById(R.id.greenBladeDialog);
        m_dlgColorRed    = (ImageButton) swordOptionsView.findViewById(R.id.redBladeDialog);
        m_dlgColorPurple = (ImageButton) swordOptionsView.findViewById(R.id.purpleBladeDialog);

        // SoundPool ctor: int maxStreams, int streamType, int srcQuality
        m_soundPool = new SoundPool(12, 3, 0);
    }

    protected void onCreateListeners() {
        m_viewListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            int btnId = view.getId();
            switch (btnId) {
                case R.id.btn_hilt:
                    toggleSword();
                    break;
                case R.id.btn_blade:
                    showBladeColorDialog();
                    break;
                case R.id.purpleBladeDialog:
                    m_swordState.m_color = LightSwordState.bladeColor_e.PURPLE;
                    break;
                case R.id.redBladeDialog:
                    m_swordState.m_color = LightSwordState.bladeColor_e.RED;
                    break;
                case R.id.greenBladeDialog:
                    m_swordState.m_color = LightSwordState.bladeColor_e.GREEN;
                    break;
                case R.id.blueBladeDialog:
                    m_swordState.m_color = LightSwordState.bladeColor_e.BLUE;
                    break;
                case R.id.hilt1Dialog:
                    m_swordState.m_hilt = 1;
                    break;
                case R.id.hilt2Dialog:
                    m_swordState.m_hilt = 2;
                    break;
                case R.id.hilt3Dialog:
                    m_swordState.m_hilt = 3;
                    break;
            } // end switch
        } // end onClick
        }; // end m_btnListener = onClickListener ...
    }

    protected void onSetListeners() {
        //todo: this is ugly ugly ugly.  please replace with something cleaner.  does the onclick from xml work?
        m_hiltBtn.setOnClickListener(m_viewListener);
        m_bladeBtn.setOnClickListener(m_viewListener);
        m_dlgHilt1.setOnClickListener(m_viewListener);
        m_dlgHilt2.setOnClickListener(m_viewListener);
        m_dlgHilt3.setOnClickListener(m_viewListener);
        m_dlgColorBlue.setOnClickListener(m_viewListener);
        m_dlgColorGreen.setOnClickListener(m_viewListener);
        m_dlgColorRed.setOnClickListener(m_viewListener);
        m_dlgColorPurple.setOnClickListener(m_viewListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.mainsword_activity);

        onInflateObjects();

        onCreateListeners();

        onSetListeners();

        // TODO: make all of the initialization functions in their own little "do all" function
        // group similar to above

        m_bladeBtn.setVisibility(View.INVISIBLE);

        m_swordState = new LightSwordState();
        m_swordState.m_isOn = false;
        // TODO: read from preferences here for defaults
        m_swordState.m_color = LightSwordState.bladeColor_e.BLUE;
        m_swordState.m_hilt = 2;

        m_swooshSound = false;
        m_clashSound = false;
        m_humId = -99;

        m_soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        m_soundMap = new HashMap<Integer, Integer>();
        m_soundMap.put(m_swordOnSoundId, m_soundPool.load(this, R.raw.swordon, 1));
        m_soundMap.put(m_swordOffSoundId, m_soundPool.load(this, R.raw.swordoff, 1));
        m_soundMap.put(m_swordSwing1SoundId, m_soundPool.load(this, R.raw.swing1, 1));
        m_soundMap.put(m_swordSwing2SoundId, m_soundPool.load(this, R.raw.swing2, 1));
        m_soundMap.put(m_swordClashSoundId, m_soundPool.load(this, R.raw.clash1, 1));
        m_soundMap.put(m_swordHumSoundId, m_soundPool.load(this, R.raw.hum2, 1));

        // setup accelerometer stuff
        m_lastX = new float[3];
        m_lastY = new float[3];
        m_lastZ = new float[3];
        m_xyzInitialized = false;
        m_sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        m_accelerometer = m_sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        m_sensorMgr.registerListener(this, m_accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        // Animation loads
        m_animSwordOn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.sword_on_anim);

        customizeSwordDlg = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_HOLO_DARK)
                .setTitle("Customize your Light Sword")
                .setView(swordOptionsView)
            .setPositiveButton(R.string.dlgConfirm, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Toast.makeText(MainActivity.this, "dialog clicked", Toast.LENGTH_SHORT).show();
                    m_hiltBtn.setImageDrawable(null);
                    m_bladeBtn.setImageDrawable(null);

                    switch (m_swordState.m_hilt) {
                        case 1:
                            m_hiltBtn.setBackgroundResource(R.drawable.hilt1_med);
                            break;
                        case 2:
                            m_hiltBtn.setBackgroundResource(R.drawable.hilt2_med);
                            break;
                        case 3:
                            m_hiltBtn.setBackgroundResource(R.drawable.hilt3_med);
                            break;
                    }
                    switch (m_swordState.getColorAsEnum()) {
                        case BLUE:
                            m_bladeBtn.setBackgroundResource(R.drawable.blue_blade_med);
                            break;
                        case RED:
                            m_bladeBtn.setBackgroundResource(R.drawable.red_blade_med);
                            break;
                        case GREEN:
                            m_bladeBtn.setBackgroundResource(R.drawable.green_blade_med);
                            break;
                        case PURPLE:
                            m_bladeBtn.setBackgroundResource(R.drawable.purple_blade_med);
                            break;
                    }
                }
            })
            .setNegativeButton(R.string.dlgCancel, null).create();
    }

    @Override
    // todo: consider moving all accelerometer data to its own class
    public synchronized void onSensorChanged(SensorEvent ev) {
        if (ev.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(ev);
        }
    }

    private void getAccelerometer(SensorEvent ev) {
        float[] aValues = ev.values;
        float currentX = aValues[0];
        float currentY = aValues[1];
        float currentZ = aValues[2];

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
            float deltaX = Math.abs(m_lastX[0] - currentX);
            float deltaY = Math.abs(m_lastY[0] - currentY);
            float deltaZ = Math.abs(m_lastZ[0] - currentZ);

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
            // but don't interrupt if already playing swoosh currently.

            if ((deltaX > m_noise ||
                 deltaY > m_noise ||
                 deltaZ > m_noise) &&
                m_swordState.m_isOn) {
                // TODO: randomize the swing sound so it doesn't sound exactly the same each time
                if (!m_swooshSound) {
                    m_swooshSound = true;
                    playSoundSoundPool(m_swordSwing2SoundId, false);
                    handler.postDelayed(r, 620);  // delay = duration of sound should be 620
                } else {
                    return;
                }
            }

            // High negative delta from previous to current means you're swinging and then stopping, play clash
            if ((currentX < m_noise && deltaX > m_clashThreshold ||
                 currentY < m_noise && deltaY > m_clashThreshold ||
                 currentY < m_noise && deltaZ > m_clashThreshold) &&
                m_swordState.m_isOn) {
                // TODO: light up LED for split second

                if (!m_clashSound) {
                    m_clashSound = true;
                    playSoundSoundPool(m_swordClashSoundId, false);
                    handler.postDelayed(r, 1340); // delay = duration of sound
                } else {
                    return;
                }
            }

            // Sudden change in direction means a clash
            if ((Math.abs(currentX - m_lastX[2]) > 15.0 ||
                 Math.abs(currentY - m_lastY[2]) > 15.0 ||
                 Math.abs(currentY - m_lastZ[2]) > 15.0) &&
                m_swordState.m_isOn) {

                if (!m_clashSound) {
                    m_clashSound = true;
                    playSoundSoundPool(m_swordClashSoundId, false);
                    handler.postDelayed(r, 1340); // delay = duration of sound
                } else {
                    return;
                }
            }
            // TODO: customize sensitivity - allow the user to figure it out themselves

            // TODO: block the laser fire?

            // TODO: make sure you get the "stab the ground" action from lego star wars

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_sword, menu);
        return true;
    }

    public int playSoundSoundPool(int sound, boolean loop) {
        AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;

        if (!loop) {
            return m_soundPool.play(m_soundMap.get(sound), volume, volume, 1, 0, 1.0f);
        } else {
            return m_soundPool.play(m_soundMap.get(sound), volume, volume, 1, -1, 1.0f);
        }
    }

    private void toggleSword() {
        if (!m_swordState.m_isOn) {
            m_bladeBtn.setVisibility(View.VISIBLE);
            playSoundSoundPool(m_swordOnSoundId, false);
            m_humId = playSoundSoundPool(m_swordHumSoundId, true);
            m_swordState.m_isOn = true;
        } else {
            m_bladeBtn.setVisibility(View.INVISIBLE);
            playSoundSoundPool(m_swordOffSoundId, false);
            m_soundPool.stop(m_humId);
            m_swordState.m_isOn = false;
        }

        //Toast.makeText(MainSwordActivity.this, "Off!", Toast.LENGTH_SHORT).show();
    }

    private void showBladeColorDialog() { customizeSwordDlg.show(); }

    final Handler handler = new Handler();
    final Runnable r = new Runnable() {
        public void run() {
            if (m_swooshSound) {
                m_swooshSound = false;
            }
            if (m_clashSound) {
                m_clashSound = false;
            }
        }

    };

    @Override
    protected void onDestroy() {
        // todo: is this where we would close down the app on exit?  right now if you hit "power,"
        // to turn the phone off, the app continues to hum as if the light saber is on (which it is)
        super.onDestroy();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // ignore
    }

    // NOTE: must implement onResume and onPause to deregister accelerometer listener call back
    // so that you conserve battery life and don't let the callback sit there waiting
    protected void onResume() {
        super.onResume();
        m_sensorMgr.registerListener(this, m_accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        m_sensorMgr.unregisterListener(this);
        m_soundPool.stop(m_humId);
    }
}
