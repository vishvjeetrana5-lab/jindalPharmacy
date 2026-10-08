package dataman.webJobs.smsManager.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.webJobs.emailManager.shared.Template_Mdl;
import dataman.webJobs.smsTemplate.shared.GrdFieldName_Mdl;

public abstract class DraftPopup_Dsg {
    
    public final static String widgetName = "Draft SMS";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button();
    
    final DmTextArea txtMsg = new DmTextArea(500, true);

    //<editor-fold defaultstate="collapsed" desc="txtHTemplate Name">
    interface TxtHTemp_PrpAcs extends KeyField_T_PrpAcs<Template_Mdl> {
        
        @Editor.Path("TemplateGroupText")
        ValueProvider<Template_Mdl, String> underTemp();
        
        @Editor.Path("FuncOrFileName")
        ValueProvider<Template_Mdl, String> funFileName();
        
        @Editor.Path("MessageTemplate")
        ValueProvider<Template_Mdl, String> messageTemplate();
    }
    final DmTextHelp<Template_Mdl, TxtHTemp_PrpAcs> txtHTemplateName = new DmTextHelp(GWT.create(TxtHTemp_PrpAcs.class), 7, true);
    
    private void iniTempName() {
        txtHTemplateName.getDmGrid().addTextColumn(txtHTemplateName.getDmGrid().getPropertyAccess().name(), "Template Name", 200, "name");
        txtHTemplateName.getDmGrid().addTextColumn(txtHTemplateName.getDmGrid().getPropertyAccess().underTemp(), "Under Temp", 100, "groupName");
        txtHTemplateName.getDmGrid().addTextColumn(txtHTemplateName.getDmGrid().getPropertyAccess().funFileName(), "Function / File Name", 200, "functionName");
        txtHTemplateName.getDmGrid().addTextColumn(txtHTemplateName.getDmGrid().getPropertyAccess().messageTemplate(), "Message Template", 100, "message");
        txtHTemplateName.getDmGrid().initializeGrid(txtHTemplateName.getDmGrid().getPropertyAccess().code(), 600, 0, 1);
        
        txtHTemplateName.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public DraftPopup_Dsg() {
        iniTempName();
        ini();
        
    }
    
    private void ini() {
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("900px");
        popupPanel.setHeight("400px");
        vrtInnerForm.add(iniToolContorls());
        
        FlowPanel flw;
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        DmClient.setStyle(flw, "100%", "350px", "left", null, DmPosition.NONE, true);
        
        flw.add(leftArea());
        flw.add(rightArea());
        
        popupPanel.add(vrtInnerForm);
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
    }
    
    private FlowPanel leftArea() {
//        FlowPanel flw, flwTr, flwTd;
//        DmLabel lbl;
//
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
//        flwTr.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Template Name");
//        flwTd.add(vl.add(lbl));
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtHTemplateName));
//        flwTr.add(flwTd);
//
//        flw.add(flwTr);
//
//        vrtInnerForm.add(flw);
//        vrtInnerForm.setCellHeight(flw, "100%");

        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "49%", "350px", "left", null, DmPosition.NONE, true);
        flwTable.getElement().getStyle().setPaddingRight(1, Style.Unit.PCT);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, true);
        flwTd.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        lbl = new DmLabel("Template Name");
        flwTd.add(vl.add(lbl));
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, true);
        flwTd.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTd.add(vl.add(txtHTemplateName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "475px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "215px", "left", null, DmPosition.NONE, true);
        flwTd.add(iniFieldGrid());
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Field GRid">
    interface FieldGrdDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdFieldName_Mdl> {
        
        @Editor.Path("FieldName")
        ValueProvider<GrdFieldName_Mdl, String> fieldName();
        
        @Editor.Path("FormattedFieldName")
        ValueProvider<GrdFieldName_Mdl, String> formateField();
        
    }
    
    final DmGrid<GrdFieldName_Mdl, FieldGrdDetail_PrpAcs> grdFieldDetail = new DmGrid(GWT.create(FieldGrdDetail_PrpAcs.class));
    
    private Grid iniFieldGrid() {
        grdFieldDetail.addRowNumberColumn();
        grdFieldDetail.addTextColumn(grdFieldDetail.getPropertyAccess().fieldName(), "Field Name", 100, null);
        grdFieldDetail.addTextColumn(grdFieldDetail.getPropertyAccess().formateField(), "Format", 200, null);
        
        grdFieldDetail.initializeGrid(grdFieldDetail.getPropertyAccess().fieldHashCode(), 445, 350, 2);
        
        return grdFieldDetail.getGrid();
    }
    //</editor-fold>

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
        flwOk.add(vl.add(btnOk));
        
        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");
        flwClose.add(btnClose);
        
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();
        
        return toolContainer;
    }
    
    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "350px", "left", null, DmPosition.NONE, true);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");
        flwTr.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", "20px", null, null, DmPosition.CENTER, false);
        lbl = new DmLabel("Message");
        lbl.getElement().getStyle().setHeight(13, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMsg));
        txtMsg.getElement().getStyle().setWidth(448, Style.Unit.PX);
        txtMsg.getElement().getStyle().setHeight(345, Style.Unit.PX);
        
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        return flwTable;
    }
    
}
