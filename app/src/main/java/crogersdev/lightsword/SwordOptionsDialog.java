package crogersdev.lightsword;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

public class SwordOptionsDialog extends DialogFragment implements android.view.View.OnClickListener {

    private View m_swordOptionsView;

    private View.OnClickListener m_listener;

    private ImageButton m_dlgHilt1;
    private ImageButton m_dlgHilt2;
    private ImageButton m_dlgHilt3;
    private ImageButton m_dlgColorBlue;
    private ImageButton m_dlgColorGreen;
    private ImageButton m_dlgColorRed;
    private ImageButton m_dlgColorPurple;

    private int m_hilt;
    private LightSwordState.bladeColor_e m_bladeColor;

    private static final String CUR_HILT = "current hilt";
    private static final String CUR_BLADECOLOR = "current blade color";

    public SwordOptionsDialog() {}

    public static SwordOptionsDialog newInstance(int curHilt,
                                                       LightSwordState.bladeColor_e curBladeColor) {
        SwordOptionsDialog fragment = new SwordOptionsDialog();
        Bundle bundle = new Bundle(2);
        bundle.putInt(CUR_HILT, curHilt);
        bundle.putInt(CUR_BLADECOLOR, curBladeColor.getValue());
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface DlgIfc {
        // potential addition in the future - another ifc function for 'cancel'
        void okClicked(int hilt, LightSwordState.bladeColor_e color);
    }

    DlgIfc m_callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            m_callback = (DlgIfc) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement DlgIfc");
        }
    }

    @Override
    public void onClick(View v) {
        Log.d("LOG_CROGERS", "onClick in swordOptionsDialog called");
        int btnId = v.getId();
        switch (btnId) {
            case R.id.hilt1Dialog:
                m_hilt = 1;
                break;
            case R.id.hilt2Dialog:
                m_hilt = 2;
                break;
            case R.id.hilt3Dialog:
                m_hilt = 3;
                break;

            case R.id.redBladeDialog:
                m_bladeColor = LightSwordState.bladeColor_e.RED;
                break;
            case R.id.greenBladeDialog:
                m_bladeColor = LightSwordState.bladeColor_e.GREEN;
                break;
            case R.id.blueBladeDialog:
                m_bladeColor = LightSwordState.bladeColor_e.BLUE;
                break;
            case R.id.purpleBladeDialog:
                m_bladeColor = LightSwordState.bladeColor_e.PURPLE;
                break;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        m_hilt = getArguments().getInt(CUR_HILT);
        int tmp = getArguments().getInt(CUR_BLADECOLOR);
        m_bladeColor = LightSwordState.bladeColor_e.values()[tmp];

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        m_swordOptionsView = inflater.inflate(R.layout.sword_options, null);

        m_dlgHilt1       = (ImageButton) m_swordOptionsView.findViewById(R.id.hilt1Dialog);
        m_dlgHilt2       = (ImageButton) m_swordOptionsView.findViewById(R.id.hilt2Dialog);
        m_dlgHilt3       = (ImageButton) m_swordOptionsView.findViewById(R.id.hilt3Dialog);
        m_dlgColorBlue   = (ImageButton) m_swordOptionsView.findViewById(R.id.blueBladeDialog);
        m_dlgColorGreen  = (ImageButton) m_swordOptionsView.findViewById(R.id.greenBladeDialog);
        m_dlgColorRed    = (ImageButton) m_swordOptionsView.findViewById(R.id.redBladeDialog);
        m_dlgColorPurple = (ImageButton) m_swordOptionsView.findViewById(R.id.purpleBladeDialog);

        m_dlgHilt1.setOnClickListener(this);
        m_dlgHilt2.setOnClickListener(this);
        m_dlgHilt3.setOnClickListener(this);
        m_dlgColorBlue.setOnClickListener(this);
        m_dlgColorGreen.setOnClickListener(this);
        m_dlgColorRed.setOnClickListener(this);
        m_dlgColorPurple.setOnClickListener(this);

        builder.setView(inflater.inflate(R.layout.sword_options, null))
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       m_callback.okClicked(m_hilt, m_bladeColor);
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       SwordOptionsDialog.this.getDialog().cancel();
                   }
               });
        return builder.create();
    }
}
