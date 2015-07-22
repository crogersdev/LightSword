package crogersdev.lightsword;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SwordOptionsDialog extends DialogFragment implements android.view.View.OnClickListener {

    private View swordOptionsView;

    private ImageButton m_dlgHilt1;
    private ImageButton m_dlgHilt2;
    private ImageButton m_dlgHilt3;
    private ImageButton m_dlgColorBlue;
    private ImageButton m_dlgColorGreen;
    private ImageButton m_dlgColorRed;
    private ImageButton m_dlgColorPurple;

    public interface DlgIfc {
        // 'ok' of 'cancel'
        void okClicked(DialogFragment dlg);
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
        public void onClick (View v) {
            int btnId = v.getId();
            switch (btnId) {

            }
        }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        swordOptionsView = inflater.inflate(R.layout.sword_options, null);

        m_dlgHilt1       = (ImageButton) swordOptionsView.findViewById(R.id.hilt1Dialog);
        m_dlgHilt2       = (ImageButton) swordOptionsView.findViewById(R.id.hilt2Dialog);
        m_dlgHilt3       = (ImageButton) swordOptionsView.findViewById(R.id.hilt3Dialog);
        m_dlgColorBlue   = (ImageButton) swordOptionsView.findViewById(R.id.blueBladeDialog);
        m_dlgColorGreen  = (ImageButton) swordOptionsView.findViewById(R.id.greenBladeDialog);
        m_dlgColorRed    = (ImageButton) swordOptionsView.findViewById(R.id.redBladeDialog);
        m_dlgColorPurple = (ImageButton) swordOptionsView.findViewById(R.id.purpleBladeDialog);

//        ViewListener m_viewListener = new ViewListener();

 /*       m_dlgHilt1.setOnClickListener(m_viewListener);
        m_dlgHilt2.setOnClickListener(m_viewListener);
        m_dlgHilt3.setOnClickListener(m_viewListener);
        m_dlgColorBlue.setOnClickListener(m_viewListener);
        m_dlgColorGreen.setOnClickListener(m_viewListener);
        m_dlgColorRed.setOnClickListener(m_viewListener);
        m_dlgColorPurple.setOnClickListener(m_viewListener);
        */

        builder.setView(inflater.inflate(R.layout.sword_options, null))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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
