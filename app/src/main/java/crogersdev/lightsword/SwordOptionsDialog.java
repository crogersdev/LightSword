package crogersdev.lightsword;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SwordOptionsDialog extends DialogFragment {

    /* Logging message levels */
    public static int LOGLEVEL = 1;
    public static boolean WARN = LOGLEVEL > 1;
    public static boolean DEBUG = LOGLEVEL > 0;

    // set the custom dialog components - text, image and button
    public ImageButton m_hilt1SelectBtn;
    public ImageButton m_hilt2SelectBtn;
    public ImageButton m_hilt3SelectBtn;
    public ImageButton m_blueBladeSelectBtn;
    public ImageButton m_redBladeSelectBtn;
    public ImageButton m_greenBladeSelectBtn;
    public ImageButton m_purpleBladeSelectBtn;

    private View.OnClickListener m_dialogListener;

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (DEBUG)
            Log.d("SwordOptionsDialog", "Able to create builder");

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.sword_options, null);

        if (DEBUG)
            Log.d("SwordOptionsDialog", "Able to inflate");

        m_hilt1SelectBtn       = (ImageButton) v.findViewById(R.id.hilt1Dialog);
        m_hilt2SelectBtn       = (ImageButton) v.findViewById(R.id.hilt2Dialog);
        m_hilt3SelectBtn       = (ImageButton) v.findViewById(R.id.hilt3Dialog);
        m_blueBladeSelectBtn   = (ImageButton) v.findViewById(R.id.blueBladeDialog);
        m_redBladeSelectBtn    = (ImageButton) v.findViewById(R.id.redBladeDialog);
        m_greenBladeSelectBtn  = (ImageButton) v.findViewById(R.id.greenBladeDialog);
        m_purpleBladeSelectBtn = (ImageButton) v.findViewById(R.id.purpleBladeDialog);

        if (DEBUG)
            Log.d("SwordOptionsDialog", "Able to find view by id");

        m_hilt1SelectBtn.setOnClickListener(m_dialogListener);
        m_hilt2SelectBtn.setOnClickListener(m_dialogListener);
        m_hilt3SelectBtn.setOnClickListener(m_dialogListener);
        m_blueBladeSelectBtn.setOnClickListener(m_dialogListener);
        m_redBladeSelectBtn.setOnClickListener(m_dialogListener);
        m_greenBladeSelectBtn.setOnClickListener(m_dialogListener);
        m_purpleBladeSelectBtn.setOnClickListener(m_dialogListener);

        if (DEBUG)
            Log.d("SwordOptionsDialog", "able to set on click listeners");

        m_dialogListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int btnId = view.getId();
                switch (btnId) {
                    case R.id.hilt1Dialog:
                        break;
                    case R.id.hilt2Dialog:
                        break;
                    case R.id.hilt3Dialog:
                        break;

                    case R.id.blueBladeDialog:
                        break;
                    case R.id.greenBladeDialog:
                        break;
                    case R.id.redBladeDialog:
                        break;
                    case R.id.purpleBladeDialog:
                        break;
                    default:
                        break;
                } // end switch
            } // end onClick
        };

        if (DEBUG)
            Log.d("SwordOptionsDialog", "Able to create dialog listener");

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v)
               .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {

                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                   }
               });
        // Create the AlertDialog object and return it

        if (DEBUG)
            Log.d("SwordOptionsDialog", "Able to call setView(v)");

        return builder.create();
    }



    @Override
    public void onCreate(Bundle bundle) {

    }
}