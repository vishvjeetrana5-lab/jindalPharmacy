package aarogya.pharmacy.saleBillItemSearch.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface SaleBillItemSearch_SrvAsync {

    public void getSaleBillItemSearchDetail(SaleBillItemSearch_Mdl searchValue_Mdl,String callFrom, AsyncCallback<DmResult<List<GrdSaleBillItemSearch_Mdl>>> result);

    public void getTxtHDepartmentRequisitionSearch(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void openRecord(boolean isOpen, String docId, String vSNo, AsyncCallback<DmResult<String>> result);

}
