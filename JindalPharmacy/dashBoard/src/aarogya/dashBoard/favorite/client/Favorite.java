package aarogya.dashBoard.favorite.client;

import aarogya.dashBoard.favorite.shared.Common_Model;
import aarogya.dashBoard.favorite.shared.Favorite_Srv;
import aarogya.dashBoard.favorite.shared.Favorite_SrvAsync;
import aarogya.dashBoard.favorite.shared.ModuleDetail_Mdl;
import aarogya.projectMenu.self.shared.pmAssets;
import aarogya.projectMenu.self.shared.pmBMW;
import aarogya.projectMenu.self.shared.pmCSSD;
import aarogya.projectMenu.self.shared.pmDayCare;
import aarogya.projectMenu.self.shared.pmDiagnostic;
import aarogya.projectMenu.self.shared.pmDietary;
import aarogya.projectMenu.self.shared.pmEmergency;
import aarogya.projectMenu.self.shared.pmHouseKeeping;
import aarogya.projectMenu.self.shared.pmIPD;
import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.projectMenu.self.shared.pmMIS;
import aarogya.projectMenu.self.shared.pmMRD;
import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.projectMenu.self.shared.pmNABH;
import aarogya.projectMenu.self.shared.pmNursingStation;
import aarogya.projectMenu.self.shared.pmOPD;
import aarogya.projectMenu.self.shared.pmOT;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.projectMenu.self.shared.pmTPA;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.libraryMenu.self.shared.lmUtility;
import dataman.libraryMenu.self.shared.lmWebJobs;
import java.util.List;

public class Favorite extends Favorite_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler {

    final Manage managePopup = new Manage();
    ServiceInvoker<Favorite_SrvAsync> srvAsync;

    public Favorite(int height) {
        super(height);
        srvAsync = new ServiceInvoker<Favorite_SrvAsync>(GWT.create(Favorite_Srv.class), Favorite.class.getName());
        manageEvent();
        getFavorite();
    }

    private void manageEvent() {
        settingAnchor.addClickHandler(this);
        managePopup.addCallBack(new Manage.CallBack() {

            @Override
            public void onClose() {
                getFavorite();
            }
        });
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == settingAnchor) {
            showManage();
        } else if (event.getSource() instanceof InlineLabel) {
            InlineLabel mnu = (InlineLabel) event.getSource();
            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(DmShared.xNull(mnu.getTitle()).trim(), DmShared.xNull(mnu.getText()).trim(), DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
        }
    }

