package dataman.dmToolkitR.dmGridPaging.client;

import com.google.gwt.user.client.ui.IsWidget;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmToolkitR.dmGridHelp.client.DmGridHelp;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.InputType;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

abstract class DmGridPaging_Dsg<T extends KeyField> implements IsWidget {

    FlowPanel flowPanel = new FlowPanel();
    Input txtSearch = new Input(InputType.TEXT);
    Icon firstPageIcn, prvPageIcn, nextPageIcn, lastPageIcn;
    DmGridHelp<T> grid;

    public DmGridPaging_Dsg() {
        grid = new DmGridHelp<T>();
        ini();
    }

    private void ini() {
        FlowPanel flwBody = new FlowPanel();
        flowPanel.addStyleName("dmGridPaging");
        flowPanel.add(iniSearch());

        flwBody.add(grid);
        flwBody.addStyleName("dmGridPaging-Body");

        flowPanel.add(flwBody);
    }

    private FlowPanel iniSearch() {
        FlowPanel flowPanel = new FlowPanel();

        flowPanel.setStyleName("search");

        flowPanel.add(txtSearch);
        flowPanel.add(iniPagination());

        return flowPanel;
    }

    private FlowPanel iniPagination() {
        FlowPanel flwPagination = new FlowPanel();
        flwPagination.setStyleName("paging-container");

        firstPageIcn = new Icon();
        firstPageIcn.addStyleName("paging-icon");
        firstPageIcn.setType(IconType.ANGLE_DOUBLE_LEFT);

        prvPageIcn = new Icon();
        prvPageIcn.addStyleName("paging-icon");
        prvPageIcn.setType(IconType.ANGLE_LEFT);

        nextPageIcn = new Icon();
        nextPageIcn.addStyleName("paging-icon");
        nextPageIcn.setType(IconType.ANGLE_RIGHT);

        lastPageIcn = new Icon();
        lastPageIcn.addStyleName("paging-icon");
        lastPageIcn.setType(IconType.ANGLE_DOUBLE_RIGHT);

        flwPagination.add(firstPageIcn);
        flwPagination.add(prvPageIcn);
        flwPagination.add(nextPageIcn);
        flwPagination.add(lastPageIcn);

        return flwPagination;
    }
}
