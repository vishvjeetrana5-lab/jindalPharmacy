package dataman.dmToolkit.accountAdjustment.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdHistory_Mdl extends KeyField implements Serializable, IsSerializable {

    private String main_DocId;
    private String main_VNo;
    private String main_VType;
    private String main_VDate;
    private String main_LgName;
    private String main_MnuText;
    private String main_MnuAttachedInModule;

    private String adj_DocId;
    private String adj_VNo;
    private String adj_VType;
    private String adj_VDate;
    private String adj_MnuText;
    private String adj_MnuAttachedInModule;

    private String narration;
    private String amount;
    private String adjustment;

    public GrdHistory_Mdl() {
    }

    public String getMain_DocId() {
        return main_DocId;
    }

    public void setMain_DocId(String main_DocId) {
        this.main_DocId = main_DocId;
    }

    public String getMain_VNo() {
        return main_VNo;
    }

    public void setMain_VNo(String main_VNo) {
        this.main_VNo = main_VNo;
    }

    public String getMain_VType() {
        return main_VType;
    }

    public void setMain_VType(String main_VType) {
        this.main_VType = main_VType;
    }

    public String getMain_VDate() {
        return main_VDate;
    }

    public void setMain_VDate(String main_VDate) {
        this.main_VDate = main_VDate;
    }

    public String getMain_LgName() {
        return main_LgName;
    }

    public void setMain_LgName(String main_LgName) {
        this.main_LgName = main_LgName;
    }

    public String getMain_MnuText() {
        return main_MnuText;
    }

    public void setMain_MnuText(String main_MnuText) {
        this.main_MnuText = main_MnuText;
    }

    public String getMain_MnuAttachedInModule() {
        return main_MnuAttachedInModule;
    }

    public void setMain_MnuAttachedInModule(String main_MnuAttachedInModule) {
        this.main_MnuAttachedInModule = main_MnuAttachedInModule;
    }

    public String getAdj_DocId() {
        return adj_DocId;
    }

    public void setAdj_DocId(String adj_DocId) {
        this.adj_DocId = adj_DocId;
    }

    public String getAdj_VNo() {
        return adj_VNo;
    }

    public void setAdj_VNo(String adj_VNo) {
        this.adj_VNo = adj_VNo;
    }

    public String getAdj_VType() {
        return adj_VType;
    }

    public void setAdj_VType(String adj_VType) {
        this.adj_VType = adj_VType;
    }

    public String getAdj_VDate() {
        return adj_VDate;
    }

    public void setAdj_VDate(String adj_VDate) {
        this.adj_VDate = adj_VDate;
    }

    public String getAdj_MnuText() {
        return adj_MnuText;
    }

    public void setAdj_MnuText(String adj_MnuText) {
        this.adj_MnuText = adj_MnuText;
    }

    public String getAdj_MnuAttachedInModule() {
        return adj_MnuAttachedInModule;
    }

    public void setAdj_MnuAttachedInModule(String adj_MnuAttachedInModule) {
        this.adj_MnuAttachedInModule = adj_MnuAttachedInModule;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(String adjustment) {
        this.adjustment = adjustment;
    }

}
