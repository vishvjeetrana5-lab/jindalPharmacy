package dataman.accounts.bankLetterCreation.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.HasDirection;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;

abstract class PrintPopup_Dsg {

    public final static String widgetName = "Print";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final Button btnCheque = new Button("Cheque");
    final Button btnBankLetter = new Button("Bank Letter");

    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final FlowPanel flwSetting = new FlowPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final CheckBox cbCheckPrint = new CheckBox("", HasDirection.Direction.RTL);
    final CheckBox cbBankLetterPrint = new CheckBox("", HasDirection.Direction.RTL);

    public PrintPopup_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("243px");
        popupPanel.setHeight("90px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("Ok");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOk.add(btnOk);

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Close");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "55px", null, null, DmPosition.CENTER, true);


        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "55px", "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "55px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(topArea()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "55px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTr.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(cbCheckPrint));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "85%", null, "left", null, DmPosition.NONE, false);
        btnCheque.setTitle("Cheque");
        btnCheque.addStyleName("dm-button-grad");
        btnCheque.setWidth("92%");
        btnCheque.setHeight("20px");
        btnCheque.getElement().setAttribute("style", btnCheque.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnCheque.getElement().setAttribute("style", btnCheque.getElement().getAttribute("style")
                + ";margin-bottom:2px;padding:0px;");
        flwTd.add(vl.add(btnCheque));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTr.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(cbBankLetterPrint));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "85%", null, "left", null, DmPosition.NONE, false);
        btnBankLetter.setTitle("Bank Letter");
        btnBankLetter.addStyleName("dm-button-grad");
        btnBankLetter.setWidth("92%");
        btnBankLetter.setHeight("20px");
        btnBankLetter.getElement().setAttribute("style", btnBankLetter.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnBankLetter.getElement().setAttribute("style", btnBankLetter.getElement().getAttribute("style")
                + ";margin-bottom:2px;padding:0px;");
        flwTd.add(vl.add(btnBankLetter));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

}
