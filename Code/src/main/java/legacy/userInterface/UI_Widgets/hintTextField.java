package legacy.userInterface.UI_Widgets;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class hintTextField extends JTextField implements FocusListener {
    private String hint;
    private boolean showingHint = true;

    public hintTextField(String hint){
        this(null, null, 0, hint);
    }

    public hintTextField(String text, String hint){
        this(null, text, 0, hint);
    }

    public hintTextField(int columns, String hint) {
        this(null, null, columns, hint);
    }

    public hintTextField(String text, int columns, String hint) {
        this(null, text, columns, hint);
    }

    public hintTextField(Document doc, String text, int columns, String hint) {
        super(doc, text, columns);
        this.hint = hint;
        super.addFocusListener(this);
        focusLost(new FocusEvent(this, 0, true, null));
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (this.getText().isEmpty()){
            this.setForeground(getForeground().darker());
            setText("");
            showingHint = false;
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (this.getText().isEmpty()){
            this.setForeground(getForeground().brighter());
            setText(hint);
            showingHint = true;
        }
    }

    @Override
    public String getText(){
        return showingHint ? "" : super.getText();
    }

    public void setHint(String hint){
        this.hint = hint;
    }

    public void setText(String text){
        super.setText(text);
        showingHint = false;
    }

}
