package dataman.webJobs.environmentSetting.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class EnvironmentSetting_Vld {

    public static void chkSave(EnvironmentSetting_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Site", 100);
        if (!DmShared.xNull(mrTemp.getUrl()).equalsIgnoreCase("")
                || !DmShared.xNull(mrTemp.getAccName()).equalsIgnoreCase("")
                || !DmShared.xNull(mrTemp.getPassword()).equalsIgnoreCase("")
                || !DmShared.xNull(mrTemp.getFormat()).equalsIgnoreCase("")
                || DmShared.iNull(mrTemp.getMsgLength()).intValue() > 0) {
            DmShared.isValidText(mrTemp.getUrl().trim(), "URL", 200);
            DmShared.isValidText(mrTemp.getAccName().trim(), "Account Name", 300);
            DmShared.isValidText(mrTemp.getPassword().trim(), "Password", 400);
            DmShared.isValidText(mrTemp.getFormat().trim(), "Format", 500);
            if (DmShared.iNull(mrTemp.getMsgLength()).intValue() == 0) {
                throw new DmException(600, "Message Length is mandatory. ");
            }
        }
        if (!DmShared.xNull(mrTemp.getSmtpServer()).equalsIgnoreCase("")
                || DmShared.iNull(mrTemp.getSmtpPort()).intValue() > 0) {
            DmShared.isValidText(mrTemp.getSmtpServer().trim(), "SMTP Sever", 700);
            DmShared.isValidText(mrTemp.getSmtpPort().trim(), "SMTP Port", 800);

        }
         DmShared.isValidMobile(mrTemp.getPayRefOtpMobile(), 1000);
    }
}
