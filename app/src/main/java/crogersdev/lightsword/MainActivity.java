package crogersdev.lightsword;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.os.Handler;
import android.widget.Toast;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.HashMap;

public class MainActivity extends FragmentActivity implements AccelerometerIfc.EventCB, SwordOptionsDialog.DlgIfc {
    /* Logger Tag */
    private static final String LOG_TAG = "log_mainactivity";

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
    SwordOptionsDialog customSwordDlg;

    /* Accelerometer Object */
    AccelerometerIfc m_accelIfc;

    /* Animation */
    Animation m_animSwordOn;

    protected void onInflateObjects() {
        m_hiltBtn  = (ImageButton) findViewById(R.id.btn_hilt);
        m_bladeBtn = (ImageButton) findViewById(R.id.btn_blade);

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
                    Bundle bundle = new Bundle(2);
                    bundle.putInt("hilt", m_swordState.m_hilt);
                    bundle.putInt("color", m_swordState.m_bladeColor);
                    customSwordDlg.setArguments(bundle);
                    customSwordDlg.show(getFragmentManager(), "Customize Light Sword");
                    break;
            } // end switch
        } // end onClick
        }; // end m_btnListener = onClickListener ...
    }

    protected void onSetListeners() {
        m_hiltBtn.setOnClickListener(m_viewListener);
        m_bladeBtn.setOnClickListener(m_viewListener);
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

        m_bladeBtn.setVisibility(View.INVISIBLE);

        m_swordState = new LightSwordState();
        m_swordState.m_isOn = false;
        // TODO: read from preferences here for defaults

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
        m_accelIfc = new AccelerometerIfc(this, m_swordState);

        // Animation loads
        m_animSwordOn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.sword_on_anim);

        // Customize LightSword
        customSwordDlg = SwordOptionsDialog.newInstance();
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

    @Override
    public void okClicked(int newHiltSelection, int newBladeColor) {
        m_swordState.m_bladeColor = newBladeColor;
        m_swordState.m_hilt = newHiltSelection;
        redrawSword();
    }

    @Override
    public void swooshEvent() {
        // todo: randomize between m_swordSwing2SoundId and m_swordSwing1SoundId
        if (!m_swooshSound) {
            m_swooshSound = true;
            playSoundSoundPool(m_swordSwing2SoundId, false);
            handler.postDelayed(r, 620); // delay = duration of sound should be 620 ms
        } else {
            return;
        }
    }

    @Override
    public void clashEvent() {
        if (!m_clashSound) {
            m_clashSound = true;
            playSoundSoundPool(m_swordClashSoundId, false);
            handler.postDelayed(r, 1340); // delay = duration of sound should be 1340ms
        }
    }

    private void redrawSword() {
        switch (m_swordState.m_hilt) {
            case 1:
                m_hiltBtn.setImageResource(R.drawable.hilt1_med);
                break;
            case 2:
                m_hiltBtn.setImageResource(R.drawable.hilt2_med);
                break;
            case 3:
                m_hiltBtn.setImageResource(R.drawable.hilt3_med);
                break;
        }

        switch (m_swordState.m_bladeColor) {
            case 0:
                m_bladeBtn.setImageResource(R.drawable.red_blade_med);
                break;
            case 1:
                m_bladeBtn.setImageResource(R.drawable.green_blade_med);
                break;
            case 2:
                m_bladeBtn.setImageResource(R.drawable.blue_blade_med);
                break;
            case 3:
                m_bladeBtn.setImageResource(R.drawable.purple_blade_med);
                break;
        }
    }

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

    // NOTE: must implement onResume and onPause to deregister accelerometer listener call back
    // so that you conserve battery life and don't let the callback sit there waiting
    protected void onResume() {
        super.onResume();
        m_accelIfc.registerListener();
    }

    protected void onPause() {
        // todo: on screen lock stop the sound pool
        super.onPause();
        m_accelIfc.unregisterListener();
        m_soundPool.stop(m_humId);
    }
}
