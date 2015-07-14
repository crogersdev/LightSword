package crogersdev.lightsword;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class SwordOptionsDialog extends Dialog {
    public LightSwordState.bladeColor_e m_swordColor;
    public int m_hilt;

    OnMyDialogResult m_dlgResult;

    private ImageButton m_dlgHilt1;
    private ImageButton m_dlgHilt2;
    private ImageButton m_dlgHilt3;
    private ImageButton m_dlgColorBlue;
    private ImageButton m_dlgColorGreen;
    private ImageButton m_dlgColorRed;
    private ImageButton m_dlgColorPurple;

    public SwordOptionsDialog(Context cxt) {
        super(cxt);
        this.m_swordColor = LightSwordState.bladeColor_e.BLUE;
        this.m_hilt = 1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sword_options);
        setTitle("Customize your Light Sword!");
        //Button okBtn = (Button) findViewById()
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
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
                m_swordColor = LightSwordState.bladeColor_e.RED;
                break;
            case R.id.blueBladeDialog:
                m_swordColor = LightSwordState.bladeColor_e.BLUE;
                break;
            case R.id.greenBladeDialog:
                m_swordColor = LightSwordState.bladeColor_e.GREEN;
                break;
            case R.id.purpleBladeDialog:
                m_swordColor = LightSwordState.bladeColor_e.PURPLE;
                break;
        } // end switch
    }

    private class OKListener implements android.view.View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (m_dlgResult != null) {
                m_dlgResult.finish(m_hilt, m_swordColor);
            }
            SwordOptionsDialog.this.dismiss();
        }
    }

    public void setDialogResult(OnMyDialogResult dlgResult) {
        m_dlgResult = dlgResult;
    }

    public interface OnMyDialogResult {
        void finish(int hilt, LightSwordState.bladeColor_e color);
    }
}