package aarogya.login.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class Login_Model {

    public static final String strWidgetName = "Login";
    public static final String strStyleFileName = "Login_Style.css";
    public static final String strModuleName = "Login";

    public static class Body implements Serializable, IsSerializable {

        private byte[] userName;
        private byte[] password;

        public Body() {
        }

        public String getUserName() {
            return new String(userName);
        }

        public void setUserName(String userName) {
            this.userName = userName.getBytes();
        }

        public String getPassword() {
            return new String(password);
        }

        public void setPassword(String password) {
            this.password = password.getBytes();
        }
    }
}
