package dataman.dmBase.dmSession.server;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

public class DmSessionPropertyListener implements HttpSessionAttributeListener {

    @Override
    public void attributeAdded(HttpSessionBindingEvent hsbe) {
//        setFileManager(hsbe.getName(), hsbe.getValue());
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent hsbe) {
//        setFileManager(hsbe.getName(), hsbe.getValue());
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent hsbe) {
//        setFileManager(hsbe.getName(), hsbe.getValue());
    }

}
