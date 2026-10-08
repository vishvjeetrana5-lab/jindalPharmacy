package dataman.accounts.tallyExportMasters.client;


import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.libraryMenu.self.shared.lmAccounts;

public class TallyExportMasters_Dsg {

    final String widgetName = new lmAccounts().Tally_Export_Masters.caption;
    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmToolContainer toolContainer = new DmToolContainer();
    final Button btnExportXML = new Button("Export Account Master XML");
    final Button btnClose = new Button();



    public TallyExportMasters_Dsg() {
       
        ini();
    }

    private void ini() {
        FlowPanel flw;
        VerticalPanel vrtInnerForm = new VerticalPanel();
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("200px");
        flwForm.setWidth("980px");
        vrtInnerForm.add(iniToolContainer());

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContainer() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close: (Escap)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.getElement().setTitle("Close: (Esc)");
        btnClose.setWidth("70px");

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();
        toolContainer.setModeVisible(false);
        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "23%", "32px", null, null, DmEnum.DmPosition.CENTER, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99%", null, "left", null, DmEnum.DmPosition.NONE, false);
       
        btnExportXML.setHeight("30px");
        btnExportXML.setWidth("211px");
        btnExportXML.getElement().setAttribute("style", btnExportXML.getElement().getAttribute("style")+ ";font-size:11px;padding:0px;");
        btnExportXML.addStyleName("dm-button-grad");
        flwTd.add(vl.add(btnExportXML));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        return flwTable;
    }

}
