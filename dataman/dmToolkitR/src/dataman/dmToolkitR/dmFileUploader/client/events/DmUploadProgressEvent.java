package dataman.dmToolkitR.dmFileUploader.client.events;

import dataman.dmToolkitR.dmFileUploader.client.DmFile;

/**
 * Provides access to the raw information provided by Uploader when an 'uploader
 * progress' event occurs. This class should not be instantiated directly, but
 * instead you should create a {@link UploadProgressHandler} and register it via
 * the
 * {@link org.moxieapps.gwt.uploader.client.Uploader#setUploadProgressHandler(UploadProgressHandler)}
 * method in order to access progress events.
 * <p/>
 * Note that the 'uploadProgress' event is fired periodically by the Flash
 * Control. This event is useful for providing UI updates on the page.
 * <p/>
 * Note: The Linux Flash Player fires a single uploadProgress event after the
 * entire file has been uploaded. This is a bug in the Linux Flash Player that
 * we cannot work around.
 *
 * @author squinn@moxiegroup.com (Shawn Quinn)
 */
public class DmUploadProgressEvent extends DmUploadEvent {

    private long bytesComplete;
    private long bytesTotal;

    /**
     * This constructor is intended for internal use only. You should not create
     * progress events directly, but instead should register a
     * {@link UploadProgressHandler}.
     *
     * @param file The native javascript object containing the details of the
     * file being uploaded.
     * @param bytesComplete Bytes transferred since the uploader process began.
     * @param bytesTotal Total size of the file being uploaded.
     */
    public DmUploadProgressEvent(DmFile file, long bytesComplete, long bytesTotal) {
        super(file);
        this.bytesComplete = bytesComplete;
        this.bytesTotal = bytesTotal;
    }

    /**
     * Return the bytes transferred since the uploader process began.
     *
     * @return double
     */
    public long getBytesComplete() {
        return bytesComplete;
    }

    /**
     * Return the total size of the file being uploaded.
     *
     * @return long
     */
    public long getBytesTotal() {
        return bytesTotal;
    }

}
