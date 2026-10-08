package aarogya.pharmacy.saleBillItemSearch.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface SaleBillItemSearch_Srv extends RemoteService {

    public DmResult<List<GrdSaleBillItemSearch_Mdl>> getSaleBillItemSearchDetail(SaleBillItemSearch_Mdl searchValue_Mdl,String callFrom);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartmentRequisitionSearch(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> openRecord(boolean isOpen, String docId, String vSNo);

}
