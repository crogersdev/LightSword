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

public class SwordOptionsDialog extends DialogFragment {

    public interface DlgIfc {
        // 'ok' of 'cancel'
        public void okClicked(DialogFragment dlg);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

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
