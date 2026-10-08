package dataman.dmToolkit.dmFileUploader.client.events;

/**
 * An interface that can be used as a callback handler when uploader start
 * events are fired by the Uploader component. General usage is as follows:  <code><pre>
 * uploader.setUploadStartHandler(new UploadStartHandler() {
 *    public boolean onUploadStart(UploadStartEvent event) {
 *       Window.alert("Beginning uploader of " + event.getFile().getName());
 *       return true;
 *    }
 * });
 * </pre></code>
 * <p/>
 * Note that the 'uploadStart' event is called immediately before the file is
 * uploaded. This event provides an opportunity to perform any last minute
 * validation, add post params or do any other work before the file is uploaded.
 * <p/>
 * The uploader can be cancelled by returning 'false' from uploadStart in the
 * handler. If you return 'true' or do not return any value then the uploader
 * proceeds. Returning 'false' will cause an uploadError event to fired.
 * <p/>
 * See the documentation on the {@link DmUploadStartEvent} class for more
 * details on the data available when an uploader start event occurs.
 *
 * @author squinn@moxiegroup.com (Shawn Quinn)
 */
public interface DmUploadStartHandler {

    /**
     * This method is fired whenever a uploader start event occurs in the
     * Uploader component. See the {@link DmUploadStartEvent} class for more
     * details on the data available when this event is fired.
     *
     * @param uploadStartEvent The details of the event that occurred.
     * @return The response to send back to the event handler function
     */
    public boolean onUploadStart(DmUploadStartEvent uploadStartEvent);

}
