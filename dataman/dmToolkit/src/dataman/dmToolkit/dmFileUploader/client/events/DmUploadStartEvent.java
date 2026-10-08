package dataman.dmToolkit.dmFileUploader.client.events;

import dataman.dmToolkit.dmFileUploader.client.DmFile;

/**
 * Provides access to the raw information provided by Uploader when an 'uploader
 * start' event occurs. This class should not be instantiated directly, but
 * instead you should create a {@link UploadStartHandler} and register it via
 * the
 * {@link org.moxieapps.gwt.uploader.client.Uploader#setUploadStartHandler(UploadStartHandler)}
 * method in order to access uploader start events.
 * <p/>
 * Note that the 'uploadStart' event is called immediately before the file is
 * uploaded. This event provides an opportunity to perform any last minute
 * validation, add post params or do any other work before the file is uploaded.
 * <p/>
 * The uploader can be cancelled by returning 'false' from uploadStart in the
 * handler. If you return 'true' or do not return any value then the uploader
 * proceeds. Returning 'false' will cause an uploadError event to fired.
 *
 * @author squinn@moxiegroup.com (Shawn Quinn)
 */
public class DmUploadStartEvent extends DmUploadEvent {

    /**
     * This constructor is intended for internal use only. You should not create
     * uploader start events directly, but instead should register a
     * {@link UploadStartHandler}.
     *
     * @param file The native javascript object containing the details of the
     * file being uploaded.
     */
    public DmUploadStartEvent(DmFile file) {
        super(file);
    }

}
