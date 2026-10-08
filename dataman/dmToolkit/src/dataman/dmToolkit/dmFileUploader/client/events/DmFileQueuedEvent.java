package dataman.dmToolkit.dmFileUploader.client.events;

import dataman.dmToolkit.dmFileUploader.client.DmFile;

/**
 * Provides access to the raw information provided by Uploader when a
 * 'fileQueued' event occurs. This class should not be instantiated directly,
 * but instead you should create a {@link FileQueuedHandler} and register it via
 * the
 * {@link org.moxieapps.gwt.uploader.client.Uploader#setFileQueuedHandler(FileQueuedHandler)}
 * method in order to access file queued events.
 * <p/>
 * Note that the 'fileQueued' event is fired for each file that is queued after
 * the File Selection Dialog window is closed.
 *
 * @author squinn@moxiegroup.com (Shawn Quinn)
 */
public class DmFileQueuedEvent {

    private DmFile file;

    /**
     * This constructor is intended for internal use only. You should not create
     * file queued events directly, but instead should register a
     * {@link FileQueuedHandler}.
     *
     * @param file The file instance that was added to the queue.
     */
    public DmFileQueuedEvent(DmFile file) {
        this.file = file;
    }

    /**
     * Return the details of the file that was added to the queue.
     *
     * @return File
     */
    public DmFile getFile() {
        return file;
    }
}
