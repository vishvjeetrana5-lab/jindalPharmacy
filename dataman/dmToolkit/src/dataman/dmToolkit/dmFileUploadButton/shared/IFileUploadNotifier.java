package dataman.dmToolkit.dmFileUploadButton.shared;

import dataman.dmBase.dmFileManager.shared.DmIFile_Mdl;

public interface IFileUploadNotifier {

    public void notifyForUpload(String notifier);

    public void processAfterUploaded(DmIFile_Mdl file);
}
