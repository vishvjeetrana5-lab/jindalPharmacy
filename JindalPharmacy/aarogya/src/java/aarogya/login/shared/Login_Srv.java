package aarogya.login.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.PCSData;
import java.util.List;

public interface Login_Srv extends RemoteService {

    public DmResult<Initialze_Mdl> initialize();

    public DmResult<PCSData> validateUser(Login_Model.Body bTemp);

    public DmResult<Boolean> validateDBPassword(String password);

    public DmResult<String> lockUser();
    
    public DmResult<String> forgetPassword(ForgetPassword_Mdl forgetPasswordMdl);

    public DmResult<ForgetPassword_Mdl> getDefaultValues();

    public DmResultPageing<List<KeyFieldExtended>> getTxtHPltCode(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHFinancialYear(String pltCode, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> setLocalIPAddress(List<String> ipAddresses);
}
