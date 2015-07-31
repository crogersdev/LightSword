package crogersdev.lightsword;

public class LightSwordState {

    /*
        hilt 1, 2, 3, are just designs
        colors:
            red: 0
            green: 1
            blue: 2
            purple: 3
     */

    LightSwordState() {
        this.m_bladeColor = 1;
        this.m_hilt = 1;
        this.m_isOn = false;
    }

    public boolean m_isOn;  // true for on, false for off
    public int m_bladeColor;
    public int m_hilt;

}
