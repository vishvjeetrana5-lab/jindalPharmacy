package dataman.dmToolkitR.dmFileUploader.client.events;

import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmToolkitR.dmFileUploader.client.DmFile;

/**
 * Provides access to the raw information provided by Uploader when an 'uploader
 * success' event occurs. This class should not be instantiated directly, but
 * instead you should create a {@link UploadSuccessHandler} and register it via
 * the
 * {@link org.moxieapps.gwt.uploader.client.Uploader#setUploadSuccessHandler(UploadSuccessHandler)}
 * method in order to access uploader success events.
 * <p/>
 * Note that 'uploadSuccess' is fired when the entire uploader has been
 * transmitted and the server returns a HTTP 200 status code. Any data outputted
 * by the server is available in the server data property.
 * <p/>
 * Due to some bugs in the Flash Player the server response may not be
 * acknowledged and no uploadSuccess event is fired by Flash. In this case the
 * {@link org.moxieapps.gwt.uploader.client.Uploader#setAssumeSuccessTimeout(long)}
 * setting is checked to see if enough time has passed to fire uploadSuccess
 * anyway. In this case the received response parameter will be false.
 * <p/>
 * The
 * {@link org.moxieapps.gwt.uploader.client.Uploader#setHttpSuccess(long...)}
 * setting allows uploadSuccess to be fired for HTTP status codes other than
 * 200. In this case no server data is available from the Flash Player.
 * <p/>
 * At this point the uploader is not yet complete. Another uploader cannot be
 * started from uploadSuccess.
 *
 * @author squinn@moxiegroup.com (Shawn Quinn)
 */
public class DmUploadSuccessEvent extends DmUploadEvent {

    private String serverData;
    private String responseReceived;

    private String serverfileLinkId;
    private String resourceAllocationId;
    private String fileName;
    private String ruleCode;
    private String clientFileLinkId;

    /**
     * This constructor is intended for internal use only. You should not create
     * uploader success events directly, but instead should register a
     * {@link UploadSuccessHandler}.
     *
     * @param file The native javascript object containing the details of the
     * file being uploaded.
     * @param serverData Any HTTP response data sent from the server at the
     * completion of the uploader.
     * @param responseReceived The HTTP response code sent from the server at
     * the completion of the uploader.
     */
    public DmUploadSuccessEvent(DmFile file, String serverData, String responseReceived) {
        super(file);
        this.serverData = serverData;
        this.responseReceived = responseReceived;
        assignServerValues();
    }

    private void assignServerValues() {
        if (!DmBaseShared.xNull(this.serverData).trim().equals("")) {
            String[] splitArray = this.serverData.split("\\|");
            this.resourceAllocationId = DmBaseShared.xNull(splitArray[0]).trim();
            this.clientFileLinkId = DmBaseShared.xNull(splitArray[1]).trim();
            this.serverfileLinkId = DmBaseShared.xNull(splitArray[2]).trim();
            this.fileName = DmBaseShared.xNull(splitArray[3]).trim();
            this.ruleCode = DmBaseShared.xNull(splitArray[4]).trim();
        }
    }

    /**
     * Return any HTTP response data sent from the server at the completion of
     * the uploader.
     *
     * @return String
     */
    public String getServerData() {
        return serverData;
    }

    /**
     * Return the HTTP response code sent from the server at the completion of
     * the uploader.
     *
     * @return String
     */
    public String getResponseReceived() {
        return responseReceived;
    }

    public String getResourceAllocationId() {
        return resourceAllocationId;
    }

    public String getServerfileLinkId() {
        return serverfileLinkId;
    }

    public String getClientFileLinkId() {
        return clientFileLinkId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getRuleCode() {
        return ruleCode;
    }

}
