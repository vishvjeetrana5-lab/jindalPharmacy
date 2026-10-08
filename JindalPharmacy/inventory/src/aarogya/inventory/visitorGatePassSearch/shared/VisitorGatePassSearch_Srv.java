/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.inventory.visitorGatePassSearch.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.UserPermission;
import java.util.List;

/**
 *
 * @author Ashish Gupta
 */
public interface VisitorGatePassSearch_Srv extends RemoteService {
    
     
       public DmResultPageing<List<GrdVisitorGatePassDetailsSearch_Mdl>> getVisitorGateSearchDetail(UserPermission userPermission,boolean gateOutFlag, VisitorGateSearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile);
}
