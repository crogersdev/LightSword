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

public class SwordOptionsDialog extends DialogFragment {

    private static String LOG_TAG = "log_swordoptionsdlg";

    private View m_swordOptionsView;
    private View.OnClickListener m_listener;

    private ImageButton m_dlgHilt1;
    private ImageButton m_dlgHilt2;
    private ImageButton m_dlgHilt3;
    private ImageButton m_dlgColorBlue;
    private ImageButton m_dlgColorGreen;
    private ImageButton m_dlgColorRed;
    private ImageButton m_dlgColorPurple;

    private static int m_hilt;
    private static LightSwordState.bladeColor_e m_bladeColor;

    public SwordOptionsDialog() {}

    public static SwordOptionsDialog newInstance(int curHilt,
                                                       LightSwordState.bladeColor_e curBladeColor) {
        m_hilt = curHilt;
        m_bladeColor = curBladeColor;

        SwordOptionsDialog fragment = new SwordOptionsDialog();
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
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        m_swordOptionsView = inflater.inflate(R.layout.sword_options, null);

        m_dlgHilt1       = (ImageButton) m_swordOptionsView.findViewById(R.id.hilt1Dialog);
        m_dlgHilt2       = (ImageButton) m_swordOptionsView.findViewById(R.id.hilt2Dialog);
        m_dlgHilt3       = (ImageButton) m_swordOptionsView.findViewById(R.id.hilt3Dialog);
        m_dlgColorBlue   = (ImageButton) m_swordOptionsView.findViewById(R.id.blueBladeDialog);
        m_dlgColorGreen  = (ImageButton) m_swordOptionsView.findViewById(R.id.greenBladeDialog);
        m_dlgColorRed    = (ImageButton) m_swordOptionsView.findViewById(R.id.redBladeDialog);
        m_dlgColorPurple = (ImageButton) m_swordOptionsView.findViewById(R.id.purpleBladeDialog);

        m_listener = new View.OnClickListener() {
            Integer h, c;
            @Override
            public void onClick(View v) {
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
        };

        m_dlgHilt1.setOnClickListener(m_listener);
        m_dlgHilt2.setOnClickListener(m_listener);
        m_dlgHilt3.setOnClickListener(m_listener);
        m_dlgColorBlue.setOnClickListener(m_listener);
        m_dlgColorGreen.setOnClickListener(m_listener);
        m_dlgColorRed.setOnClickListener(m_listener);
        m_dlgColorPurple.setOnClickListener(m_listener);

        builder.setView(m_swordOptionsView)
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
