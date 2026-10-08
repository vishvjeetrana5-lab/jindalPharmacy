package dataman.dmToolkit.dmSnapshot.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmWebCamera.self.client.DmWebCamera;

public class DmShowCamera {

    public void showCamera(final DmSnapshot dmSnapshot) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess() {
                DmWebCamera webCamera = new DmWebCamera();
                webCamera.setSize("600", "450");
                webCamera.addCallBack(new DmWebCamera.CallBack() {
                    @Override
                    public void getImage(String imgData) {
                        if (!DmShared.xNull(imgData).trim().equalsIgnoreCase("")) {
                            dmSnapshot.getSaveCaptureImage(imgData);
                        }
                    }
                });
                webCamera.show();
            }
        });
    }
}
