package dataman.dmToolkit.overHeadLine.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.overHead.client.OverHead;
import dataman.dmToolkit.overHead.shared.OverHeadMA_Mdl;
import dataman.dmToolkit.overHead.shared.OverHead_Mdl;
import dataman.dmToolkit.overHeadLine.shared.*;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import java.util.ArrayList;
import java.util.List;

public class OverHeadLineHandler<T extends OHLExtended> {

    public interface Callback<T extends OHLExtended> {

        public void initialized();

        public String getVoucherType();

        public String getVoucherDate();

        public void endCalculate();

        public void endSyncToOH();

        public List<T> getRows();
    }

    private Callback<T> callback;
    protected ServiceInvoker<OverHeadLine_SrvAsync> srvAsync;
    protected List<OverHeadLine_Mdl> defaultOHLList;
    private OverHeadLine overHeadLineWgt;
    protected OverHead overHead;

    public void calculate(String grossAmount, int rowIndex) {
        calculate(grossAmount, rowIndex, true);
    }

    public void calculate(String grossAmount, int rowIndex, boolean invokeEndCalculate) {
        List<OverHeadLine_Mdl> overHeadList;
        OverHeadLine_Mdl tempOH = null;

        overHeadList = getOverHeadList(rowIndex);

        if (overHeadList != null) {
            for (int i = 0; i < overHeadList.size(); i++) {
                tempOH = overHeadList.get(i);
                OverHeadLine_Shared.calculateSingleOH(overHeadList, tempOH, grossAmount);
            }
        }

        syncronizeLineHeader();

        if (invokeEndCalculate) {
            if (callback != null) {
                callback.endCalculate();
            }
        }
    }

    protected List<OverHeadLine_Mdl> clone(List<OverHeadLine_Mdl> list) {
        List<OverHeadLine_Mdl> overHeadList = null;

        if (list != null) {
            overHeadList = new ArrayList<>();
            for (OverHeadLine_Mdl overHeadLine : list) {
                overHeadList.add(overHeadLine.clone());
            }
        }

        return overHeadList;
    }

    public List<OverHeadLine_Mdl> getOverHeadList(int rowIndex) {
        return getOverHeadList(rowIndex, true);
    }

    public List<OverHeadLine_Mdl> getOverHeadList(int rowIndex, boolean isByReference) {
        List<OverHeadLine_Mdl> overHeadList = null;

        List<T> tList = callback.getRows();
        if (isByReference) {
            overHeadList = tList.get(rowIndex).getOHLList();
        } else {
            overHeadList = clone(tList.get(rowIndex).getOHLList());
        }

        if (overHeadList == null) {
            overHeadList = clone(defaultOHLList);
            tList.get(rowIndex).setOHLList(overHeadList);
        } else if (overHeadList.size() == 0) {
            overHeadList = clone(defaultOHLList);
            tList.get(rowIndex).setOHLList(overHeadList);
        }

        return overHeadList;
    }

    public void setITS(List<OverHeadITS_Mdl> overHeadITSList, int rowIndex) {
        setITS(overHeadITSList, getOverHeadList(rowIndex));
    }

