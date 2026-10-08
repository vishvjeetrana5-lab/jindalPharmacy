package dataman.dmToolkitR.dmNotify.client;

import org.gwtbootstrap3.client.ui.gwt.FlowPanel;
import org.gwtbootstrap3.client.ui.html.Span;

abstract public class DmNotify_Dsg {

    final FlowPanel flwNotify = new FlowPanel();
    Span span = new Span("");

    public DmNotify_Dsg() {
        flwNotify.addStyleName("dm-notify");
        flwNotify.add(span);
    }

}
