package crogersdev.lightsword;

public class LightSwordState {

    LightSwordState() {
        this.m_bladeColor = bladeColor_e.BLUE;
        this.m_hilt = 1;
    }

    public boolean m_isOn;  // true for on, false for off

    public enum bladeColor_e {
        RED(0), GREEN(1), BLUE(2), PURPLE(2);
        private final int value;
        bladeColor_e(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    public bladeColor_e m_bladeColor;

    public int m_hilt;

    public bladeColor_e getColorAsEnum() {
        return m_bladeColor;
    }
}
