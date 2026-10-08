package dataman.accounts.chequePrintSetup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class ChequePrintSetup_Mdl extends KeyField implements Serializable, IsSerializable {

    private String bankName;
    private String bankCode;
    private String chequeDateTop;
    private String chequeDateLeft;
    private String crossChequeTop;
    private String crossChequeLeft;
    private String favourOfTop;
    private String favourOfLeft;
    private String amtInWordsTop;
    private String amtInWordsLeft;
    private String amtTop;
    private String amtLeft;
    private String authSign;
    private String authSignTop;
    private String authSignLeft;
    private String companyName;
    private String companyNameTop;
    private String companyNameLeft;
    private String paperWidth;
    private String paperHeight;

    public ChequePrintSetup_Mdl() {
    }

    public String getCrossChequeTop() {
        return crossChequeTop;
    }

    public void setCrossChequeTop(String crossChequeTop) {
        this.crossChequeTop = crossChequeTop;
    }

    public String getCrossChequeLeft() {
        return crossChequeLeft;
    }

    public void setCrossChequeLeft(String crossChequeLeft) {
        this.crossChequeLeft = crossChequeLeft;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getChequeDateTop() {
        return chequeDateTop;
    }

    public void setChequeDateTop(String chequeDateTop) {
        this.chequeDateTop = chequeDateTop;
    }

    public String getChequeDateLeft() {
        return chequeDateLeft;
    }

    public void setChequeDateLeft(String chequeDateLeft) {
        this.chequeDateLeft = chequeDateLeft;
    }

    public String getFavourOfTop() {
        return favourOfTop;
    }

    public void setFavourOfTop(String favourOfTop) {
        this.favourOfTop = favourOfTop;
    }

    public String getFavourOfLeft() {
        return favourOfLeft;
    }

    public void setFavourOfLeft(String favourOfLeft) {
        this.favourOfLeft = favourOfLeft;
    }

    public String getAmtInWordsTop() {
        return amtInWordsTop;
    }

    public void setAmtInWordsTop(String amtInWordsTop) {
        this.amtInWordsTop = amtInWordsTop;
    }

    public String getAmtInWordsLeft() {
        return amtInWordsLeft;
    }

    public void setAmtInWordsLeft(String amtInWordsLeft) {
        this.amtInWordsLeft = amtInWordsLeft;
    }

    public String getAmtTop() {
        return amtTop;
    }

    public void setAmtTop(String amtTop) {
        this.amtTop = amtTop;
    }

    public String getAmtLeft() {
        return amtLeft;
    }

    public void setAmtLeft(String amtLeft) {
        this.amtLeft = amtLeft;
    }

    public String getAuthSign() {
        return authSign;
    }

    public void setAuthSign(String authSign) {
        this.authSign = authSign;
    }

    public String getAuthSignTop() {
        return authSignTop;
    }

    public void setAuthSignTop(String authSignTop) {
        this.authSignTop = authSignTop;
    }

    public String getAuthSignLeft() {
        return authSignLeft;
    }

    public void setAuthSignLeft(String authSignLeft) {
        this.authSignLeft = authSignLeft;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNameTop() {
        return companyNameTop;
    }

    public void setCompanyNameTop(String companyNameTop) {
        this.companyNameTop = companyNameTop;
    }

    public String getCompanyNameLeft() {
        return companyNameLeft;
    }

    public void setCompanyNameLeft(String companyNameLeft) {
        this.companyNameLeft = companyNameLeft;
    }

    public String getPaperWidth() {
        return paperWidth;
    }

    public void setPaperWidth(String paperWidth) {
        this.paperWidth = paperWidth;
    }

    public String getPaperHeight() {
        return paperHeight;
    }

    public void setPaperHeight(String paperHeight) {
        this.paperHeight = paperHeight;
    }

}
