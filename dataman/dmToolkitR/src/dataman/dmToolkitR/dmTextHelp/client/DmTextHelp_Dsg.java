package dataman.dmToolkitR.dmTextHelp.client;

import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmToolkitR.dmTextBox.client.DmTextBox;
import org.gwtbootstrap3.client.ui.html.Span;

class DmTextHelp_Dsg {

    final FlowPanel flowPanel = new FlowPanel();
    final DmTextBox textBox = new DmTextBox();
    final Span span = new Span();

    public DmTextHelp_Dsg() {
        flowPanel.setStyleName("dmTextHelp");

        flowPanel.add(textBox);
        span.setHTML("<i class=\"caret\"></i>");
        flowPanel.add(span);
    }
}
