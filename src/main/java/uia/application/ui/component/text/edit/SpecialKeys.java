package uia.application.ui.component.text.edit;

/**
 * SpecialKeys is responsible for listing the relevant key codes.
 */

public enum SpecialKeys {
    BACKSPACE(8),
    SHIFT(16),
    CTRL(17),
    ALT_GR(18),
    CAPS_LOCK(20),
    ESC(27),
    PAGE_UP(33),
    PAGE_DOWN(34),
    KEY_LEFT(37),
    KEY_UP(38),
    KEY_RIGHT(39),
    KEY_DOWN(40);

    final int keyCode;

    SpecialKeys(int keyCode) {
        this.keyCode = keyCode;
    }

    /**
     * @return the code of the registered key
     */

    public int getKeyCode() {
        return keyCode;
    }

    /**
     * @return all the registered keys
     */

    public static int[] getAllKeys() {
        SpecialKeys[] keys = SpecialKeys.values();
        int[] result = new int[keys.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = keys[i].keyCode;
        }
        return result;
    }
}
