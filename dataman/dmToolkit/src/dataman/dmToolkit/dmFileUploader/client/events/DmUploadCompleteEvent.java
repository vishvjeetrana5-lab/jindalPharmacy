package dataman.dmToolkit.dmFileUploader.client.events;

import dataman.dmToolkit.dmFileUploader.client.DmFile;

/**
 * Provides access to the raw information provided by Uploader when an 'uploader
 * complete' event occurs. This class should not be instantiated directly, but
 * instead you should create a {@link UploadCompleteHandler} and register it via
 * the
 * {@link org.moxieapps.gwt.uploader.client.Uploader#setUploadCompleteHandler(UploadCompleteHandler)}
 * method in order to access uploader complete events.
 * <p/>
 * Note that the 'uploadComplete' event is always fired at the end of an
 * uploader cycle (after uploadError or uploadSuccess). At this point the
 * uploader is complete and another uploader can be started.
 * <p/>
 * If you want the next uploader to start automatically this is a good place to
 * call this.uploadStart(). Use caution when calling uploadStart inside the
 * uploadComplete event if you also have code that cancels all the uploads in a
 * queue.
 *
 * @author squinn@moxiegroup.com (Shawn Quinn)
 */
public class DmUploadCompleteEvent extends DmUploadEvent {

    /**
     * This constructor is intended for internal use only. You should not create
     * uploader complete events directly, but instead should register a
     * {@link UploadCompleteHandler}.
     *
     * @param file The native javascript object containing the details of the
     * file being uploaded.
     */
    public DmUploadCompleteEvent(DmFile file) {
        super(file);
    }

}
