package com.fastandfood.gui.res;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Use hints in JPasswordField
 *
 * Inspired by Bart Kiers implementation of
 * @see com.fastandfood.gui.res.HintTextField
 * @author Borja
 */
public class HintPassField extends JPasswordField implements FocusListener {

    private final String hint;
    private boolean showingHint;

    public HintPassField(final String hint) {
        super(hint);
        super.setEchoChar((char) 0);
        this.hint = hint;
        this.showingHint = true;
        super.addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if(this.getPassword() == null) {
            // 0x2022 is • (hex UTF-16)
            super.setEchoChar((char) 0x2022);
            super.setText("");
            showingHint = false;
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if(this.getPassword() == null) {
            super.setEchoChar((char) 0);
            super.setText(hint);
            showingHint = true;
        }
    }

    @Override
    public char[] getPassword() {
        return showingHint ? null : super.getPassword();
    }
}
