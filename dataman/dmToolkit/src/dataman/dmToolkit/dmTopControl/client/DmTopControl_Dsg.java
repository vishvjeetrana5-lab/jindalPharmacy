package dataman.dmToolkit.dmTopControl.client;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class DmTopControl_Dsg {

    final DmToolContainer toolContainer = new DmToolContainer();
    final FlowPanel flwNew = new FlowPanel();
    final Button btnNew = new Button();
    final String btnNewLabel = "New";

    final FlowPanel flwEdit = new FlowPanel();
    final Button btnEdit = new Button();
    final String btnEditLabel = "Edit";

    final FlowPanel flwDelete = new FlowPanel();
    final Button btnDelete = new Button();
    final String btnDeleteLabel = "Delete";
    final InlineLabel deleteLabel = new InlineLabel(btnDeleteLabel);
    final Anchor deleteAnchor = new Anchor();

    final FlowPanel flwCard = new FlowPanel();
    final Button btnCard = new Button();
    final String btnCardLabel = "Card";
    final Anchor aBtnCard = new Anchor();

    Anchor aFirst, aPrevious, aNext, aLast;
    final FlowPanel flwFirst = new FlowPanel();
    final Button btnFirst = new Button();
    final String btnFirstLabel = "First";

    final FlowPanel flwPrevious = new FlowPanel();
    final Button btnPrevious = new Button();
    final String btnPreviousLabel = "Previous";

    final FlowPanel flwNext = new FlowPanel();
    final Button btnNext = new Button();
    final String btnNextLabel = "Next";

    final FlowPanel flwLast = new FlowPanel();
    final Button btnLast = new Button();
    final String btnLastLabel = "Last";

    final FlowPanel flwFind = new FlowPanel();
    final Button btnFind = new Button();
    final String btnFindLabel = "Find";

    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final String btnCancelLabel = "Cancel";

    final FlowPanel flwSave = new FlowPanel();
    final Button btnSave = new Button();
    final String btnSaveLabel = "Save";

    final FlowPanel flwPrint = new FlowPanel();
    final Button btnPrint = new Button();
    final String btnPrintLabel = "Print";

    final FlowPanel flwOther = new FlowPanel();
    final Button btnOther = new Button();
    final String btnOtherLabel = "Other";

    final FlowPanel flwRefresh = new FlowPanel();
    final Button btnRefresh = new Button();
    final String btnRefreshLabel = "Refresh";

    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button();
    final String btnCloseLabel = "Close";

    final InlineLabel lblRecords = new InlineLabel();

    public DmTopControl_Dsg() {
//        dcStyle.ensureInjected();
        Ini();
    }

    private void Ini() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel(btnNewLabel);
//        label.setWidth("25px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-plus");
//        aBtn.getElement().getStyle().setColor("green");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnNew.addStyleName("dm-top-control-btnStyle");
        btnNew.setTitle("New: (F2)");
        btnNew.getElement().appendChild(aBtn.getElement());
        btnNew.getElement().appendChild(label.getElement());
        btnNew.setWidth("50px");
        flwNew.add(btnNew);

        label = new InlineLabel(btnEditLabel);
//        label.setWidth("25px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-pencil");
//        aBtn.getElement().getStyle().setColor("brown");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnEdit.addStyleName("dm-top-control-btnStyle");
        btnEdit.setTitle("Edit: (Ctrl + E)");
        btnEdit.getElement().appendChild(aBtn.getElement());
        btnEdit.getElement().appendChild(label.getElement());
        btnEdit.setWidth("50px");
        flwEdit.add(btnEdit);

//        deleteLabel.setWidth("40px");
        deleteAnchor.addStyleName("fa fa-trash-o");
//        deleteAnchor.getElement().getStyle().setColor("red");
        deleteAnchor.getElement().getStyle().setColor("#7d7d7d");
        btnDelete.addStyleName("dm-top-control-btnStyle");
        btnDelete.setTitle("Delete: (Ctrl + D)");
        btnDelete.getElement().appendChild(deleteAnchor.getElement());
        btnDelete.getElement().appendChild(deleteLabel.getElement());
        btnDelete.setWidth("65px");
        flwDelete.add(btnDelete);

        label = new InlineLabel(btnCardLabel);
//        label.setWidth("30px");
        aBtnCard.addStyleName("fa fa-toggle-on");
//        aBtnCard.getElement().getStyle().setColor("black");
        aBtnCard.getElement().getStyle().setColor("#7d7d7d");
        btnCard.addStyleName("dm-top-control-btnStyle");
        btnCard.setTitle("Card: (Ctrl + T)");
        btnCard.getElement().appendChild(aBtnCard.getElement());
        btnCard.getElement().appendChild(label.getElement());
        btnCard.setWidth("58px");
        flwCard.add(btnCard);

        label = new InlineLabel(btnFirstLabel);
//        label.setWidth("30px");
        aFirst = new Anchor();
        aFirst.addStyleName("fa fa-angle-double-left");
//        aFirst.getElement().getStyle().setColor("black");
        aFirst.getElement().getStyle().setColor("#7d7d7d");
        btnFirst.addStyleName("dm-top-control-btnStyle");
        btnFirst.setTitle("First: (Ctrl + UP Arrow)");
        btnFirst.getElement().appendChild(aFirst.getElement());
        btnFirst.getElement().appendChild(label.getElement());
        btnFirst.setWidth("55px");
        flwFirst.add(btnFirst);

        label = new InlineLabel(btnPreviousLabel);
//        label.setWidth("48px");
        aPrevious = new Anchor();
        aPrevious.addStyleName("fa fa-angle-left");
//        aPrevious.getElement().getStyle().setColor("black");
        aPrevious.getElement().getStyle().setColor("#7d7d7d");
        btnPrevious.addStyleName("dm-top-control-btnStyle");
        btnPrevious.setTitle("Previous: (Ctrl + Left Arrow)");
        btnPrevious.getElement().appendChild(aPrevious.getElement());
        btnPrevious.getElement().appendChild(label.getElement());
        btnPrevious.setWidth("70px");
        flwPrevious.add(btnPrevious);

        label = new InlineLabel(btnNextLabel);
//        label.setWidth("30px");
        aNext = new Anchor();
        aNext.addStyleName("fa fa-angle-right");
//        aNext.getElement().getStyle().setColor("black");
        aNext.getElement().getStyle().setColor("#7d7d7d");
        btnNext.addStyleName("dm-top-control-btnStyle");
        btnNext.setTitle("Next: (Ctrl + Right Arrow)");
        btnNext.getElement().appendChild(aNext.getElement());
        btnNext.getElement().appendChild(label.getElement());
        btnNext.setWidth("55px");
        flwNext.add(btnNext);

        label = new InlineLabel(btnLastLabel);
//        label.setWidth("30px");
        aLast = new Anchor();
        aLast.addStyleName("fa fa-angle-double-right");
//        aLast.getElement().getStyle().setColor("black");
        aLast.getElement().getStyle().setColor("#7d7d7d");
        btnLast.addStyleName("dm-top-control-btnStyle");
        btnLast.setTitle("Last: (Ctrl + Down Arrow)");
        btnLast.getElement().appendChild(aLast.getElement());
        btnLast.getElement().appendChild(label.getElement());
        btnLast.setWidth("55px");
        flwLast.add(btnLast);

        label = new InlineLabel(btnFindLabel);
//        label.setWidth("30px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-search");
//        aBtn.getElement().getStyle().setColor("#9270FA");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnFind.addStyleName("dm-top-control-btnStyle");
        btnFind.setTitle("Find: (Ctrl + F)");
        btnFind.getElement().appendChild(aBtn.getElement());
        btnFind.getElement().appendChild(label.getElement());
        btnFind.setWidth("55px");
        flwFind.add(btnFind);

        label = new InlineLabel(btnPrintLabel);
//        label.setWidth("30px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-print");
//        aBtn.getElement().getStyle().setColor("tomato");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnPrint.addStyleName("dm-top-control-btnStyle");
        btnPrint.setTitle("Print: (Ctrl + P)");
        btnPrint.getElement().appendChild(aBtn.getElement());
        btnPrint.getElement().appendChild(label.getElement());
        btnPrint.setWidth("55px");
        flwPrint.add(btnPrint);

        label = new InlineLabel(btnSaveLabel);
//        label.setWidth("30px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o");
//        aBtn.getElement().getStyle().setColor("black");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSave.addStyleName("dm-top-control-btnStyle");
        btnSave.setTitle("Save: (Ctrl + S)");
        btnSave.getElement().appendChild(aBtn.getElement());
        btnSave.getElement().appendChild(label.getElement());
        btnSave.setWidth("55px");
        flwSave.add(btnSave);

        label = new InlineLabel(btnCancelLabel);
//        label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-undo");
//        aBtn.getElement().getStyle().setColor("skyblue");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.setTitle("Cancel: (Escape)");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("63px");
        flwCancel.add(btnCancel);

        label = new InlineLabel(btnRefreshLabel);
//        label.setWidth("45px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh");
//        aBtn.getElement().getStyle().setColor("orange");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.setTitle("Refresh: (Ctrl + R)");
        btnRefresh.getElement().appendChild(aBtn.getElement());
        btnRefresh.getElement().appendChild(label.getElement());
        btnRefresh.setWidth("70px");
        flwRefresh.add(btnRefresh);

        label = new InlineLabel(btnOtherLabel);
//        label.setWidth("35px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-share-alt");
//        aBtn.getElement().getStyle().setColor("rgb(76, 158, 217)");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOther.addStyleName("dm-top-control-btnStyle");
        btnOther.setTitle("Other: (Ctrl + O)");
        btnOther.getElement().appendChild(aBtn.getElement());
        btnOther.getElement().appendChild(label.getElement());
        btnOther.setWidth("60px");
        flwOther.add(btnOther);

        label = new InlineLabel(btnCloseLabel);
//        label.setWidth("35px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
//        aBtn.getElement().getStyle().setColor("red");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.setTitle("Close: (Escape)");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("60px");
        flwClose.add(btnClose);

        lblRecords.getElement().setAttribute("style", lblRecords.getElement().getAttribute("style")
                + ";max-width:85px; color: red; float: right; padding-right: 9px; padding-top: 8px;"
                + ";font-family: arial;font-weight: bold;font-size: 12px;"
        );
        lblRecords.setStyleName("ellipsis");

    }

}