    private void showManage() {
        managePopup.show();
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            managePopup.hide();
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    protected void getFavorite() {
        srvAsync.invokeRPC(false).getFillRecords(new AsyncCallback<DmResult<List<ModuleDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<ModuleDetail_Mdl>> result) {
                if (result.isStatus()) {
                    createBlocks(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    protected void createBlocks(List<ModuleDetail_Mdl> roomBlock_Mdl) {
        FlowPanel moduleBlockFlw, moduleFlw;
        InlineLabel menuTextILbl;
        Common_Model.dmImgResourse lstImgRe;
        Image image;
        container.clear();

        for (ModuleDetail_Mdl moduleBlockMdl : roomBlock_Mdl) {
            moduleBlockFlw = new FlowPanel();
            moduleFlw = new FlowPanel();
            menuTextILbl = new InlineLabel();
            moduleFlw.setStyleName("favorite-module");

            lstImgRe = initiateModule(DmShared.xNull(moduleBlockMdl.getModuleName()));
            image = new Image(lstImgRe.getImg());
            image.setStyleName("favorite-module");

            menuTextILbl.setText(DmShared.xNull(moduleBlockMdl.getModuleName()));
            menuTextILbl.setTitle(DmShared.xNull(moduleBlockMdl.getModuleName()));

            moduleFlw.add(image);
            moduleFlw.add(menuTextILbl);

            moduleBlockFlw.add(moduleFlw);

            for (int j = 0; j < moduleBlockMdl.getMenuDetailMdl().size(); j++) {
                moduleFlw = new FlowPanel();
                menuTextILbl = new InlineLabel();
                moduleFlw.setStyleName("favorite-list");

                menuTextILbl.setText(DmShared.xNull(moduleBlockMdl.getMenuDetailMdl().get(j).getMenuName()));
                menuTextILbl.setTitle(DmShared.xNull(moduleBlockMdl.getModuleName()));
                menuTextILbl.addClickHandler(this);
                moduleFlw.add(menuTextILbl);
                moduleBlockFlw.add(moduleFlw);
            }
            container.add(moduleBlockFlw);
        }
    }

    private Common_Model.dmImgResourse initiateModule(String moduleName) {
        if (new pmMaster().moduleName.equals(moduleName)) {
            return new Common_Model.dmImgResourse(new pmMaster().moduleName, Common_Res.rsObj.imgRMaster());
        } else if (new pmMIS().moduleName.equals(moduleName)) {
            return new Common_Model.dmImgResourse(new pmMIS().moduleName, Common_Res.rsObj.imgRMIS());
        } else if (new pmOPD().moduleName.equals(moduleName)) {
            return new Common_Model.dmImgResourse(new pmOPD().moduleName, Common_Res.rsObj.imgROPD());
        } else if (new pmDayCare().moduleName.equals(moduleName)) {
            return new Common_Model.dmImgResourse(new pmDayCare().moduleName, Common_Res.rsObj.imgRDayCare());
        } else if (new pmIPD().moduleName.equals(moduleName)) {
            return new Common_Model.dmImgResourse(new pmIPD().moduleName, Common_Res.rsObj.imgRIPD());
        } else if (new pmEmergency().moduleName.equals(moduleName)) {
            return new Common_Model.dmImgResourse(new pmEmergency().moduleName, Common_Res.rsObj.imgREmergency());
        } else if (new pmDiagnostic().moduleName.equals(moduleName)) {
            return new Common_Model.dmImgResourse(new pmDiagnostic().moduleName, Common_Res.rsObj.imgRDiagnostic());
        } else if (new pmNursingStation().moduleName.equals(moduleName)) {
            return new Common_Model.dmImgResourse(new pmNursingStation().moduleName, Common_Res.rsObj.imgRNursingStation());
        } else if (new pmCSSD().moduleName.equals(moduleName)) {
            return new Common_Model.dmImgResourse(new pmCSSD().moduleName, Common_Res.rsObj.imgRCSSD());
        } else if (new pmBMW().moduleName.equals(moduleName)) {
            return new Common_Model.dmImgResourse(new pmBMW().moduleName, Common_Res.rsObj.imgRBMW());
        } else if (new pmDietary().moduleName.equals(moduleName)) {
            return new Common_Model.dmImgResourse(new pmDietary().moduleName, Common_Res.rsObj.imgRDietary());
        } else if (new pmAssets().moduleName.equals(moduleName)) {
            return new Common_Model.dmImgResourse(new pmAssets().moduleName, Common_Res.rsObj.imgRAsset());
        } else if (new pmPharmacy().moduleName.equals(moduleName)) {
            return new Common_Model.dmImgResourse(new pmPharmacy().moduleName, Common_Res.rsObj.imgRPharmacy());
        } else if (new lmAccounts().moduleName.equals(moduleName)) {
            return new Common_Model.dmImgResourse(new lmAccounts().moduleName, Common_Res.rsObj.imgRAccounts());
        } else if (new pmInventory().moduleName.equals(moduleName)) {
            return new Common_Model.dmImgResourse(new pmInventory().moduleName, Common_Res.rsObj.imgRInventory());
        } else if (new pmTPA().moduleName.equals(moduleName)) {
            return new Common_Model.dmImgResourse(new pmTPA().moduleName, Common_Res.rsObj.imgRTPA());
        } else if (new pmNABH().moduleName.equals(moduleName)) {
            return new Common_Model.dmImgResourse(new pmNABH().moduleName, Common_Res.rsObj.imgRNABH());
        } else if (new pmOT().moduleName.equals(moduleName)) {
            return new Common_Model.dmImgResourse(new pmOT().moduleName, Common_Res.rsObj.imgROT());
        } else if (new pmMRD().moduleName.equals(moduleName)) {
            return new Common_Model.dmImgResourse(new pmMRD().moduleName, Common_Res.rsObj.imgRMRD());
        } else if (new pmHouseKeeping().moduleName.equals(moduleName)) {
            return new Common_Model.dmImgResourse(new pmHouseKeeping().moduleName, Common_Res.rsObj.imgRHousekeeping());
        } else if (new lmWebJobs().moduleName.equals(moduleName)) {
            return new Common_Model.dmImgResourse(new lmWebJobs().moduleName, Common_Res.rsObj.imgRWebJobs());
        } else if (new lmUtility().moduleName.equals(moduleName)) {
            return new Common_Model.dmImgResourse(new lmUtility().moduleName, Common_Res.rsObj.imgRUtility());
        } else {
            return new Common_Model.dmImgResourse(new lmUtility().moduleName, Common_Res.rsObj.imgRUtility());
        }
    }
}
