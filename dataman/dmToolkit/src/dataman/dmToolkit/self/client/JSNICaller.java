package dataman.dmToolkit.self.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class JSNICaller {

    public interface Callback {

        public void handle(NativeEvent nativeEvent, String elementId);
    }

    private Callback callback;

    public JSNICaller(Callback callBack) {
        registerJSNI(this);
        this.callback = callBack;
    }

    public void handle(JavaScriptObject obj) {
        if (obj instanceof NativeEvent) {
            NativeEvent nativeEvent = (NativeEvent) obj.cast();
            String elementId = Element.as(nativeEvent.getEventTarget()).getId();
            if (this.callback != null) {
                this.callback.handle(nativeEvent, DmShared.xNull(elementId).trim());
            }
        }

    }

    private native void registerJSNI(JSNICaller instance)/*-{                
        $doc.callJSNI= $entry(function callJSNI(event)
        {
            instance.@dataman.dmToolkit.self.client.JSNICaller::handle(Lcom/google/gwt/core/client/JavaScriptObject;)(event);
        });
    }-*/;

}
