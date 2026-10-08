package dataman.dmToolkit.dmNotifications.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class DmNotificationPopup_Dsg extends IsWidgetExtended {

    public final static String widgetName = "Notification";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmToolContainer toolContainer = new DmToolContainer();
    FlowPanel flwTableDisplaySection = new FlowPanel();
    final DmLabel LblTitle = new DmLabel("");
    final DmLabel LblMessage = new DmLabel("");
    final DmLabel LblUserName = new DmLabel("");
    final DmLabel LblMessageDateTime = new DmLabel("");
    final FlowPanel flwCancel = new FlowPanel();
    final Anchor aBtn = new Anchor("");

    public abstract void onBtnClick(ClickEvent event);

    public DmNotificationPopup_Dsg() {
        ini();
        handlerBtnClick(new Anchor[]{aBtn});
    }

    private void handlerBtnClick(Anchor[] btn) {
        for (int i = 0; i < btn.length; i++) {
            btn[i].addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    onBtnClick(event);
                }
            });
        }
    }

    private void ini() {
        //height set by parameter
        vrtInnerForm.getElement().setAttribute("style", "max-height:90px; min-heght:60px;");
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.NONE, true);
        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");

        flwWorkingArea.getElement().getStyle().setProperty("background", "white");

        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
        flwForm.add(vrtInnerForm);

        flwTableDisplaySection.setVisible(true);

    }

    private VerticalPanel workingArea() {
        FlowPanel flwTr, flwTd;

        final VerticalPanel flwTable = new VerticalPanel();
        flwTable.getElement().setAttribute("style", " border-top:1px solid ; max-height:85px; min-height:53px;");
        DmClient.setStyle(flwTable, "100%", null, null, null, DmPosition.NONE, true);


        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "18px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("style", "width:99%; padding-left:2px;");
        DmClient.setStyle(flwTd, "99%", "18px", "left", null, DmPosition.NONE, true);

        HorizontalPanel hrtPnl = new HorizontalPanel();
        hrtPnl.setWidth("99%");
        hrtPnl.add(LblUserName);
        hrtPnl.setCellHeight(LblUserName, "18px");
        LblUserName.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        LblUserName.getElement().getStyle().setColor("gray");
        LblUserName.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);

        hrtPnl.add(LblMessageDateTime);
        hrtPnl.setCellHeight(LblMessageDateTime, "18px");
        hrtPnl.setCellWidth(LblMessageDateTime, "108px");
        LblMessageDateTime.getElement().setAttribute("style", " font-size:11px; color:gray;");
        flwTd.add(hrtPnl);

        hrtPnl.add(aBtn);
        aBtn.setHeight("16px");
        hrtPnl.setCellWidth(aBtn, "15px");
        hrtPnl.setCellHeight(aBtn, "18px");
        aBtn.addStyleName("fa fa-remove");
        aBtn.getElement().getStyle().setColor("brown");
        aBtn.getElement().getStyle().setMarginLeft(1, Style.Unit.PX);
        aBtn.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        aBtn.getElement().getStyle().setFontSize(16, Style.Unit.PX);
        aBtn.getElement().getStyle().setTextDecoration(Style.TextDecoration.NONE);

        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99%", "20px", "left", null, DmPosition.NONE, true);
        flwTd.add(LblTitle);
        LblTitle.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        LblTitle.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTd.getElement().getStyle().setPaddingLeft(2, Style.Unit.PX);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        flwTr.getElement().setAttribute("Style", "margin-bottom:5px;");
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("Style", "max-height: 40px; overflow: hidden; ");
        DmClient.setStyle(flwTd, "99%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(LblMessage);
        LblMessage.getElement().setAttribute("Style", " word-wrap:break-word; font-size:11px; color:gray; line-height:13px; padding-left:2px;");
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "18px", null, null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", "18px", "left", null, DmPosition.NONE, true);
        flwTd.add(LblUserName);
        LblUserName.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        LblUserName.getElement().getStyle().setColor("gray");
        LblUserName.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "52%", "18px", "left", null, DmPosition.NONE, true);
        flwTd.add(LblMessageDateTime);
        LblMessageDateTime.getElement().getStyle().setFontSize(11, Style.Unit.PX);
        LblMessageDateTime.getElement().getStyle().setColor("gray");
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTable.add(flwTr);
        return flwTable;
    }

}
