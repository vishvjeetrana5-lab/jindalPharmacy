package dataman.dmToolkitR.dmFileUploader.client.events;

/**
 * An interface that can be used as a callback handler when file dialog start
 * events are fired by the Uploader component. General usage is as follows:  <code><pre>
 * uploader.setFileDialogStartHandler(new FileDialogStartHandler() {
 *    public boolean onFileDialogStart(FileDialogStartEvent event) {
 *       Window.alert("Starting the file selection dialog...");
 *       return true;
 *    }
 * });
 * </pre></code> Note that the 'fileDialogStart' is fired after selectFile for
 * selectFiles is called. This event is fired immediately before the File
 * Selection Dialog window is displayed. However, the event may not execute
 * until after the Dialog window is closed.
 * <p/>
 * See the documentation on the {@link DmFileDialogStartEvent} class for more
 * details on the data available when a file dialog start event occurs.
 *
 * @author squinn@moxiegroup.com (Shawn Quinn)
 */
public interface DmFileDialogStartHandler {

    /**
     * This method is fired whenever a file dialog start event occurs in the
     * Uploader component. See the {@link DmFileDialogStartEvent} class for more
     * details on the data available when this event is fired.
     *
     * @param fileDialogStartEvent The details of the event that occurred.
     * @return The response to send back to the event handler function
     */
    public boolean onFileDialogStartEvent(DmFileDialogStartEvent fileDialogStartEvent);

}
