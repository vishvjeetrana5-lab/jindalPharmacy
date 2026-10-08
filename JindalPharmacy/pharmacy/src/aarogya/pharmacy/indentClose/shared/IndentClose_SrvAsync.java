package aarogya.pharmacy.indentClose.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface IndentClose_SrvAsync {

    public void save(GrdMedicineDetail_Mdl approval_Mdl, AsyncCallback<DmResult<String>> result);

    public void openRecord(boolean isOpen, String docId, String v_SNo, AsyncCallback<DmResult<String>> result);

    public void getFillRecords(IndentClose_Mdl param, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdIndentClose_Mdl>>> result);
    
    public void getFillCancelRecords(List<GrdIndentClose_Mdl> param, AsyncCallback<DmResult<GrdIndentClose_Mdl>> result);

    public void getTxtHItem(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHStatus(String beforeDays, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHIndenter(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void fillItemGrid(String code, AsyncCallback<DmResult<IndentClose_Mdl>> result);

    public void getTxtHIndentNo(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHIndentNo_Mdl>>> result);

}
