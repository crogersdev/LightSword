package crogersdev.lightsword;

public class LightSwordState {

    LightSwordState() {
        this.m_bladeColor = bladeColor_e.GREEN;
        this.m_hilt = 1;
    }

    public boolean m_isOn;  // true for on, false for off

    public enum bladeColor_e {
        RED(0), GREEN(1), BLUE(2), PURPLE(3);
        private int value;
        bladeColor_e(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
        public void setValue(int v) { this.value = v; }
    }

    public bladeColor_e m_bladeColor;

    public int m_hilt;

    public bladeColor_e getColorAsEnum() {
        return m_bladeColor;
    }
}
