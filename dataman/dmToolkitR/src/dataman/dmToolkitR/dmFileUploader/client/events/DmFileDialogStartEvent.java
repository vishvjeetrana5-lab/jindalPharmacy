package dataman.dmToolkitR.dmFileUploader.client.events;

/**
 * Provides access to the raw information provided by Uploader when a
 * 'fileDialogStart' event occurs. This class should not be instantiated
 * directly, but instead you should create a {@link FileDialogStartHandler} and
 * register it via the
 * {@link org.moxieapps.gwt.uploader.client.Uploader#setFileDialogStartHandler(FileDialogStartHandler)}
 * method in order to access file dialog start events.
 * <p/>
 * Note that the 'fileDialogStart' is fired after selectFile for selectFiles is
 * called. This event is fired immediately before the File Selection Dialog
 * window is displayed. However, the event may not execute until after the
 * Dialog window is closed.
 * <p/>
 *
 * @author squinn@moxiegroup.com (Shawn Quinn)
 */
public class DmFileDialogStartEvent {

    /**
     * This constructor is intended for internal use only. You should not create
     * file dialog start events directly, but instead should register a
     * {@link FileDialogStartHandler}.
     */
    public DmFileDialogStartEvent() {
    }

}
