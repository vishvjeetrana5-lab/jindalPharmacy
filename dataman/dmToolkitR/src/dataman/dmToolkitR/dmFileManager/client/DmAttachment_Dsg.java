package dataman.dmToolkitR.dmFileManager.client;

import com.google.gwt.dom.client.Style;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.HorizontalPanel;
import dataman.dmToolkitR.dmLabel.client.DmLabel;
import dataman.dmToolkitR.dmStatic.client.DmClient;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.DropDown;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.Popover;
import org.gwtbootstrap3.client.ui.constants.Placement;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;
import org.gwtbootstrap3.extras.bootbox.client.Bootbox;

abstract class DmAttachment_Dsg {
    
    final FlowPanel fileDetailFlw = new FlowPanel();
    final DmLabel lblFileDateTime = new DmLabel("");
    final FlowPanel fileActionFlw = new FlowPanel();
    final Anchor fileActionAnchor = new Anchor("");
    final FlowPanel flwImage = new FlowPanel();
    final FlowPanel flwImageDisplay = new FlowPanel();
    final Image imgView = new Image();
    final DmLabel lblRemark = new DmLabel("");
    final Bootbox bootbox = new Bootbox();
    final DropDown dropDown = new DropDown();
    final DropDownMenu dropDownMenu = new DropDownMenu();
    final Anchor aDescription = new Anchor();
    final Anchor aFileSize = new Anchor();
    final Anchor aDateTime = new Anchor();
    final Anchor aUserName = new Anchor();
    final Anchor aRemark = new Anchor();
    final Anchor aDownload = new Anchor();
    final Anchor aDelete = new Anchor();
    final FlowPanel flwDelete = new FlowPanel();    
    final FlowPanel flwDownload = new FlowPanel();    
    final FlowPanel flwRemark = new FlowPanel();    
    final Popover popover = new Popover();
    
    public DmAttachment_Dsg() {
        fileDetailFlw.getElement().setAttribute("style", "float:left;width:140px; height:180px:float:left;margin-left:5px; margin-top:5px;"
                + " background-color:#fff; box-shadow: 0px 0px 4px #777;");
        fileDetailFlw.add(workingArea());
    }
    
    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;
        FlowPanel hrtPnl;
        
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "140px", "180px", "left", null, DmPosition.NONE, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "35px", null, null, DmPosition.NONE, true);
        hrtPnl = new FlowPanel();
        DmClient.setStyle(hrtPnl, "75%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl.getElement().setAttribute("Style", "background-color: rgba(231,231,231,.5);padding: 6px;");
        hrtPnl.add(lblFileDateTime);
        lblFileDateTime.setHeight("18px");
        lblFileDateTime.getElement().getStyle().setWidth(75, Style.Unit.PCT);
        lblFileDateTime.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        lblFileDateTime.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        lblFileDateTime.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblFileDateTime.getElement().getStyle().setFontSize(11, Style.Unit.PX);
        hrtPnl.add(lblFileDateTime);
        DmClient.setStyle(fileActionFlw, "4%", "20px", null, null, DmPosition.NONE, true);
        fileActionFlw.getElement().setAttribute("Style", "display:inline-block;margin-left: 11px;");
        fileActionAnchor.addStyleName("fa fa-ellipsis-v");
        fileActionAnchor.getElement().setAttribute("style", "font-weight: bold;padding-right: 10px; cursor: pointer;text-decoration: none;"
                + "margin-top: 4px; color:#444; ");
        
        fileActionAnchor.setDataToggle(Toggle.DROPDOWN);
        dropDown.add(fileActionAnchor);
        dropDownMenu.addStyleName("dm-dropdown-style");
        dropDownMenu.add(aDescription);
        dropDownMenu.add(aFileSize);
        dropDownMenu.add(aDateTime);
        dropDownMenu.add(aUserName);
        aRemark.addStyleName("dropdowndiv");
        aDelete.addStyleName("dropdowndiv");
        aDownload.addStyleName("dropdowndiv");
        flwRemark.add(aRemark);
        flwDelete.add(aDelete);
        flwDownload.add(aDownload);
        dropDownMenu.add(flwRemark);
        dropDownMenu.add(flwDownload);
        dropDownMenu.add(flwDelete);
        dropDown.add(dropDownMenu);
        fileActionFlw.add(dropDown);
        hrtPnl.add(fileActionFlw);
        flwTr.add(hrtPnl);
        
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "125px", "left", null, DmPosition.NONE, false);
        DmClient.setStyle(flwImage, "100%", "125px", "left", null, DmPosition.NONE, false);
        imgView.getElement().setAttribute("style", "width:100%; height:100%; cursor : pointer;");
        flwImage.add(imgView);
        flwTr.add(flwImage);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "17px", "left", null, DmPosition.NONE, false);
        flwTr.getElement().setAttribute("style", "text-overflow: ellipsis; overflow: hidden; white-space: initial");
        popover.add(lblRemark);
        popover.setPlacement(Placement.TOP);
        flwTr.add(popover);
        flwTable.add(flwTr);
        
        return flwTable;
    }
    
}
