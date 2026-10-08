package dataman.dmToolkit.self.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import static dataman.dmToolkit.dmStatic.client.DmClient.createProgressBar;

public class DmRunAsyncIni {
//    private DmRunAsync dmRunAsync;
//    
//    public DmRunAsyncIni(DmRunAsync dmRunAsync) {
//        this.dmRunAsync = dmRunAsync;
//    }

    public void runAsync() {
        final PopupPanel ppCallBackWait = new PopupPanel(false, true);;
        FlowPanel flwTemp = new FlowPanel();

        ppCallBackWait.setGlassStyleName("dm-PopupPanelGlass");
        flwTemp.setWidth("100%");
        ppCallBackWait.getElement().setAttribute("style", "border-radius: 50px 50px 50px 50px;background:none;border:none;rgb(59, 153, 217);padding:0px;");
        ppCallBackWait.add(createProgressBar());
        ppCallBackWait.setGlassEnabled(true);
        ppCallBackWait.show();
        ppCallBackWait.center();

        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                try {
//                    dmRunAsync.onFailure(reason);
                } catch (Exception e) {
                    Window.alert(e.getMessage());
                } finally {
                    ppCallBackWait.hide(true);
                }
            }

            @Override
            public void onSuccess() {
                try {
//                    dmRunAsync.onSuccess();
                } catch (Exception e) {
                    Window.alert(e.getMessage());
                } finally {
                    ppCallBackWait.hide(true);
                }
            }
        });
    }
}
