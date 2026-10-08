package dataman.dmToolkit.dmSuggestBox.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmPaging;
import java.util.List;

public class DmSuggestBox extends DmSuggestBox_Dsg implements IsWidget, BlurHandler, KeyUpHandler {

    public interface CallBack {

        public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait);
    }

    private DmHelper helper;
    private CallBack callBack;
    private boolean blnMandatory = false;
    private String previousValue = "";
    private String textValueDuringEnable;
    private boolean textEnable = true;
    private String searchDbFieldName;
    private DmEnum.DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;

    private void txtKeyEvents() {
        this.addKeyUpHandler(this);

        this.addBlurHandler(this);
    }

    @Override
    public void onBlur(BlurEvent event) {
        ppSuggestion.hide();
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        txtKeyUp(event);
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void setData(List<String> data, DmPaging pageingVariables) {
        helper.setPaging(pageingVariables);
        lbxSuggestion.clear();
        for (int i = 0; i < data.size(); i++) {
            lbxSuggestion.addItem(data.get(i));
        }
        if (data.size() > 0) {
            ppSuggestion.showRelativeTo(this);
        } else {
            ppSuggestion.hide();
        }
    }

    public void setMandatory(boolean blnMandatory) {
        this.getElement().getStyle().setBorderColor("lightgray");
        if (blnMandatory) {
            this.getElement().setAttribute("style", this.getElement().getAttribute("style")
                    + "; border-left: 1px solid red;"
            );
        }
        this.blnMandatory = blnMandatory;
    }

    public boolean isMandatory() {
        return blnMandatory;
    }

    @Override
    public String getText() {
        if (textEnable) {
            return super.getText();
        } else {
            return textValueDuringEnable;
        }
    }

    @Override
    public void setText(String value) {
        textValueDuringEnable = value;
        super.setText(value);
    }

    @Override
    public void setEnabled(boolean enable) {
        textEnable = enable;
        if (!enable) {
            textValueDuringEnable = super.getText();
        }
        super.setEnabled(enable);
    }

    @Override
    public boolean isEnabled() {
        return textEnable;
    }

    public DmSuggestBox(int maxLengtgh, int rowsPerPage, boolean mandatory, String searchDbFieldName, DmEnum.DmDataType DbFieldDataType) {
        super();
        txtKeyEvents();
        setMaxLength(maxLengtgh);
        setMandatory(mandatory);
        helper = new DmHelper(0, rowsPerPage);
        helper.addFilter(searchDbFieldName, DbFieldDataType, DmEnum.DmOperator.BEGINSWITH, "", null, DmEnum.DmOperand.AND);
        ppSuggestion.setHeight(String.valueOf(helper.getRowsPerPage() * 20) + "px");
    }

//    public DmSuggestBox(int maxLengtgh, int rowsPerPage, boolean mandatory) {
//        super();
//        txtKeyEvents();
//        setMaxLength(maxLengtgh);
//        setMandatory(mandatory);
//        helper = new DmHelper(0, rowsPerPage);
////        helper.addFilter(searchDbFieldName,DbFieldDataType , DmEnum.DmOperator.BEGINSWITH, "", null, DmEnum.DmOperand.AND);
//        ppSuggestion.setHeight(String.valueOf(helper.getRowsPerPage() * 20) + "px");
//    }
    @Override
    public Widget asWidget() {
        return this;
    }

    private void txtKeyUp(KeyUpEvent event) {
        if ((!previousValue.equalsIgnoreCase(this.getText())) && (!this.getText().equalsIgnoreCase(""))) {
            reQuery();
        }
        previousValue = this.getText();
    }

    private void reQuery() {
        if (callBack != null) {
            helper.setRowsStartFrom(0);
            helper.getFilter().get(0).setValue1(super.getText());
            callBack.populateData(helper, false, false);
        }
    }
}
