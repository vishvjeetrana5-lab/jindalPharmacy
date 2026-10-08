package dataman.dmBase.self.shared;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;

public interface SetWidgetInvokerExtended extends SetWidgetInvoker {

    public void reInitialize(DmEnum.PCSEnum reInitialize);

    public void next(DmEnum.PCSEnum next, PCSData pcsTemp);

    public void showCurrentList(PopupPanel containerPPanel, FlowPanel currentListFPanel);
}
 