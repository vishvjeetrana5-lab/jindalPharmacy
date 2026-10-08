package dataman.dmToolkit.dmFileUploader.client.events;

/**
 * Provides access to the raw information provided by the Uploader when an
 * 'swfUploadLoaded' event occurs. This class should not be instantiated
 * directly, but instead you should create a {@link SWFUploadLoadedHandler} and
 * register it via the
 * {@link org.moxieapps.gwt.uploader.client.Uploader#setSWFUploadLoadedHandler(SWFUploadLoadedHandler)}
 * <p/>
 * Note that the 'swfUploadLoaded' event is fired by the Uploader component's
 * internal 'flashReady' event to let you know that it is safe to call Uploader
 * methods. Also note that these events are currently only fired when the
 * Uploader component determines that the browser does not support the Ajax
 * upload technique and is therefore utilizing the SWFUpload/Flash technique for
 * handling the upload. If you need to determine which state the Uploader
 * component is in, you can make use of the
 * {@link org.moxieapps.gwt.uploader.client.Uploader#isAjaxUploadWithProgressEventsSupported()}
 * method.
 * <p/>
 *
 * @author squinn@moxiegroup.com (Shawn Quinn)
 */
public class DmSWFUploadLoadedEvent {

    /**
     * This constructor is intended for internal use only. You should not create
     * Uploader loaded events directly, but instead should register a
     * {@link SWFUploadLoadedHandler}.
     */
    public DmSWFUploadLoadedEvent() {
    }

}
