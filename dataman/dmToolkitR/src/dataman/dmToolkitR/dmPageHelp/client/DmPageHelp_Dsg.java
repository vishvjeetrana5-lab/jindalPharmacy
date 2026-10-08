package dataman.dmToolkitR.dmPageHelp.client;

import com.google.gwt.user.client.ui.IsWidget;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmToolkitR.dmGridPaging.client.DmGridPaging;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;
import org.gwtbootstrap3.client.ui.html.Span;

abstract class DmPageHelp_Dsg<T extends KeyField> implements IsWidget {

    FlowPanel helpModal = new FlowPanel();
    private ModalBody helpModalBody = new ModalBody();
    FlowPanel bodyContainer = new FlowPanel();
    Span captionSpan = new Span();
    Icon backIcn = new Icon();
    DmGridPaging<T> grid;

    public DmPageHelp_Dsg(String dbField, int rowsPerPage) {
        grid = new DmGridPaging<T>(dbField, rowsPerPage);
        ini();
    }

    private void ini() {
        FlowPanel flwBody = new FlowPanel();
        helpModal.addStyleName("dmPageHelp modal");
//        helpModal.addStyleName("dmPageHelp modal");

        helpModalBody.add(iniTitle());

        flwBody.add(grid);
        flwBody.addStyleName("dmPageHelp-Body");

        helpModalBody.add(flwBody);

        helpModal.add(helpModalBody);
    }

    private FlowPanel iniTitle() {
        FlowPanel flowPanel = new FlowPanel();
        flowPanel.setStyleName("caption");

        backIcn.setType(IconType.ARROW_LEFT);
        flowPanel.add(backIcn);
        flowPanel.add(captionSpan);

        return flowPanel;
    }
}
