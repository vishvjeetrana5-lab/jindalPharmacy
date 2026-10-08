package dataman.payroll.leaveEntryApproval.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;



public abstract class LeaveEntryApprovalGridEditCopy_Dsg<T1, T2> extends DmGridEditPanel<T1, T2>  {
    
   final FlowPanel flwForm = new FlowPanel();
   final DmUserControl userControl = new DmUserControl();

    final DmTextBox txtEntryNo = new DmTextBox();
    final DmTextBox txtStatus = new DmTextBox();
    final DmTextBox txtEmployeeName = new DmTextBox();
    final DmTextBox txtApplyDate = new DmTextBox();
    final DmTextBox txtFromDate = new DmTextBox();
    final DmTextBox txtToDate = new DmTextBox();
    final DmTextBox txtLeaveName= new DmTextBox();
    final DmTextBox txtDuration = new DmTextBox();
    final DmTextBox txtReason = new DmTextBox();
    final DmTextBox txtDepartment = new DmTextBox();
    final DmTextBox txtApprovedBy = new DmTextBox();
    final DmTextBox txtApprovedDateTime = new DmTextBox();
    final DmLabel isCancelled = new DmLabel("Discard");
    final Button btnApprovedBy = new Button();

    public final static String approvedByBlog = "Approved By";
    public final static String approvedDateTimeBlog = "Approved Date & Time";
   
     
     public LeaveEntryApprovalGridEditCopy_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
   
    }

     
     private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "168px", null, null, DmEnum.DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "170px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "170px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(entryArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
     }
     
      private FlowPanel entryArea() {
         FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "375px", null, null, DmEnum.DmPosition.CENTER, true);

        //==================< Row 1 >=============================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Entry No.");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtEntryNo));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Apply Date");
        lbl.getElement().getStyle().setMarginLeft(6, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtApplyDate));
        flwTR.add(flwTD);
        
        flwTable.add(flwTR);
        

        //==================================< Row 2 >=====================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("EmployeeName");
     //   lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "70", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtEmployeeName));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //==================================< Row 3 >==================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Department");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "70%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtDepartment));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        
        //=============================== < Row 4 >======================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("From Date");
        //lbl.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtFromDate));
        flwTR.add(flwTD);
        
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("To Date");
        lbl.getElement().getStyle().setMarginLeft(6, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtToDate));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        
        //==========================< Row 5 >==========================================
        
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Leave");
        //lbl.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtLeaveName));
        flwTR.add(flwTD);
        
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Duration");
        lbl.getElement().getStyle().setMarginLeft(6, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtDuration));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        //================================< Row 6 >==============================
        
         
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Reason");
        //lbl.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "70", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtReason));
        flwTR.add(flwTD);
        
        flwTable.add(flwTR);
        
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(isCancelled);
        isCancelled.getElement().getStyle().setColor("red");
        isCancelled.getElement().getStyle().setFontSize(14, Style.Unit.PX);
        isCancelled.getElement().getStyle().setMarginLeft(66, Style.Unit.PCT);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTR.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        userControl.addTextBlog(approvedByBlog, 150, false, true);
        userControl.addGap(53);
        userControl.addTextBlog(approvedDateTimeBlog, 150, true, false);
        
        Anchor aBtn = new Anchor();
        aBtn.addStyleName("fa fa-thumbs-o-up");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnApprovedBy.addStyleName("dm-button-grad");
        btnApprovedBy.getElement().appendChild(aBtn.getElement());
        btnApprovedBy.setWidth("30px");
        userControl.addControlBlog(approvedByBlog, btnApprovedBy);
        flwTD.add(vl.add(userControl));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        return flwTable;
    

      }
}
