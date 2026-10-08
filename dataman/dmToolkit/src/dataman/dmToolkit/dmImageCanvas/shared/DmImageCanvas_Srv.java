/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.dmToolkit.dmImageCanvas.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.dmFileManager.shared.DmFile_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmSize;

/**
 *
 * @author abhishek
 */
public interface DmImageCanvas_Srv extends RemoteService {

    public DmResult<DmFile_Mdl> saveImage(String imgStr, DmFile_Mdl fileModel);

    public DmResult<DmSize> getAspectImageSize(String ruleCode, String mongoFileLink, int expWidth,int expHeight);
}
