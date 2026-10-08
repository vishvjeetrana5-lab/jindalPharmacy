package dataman.dmBase.dmBaseStatic.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import dataman.dmBase.dmSession.client.DmRequestBuilder;

class DmRootEventsListener implements Event.NativePreviewHandler {

    private DmRootEvent rootEvent;
    private boolean pageUnload = false;

    protected DmRootEventsListener() {
        registerOnBeforePageUnload(this);
    }

    protected DmRootEventsListener(boolean callPageUnload) {
        if (callPageUnload) {
            registerOnBeforePageUnload(this);
        }
    }

    public String onBeforePageUnloadMessage(JavaScriptObject obj) {
        return "Are you sure? You want to exit from application.";
    }

    public static native void setPageUnload(boolean pageUnload)/*-{
        $wnd.pageUnload = pageUnload;
    }-*/;

    public static native boolean getPageUnload()/*-{
        return $wnd.pageUnload;
    }-*/;

    private native void deregisterOnBeforePageUnload()/*-{
        $wnd.onbeforeunload = null;
    }-*/;

    private native void registerOnBeforePageUnload(DmRootEventsListener instance)/*-{
        $wnd.onbeforeunload = $entry(function(e) {            
            return instance.@dataman.dmBase.dmBaseStatic.client.DmRootEventsListener::onBeforePageUnloadMessage(Lcom/google/gwt/core/client/JavaScriptObject;)(e);            
        });
    }-*/;

    @Override
    public void onPreviewNativeEvent(Event.NativePreviewEvent event) {
        checkConditionForPageUnload(event);

        if (event.getTypeInt() == Event.ONKEYDOWN) {

            cancelDefaultAction(event);
            if (!DmRequestBuilder.isServiceCalling()) {
                if (DmBaseClient.getRootKeyEvents().containsKey(event.getTypeInt())) {
                    rootEvent = new DmRootEvent();
                    rootEvent.setEvent(event);
                    for (int i = DmBaseClient.getRootKeyEvents().get(event.getTypeInt()).size() - 1; i >= 0; i--) {
                        DmBaseClient.getRootKeyEvents().get(event.getTypeInt()).get(i).rootKeyInvoked(rootEvent);
                        if (rootEvent.isStopPropagation()) {
                            break;
                        }
                    }
                }
            }
        }
    }

    private void cancelDefaultAction(Event.NativePreviewEvent event) {
        if (event.getNativeEvent().getCtrlKey()
                && (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_A //stop selecting all content of page
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_B //stop showing the bookmarks
                //                    || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_C //stop copying text
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_D //stopping bookmark addition
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_E
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F //stop finding on current page
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_G //stop finding on current page
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_H //stop opening the history page
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_I //stop opening the bookmark window
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_J //stop opening the history window
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_K
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_L //stop focussing on address bar
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_M
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_N //stop opening new window
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_O
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_P //stop printing
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_Q //stop opening new file
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_R //stop refreshing the page
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_S //stop saving
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_T //stop opening new tab
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_U //stop showing sourcecode
                //                    || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_V //stop pasting of text
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_W //stop closing window
                //                    || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_X //stop cut
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_Y
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_Z //stop doing undo
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_NUM_PLUS //stop zoom in 
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_NUM_MINUS //stop zoom out 
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F1
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F2
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F3
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F4 //stop closing current tab
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F5 //stop refreshing the current page
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F6
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F7
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F8
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F9
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F10 //stop showing the toolbars of browser
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F11
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F12 ////stop showing the code inspector
                )) {
            event.getNativeEvent().preventDefault();
            event.cancel();
        }

        if (event.getNativeEvent().getAltKey()
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F1
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F2
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F3 //stop finding on current page
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F4
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F5 //stop refreshing the current page
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F6 //stop focussing the address bar
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F7
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F8
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F9
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F10 //stop showing the toolbars of browser
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F11 //stop showing the full screen of browser
                || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F12 //stop showing code inspector
                ) {
            event.getNativeEvent().preventDefault();
            event.cancel();
        }

        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_F2) {
            event.getNativeEvent().preventDefault();
            event.cancel();
        }
    }

    private boolean checkConditionForPageUnload(Event.NativePreviewEvent event) {
        boolean bool = true;

        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_BACKSPACE) {
            if (!checkForPageUnload(event)) {
                event.getNativeEvent().preventDefault();
                event.cancel();
                event.consume();
                bool = false;
            }
        }
        return bool;
    }

    private static boolean checkBrowser(String browserName) {
        return (Window.Navigator.getUserAgent().toLowerCase().indexOf(browserName.toLowerCase()) != -1);
    }

    private boolean checkForPageUnload(Event.NativePreviewEvent event) {
        boolean bool = true;
        try {

            Element element = Element.as(event.getNativeEvent().getEventTarget());
            String tagName = element.getTagName();
            if ((!tagName.equalsIgnoreCase("input")) && (!tagName.equalsIgnoreCase("textarea"))) {
                if ((checkBrowser("firefox") && (event.getTypeInt() == Event.ONKEYPRESS)) || (!checkBrowser("firefox") && (event.getTypeInt() == Event.ONKEYDOWN))) {
                    bool = Window.confirm(onBeforePageUnloadMessage(null));
                    if (bool) {
                        deregisterOnBeforePageUnload();
                    }
                }
            }

        } catch (Exception ex) {
            bool = false;
        }
        return bool;
    }

}
