package dataman.dmToolkit.dmUserControl.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;

public class DmControlBlog {

    FlowPanel flowPanel = new FlowPanel();
    IsWidget isWidget;

    public DmControlBlog(IsWidget isWidget) {
        this.isWidget = isWidget;
        setVisible(true);
    }

    public void setVisible(boolean visible) {
        flowPanel.clear();
        if (visible) {
            flowPanel.add(isWidget);
        }
    }

    public IsWidget getControlBlog() {
        return isWidget;
    }

    public FlowPanel getControlBlogContainer() {
        return flowPanel;
    }
}