    protected void setITS(List<OverHeadITS_Mdl> overHeadITSList, List<OverHeadLine_Mdl> overHeadLineList) {
        if (overHeadLineList != null) {
            if (overHeadITSList != null) {
                for (OverHeadITS_Mdl overHeadITS : overHeadITSList) {
                    if (overHeadITS != null) {
                        for (OverHeadLine_Mdl overHeadLine : overHeadLineList) {
                            if (overHeadLine != null) {
                                if (DmShared.iNull(overHeadLine.getSharedKeyFieldCode()).longValue() == DmShared.iNull(overHeadITS.getOverHeadCode()).longValue()) {
                                    overHeadLine.setAtRate(DmClient.vNull(overHeadITS.getAtRate(), "#.#######"));
                                    overHeadLine.setAmount(DmClient.vNull(overHeadITS.getAmount(), "#.#######"));
                                    overHeadLine.setAccountName(DmShared.xNull(overHeadITS.getAccountName()).toString());
                                    overHeadLine.setAccountCode(DmShared.xNull(overHeadITS.getAccountCode()).toString());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public OverHeadLine getWidget() {
        return overHeadLineWgt;
    }

    public OverHeadLineHandler(OverHead overHead) {
        startUP(overHead, 350, 150);
    }

    public OverHeadLineHandler(OverHead overHead, int width, int height) {
        startUP(overHead, width, height);
    }

    private void startUP(OverHead overHead, int width, int height) {
        srvAsync = new ServiceInvoker<>(GWT.create(OverHeadLine_Srv.class), OverHeadLineHandler.class.getName());
        this.overHead = overHead;
        overHeadLineWgt = new OverHeadLine(this, width, height);
    }

    public void addCallback(Callback callback) {
        this.callback = callback;
    }

    public void setDefault() {
        defaultOHLList = null;
        List<T> tList = callback.getRows();
        T t;
        List<OverHeadLine_Mdl> overHeadList;
        OverHeadLine_Mdl overHeadLine;

        if (tList != null) {
            if (tList.size() > 0) {
                t = tList.get(0);
                if (t != null) {
                    overHeadList = clone(t.getOHLList());
                    if (overHeadList != null) {
                        for (int i = 0; i < overHeadList.size(); i++) {
                            overHeadLine = overHeadList.get(i);
                            if (overHeadLine != null) {
                                overHeadLine.setAmount("");
                                overHeadLine.setAtRate("");
                            }
                        }
                    }
                    defaultOHLList = overHeadList;
                }
            }
        }
    }

    public void initialize() {
        initialize(false);
    }

    public void initialize(final boolean isRefresh) {
        defaultOHLList = null;

        if ((DmShared.iNull(callback.getVoucherType()).intValue() != 0) && (!DmShared.dNull(callback.getVoucherDate()).equalsIgnoreCase(""))) {
            srvAsync.invokeRPC(true).getOverHead(callback.getVoucherType(), callback.getVoucherDate(), new AsyncCallback<DmResult<List<OverHeadLine_Mdl>>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
                }

                @Override
                public void onSuccess(DmResult<List<OverHeadLine_Mdl>> result) {
                    if (result.isStatus()) {
                        defaultOHLList = result.getData();
                        if (isRefresh) {
                            overHeadLineWgt.refresh();
                        } else {
                            callback.initialized();
                        }
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (Button) null).show();
                    }
                }
            });
        }
    }

    public void syncronizeLineHeader() {
        if (overHead == null) {
            return;
        }
        List<SyncOverHead_Mdl> syncOverHeadList = new ArrayList<>();
        List<OverHeadLine_Mdl> overHeadLineList;
        OverHeadLine_Mdl overHeadLine;
        OverHead_Mdl overHeadMdl;
        OverHeadMA_Mdl overHeadMA;
        List<OverHeadMA_Mdl> overHeadMAList;
        List<T> tList;
        T t;
        double ohGAmountTotal = 0, ohGAtRateTotal = 0;
        boolean isExist = false;

        tList = callback.getRows();
        if (tList != null) {
            for (int rowIndex = 0; rowIndex < tList.size(); rowIndex++) {
                t = tList.get(rowIndex);
                if (t != null) {
                    overHeadLineList = t.getOHLList();
                    if (overHeadLineList == null) {
                        overHeadLineList = getOverHeadList(rowIndex);
                    }
                    if (overHeadLineList != null) {
                        overHeadLine = OverHeadLine_Shared.getOHByManualCode("G", overHeadLineList);
                        if (overHeadLine != null) {
                            ohGAtRateTotal += DmShared.vNull(overHeadLine.getAtRate()).doubleValue();
                            ohGAmountTotal += DmShared.vNull(overHeadLine.getAmount()).doubleValue();
                        }

                        for (int i = 0; i < overHeadLineList.size(); i++) {
                            overHeadLine = overHeadLineList.get(i);
                            if (overHeadLine != null) {
                                if (!DmShared.xNull(overHeadLine.getOhManualCode()).trim().equalsIgnoreCase("")) {

                                    isExist = false;
                                    for (SyncOverHead_Mdl syncOverHead : syncOverHeadList) {
                                        if (syncOverHead.getOhManualCode().equalsIgnoreCase(overHeadLine.getOhManualCode())) {
                                            isExist = true;
                                            injectSync(syncOverHead, overHeadLine);
                                        }
                                    }

                                    if (!isExist) {
                                        SyncOverHead_Mdl syncOverHead = new SyncOverHead_Mdl();
                                        syncOverHeadList.add(syncOverHead);
                                        injectSync(syncOverHead, overHeadLine);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (SyncOverHead_Mdl syncOverHead : syncOverHeadList) {
            overHeadMdl = overHead.getOHByManualCode(syncOverHead.getOhManualCode());
            if (overHeadMdl != null) {
                overHeadMdl.setAmount(syncOverHead.getAmount());
                overHeadMAList = new ArrayList<>();
                isExist = false;

                for (SyncOverHeadMA_Mdl syncOverHeadMA : syncOverHead.getOverHeadMAList()) {
                    overHeadMA = new OverHeadMA_Mdl();
                    overHeadMA.setSharedKeyFieldCode(syncOverHeadMA.getSharedKeyFieldCode());
                    overHeadMA.setSharedKeyFieldName(syncOverHeadMA.getSharedKeyFieldName());
                    overHeadMA.setAmount(syncOverHeadMA.getAmount());
                    overHeadMAList.add(overHeadMA);
                    if (DmShared.iNull(overHeadMA.getSharedKeyFieldCode()).longValue() != 0) {
                        isExist = true;
                    }
                }

                if (isExist) {
                    if (overHeadMAList.size() > 0) {
                        overHeadMdl.setOverHeadMA(overHeadMAList);
                    }
                    overHeadMdl.setAcEditable("0");
                }
            }
        }

        overHeadMdl = overHead.getOHByManualCode("G");
        if (overHeadMdl != null) {
            overHeadMdl.setAtRate(DmClient.vNull(ohGAtRateTotal, "#.#######"));
        }
        overHead.commit();
        overHead.calculate(DmClient.vNull(ohGAmountTotal, "0.00"));
    }

    private void injectSync(SyncOverHead_Mdl syncOverHead, OverHeadLine_Mdl overHeadLine) {
        SyncOverHeadMA_Mdl syncOverHeadMA = new SyncOverHeadMA_Mdl();
        List<SyncOverHeadMA_Mdl> syncOverHeadMAList;
        boolean isExist = false;

        syncOverHead.setOhManualCode(overHeadLine.getOhManualCode());
        syncOverHead.setAmount(DmClient.vNull(DmShared.vNull(syncOverHead.getAmount()) + DmShared.vNull(overHeadLine.getAmount()), "0.00"));

        if (syncOverHead.getOverHeadMAList() == null) {
            syncOverHead.setOverHeadMAList(new ArrayList<SyncOverHeadMA_Mdl>());
        }

        syncOverHeadMAList = syncOverHead.getOverHeadMAList();

        isExist = false;
        for (int i = 0; i < syncOverHeadMAList.size(); i++) {
            if (DmShared.iNull(syncOverHeadMAList.get(i).getSharedKeyFieldCode()).longValue() == DmShared.iNull(overHeadLine.getAccountCode()).longValue()) {
                syncOverHeadMA = syncOverHeadMAList.get(i);
                syncOverHeadMA.setAmount(DmClient.vNull(DmShared.vNull(syncOverHeadMA.getAmount()) + DmShared.vNull(overHeadLine.getAmount()), "0.00"));
                isExist = true;
                break;
            }
        }

        if (!isExist) {
            syncOverHeadMA = new SyncOverHeadMA_Mdl();
            syncOverHeadMA.setAmount(DmClient.vNull(DmShared.vNull(overHeadLine.getAmount()), "0.00"));
            syncOverHeadMA.setSharedKeyFieldCode(overHeadLine.getAccountCode());
            syncOverHeadMA.setSharedKeyFieldName(overHeadLine.getAccountName());
            syncOverHead.getOverHeadMAList().add(syncOverHeadMA);
        }
    }

    public void setReference(List<OverHeadLine_Mdl> prevOhl, List<OverHeadLine_Mdl> currOhl) {
        if (prevOhl != null) {
            for (OverHeadLine_Mdl prevOhlMdl : prevOhl) {
                if (currOhl != null) {
                    for (OverHeadLine_Mdl currOhlMdl : currOhl) {
                        if (prevOhlMdl != null && currOhlMdl != null) {
                            if (DmShared.iNull(prevOhlMdl.getSharedKeyFieldCode()).longValue() == DmShared.iNull(currOhlMdl.getSharedKeyFieldCode()).longValue()) {
                                currOhlMdl.setAtRate(prevOhlMdl.getAtRate());
                                currOhlMdl.setAmount(prevOhlMdl.getAmount());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

}
