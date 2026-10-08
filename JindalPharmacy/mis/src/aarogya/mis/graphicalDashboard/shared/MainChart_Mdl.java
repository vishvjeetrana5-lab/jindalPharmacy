package aarogya.mis.graphicalDashboard.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmToolkit.dmChart.shared.DmChart_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainChart_Mdl implements Serializable, IsSerializable {

    //================================OPD Charts List============================
    private List<DmChart_Mdl> lstOPDConsultantWise = new ArrayList<DmChart_Mdl>();
    private List<DmChart_Mdl> lstOPDCollection = new ArrayList<DmChart_Mdl>();
    private List<DmChart_Mdl> lstOPDDepartment = new ArrayList<DmChart_Mdl>();
    private List<DmChart_Mdl> lstOPDDepartmentExpand = new ArrayList<DmChart_Mdl>();
    private List<DmChart_Mdl> listOPDChart4 = new ArrayList<DmChart_Mdl>();
    private List<DmChart_Mdl> lstOPDCaseWise = new ArrayList<DmChart_Mdl>(); 
    private List<DmChart_Mdl> listOPDChart6 = new ArrayList<DmChart_Mdl>();

    private String lblOPDUnBilled;
    private String lblOPDBilled;
    private String lblOPDMedCount;

    //================================IPD Charts List============================
    private List<DmChart_Mdl> listIPDChart1 = new ArrayList<DmChart_Mdl>();
    private List<DmChart_Mdl> listIPDChart2 = new ArrayList<DmChart_Mdl>();
    private List<DmChart_Mdl> listIPDChart3 = new ArrayList<DmChart_Mdl>();
    private List<DmChart_Mdl> listExpandIPDChart3 = new ArrayList<DmChart_Mdl>();
    private List<DmChart_Mdl> listIPDChart4 = new ArrayList<DmChart_Mdl>();
    private List<DmChart_Mdl> listIPDChart5 = new ArrayList<DmChart_Mdl>();
    private List<DmChart_Mdl> listIPDChart6 = new ArrayList<DmChart_Mdl>();

    //================================DIAG Charts List============================
    private List<DmChart_Mdl> listDIAGChart1 = new ArrayList<DmChart_Mdl>();
    private List<DmChart_Mdl> listDIAGChart2 = new ArrayList<DmChart_Mdl>();
    private List<DmChart_Mdl> listDIAGChart3 = new ArrayList<DmChart_Mdl>();
    private List<DmChart_Mdl> listExpandDIAGChart3 = new ArrayList<DmChart_Mdl>();

    private List<DmChart_Mdl> listDIAGChart6 = new ArrayList<DmChart_Mdl>();

    //================================PHARM Charts List============================
    private List<DmChart_Mdl> listPHARMChart1 = new ArrayList<DmChart_Mdl>();
    private List<DmChart_Mdl> listPHARMChart2 = new ArrayList<DmChart_Mdl>();
    private List<DmChart_Mdl> listPHARMChart3 = new ArrayList<DmChart_Mdl>();
    private List<DmChart_Mdl> listExpandPHARMChart3 = new ArrayList<DmChart_Mdl>();
    private List<DmChart_Mdl> listPHARMChart4 = new ArrayList<DmChart_Mdl>();
    private List<DmChart_Mdl> listPHARMChart5 = new ArrayList<DmChart_Mdl>();
    private List<DmChart_Mdl> listPHARMChart6 = new ArrayList<DmChart_Mdl>();

    private String totalSale;

    //================================NET Charts List============================
    private List<DmChart_Mdl> listNETChart1 = new ArrayList<DmChart_Mdl>();

    public MainChart_Mdl() {
    }

    public List<DmChart_Mdl> getLstOPDConsultantWise() {
        return lstOPDConsultantWise;
    }

    public List<DmChart_Mdl> getLstOPDCollection() {
        return lstOPDCollection;
    }

    public List<DmChart_Mdl> getLstOPDDepartment() {
        return lstOPDDepartment;
    }

    public List<DmChart_Mdl> getListOPDChart4() {
        return listOPDChart4;
    }

    public List<DmChart_Mdl> getLstOPDCaseWise() {
        return lstOPDCaseWise;
    }

    public List<DmChart_Mdl> getListOPDChart6() {
        return listOPDChart6;
    }

    public List<DmChart_Mdl> getListIPDChart1() {
        return listIPDChart1;
    }

    public List<DmChart_Mdl> getListIPDChart2() {
        return listIPDChart2;
    }

    public List<DmChart_Mdl> getListIPDChart3() {
        return listIPDChart3;
    }

    public List<DmChart_Mdl> getListIPDChart4() {
        return listIPDChart4;
    }

    public List<DmChart_Mdl> getListIPDChart5() {
        return listIPDChart5;
    }

    public List<DmChart_Mdl> getListIPDChart6() {
        return listIPDChart6;
    }

    public List<DmChart_Mdl> getLstOPDDepartmentExpand() {
        return lstOPDDepartmentExpand;
    }

    public List<DmChart_Mdl> getListExpandIPDChart3() {
        return listExpandIPDChart3;
    }

    public List<DmChart_Mdl> getListDIAGChart1() {
        return listDIAGChart1;
    }

    public List<DmChart_Mdl> getListDIAGChart2() {
        return listDIAGChart2;
    }

    public List<DmChart_Mdl> getListDIAGChart3() {
        return listDIAGChart3;
    }

    public List<DmChart_Mdl> getListExpandDIAGChart3() {
        return listExpandDIAGChart3;
    }

    public List<DmChart_Mdl> getListDIAGChart6() {
        return listDIAGChart6;
    }

    public List<DmChart_Mdl> getListPHARMChart1() {
        return listPHARMChart1;
    }

    public List<DmChart_Mdl> getListPHARMChart2() {
        return listPHARMChart2;
    }

    public List<DmChart_Mdl> getListPHARMChart3() {
        return listPHARMChart3;
    }

    public List<DmChart_Mdl> getListExpandPHARMChart3() {
        return listExpandPHARMChart3;
    }

    public List<DmChart_Mdl> getListPHARMChart4() {
        return listPHARMChart4;
    }

    public List<DmChart_Mdl> getListPHARMChart5() {
        return listPHARMChart5;
    }

    public List<DmChart_Mdl> getListPHARMChart6() {
        return listPHARMChart6;
    }

    public List<DmChart_Mdl> getListNETChart1() {
        return listNETChart1;
    }

    public String getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(String totalSale) {
        this.totalSale = totalSale;
    }

    public String getLblOPDUnBilled() {
        return lblOPDUnBilled;
    }

    public void setLblOPDUnBilled(String lblOPDUnBilled) {
        this.lblOPDUnBilled = lblOPDUnBilled;
    }

    public String getLblOPDBilled() {
        return lblOPDBilled;
    }

    public void setLblOPDBilled(String lblOPDBilled) {
        this.lblOPDBilled = lblOPDBilled;
    }

    public String getLblOPDMedCount() {
        return lblOPDMedCount;
    }

    public void setLblOPDMedCount(String lblOPDMedCount) {
        this.lblOPDMedCount = lblOPDMedCount;
    }

}
