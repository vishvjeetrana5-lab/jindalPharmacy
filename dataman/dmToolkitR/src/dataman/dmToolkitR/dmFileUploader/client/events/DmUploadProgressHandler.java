package dataman.dmToolkitR.dmFileUploader.client.events;

/**
 * An interface that can be used as a callback handler when uploader progress
 * events are fired by the Uploader component. General usage is as follows:  <code><pre>
 * uploader.setUploadProgressHandler(new UploadProgressHandler() {
 *    public boolean onUploadProgress(UploadProgressEvent event) {
 *       Window.alert("Uploader progressed to " + event.getBytesTotal() + " bytes");
 *       return true;
 *    }
 * });
 * </pre></code>
 * <p/>
 * Note that the 'uploadProgress' event is fired periodically by the Flash
 * Control. This event is useful for providing UI updates on the page.
 * <p/>
 * Note: The Linux Flash Player fires a single uploadProgress event after the
 * entire file has been uploaded. This is a bug in the Linux Flash Player that
 * we cannot work around.
 * <p/>
 * See the documentation on the {@link DmUploadProgressEvent} class for more
 * details on the data available when a progress event occurs.
 *
 * @author squinn@moxiegroup.com (Shawn Quinn)
 */
public interface DmUploadProgressHandler {

    /**
     * This method is fired whenever a progress event occurs in the Uploader
     * component. See the {@link DmUploadProgressEvent} class for more details
     * on the data available when this event is fired.
     *
     * @param uploadProgressEvent The details of the event that occurred.
     * @return The response to send back to the event handler function
     */
    public boolean onUploadProgress(DmUploadProgressEvent uploadProgressEvent);

}
