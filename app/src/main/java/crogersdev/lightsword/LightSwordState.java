package crogersdev.lightsword;

public class LightSwordState {
    public boolean m_isOn;  // true for on, false for off

    public enum bladeColor_e {
        RED,
        GREEN,
        BLUE,
        PURPLE
    }

    public bladeColor_e m_color;

    public int m_hilt;

    public bladeColor_e getColor() {
        return m_color;
    }
}
