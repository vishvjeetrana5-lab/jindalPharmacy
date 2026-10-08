package dataman.dmToolkit.dmFileUploader.client.events;

/**
 * An interface that can be used as a callback handler when file queue error
 * events are fired by the Uploader component. General usage is as follows:  <code><pre>
 * uploader.setFileQueueErrorEventHandler(new FileQueueErrorHandler() {
 *    public boolean onFileQueueError(FileQueueErrorEvent event) {
 *       Window.alert("File with name " + event.getFile().getName() + " failed in the queue due to: " + event.getErrorCode().toString());
 *       return true;
 *    }
 * });
 * </pre></code> Note that the 'fileQueueError' event is fired for each file
 * that was not queued after the File Selection Dialog window is closed. A file
 * may not be queued for several reasons such as, the file exceeds the file
 * size, the file is empty or a file or queue limit has been exceeded.
 * <p/>
 * <p/>
 * See the documentation on the {@link DmFileQueueErrorEvent} class for more
 * details on the data available when a file queue error event occurs.
 *
 * @author squinn@moxiegroup.com (Shawn Quinn)
 */
public interface DmFileQueueErrorHandler {

    /**
     * This method is fired whenever a file queue error event occurs in the
     * Uploader component. See the
     * {@link org.moxieapps.gwt.uploader.client.events.FileQueueErrorEvent}
     * class for more details on the data available when this event is fired.
     *
     * @param fileQueueErrorEvent The details of the event that occurred.
     * @return The response to send back to the event handler function
     */
    public boolean onFileQueueError(DmFileQueueErrorEvent fileQueueErrorEvent);

}
