package dataman.dmToolkit.dmFileUploader.client.events;

/**
 * An interface that can be used as a callback handler when uploader error
 * events are fired by the Uploader component. General usage is as follows:  <code><pre>
 * uploader.setUploadErrorHandler(new UploadErrorHandler() {
 *    public boolean onUploadError(UploadErrorEvent event) {
 *       Window.alert("Unable to uploader file " + event.getFile().getName());
 *       return true;
 *    }
 * });
 * </pre></code>
 * <p/>
 * Note that the 'uploadError' event is fired any time an uploader is
 * interrupted or does not complete successfully. The error code parameter
 * indicates the type of error that occurred.
 * <p/>
 * Stopping, Cancelling or returning 'false' from uploadStart will cause
 * uploadError to fire. Uploader error will not fire for files that are
 * cancelled but still waiting in the queue.
 * <p/>
 * See the documentation on the {@link DmUploadErrorEvent} class for more
 * details on the data available when an uploader error event occurs.
 *
 * @author squinn@moxiegroup.com (Shawn Quinn)
 */
public interface DmUploadErrorHandler {

    /**
     * This method is fired whenever a uploader error event occurs in the
     * Uploader component. See the {@link DmUploadErrorEvent} class for more
     * details on the data available when this event is fired.
     *
     * @param uploadErrorEvent The details of the event that occurred.
     * @return The response to send back to the event handler function
     */
    public boolean onUploadError(DmUploadErrorEvent uploadErrorEvent);

}
