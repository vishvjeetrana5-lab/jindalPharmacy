package dataman.dmToolkit.gsts.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GSTS_Mdl implements Serializable, IsSerializable {

    private String gstsHeadCode;
    private String gstsHeadShortName;
    private String gstsHeadName;
    private String atRate;
    private String purchaseSalesAcName;
    private String purchaseSalesAcCode;
    private String purchaseSalesAcManualCode;
    private String inputTaxAcName;
    private String inputTaxAcCode;
    private String inputTaxAcManualCode;
    private String outputTaxAcName;
    private String outputTaxAcCode;
    private String outputTaxAcManualCode;
    private String rcmAcName;
    private String rcmAcCode;
    private String rcmAcManualCode;

    public GSTS_Mdl() {
    }

    public String getGstsHeadCode() {
        return gstsHeadCode;
    }

    public void setGstsHeadCode(String gstsHeadCode) {
        this.gstsHeadCode = gstsHeadCode;
    }

    public String getGstsHeadShortName() {
        return gstsHeadShortName;
    }

    public void setGstsHeadShortName(String gstsHeadShortName) {
        this.gstsHeadShortName = gstsHeadShortName;
    }

    public String getGstsHeadName() {
        return gstsHeadName;
    }

    public void setGstsHeadName(String gstsHeadName) {
        this.gstsHeadName = gstsHeadName;
    }

    public String getAtRate() {
        return atRate;
    }

    public void setAtRate(String atRate) {
        this.atRate = atRate;
    }

    public String getPurchaseSalesAcName() {
        return purchaseSalesAcName;
    }

    public void setPurchaseSalesAcName(String purchaseSalesAcName) {
        this.purchaseSalesAcName = purchaseSalesAcName;
    }

    public String getPurchaseSalesAcCode() {
        return purchaseSalesAcCode;
    }

    public void setPurchaseSalesAcCode(String purchaseSalesAcCode) {
        this.purchaseSalesAcCode = purchaseSalesAcCode;
    }

    public String getPurchaseSalesAcManualCode() {
        return purchaseSalesAcManualCode;
    }

    public void setPurchaseSalesAcManualCode(String purchaseSalesAcManualCode) {
        this.purchaseSalesAcManualCode = purchaseSalesAcManualCode;
    }

    public String getInputTaxAcName() {
        return inputTaxAcName;
    }

    public void setInputTaxAcName(String inputTaxAcName) {
        this.inputTaxAcName = inputTaxAcName;
    }

    public String getInputTaxAcCode() {
        return inputTaxAcCode;
    }

    public void setInputTaxAcCode(String inputTaxAcCode) {
        this.inputTaxAcCode = inputTaxAcCode;
    }

    public String getInputTaxAcManualCode() {
        return inputTaxAcManualCode;
    }

    public void setInputTaxAcManualCode(String inputTaxAcManualCode) {
        this.inputTaxAcManualCode = inputTaxAcManualCode;
    }

    public String getOutputTaxAcName() {
        return outputTaxAcName;
    }

    public void setOutputTaxAcName(String outputTaxAcName) {
        this.outputTaxAcName = outputTaxAcName;
    }

    public String getOutputTaxAcCode() {
        return outputTaxAcCode;
    }

    public void setOutputTaxAcCode(String outputTaxAcCode) {
        this.outputTaxAcCode = outputTaxAcCode;
    }

    public String getOutputTaxAcManualCode() {
        return outputTaxAcManualCode;
    }

    public void setOutputTaxAcManualCode(String outputTaxAcManualCode) {
        this.outputTaxAcManualCode = outputTaxAcManualCode;
    }

    public String getRcmAcName() {
        return rcmAcName;
    }

    public void setRcmAcName(String rcmAcName) {
        this.rcmAcName = rcmAcName;
    }

    public String getRcmAcCode() {
        return rcmAcCode;
    }

    public void setRcmAcCode(String rcmAcCode) {
        this.rcmAcCode = rcmAcCode;
    }

    public String getRcmAcManualCode() {
        return rcmAcManualCode;
    }

    public void setRcmAcManualCode(String rcmAcManualCode) {
        this.rcmAcManualCode = rcmAcManualCode;
    }

   

    public GSTS_Mdl clone() {
        GSTS_Mdl its_Mdl = new GSTS_Mdl();

        its_Mdl.setGstsHeadCode(this.getGstsHeadCode());
        its_Mdl.setGstsHeadShortName(this.getGstsHeadShortName());
        its_Mdl.setGstsHeadName(this.getGstsHeadName());
        its_Mdl.setAtRate(this.getAtRate());
        its_Mdl.setPurchaseSalesAcCode(this.getPurchaseSalesAcCode());
        its_Mdl.setPurchaseSalesAcName(this.getPurchaseSalesAcName());
        its_Mdl.setPurchaseSalesAcManualCode(this.getPurchaseSalesAcManualCode());
        its_Mdl.setInputTaxAcCode(this.getInputTaxAcCode());
        its_Mdl.setInputTaxAcName(this.getInputTaxAcName());
        its_Mdl.setInputTaxAcManualCode(this.getInputTaxAcManualCode());
        its_Mdl.setOutputTaxAcCode(this.getOutputTaxAcCode());
        its_Mdl.setOutputTaxAcName(this.getOutputTaxAcName());
        its_Mdl.setOutputTaxAcManualCode(this.getOutputTaxAcManualCode());
        its_Mdl.setRcmAcCode(this.getRcmAcCode());
        its_Mdl.setRcmAcName(this.getRcmAcName());
        its_Mdl.setRcmAcManualCode(this.getRcmAcManualCode());

        return its_Mdl;
    }

    public static List<GSTS_Mdl> getITSListClone(List<GSTS_Mdl> list) {
        List<GSTS_Mdl> itsList = null;

        if (list != null) {
            itsList = new ArrayList<>();
            for (GSTS_Mdl its : list) {
                itsList.add(its.clone());
            }
        }

        return itsList;
    }

}
