/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.dmToolkit.dmImageCanvas.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.dmFileManager.shared.DmFile_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmSize;

/**
 *
 * @author abhishek
 */
public interface DmImageCanvas_SrvAsync {

    public void saveImage(String imgStr, DmFile_Mdl fileModel, AsyncCallback<DmResult<DmFile_Mdl>> callback);

    public void getAspectImageSize(String ruleCode, String mongoFileLink, int expWidth,int expHeight, AsyncCallback<DmResult<DmSize>> cakkback);
}
