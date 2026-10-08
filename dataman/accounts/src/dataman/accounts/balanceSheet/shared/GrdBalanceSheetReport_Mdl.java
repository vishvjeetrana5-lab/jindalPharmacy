package dataman.accounts.balanceSheet.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class GrdBalanceSheetReport_Mdl implements Serializable, IsSerializable {

    private String liabiltyCode;
    private String liabiltyName;
    private String isLiabiltyChild;
    private String isLiabiltyBold;
    private String liabiltyChildBalance;
    private String liabiltyAmount;

    private String assetCode;
    private String assetName;
    private String isAssetChild;
    private String isAssetBold;
    private String assetChildBalance;
    private String assetAmount;

    public GrdBalanceSheetReport_Mdl() {

    }

    public String getLiabiltyCode() {
        return liabiltyCode;
    }

    public void setLiabiltyCode(String liabiltyCode) {
        this.liabiltyCode = liabiltyCode;
    }

    public String getLiabiltyName() {
        return liabiltyName;
    }

    public void setLiabiltyName(String liabiltyName) {
        this.liabiltyName = liabiltyName;
    }

    public String getIsLiabiltyChild() {
        return isLiabiltyChild;
    }

    public void setIsLiabiltyChild(String isLiabiltyChild) {
        this.isLiabiltyChild = isLiabiltyChild;
    }

    public String getIsLiabiltyBold() {
        return isLiabiltyBold;
    }

    public void setIsLiabiltyBold(String isLiabiltyBold) {
        this.isLiabiltyBold = isLiabiltyBold;
    }

    public String getLiabiltyChildBalance() {
        return liabiltyChildBalance;
    }

    public void setLiabiltyChildBalance(String liabiltyChildBalance) {
        this.liabiltyChildBalance = liabiltyChildBalance;
    }

    public String getLiabiltyAmount() {
        return liabiltyAmount;
    }

    public void setLiabiltyAmount(String liabiltyAmount) {
        this.liabiltyAmount = liabiltyAmount;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getIsAssetChild() {
        return isAssetChild;
    }

    public void setIsAssetChild(String isAssetChild) {
        this.isAssetChild = isAssetChild;
    }

    public String getIsAssetBold() {
        return isAssetBold;
    }

    public void setIsAssetBold(String isAssetBold) {
        this.isAssetBold = isAssetBold;
    }

    public String getAssetChildBalance() {
        return assetChildBalance;
    }

    public void setAssetChildBalance(String assetChildBalance) {
        this.assetChildBalance = assetChildBalance;
    }

    public String getAssetAmount() {
        return assetAmount;
    }

    public void setAssetAmount(String assetAmount) {
        this.assetAmount = assetAmount;
    }

}
