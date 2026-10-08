package aarogya.pharmacy.barcodeGeneration.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface BarcodeGeneration_Srv extends RemoteService {

    public DmResult<String> saveRecord(BarcodeGeneration_Mdl mrTemp, Setting_Mdl setting_Mdl);

    public DmResult<BarcodeGeneration_Mdl> getFillRecords(Setting_Mdl setting_Mdl);

    public DmResult<BarcodeGeneration_Mdl> getMoveRec(String docid);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHBarCodeHeader(DmHelper pageing, boolean blnToEndOfFile);

}
