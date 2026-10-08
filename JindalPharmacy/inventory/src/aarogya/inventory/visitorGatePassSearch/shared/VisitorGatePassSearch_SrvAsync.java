/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.inventory.visitorGatePassSearch.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.UserPermission;
import java.util.List;

public interface VisitorGatePassSearch_SrvAsync {
  

 
      public void getVisitorGateSearchDetail( UserPermission userPermission,boolean gateOutFlag,VisitorGateSearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdVisitorGatePassDetailsSearch_Mdl>>> result);
}
