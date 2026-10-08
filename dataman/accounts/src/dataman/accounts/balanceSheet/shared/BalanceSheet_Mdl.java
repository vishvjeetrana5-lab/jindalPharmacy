package dataman.accounts.balanceSheet.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class BalanceSheet_Mdl extends KeyField implements Serializable, IsSerializable {

    private String liabilityCode, liabilityName, liabilityAmount;
    private String liabilityChildGroupCode, liabilityChildGroupName;
    private String liabilityChildSubCode, liabilityChildSubName;
    private String liabilityChildBalance;
    private String assetCode, assetName, assetAmount;
    private String assetChildGroupCode, assetChildGroupName;
    private String assetChildSubCode, assetChildSubName;
    private String assetChildBalance;

    public BalanceSheet_Mdl() {
    }

    public String getLiabilityCode() {
        return liabilityCode;
    }

    public void setLiabilityCode(String liabilityCode) {
        this.liabilityCode = liabilityCode;
    }

    public String getLiabilityName() {
        return liabilityName;
    }

    public void setLiabilityName(String liabilityName) {
        this.liabilityName = liabilityName;
    }

    public String getLiabilityAmount() {
        return liabilityAmount;
    }

    public void setLiabilityAmount(String liabilityAmount) {
        this.liabilityAmount = liabilityAmount;
    }

    public String getLiabilityChildGroupCode() {
        return liabilityChildGroupCode;
    }

    public void setLiabilityChildGroupCode(String liabilityChildGroupCode) {
        this.liabilityChildGroupCode = liabilityChildGroupCode;
    }

    public String getLiabilityChildGroupName() {
        return liabilityChildGroupName;
    }

    public void setLiabilityChildGroupName(String liabilityChildGroupName) {
        this.liabilityChildGroupName = liabilityChildGroupName;
    }

    public String getLiabilityChildSubCode() {
        return liabilityChildSubCode;
    }

    public void setLiabilityChildSubCode(String liabilityChildSubCode) {
        this.liabilityChildSubCode = liabilityChildSubCode;
    }

    public String getLiabilityChildSubName() {
        return liabilityChildSubName;
    }

    public void setLiabilityChildSubName(String liabilityChildSubName) {
        this.liabilityChildSubName = liabilityChildSubName;
    }

    public String getLiabilityChildBalance() {
        return liabilityChildBalance;
    }

    public void setLiabilityChildBalance(String liabilityChildBalance) {
        this.liabilityChildBalance = liabilityChildBalance;
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

    public String getAssetAmount() {
        return assetAmount;
    }

    public void setAssetAmount(String assetAmount) {
        this.assetAmount = assetAmount;
    }

    public String getAssetChildGroupCode() {
        return assetChildGroupCode;
    }

    public void setAssetChildGroupCode(String assetChildGroupCode) {
        this.assetChildGroupCode = assetChildGroupCode;
    }

    public String getAssetChildGroupName() {
        return assetChildGroupName;
    }

    public void setAssetChildGroupName(String assetChildGroupName) {
        this.assetChildGroupName = assetChildGroupName;
    }

    public String getAssetChildSubCode() {
        return assetChildSubCode;
    }

    public void setAssetChildSubCode(String assetChildSubCode) {
        this.assetChildSubCode = assetChildSubCode;
    }

    public String getAssetChildSubName() {
        return assetChildSubName;
    }

    public void setAssetChildSubName(String assetChildSubName) {
        this.assetChildSubName = assetChildSubName;
    }

    public String getAssetChildBalance() {
        return assetChildBalance;
    }

    public void setAssetChildBalance(String assetChildBalance) {
        this.assetChildBalance = assetChildBalance;
    }

}
