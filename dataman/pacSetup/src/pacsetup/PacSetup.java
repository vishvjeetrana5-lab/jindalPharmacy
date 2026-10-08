package pacsetup;

import java.awt.Dimension;
import pacsetup.commons.Functions;
import pacsetup.commons.SystemProperties;
import pacsetup.forms.frmSetup;

public class PacSetup {

    public static void main(String[] args) {
        Functions.setSystemProperties(new SystemProperties(System.getProperties()));
        try {
            Functions.readApplicationPropertiesFile();

            frmSetup frm = new frmSetup();
            frm.setLocation(Functions.getCenterOfScreenPoint(new Dimension(frm.getWidth(), frm.getHeight())));
            frm.setVisible(true);
        } catch (Exception ex) {
            Functions.writeException(ex);
        }
    }

}
