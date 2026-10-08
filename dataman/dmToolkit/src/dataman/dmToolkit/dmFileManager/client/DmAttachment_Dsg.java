package dataman.dmToolkit.dmFileManager.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Anchor;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;

abstract class DmAttachment_Dsg {

    final FlowPanel fileDetailFlw = new FlowPanel();

    final Menu menu = new Menu();
    MenuItem mnuDescription, mnuFileSize, mnuDateTime, mnuUserName, mnuRemark, mnuDownload, mnuDelete;
    SelectionHandler<MenuItem> selectionHandler;

    final DmLabel lblFileDateTime = new DmLabel("");
    final FlowPanel fileActionFlw = new FlowPanel();
    final Anchor fileActionAnchor = new Anchor("");
    final FlowPanel flwImage = new FlowPanel();
    final FlowPanel flwImageDisplay = new FlowPanel();
    final Image imgView = new Image();
    final DmLabel lblRemark = new DmLabel("");

    abstract void menuClick(SelectionEvent<MenuItem> event);

    public DmAttachment_Dsg() {
        createMenu();
        fileDetailFlw.getElement().setAttribute("style", "float:left;width:140px; height:160px:float:left;margin-left:5px; margin-top:5px;"
                + " background-color:#fff; box-shadow: 0px 0px 4px #777;");
        fileDetailFlw.add(workingArea());
    }

    private Menu createMenu() {
        selectionHandler = new SelectionHandler<MenuItem>() {

            @Override
            public void onSelection(SelectionEvent<MenuItem> event) {
                menuClick(event);
            }
        };

        mnuDescription = new MenuItem("File Name : ", selectionHandler);
        menu.add(mnuDescription);

        mnuFileSize = new MenuItem("File Size (kb) : ", selectionHandler);
        menu.add(mnuFileSize);

        mnuDateTime = new MenuItem("Date & Time : ", selectionHandler);
        menu.add(mnuDateTime);

        mnuUserName = new MenuItem("User : ", selectionHandler);
        menu.add(mnuUserName);

        mnuRemark = new MenuItem("Remark", selectionHandler);
        mnuRemark.getElement().getStyle().setColor("green");
        menu.add(mnuRemark);

        mnuDownload = new MenuItem("Download", selectionHandler);
        mnuDownload.getElement().getStyle().setColor("green");
        mnuDownload.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        menu.add(mnuDownload);

        mnuDelete = new MenuItem("Delete", selectionHandler);
        mnuDelete.getElement().getStyle().setColor("red");
        menu.add(mnuDelete);

        return menu;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "140px", "160px", "left", null, DmPosition.NONE, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", null, null, DmPosition.NONE, true);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl.add(lblFileDateTime);
        lblFileDateTime.setHeight("18px");
        lblFileDateTime.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        lblFileDateTime.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        lblFileDateTime.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblFileDateTime.getElement().getStyle().setFontSize(11, Style.Unit.PX);
        hrtPnl.setCellWidth(lblFileDateTime, "100%");
        DmClient.setStyle(fileActionFlw, "100%", "20px", null, null, DmPosition.NONE, true);
        fileActionAnchor.addStyleName("fa fa-ellipsis-v");
        fileActionAnchor.getElement().setAttribute("style", "font-weight: bold;padding-right: 10px; cursor: pointer;text-decoration: none;"
                + "margin-top: 4px; color:#444;");
        fileActionFlw.add(fileActionAnchor);
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
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTr.add(lblRemark);
        flwTable.add(flwTr);

        return flwTable;
    }
}
