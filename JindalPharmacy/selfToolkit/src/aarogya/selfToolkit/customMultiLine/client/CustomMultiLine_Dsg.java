package aarogya.selfToolkit.customMultiLine.client;

import aarogya.selfToolkit.customMultiLine.shared.*;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

abstract class CustomMultiLine_Dsg {

    public final static String widgetName = "Prescription Custom Item";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final Button btnFavriot = new Button();
    final DmTextBox txtSearchFavriote = new DmTextBox(50, false);
    final DmTextArea txtRemark = new DmTextArea(5000, false);
    protected int colArrowLeft, colArrowUp, colRemoveFav;

    public CustomMultiLine_Dsg() {
        ini();
        iniGrdFavorite();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("1200px");
        popupPanel.setHeight("400px");

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
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("50px");
        btnOk.setTitle("Ok");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("60px");
        btnCancel.setTitle("Close");
        flwCancel.add(vl.add(btnCancel));

        label = new InlineLabel("Favorite");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-star");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnFavriot.addStyleName("dm-top-control-btnStyle");
        btnFavriot.getElement().appendChild(aBtn.getElement());
        btnFavriot.getElement().appendChild(label.getElement());
        btnFavriot.setWidth("60px");
        btnFavriot.setTitle("Favorite");

        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addWidget(btnFavriot);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "370px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "60%", "370px", "left", null, DmPosition.NONE, false);
        txtRemark.setHeight("363px");
        flwTr.add(vl.add(txtRemark));

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "40%", "370px", "left", null, DmPosition.NONE, false);
        flwTr.add(favoriteTabArea());

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel favoriteTabArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "85%", "365px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", "20px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(txtSearchFavriote));
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "345px", "left", null, DmPosition.NONE, true);
        flwTr.add(grdFavorite);
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Favorite">
    interface GrdFavorite_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdFavorite_Mdl> {

        @Editor.Path("Remark")
        ValueProvider<GrdFavorite_Mdl, String> remark();

        @Editor.Path("Image")
        ValueProvider<GrdFavorite_Mdl, String> image();
    }
    final DmGridPaging<GrdFavorite_Mdl, GrdFavorite_PrpAcs, SearchValue_Mdl> grdFavorite = new DmGridPaging(GWT.create(GrdFavorite_PrpAcs.class), 14);

    private void iniGrdFavorite() {
        int colIndex = 0;
        DmColumnConfig<GrdFavorite_Mdl, String> imageRemove;
        grdFavorite.getDmGrid().addTextColumn(grdFavorite.getDmGrid().getPropertyAccess().remark(), "Favorites", 280, "remark");
        colIndex++;
        grdFavorite.getDmGrid().addTextColumn(grdFavorite.getDmGrid().getPropertyAccess().code(), "Subject", 83, "subject");
        colIndex++;
        colRemoveFav = colIndex;
        imageRemove = grdFavorite.getDmGrid().addTextColumn(grdFavorite.getDmGrid().getPropertyAccess().code(), "", 22, "");
        grdFavorite.getDmGrid().initializeGrid(grdFavorite.getDmGrid().getPropertyAccess().fieldHashCode(), 390, 0);

        imageRemove.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        imageRemove.setCellPadding(false);
        imageRemove.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-times' style='font-size:1.1em; color:red;'></span>");
            }
        });

        grdFavorite.getHelper().addSort("subject", DmSortType.ASC);
        grdFavorite.getHelper().addSort("remark", DmSortType.ASC);
    }
    //</editor-fold>
}
