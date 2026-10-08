/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aarogya.pharmacy.reports.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Manish
 */
public interface RptPaymentReceiptRegister_Srv extends RemoteService {

    public DmResult<KeyFieldExtended> getDefaultValue();

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtPartyName_Mdl>> getTxtHPartyName( DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty);

}
