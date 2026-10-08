package dataman.dmToolkitR.dmGridEditPanel.client;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.InlineLabel;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

public class DmGridEditPanel_Dsg {

//TBC//    final DmPopupPanel modal = new DmPopupPanel();
    final Modal modal = new Modal();
    final ModalBody modalBody = new ModalBody();
    final ModalHeader modalHeader = new ModalHeader();
    public final FlowPanel flwContent = new FlowPanel();
    final InlineLabel deleteLabel = new InlineLabel("Delete");
    final Anchor deleteAnchor = new Anchor();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwNew = new FlowPanel();
    final Button btnNew = new Button();
    final FlowPanel flwDelete = new FlowPanel();
    final Button btnDelete = new Button();
    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button();

    public DmGridEditPanel_Dsg(String title, String width) {
//TBC//        modal.setGlassEnabled(true);
//        modal.setWidth(width);
        modal.setDataBackdrop(ModalBackdrop.STATIC);
        modalHeader.add(getControls());
        modalHeader.setClosable(false);
        modalHeader.setTitle(title);
        modal.add(modalHeader);
        modal.setSize(ModalSize.MEDIUM);
        modalBody.add(getWorkingArea());
        modal.add(modalBody);
    }

    private FlowPanel getWorkingArea() {
        FlowPanel flwTable;

        flwTable = new FlowPanel();
        flwTable.getElement().setAttribute("Style", "width:100%; display:inline-block;");

//        //=======================< 1st Row >=======================//
//        flwTr = new FlowPanel();
//        flwTr.getElement().setAttribute("style", "width:100%; float:left; background:rgb(0, 154, 207); color:#fff;");
//
//        flwTd = new FlowPanel();
//        flwTd.getElement().setAttribute("style", "float:left");
//        lblTemp = new DmLabel(title);
//        lblTemp.getElement().setAttribute("style", "white-space: nowrap; width: 100%; padding: 6px; margin-left: 5px; font-weight: bold; font-size: 15px;");
//        flwTd.add(lblTemp);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        flwTd.getElement().setAttribute("style", "float:right");
//        flwTd.add(getControls());
//        flwTr.add(flwTd);
//
//        flwTable.add(flwTr);
        //=======================< ****** >=======================//
        flwContent.getElement().setAttribute("style", "width:100%; float:left; padding: 15px 0px;");
        flwTable.add(flwContent);

        return flwTable;

    }

    private FlowPanel getControls() {
        FlowPanel flwControls = null;

        flwControls = new FlowPanel();
        flwControls.getElement().setAttribute("style", "float:right;padding: 6px 8px;");

        flwOk.getElement().setAttribute("style", "float:left;");
        flwOk.add(btnOk);

        btnOk.setStyleName("btnVecter fa fa-check");
        flwControls.add(flwOk);

        flwNew.getElement().setAttribute("style", "float:left;");
        flwNew.add(btnNew);
        btnNew.setStyleName("btnVecter fa fa-plus");
        flwControls.add(flwNew);

        flwDelete.getElement().setAttribute("style", "float:left;");
        flwDelete.add(btnDelete);
        btnDelete.setStyleName("btnVecter fa fa-trash");
        flwControls.add(flwDelete);

        flwClose.getElement().setAttribute("style", "float:left;");
        flwClose.add(btnClose);
        btnClose.setStyleName("btnVecter fa fa-close");
        flwControls.add(flwClose);

        return flwControls;
    }

}
