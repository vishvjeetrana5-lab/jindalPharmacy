package dataman.dmToolkit.dmFileUploader.client.events;

import dataman.dmToolkit.dmFileUploader.client.DmFile;

/**
 * The base class of all events that are triggered during the uploader process,
 * and includes methods for accessing the general state of the event, most
 * notably the details of the file that is being uploaded.
 *
 * @author squinn@moxiegroup.com (Shawn Quinn)
 */
public abstract class DmUploadEvent {

    private DmFile file;

    /**
     * We can only be created by instantiating one of our sub classes.
     *
     * @param file The native javascript object containing the details of the
     * file being uploaded.
     */
    protected DmUploadEvent(DmFile file) {
        this.file = file;
    }

    /**
     * Return the details of the file upon which the uploader event has
     * occurred.
     *
     * @return File
     */
    public DmFile getFile() {
        return file;
    }

}
