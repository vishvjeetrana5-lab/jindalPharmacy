package dataman.dmToolkitR.dmFileUploader.client.events;

/**
 * An interface that can be used as a callback handler when file queued events
 * are fired by the Uploader component. General usage is as follows:  <code><pre>
 * uploader.setFileQueuedEventHandler(new FileQueuedHandler() {
 *    public boolean onFileQueued(FileQueuedEvent event) {
 *       Window.alert("File with name " + event.getFile().getName() + " is ready to uploader");
 *       return true;
 *    }
 * });
 * </pre></code> Note that the 'fileQueued' event is fired for each file that is
 * queued after the File Selection Dialog window is closed.
 * <p/>
 * See the documentation on the {@link DmFileQueuedEvent} class for more details
 * on the data available when a file queued event occurs.
 *
 * @author squinn@moxiegroup.com (Shawn Quinn)
 */
public interface DmFileQueuedHandler {

    /**
     * This method is fired whenever a file queued event occurs in the Uploader
     * component. See the {@link DmFileQueuedEvent} class for more details on
     * the data available when this event is fired.
     *
     * @param fileQueuedEvent The details of the event that occurred.
     * @return The response to send back to the event handler function
     */
    public boolean onFileQueued(DmFileQueuedEvent fileQueuedEvent);

}
