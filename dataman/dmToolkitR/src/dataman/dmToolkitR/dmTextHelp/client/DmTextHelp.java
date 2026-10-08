package dataman.dmToolkitR.dmTextHelp.client;

import com.google.gwt.dom.client.Style;
import dataman.dmBase.self.shared.*;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkitR.dmPageHelp.client.DmPageHelp;
import java.util.List;

public class DmTextHelp<T extends KeyField> extends DmTextHelp_Dsg implements IsWidget, AttachEvent.Handler, ClickHandler {

    public interface TextHelpCallBack<T extends KeyField> {

        public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait);

        public void getSelectedRow(T tRow);
    }

    private TextHelpCallBack textHelpCallBack;
    private String text;
    private String tag;
    private String tag2nd;
    private DmPageHelp<T> dmHelp;
    private boolean blnMandatory = false, blnAppendable = false;
    private boolean textEnable = true;

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {

        } else {
            dmHelp.hide();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(span)
                || event.getSource().equals(textBox)) {
            if (textEnable) {
                dmHelp.show("");
            }
        }
    }

    private void helpEvents() {

        span.addDomHandler(this, ClickEvent.getType());
        textBox.addClickHandler(this);

        dmHelp.addPageHelpCallBack(new DmPageHelp.PageHelpCallBack<T>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                if (textHelpCallBack != null) {
                    textHelpCallBack.populateData(helper, blnToEndOfFile, blnCallBackWait);
                }
            }

            @Override
            public void getSelectedRow(T t1Row) {
                KeyField keyField;

                if (t1Row != null) {
                    keyField = (KeyField) t1Row;
                    DmTextHelp.this.setText(keyField.getSharedKeyFieldName());
                    DmTextHelp.this.setTag(keyField.getSharedKeyFieldCode());
                } else if (isAppendable()) {
                    DmTextHelp.this.setTag("");

                } else {
                    DmTextHelp.this.setText("");
                    DmTextHelp.this.setTag("");
                }

                if (textHelpCallBack != null) {
                    textHelpCallBack.getSelectedRow(t1Row);
                }
            }
        });
    }

    public void addTextHelpCallBack(TextHelpCallBack textHelpCallBack) {
        this.textHelpCallBack = textHelpCallBack;
    }

    public void setAllData(List<T> lstData, DmPaging pageingVariables) {
        dmHelp.setAllData(lstData, pageingVariables);
    }

    public void setEnabled(boolean enable) {
        textEnable = enable;
        textBox.setEnabled(enable);
    }

    public void setFont(double value, Style.Unit unit) {
        textBox.getElement().getStyle().setFontSize(value, unit);
    }

    public boolean isEnabled() {
        return textEnable;
    }

    public void setVisible(boolean visible) {
        flowPanel.setVisible(visible);
    }

    public DmHelper getHelper() {
        return dmHelp.getHelper();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag2nd() {
        return tag2nd;
    }

    public void setTag2nd(String tag2nd) {
        this.tag2nd = tag2nd;
    }

    public String getText() {
        return text;
    }

    public void setText(String strValue) {
        text = strValue;
        textBox.setText(strValue);
    }

    public void setMaxLength(int length) {
        textBox.setMaxLength(length);
    }

    public void setMandatory(boolean blnMandatory) {
        textBox.removeStyleName("mandatory");
        if (blnMandatory) {
            textBox.addStyleName("mandatory");
        }
        this.blnMandatory = blnMandatory;
    }

    public boolean isMandatory() {
        return blnMandatory;
    }

    public void setAppendable(boolean blnAppendable) {
//        dmTextHelpGrid.setAppendable(blnAppendable);
        this.blnAppendable = blnAppendable;
    }

    public void setFocus(boolean focused) {
        textBox.setFocus(focused);
        textBox.selectAll();
    }

    public boolean isAppendable() {
        return blnAppendable;
    }

    public DmTextHelp(String caption, String searchField, int rowsPerPage, boolean mandatory) {
        super();
        startUp(caption, searchField, rowsPerPage, mandatory);
    }

    private void startUp(String caption, String searchField, int rowsPerPage, boolean mandatory) {
        flowPanel.addAttachHandler(this);
        dmHelp = new DmPageHelp(caption, searchField, rowsPerPage);
//        textBox.setReadOnly(true);
        textBox.getElement().setPropertyString("placeholder", caption);
        setMandatory(mandatory);
        helpEvents();
    }

    public void setPlaceholder(String caption) {
        textBox.getElement().setPropertyString("placeholder", caption);
    }
    
    public void setStyle(String styleName) {
        textBox.addStyleName(styleName);
    }

    @Override
    public Widget asWidget() {
        return flowPanel;
    }

    public DmPageHelp<T> getHelp() {
        return dmHelp;
    }

}
