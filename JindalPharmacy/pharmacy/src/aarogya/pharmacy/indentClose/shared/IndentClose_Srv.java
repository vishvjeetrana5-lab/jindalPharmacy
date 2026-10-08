package aarogya.pharmacy.indentClose.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface IndentClose_Srv extends RemoteService {

    public DmResult<String> save(GrdMedicineDetail_Mdl approval_Mdl);

    public DmResult<String> openRecord(boolean isOpen, String docId, String v_SNo);

    public DmResult<IndentClose_Mdl> fillItemGrid(String code);

    public DmResultPageing<List<GrdIndentClose_Mdl>> getFillRecords(IndentClose_Mdl param, DmHelper helper, boolean blnToEndOfFile);
    
    public DmResult<GrdIndentClose_Mdl> getFillCancelRecords(List<GrdIndentClose_Mdl> param);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHItem(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(String beforeDays, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIndenter(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHIndentNo_Mdl>> getTxtHIndentNo(DmHelper pageing, boolean blnToEndOfFile);

}
