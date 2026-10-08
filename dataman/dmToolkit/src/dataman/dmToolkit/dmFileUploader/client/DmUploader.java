package dataman.dmToolkit.dmFileUploader.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.dom.client.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.Label;
import dataman.dmToolkit.dmFileUploader.client.events.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DmUploader extends AbsolutePanel {

    private static final int BYTES_PER_KILOBYTE = 1024;
    private static final int BYTES_PER_MEGABYTE = BYTES_PER_KILOBYTE * 1024;
    private static final int BYTES_PER_GIGABYTE = BYTES_PER_MEGABYTE * 1024;

    /**
     * An enumeration of supported button action types, which can be passed to
     * the {@link Uploader#setButtonAction(ButtonAction)} method. The button
     * action defines the action taken when the uploader button (text, image, or
     * flash) is clicked.
     */
    public enum ButtonAction {

        /**
         * Select a single file when the button is clicked
         */
        SELECT_FILE(-100),
        /**
         * Select multiple files when the button is clicked
         */
        SELECT_FILES(-110),
        /**
         * Begin the uploader process when the button is clicked
         */
        START_UPLOAD(-120);

        private ButtonAction(int optionValue) {
            this.optionValue = optionValue;
        }

        private final int optionValue;

        public int toInt() {
            return optionValue;
        }
    }

    /**
     * An enumeration of supported button cursor types, which can be passed to
     * the {@link Uploader#setButtonCursor(Cursor)} method. The button cursor is
     * used to define what type of mouse cursor is displayed when hovering over
     * the uploader button..
     */
    public enum Cursor {

        /**
         * Show the cursor as an arrow when the user hovers over the button
         */
        ARROW(-1),
        /**
         * Show the cursor as a hand when the user hovers over the button
         */
        HAND(-2);

        private Cursor(int optionValue) {
            this.optionValue = optionValue;
        }

        private final int optionValue;

        public int toInt() {
            return optionValue;
        }
    }

    /**
     * An enumeration of supported button window types, which can be passed to
     * the {@link Uploader#setButtonWindowMode(WindowMode)} method. The window
     * mode is used to set the WMODE property of the Flash Movie.
     */
    public enum WindowMode {

        /**
         * Set the flash movie in the "window" mode
         */
        WINDOW("window"),
        /**
         * Set the flash movie in the "transparent" mode
         */
        TRANSPARENT("transparent"),
        /**
         * Set the flash movie in the "opaque" mode
         */
        OPAQUE("opaque");

        private WindowMode(String optionValue) {
            this.optionValue = optionValue;
        }

        private final String optionValue;

        public String toString() {
            return optionValue;
        }
    }

    /**
     * Create a new Uploader component that can be added to a GWT application,
     * setting up an element in the DOM that the uploader button elements will
     * be rendered inside of.
     */
    public DmUploader() {
        super();
    }

    private String uploadURL;

    public DmUploader setUploadURL(String uploadURL) {
        this.uploadURL = uploadURL;
//        if (swfUpload != null) {
//            nativeSetUploadURL(swfUpload, uploadURL);
//        }
        return this.setOption("/upload_url", uploadURL);
    }

//    private static native void nativeSetUploadURL(JavaScriptObject swfUpload, String url) /*-{
//     swfUpload.setUploadURL(url);
//     }-*/;
    private String ajaxUploadURL;

    /**
     * By default both the XMLHttpRequest Level 2/Ajax uploads and the
     * SWFUpload/Flash uploads will be posted to whatever URL is specified via
     * the {@link #setUploadURL(String)} method. However, if you'd like the Ajax
     * uploads to be posted to a different URL than the SWF uploads, you can
     * call this method to set a unique URL for the Ajax uploads (and then only
     * the SWF uploads will use the general "uploadURL").
     *
     * @param ajaxUploadURL The URL that only the Ajax uploads should be posted
     * to (or null to uploader both the Ajax and SWF uploads to whatever the
     * {@link #setUploadURL(String)} option has been set to.)
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setAjaxUploadURL(String ajaxUploadURL) {
        this.ajaxUploadURL = ajaxUploadURL;
        return this.setOption("/ajax_upload_url", uploadURL);
    }

    private boolean ajaxUploadEnabled = true;

    /**
     * By default the component will attempt to use a DOM based Ajax uploader
     * process if it detects that the browser can support the necessary
     * requirements of the "XMLHttpRequest Level 2". And then, only if the
     * browser can not support the "XMLHttpRequest Level 2" mechanism will the
     * component failover to using the Flash based SWFUpload mechanism instead.
     * However, if you wish to only use the SWFUpload mechanism, then this
     * option can be disabled.
     *
     * @param ajaxUploadEnabled By default ajax uploader is enabled and will be
     * used as long as the browser supports the "XMLHttpRequest Level 2" object.
     * Setting this option to "false" will instead force the component to avoid
     * using the Ajax approach and instead utilize the SWFUpload/Flash technique
     * only.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setAjaxUploadEnabled(boolean ajaxUploadEnabled) {
        this.ajaxUploadEnabled = ajaxUploadEnabled;
        return this;
    }

    private String filePostName = null;

    /**
     * Convenience method for setting the 'file_post_name' option of the
     * component, either before or after the widget has been added to the DOM.
     * Equivalent to:
     * <pre><code>
     *     uploader.setOption("file_post_name", "Filedata");
     * </code></pre> The file_post_name allows you to set the value name used to
     * post the file. This is not related to the file name. The default value is
     * 'Filedata'. For maximum compatibility it is recommended that the default
     * value is used.
     *
     * @param filePostName The value to set as the 'file_post_name' option on
     * the Uploader component.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setFilePostName(String filePostName) {
//        if (swfUpload != null) {
//            nativeSetFilePostName(swfUpload, filePostName);
//        } else {
        this.filePostName = filePostName;
//        }
        return this.setOption("/file_post_name", filePostName);
    }

//    private static native void nativeSetFilePostName(JavaScriptObject swfUpload, String filePostName) /*-{
//     swfUpload.setFilePostName(filePostName);
//     }-*/;
    private JSONObject postParams = null;

    /**
     * Convenience method for setting the 'post_params' option of the component,
     * either before or after the widget has been added to the DOM. Equivalent
     * to:
     * <pre><code>
     *     JSONObject params = new JSONObject();
     *     params.put("post_param_name_1", new JSONString("post_param_value_1"));
     *     params.put("post_param_name_2", new JSONString("post_param_value_2"));
     *     params.put("post_param_name_n", new JSONString("post_param_value_n"));
     *     uploader.setOption("post_params", params);
     * </code></pre> The post_params setting defines the name/value pairs that
     * will be posted with each uploaded file. This setting accepts a simple
     * JavaScript object. Multiple post name/value pairs should be defined as
     * demonstrated in the sample settings object. Values must be either strings
     * or numbers (as interpreted by the JavaScript typeof function).
     * <p/>
     * Note: Flash Player 8 does not support sending additional post parameters.
     * SWFUpload will automatically send the post_params as part of the query
     * string.
     * <p/>
     *
     * @param postParams The value to set as the 'post_params' option on the
     * Uploader component.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setPostParams(JSONObject postParams) {
//        if (swfUpload != null) {
//            nativeSetPostParams(swfUpload, postParams != null ? postParams.getJavaScriptObject() : null);
//        } else {
        this.postParams = postParams;
//        }
        return this.setOption("/post_params", postParams);
    }

//    private static native void nativeSetPostParams(JavaScriptObject swfUpload, JavaScriptObject postParams) /*-{
//     swfUpload.setPostParams(postParams);
//     }-*/;
    /**
     * Convenience method for setting the 'use_query_string' option of the
     * component, either before or after the widget has been added to the DOM.
     * Equivalent to:
     * <pre><code>
     *     uploader.setOption("use_query_string", false);
     * </code></pre> The use_query_string setting may be true or false. This
     * value indicates whether Uploader should send the post_params and file
     * params on the query string or the post.
     * <p/>
     * Note that this option only applies when the SWFUpload/Flash component is
     * rendered. When the DHTML/Ajax mode is being used (the default) the params
     * are always passed as part of the post.
     *
     * @param useQueryString The value to set as the 'use_query_string' option
     * on the Uploader component.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setUseQueryString(boolean useQueryString) {
//        if (swfUpload != null) {
//            nativeSetUseQueryString(swfUpload, useQueryString);
//        }
        return this.setOption("/use_query_string", useQueryString);
    }

//    private static native void nativeSetUseQueryString(JavaScriptObject swfUpload, boolean useQueryString) /*-{
//     swfUpload.setUseQueryString(useQueryString);
//     }-*/;
    /**
     * Convenience method for setting the 'preserve_relative_urls' option of the
     * component. Equivalent to:
     * <pre><code>
     *     uploader.setOption("preserve_relative_urls", false);
     * </code></pre> A boolean value that indicates whether Uploader should
     * attempt to convert relative URLs used by the Flash Player to absolute
     * URLs. If set to true Uploader will not modify any URLs. The default value
     * is false.
     * <p/>
     * Note that this option only applies when the SWFUpload/Flash component is
     * rendered.
     *
     * @param preserveRelativeURLs The value to set as the
     * 'preserve_relative_urls' option on the Uploader component.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setPreserveRelativeURLs(boolean preserveRelativeURLs) {
        return this.setOption("/preserve_relative_urls", preserveRelativeURLs);
    }

    private boolean requeueOnError = false;

    /**
     * Convenience method for setting the 'requeue_on_error' option of the
     * component. Equivalent to:
     * <pre><code>
     *     uploader.setOption("requeue_on_error", true);
     * </code></pre> The requeue_on_error setting may be true or false. When
     * this setting is true any files that has an uploadError (excluding
     * fileQueue errors and the FILE_CANCELLED uploadError) is returned to the
     * front of the queue rather than being discarded. The file can be uploaded
     * again if needed. To remove the file from the queue the cancelUpload
     * method must be called.
     * <p/>
     * All the events associated with a failed uploader are still called and so
     * the requeuing the failed uploader can conflict with the Queue Plugin (or
     * custom code that uploads the entire queue). Code that automatically
     * uploads the next file in the queue will uploader the failed file over and
     * over again if care is not taken to allow the failing uploader to be
     * cancelled.
     *
     * @param requeueOnError The value to set as the 'requeue_on_error' option
     * on the Uploader component (default: false).
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setRequeueOnError(boolean requeueOnError) {
        this.requeueOnError = requeueOnError;
        return this.setOption("/requeue_on_error", requeueOnError);
    }

    private long[] httpSuccess;

    /**
     * Convenience method for setting the 'http_success' option of the
     * component. Equivalent to:
     * <pre><code>
     *     uploader.setOption("http_success", new Long[] { 200, 203 });
     * </code></pre> An array that defines the HTTP Status Codes that will
     * trigger success. 200 is always a success. Also, only the 200 status code
     * provides the serverData.
     * <p/>
     * When returning and accepting an HTTP Status Code other than 200 it is not
     * necessary for the server to return content.
     *
     * @param httpSuccess The values to set as the HTTP response codes to treat
     * as a successful response (default: 200).
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setHttpSuccess(long... httpSuccess) {
        this.httpSuccess = httpSuccess;
        return this.setOption("/http_success", httpSuccess);
    }

    /**
     * Convenience method for setting the 'assume_success_timeout' option of the
     * component. Equivalent to:
     * <pre><code>
     *     uploader.setOption("assume_success_timeout", 1000);
     * </code></pre> The number of seconds SWFUpload should wait for Flash to
     * detect the server's response after the file has finished uploading. This
     * setting allows you to work around the Flash Player bugs where long
     * running server side scripts causes Flash to ignore the server response or
     * the Mac Flash Player bug that ignores server responses with no content.
     * <p/>
     * Testing has shown that Flash will ignore server responses that take
     * longer than 30 seconds after the last uploadProgress event.
     * <p/>
     * A timeout of zero (0) seconds disables this feature and is the default
     * value. SWFUpload will wait indefinitely for the Flash Player to trigger
     * the uploadSuccess event.
     * <p/>
     * Note that this option only applies when the SWFUpload/Flash component is
     * rendered.
     *
     * @param assumeSuccessTimeout The value to set as the
     * 'assume_success_timeout' option on the Uploader component.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setAssumeSuccessTimeout(long assumeSuccessTimeout) {
        return this.setOption("/assume_success_timeout", assumeSuccessTimeout);
    }

    private String fileTypes;

    /**
     * Convenience method for setting the 'file_types' option of the component,
     * either before or after the widget has been added to the DOM. Equivalent
     * to:
     * <pre><code>
     *     uploader.setOption("file_types", "*.jpg;*.gif");
     * </code></pre> The file_types setting accepts a semi-colon separated list
     * of file extensions that are allowed to be selected by the user. Use '*.*'
     * to allow all file types.
     *
     * @param fileTypes A semi-colon delimited list of file types that the file
     * dialog should allow the user to select.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setFileTypes(String fileTypes) {
        this.fileTypes = fileTypes;
//        if (swfUpload != null && this.fileTypes != null && this.fileTypesDescription != null) {
//            nativeSetFileTypes(swfUpload, this.fileTypes, this.fileTypesDescription);
//        }
        return this.setOption("/file_types", fileTypes);
    }

    private String fileTypesDescription;

    /**
     * Convenience method for setting the 'file_types_description' option of the
     * component, either before or after the widget has been added to the DOM.
     * Equivalent to:
     * <pre><code>
     *     uploader.setOption("file_types_description", "Web Photos");
     * </code></pre> A text description that is displayed to the user in the
     * File Browser dialog.
     *
     * @param fileTypesDescription A text description that is displayed to the
     * user in the File Browser dialog.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setFileTypesDescription(String fileTypesDescription) {
        // TODO: Add Ajax mode support for this option
        this.fileTypesDescription = fileTypesDescription;
//        if (swfUpload != null && this.fileTypes != null && this.fileTypesDescription != null) {
//            nativeSetFileTypes(swfUpload, this.fileTypes, this.fileTypesDescription);
//        }
        return this.setOption("/file_types_description", fileTypesDescription);
    }

//    private static native void nativeSetFileTypes(JavaScriptObject swfUpload, String fileTypes, String fileTypesDescription) /*-{
//     swfUpload.setFileTypes(fileTypes, fileTypesDescription);
//     }-*/;
    private long fileSizeLimit = 0;

    /**
     * Convenience method for setting the 'file_size_limit' option of the
     * component, either before or after the widget has been added to the DOM.
     * Equivalent to:
     * <pre><code>
     *     uploader.setOption("file_size_limit", "5 MB");
     * </code></pre> The file_size_limit setting defines the maximum allowed
     * size of a file to be uploaded. This setting accepts a value and unit.
     * Valid units are B, KB, MB and GB.
     * <p/>
     * Examples: 2147483648 B, 2097152, 2097152KB, 2048 MB, 2 GB
     * <p/>
     * If the unit is omitted default is KB. A value of 0 (zero) is interpreted
     * as unlimited.
     * <p/>
     * Note: This setting only applies to the user's browser. It does not affect
     * any settings or limits on the web server.
     *
     * @param fileSizeLimit The maximum allowed size of a file to be uploaded.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setFileSizeLimit(String fileSizeLimit) {
        if (fileSizeLimit != null && fileSizeLimit.length() > 0 && Character.isDigit(fileSizeLimit.charAt(0))) {
            String units = "";
            int i = 0;

            while (i < fileSizeLimit.length() && Character.isDigit(fileSizeLimit.charAt(i))) {
                i++;
            }
            if (i < fileSizeLimit.length() - 1) {
                // Get the units
                units = fileSizeLimit.substring(i).trim();
            }
            long size = Long.parseLong(fileSizeLimit.substring(0, i));

            if ("".equalsIgnoreCase(units)) {
                // Default to KB
                this.fileSizeLimit = size * BYTES_PER_KILOBYTE;
            } else if ("B".equalsIgnoreCase(units)) {
                this.fileSizeLimit = size;
            } else if ("KB".equalsIgnoreCase(units)) {
                this.fileSizeLimit = size * BYTES_PER_KILOBYTE;
            } else if ("MB".equalsIgnoreCase(units)) {
                this.fileSizeLimit = size * BYTES_PER_MEGABYTE;
            } else if ("GB".equalsIgnoreCase(units)) {
                this.fileSizeLimit = size * BYTES_PER_GIGABYTE;
            } else {
                // Default to unlimited
                this.fileSizeLimit = 0;
            }
        }

//        if (swfUpload != null) {
//            nativeSetFileSizeLimit(swfUpload, fileSizeLimit);
//        }
        return this.setOption("/file_size_limit", fileSizeLimit);
    }

//    private static native void nativeSetFileSizeLimit(JavaScriptObject swfUpload, String fileSizeLimit) /*-{
//     swfUpload.setFileSizeLimit(fileSizeLimit);
//     }-*/;
    private long fileUploadLimit = 0;
    private long totalFilesUploaded = 0;

    /**
     * Convenience method for setting the 'file_upload_limit' option of the
     * component, either before or after the widget has been added to the DOM.
     * Equivalent to:
     * <pre><code>
     *     uploader.setOption("file_upload_limit", 10);
     * </code></pre> Defines the number of files allowed to be uploaded by the
     * uploader component. This setting also sets the upper bound of the
     * file_queue_limit setting. Once the user has uploaded or queued the
     * maximum number of files she will no longer be able to queue additional
     * files. The value of 0 (zero) is interpreted as unlimited. Only successful
     * uploads (uploads the trigger the uploadSuccess event) are counted toward
     * the uploader limit.
     * <p/>
     * Note: This value is not tracked across pages and is reset when a page is
     * refreshed. File quotas should be managed by the web server.
     *
     * @param fileUploadLimit The number of files allowed to be uploaded by the
     * uploader component.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setFileUploadLimit(long fileUploadLimit) {
        this.fileUploadLimit = fileUploadLimit;
//        if (swfUpload != null) {
//            nativeSetFileUploadLimit(swfUpload, fileUploadLimit);
//        }
        return this.setOption("/file_upload_limit", fileUploadLimit);
    }

//    private static native void nativeSetFileUploadLimit(JavaScriptObject swfUpload, double fileUploadLimit) /*-{
//     swfUpload.setFileUploadLimit(fileUploadLimit);
//     }-*/;
    private long fileQueueLimit = Long.MAX_VALUE;

    /**
     * Convenience method for setting the 'file_queue_limit' option of the
     * component, either before or after the widget has been added to the DOM.
     * Equivalent to:
     * <pre><code>
     *     uploader.setOption("file_queue_limit", 10);
     * </code></pre> Defines the number of unprocessed files allowed to be
     * simultaneously queued. Once a file is uploaded, errored, or cancelled new
     * files can be queued in its place until the queue limit has been reached.
     * If the uploader limit (or remaining uploads allowed) is less than the
     * queue limit then the lower number is used.
     *
     * @param fileQueueLimit The number of unprocessed files allowed to be
     * simultaneously queued.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setFileQueueLimit(long fileQueueLimit) {
        this.fileQueueLimit = fileQueueLimit;
//        if (swfUpload != null) {
//            nativeSetFileQueueLimit(swfUpload, fileQueueLimit);
//        }
        return this.setOption("/file_queue_limit", fileQueueLimit);
    }

//    private static native void nativeSetFileQueueLimit(JavaScriptObject swfUpload, double fileQueueLimit) /*-{
//     swfUpload.setFileQueueLimit(fileQueueLimit);
//     }-*/;
//    private String flashURL;
    /**
     * Overrides the default "flash_url" setting. Normally you'll want to leave
     * this setting alone as the "uploader.swf" path will be automatically
     * determined for you based on the GWT module path. However, if you've
     * deployed the "uploader.swf" somewhere else (or on a different domain)
     * then you can set the URL to the file here in order to override the
     * default internal logic.
     * <p/>
     * The full, absolute, or relative URL to the Flash Control swf file. This
     * setting cannot be changed once the Uploader has been instantiated.
     * Relative URLs are relative to the page URL.
     * <p/>
     * Note that this option only applies when the SWFUpload/Flash component is
     * rendered.
     *
     * @param flashURL The value to set as the 'flash_url' option on the
     * Uploader component.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
//    public DmUploader setFlashURL(String flashURL) {
//        // Purposefully treating this option separately, instead of storing it in the "Configuration"
//        // instance, see the logic in the createNativeOptions() method for details...
//        this.flashURL = flashURL;
//        return this;
//    }
    /**
     * Convenience method for setting the 'prevent_swf_caching' option of the
     * component. Equivalent to:
     * <pre><code>
     *     uploader.setOption("prevent_swf_caching", false);
     * </code></pre> This boolean setting indicates whether a random value
     * should be added to the Flash URL in an attempt to prevent the browser
     * from caching the SWF movie. This works around a bug in some IE-engine
     * based browsers.
     * <p/>
     * Note: The algorithm for adding the random number to the URL is dumb and
     * cannot handle URLs that already have some parameters.
     * <p/>
     * Note that this option only applies when the SWFUpload/Flash component is
     * rendered.
     *
     * @param preventSWFCaching The value to set as the 'prevent_swf_caching'
     * option on the Uploader component.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setPreventSWFCaching(boolean preventSWFCaching) {
        return this.setOption("/prevent_swf_caching", preventSWFCaching);
    }

    /**
     * Convenience method for setting the 'debug' option of the component.
     * Equivalent to:
     * <pre><code>
     *     uploader.setOption("debug", false);
     * </code></pre> A boolean value that defines whether the debug event
     * handler should be fired.
     * <p/>
     * Note that this option only applies when the SWFUpload/Flash component is
     * rendered.
     *
     * @param debug The value to set as the 'debug' option on the Uploader
     * component.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setDebug(boolean debug) {
        return this.setOption("/debug", debug);
    }

    private String buttonImageURL;

    public DmUploader setButtonImageURL(String buttonImageURL) {
        this.buttonImageURL = buttonImageURL;
//        if (swfUpload != null) {
//            nativeSetButtonImageURL(swfUpload, buttonImageURL);
//        }
        return this.setOption("/button_image_url", buttonImageURL);
    }

//    private static native void nativeSetButtonImageURL(JavaScriptObject swfUpload, String buttonImageURL) /*-{
//     swfUpload.setButtonImageURL(buttonImageURL);
//     }-*/;
    private int buttonWidth = -1;

    /**
     * Convenience method for setting the 'button_width' option of the
     * component, either before or after the widget has been added to the DOM.
     * Equivalent to:
     * <pre><code>
     *     uploader.setOption("button_width", 62);
     * </code></pre> A number defining the width of the uploader button.
     *
     * @param buttonWidth The value to set as width of the uploader button.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setButtonWidth(int buttonWidth) {
        this.buttonWidth = buttonWidth;
        if (this.buttonWidth >= 0 && this.buttonHeight >= 0) {
            this.setWidth(this.buttonWidth + "px");
            this.setHeight(this.buttonHeight + "px");
//            if (swfUpload != null) {
//                nativeSetButtonDimensions(swfUpload, this.buttonWidth, this.buttonHeight);
//            }
        }
        return this.setOption("/button_width", buttonWidth);
    }

    private int buttonHeight = -1;

    /**
     * Convenience method for setting the 'button_height' option of the
     * component, either before or after the widget has been added to the DOM.
     * Equivalent to:
     * <pre><code>
     *     uploader.setOption("button_height", 21);
     * </code></pre> A number defining the height of the uploader button.
     *
     * @param buttonHeight The value to set as height of the uploader button.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setButtonHeight(int buttonHeight) {
        this.buttonHeight = buttonHeight;
        if (this.buttonWidth >= 0 && this.buttonHeight >= 0) {
            this.setWidth(this.buttonWidth + "px");
            this.setHeight(this.buttonHeight + "px");
//            if (swfUpload != null) {
//                nativeSetButtonDimensions(swfUpload, this.buttonWidth, this.buttonHeight);
//            }
        }
        return this.setOption("/button_height", buttonHeight);
    }

//    private static native void nativeSetButtonDimensions(JavaScriptObject swfUpload, int buttonWidth, int buttonHeight) /*-{
//     swfUpload.setButtonDimensions(buttonWidth, buttonHeight);
//     }-*/;
    private String buttonText;
    private DivElement buttonTextElement;
    private DivElement buttonImageElement;

    public DmUploader setButtonText(String buttonText) {
        this.buttonText = buttonText;
//        if (swfUpload != null) {
//            nativeSetButtonText(swfUpload, buttonText);
//        }
        if (buttonTextElement != null) {
            buttonTextElement.setInnerHTML(buttonText);
        }
        return this.setOption("/button_text", buttonText);
    }

//    private static native void nativeSetButtonText(JavaScriptObject swfUpload, String buttonText) /*-{
//     swfUpload.setButtonText(buttonText);
//     }-*/;
    private String buttonTextStyle;
    private StyleElement buttonTextStyleElement;

    public DmUploader setButtonTextStyle(String buttonTextStyle) {
        this.buttonTextStyle = buttonTextStyle;
//        if (swfUpload != null) {
//            nativeSetButtonTextStyle(swfUpload, buttonTextStyle);
//        }
        if (buttonTextStyleElement != null) {
            buttonTextStyleElement.setInnerText(buttonTextStyle);
        }
        return this.setOption("/button_text_style", buttonTextStyle);
    }

//    private static native void nativeSetButtonTextStyle(JavaScriptObject swfUpload, String buttonTextStyle) /*-{
//     swfUpload.setButtonTextStyle(buttonTextStyle);
//     }-*/;
    private int buttonTextTopPadding = Integer.MIN_VALUE;

    /**
     * Convenience method for setting the 'button_text_top_padding' option of
     * the component, either before or after the widget has been added to the
     * DOM. Equivalent to:
     * <pre><code>
     *     uploader.setOption("button_text_top_padding", 2);
     * </code></pre> Used to vertically position the button text within the
     * uploader component. Negative values may be used.
     *
     * @param buttonTextTopPadding The amount of padding to include between the
     * outer bounds of the uploader component and the top of the text.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setButtonTextTopPadding(int buttonTextTopPadding) {
        this.buttonTextTopPadding = buttonTextTopPadding;
//        if (swfUpload != null && this.buttonTextLeftPadding > Integer.MIN_VALUE && this.buttonTextTopPadding > Integer.MIN_VALUE) {
//            nativeSetButtonTextPadding(swfUpload, this.buttonTextLeftPadding, this.buttonTextTopPadding);
//        }
        return this.setOption("/button_text_top_padding", buttonTextTopPadding);
    }

    private int buttonTextLeftPadding = Integer.MIN_VALUE;

    /**
     * Convenience method for setting the 'button_text_left_padding' option of
     * the component, either before or after the widget has been added to the
     * DOM. Equivalent to:
     * <pre><code>
     *     uploader.setOption("button_text_left_padding", 2);
     * </code></pre> Used to horizontally position the button text within the
     * uploader component. Negative values may be used.
     *
     * @param buttonTextLeftPadding The amount of padding to include between the
     * outer bounds of the uploader component and the left of the text.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setButtonTextLeftPadding(int buttonTextLeftPadding) {
        this.buttonTextLeftPadding = buttonTextLeftPadding;
//        if (swfUpload != null && this.buttonTextLeftPadding > Integer.MIN_VALUE && this.buttonTextTopPadding > Integer.MIN_VALUE) {
//            nativeSetButtonTextPadding(swfUpload, this.buttonTextLeftPadding, this.buttonTextTopPadding);
//        }
        return this.setOption("/button_text_left_padding", buttonTextLeftPadding);
    }

//    private static native void nativeSetButtonTextPadding(JavaScriptObject swfUpload, int buttonTextLeftPadding, int buttonTextTopPadding) /*-{
//     swfUpload.setButtonTextPadding(buttonTextLeftPadding, buttonTextTopPadding);
//     }-*/;
    private ButtonAction buttonAction = ButtonAction.SELECT_FILES;

    /**
     * Convenience method for setting the 'button_action' option of the
     * component, either before or after the widget has been added to the DOM.
     * Equivalent to:
     * <pre><code>
     *     uploader.setOption("button_action", ButtonAction.SELECT_FILES);
     * </code></pre> Defines the action to take when the uploader component is
     * clicked.
     *
     * @param buttonAction The action to take when the uploader component is
     * clicked.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setButtonAction(ButtonAction buttonAction) {
        this.buttonAction = buttonAction;
//        if (swfUpload != null && buttonAction != null) {
//            nativeSetButtonAction(swfUpload, buttonAction.toInt());
//        }
        return this.setOption("/button_action", buttonAction != null ? buttonAction.toInt() : null);
    }

//    private static native void nativeSetButtonAction(JavaScriptObject swfUpload, int buttonAction) /*-{
//     swfUpload.setButtonAction(buttonAction);
//     }-*/;
    private boolean buttonDisabled = false;

    /**
     * Convenience method for setting the 'button_disabled' option of the
     * component, either before or after the widget has been added to the DOM.
     * Equivalent to:
     * <pre><code>
     *     uploader.setOption("button_disabled", false);
     * </code></pre> A boolean value that sets whether the uploader button is in
     * the disabled state. When in the disabled state the button will not
     * execute any actions.
     *
     * @param buttonDisabled Whether the uploader button is in the disabled
     * state.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setButtonDisabled(boolean buttonDisabled) {
        this.buttonDisabled = buttonDisabled;
//        if (swfUpload != null) {
//            nativeSetButtonDisabled(swfUpload, buttonDisabled);
//        }
        if (buttonImageElement != null && buttonHeight >= 0) {
            if (buttonDisabled) {
                buttonImageElement.getStyle().setProperty("backgroundPosition", "0px -" + (buttonHeight * 3) + "px");
            } else {
                buttonImageElement.getStyle().setProperty("backgroundPosition", "0px 0px");
            }
        }
        return this.setOption("/button_disabled", buttonDisabled);
    }

    /**
     * Returns whether or not the button has been set in a disabled state via
     * the {@link #setButtonDisabled(boolean)} method.
     *
     * @return 'true' if the button has been disabled (e.g. grayed out), or
     * 'false' if the button is still enabled.
     */
    public boolean getButtonDisabled() {
        return this.buttonDisabled;
    }

//    private static native void nativeSetButtonDisabled(JavaScriptObject swfUpload, boolean buttonDisabled) /*-{
//     swfUpload.setButtonDisabled(buttonDisabled);
//     }-*/;
    private Cursor buttonCursor;

    /**
     * Convenience method for setting the 'button_cursor' option of the
     * component, either before or after the widget has been added to the DOM.
     * Equivalent to:
     * <pre><code>
     *     uploader.setOption("button_cursor", Cursor.HAND);
     * </code></pre> Used to define what type of mouse cursor is displayed when
     * hovering over the uploader component.
     *
     * @param cursor The type of cursor to display when the mouse is hovering
     * over the uploader component.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setButtonCursor(Cursor cursor) {
        buttonCursor = cursor;
//        if (swfUpload != null && cursor != null) {
//            nativeSetButtonCursor(swfUpload, cursor.toInt());
//        }
        return this.setOption("/button_cursor", cursor != null ? cursor.toInt() : null);
    }

//    private static native void nativeSetButtonCursor(JavaScriptObject swfUpload, int buttonCursor) /*-{
//     swfUpload.setButtonCursor(buttonCursor);
//     }-*/;
    /**
     * Convenience method for setting the 'button_window_mode' option of the
     * component. Equivalent to:
     * <pre><code>
     *     uploader.setOption("button_window_mode", WindowMode.TRANSPARENT);
     * </code></pre> Sets the WMODE property of the Flash Movie.
     * <p/>
     * Note that this option only applies when the SWFUpload/Flash component is
     * rendered.
     *
     * @param buttonWindowMode The value to set as the 'button_window_mode'
     * option on the Uploader component.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setButtonWindowMode(WindowMode buttonWindowMode) {
        return this.setOption("/button_window_mode", buttonWindowMode != null ? buttonWindowMode.toString() : null);
    }

    private JSONObject customSettings;

    /**
     * Convenience method for setting the 'custom_settings' option of the
     * component. Equivalent to:
     * <pre><code>
     *     JSONObject settings = new JSONObject();
     *     settings.put("My Setting", new JSONString("This is my setting"));
     *     settings.put("myothersetting", new JSONString("This is my other setting"));
     *     settings.put("integer_setting", new JSONNumber(100));
     *     uploader.setOption("custom_settings", settings);
     * </code></pre> The custom_settings setting allows developers to safely
     * attach additional information to a uploader instance without worrying
     * about affecting internal values or changes in new GWT Uploader (or
     * SWFUpload) versions. This setting accepts a JSON object.
     * </p>
     * Once instantiated the custom settings are accessed via the
     * {@link #getCustomSettings()} method.
     *
     * @param customSettings The value to set as the 'custom_settings' option on
     * the uploader component.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setCustomSettings(JSONObject customSettings) {
        this.customSettings = customSettings;
        return this.setOption("/custom_settings", customSettings);
    }

    /**
     * Returns the custom settings that were stored on this uploader component
     * instance via the {@link #setCustomSettings(JSONObject)} method. The
     * custom_settings setting allows developers to safely attach additional
     * information to a uploader instance without worrying about affecting
     * internal values or changes in new GWT Uploader (or SWFUpload) versions.
     *
     * @return JSONObject
     */
    public JSONObject getCustomSettings() {
        return customSettings;
    }

    private DmStats stats;

    /**
     * Returns the overall general statistics being tracked by the Uploader
     * component, or null if the widget has not yet been added to the DOM.
     *
     * @return Stats
     */
    public DmStats getStats() {
        if (stats == null) {
            stats = (DmStats) JavaScriptObject.createObject();
        }
        return stats;
    }

    private DmSWFUploadLoadedHandler swfUploadLoadedHandler;

    /**
     * Set a callback handler that will be invoked whenever a Uploader loaded
     * event is fired.
     * <p/>
     * Note that this option only applies when the SWFUpload/Flash component is
     * rendered.
     *
     * @param swfUploadLoadedHandler The handler that should be invoked whenever
     * a Uploader loaded event occurs.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setSWFUploadLoadedHandler(DmSWFUploadLoadedHandler swfUploadLoadedHandler) {
        this.swfUploadLoadedHandler = swfUploadLoadedHandler;
        return this;
    }

    private DmFileDialogStartHandler fileDialogStartHandler;

    /**
     * Set a callback handler that will be invoked whenever a file dialog start
     * event is fired.
     *
     * @param fileDialogStartHandler The handler that should be invoked whenever
     * a file dialog start event occurs.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setFileDialogStartHandler(DmFileDialogStartHandler fileDialogStartHandler) {
        this.fileDialogStartHandler = fileDialogStartHandler;
        return this;
    }

    private DmFileQueuedHandler fileQueuedHandler;

    /**
     * Set a callback handler that will be invoked whenever a file queued event
     * is fired.
     *
     * @param fileQueuedHandler The handler that should be invoked whenever a
     * file queued event occurs.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setFileQueuedHandler(DmFileQueuedHandler fileQueuedHandler) {
        this.fileQueuedHandler = fileQueuedHandler;
        return this;
    }

    private DmFileQueueErrorHandler fileQueueErrorHandler;

    /**
     * Set a callback handler that will be invoked whenever a file queue error
     * event is fired.
     *
     * @param fileQueueErrorHandler The handler that should be invoked whenever
     * a file queue error event occurs.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setFileQueueErrorHandler(DmFileQueueErrorHandler fileQueueErrorHandler) {
        this.fileQueueErrorHandler = fileQueueErrorHandler;
        return this;
    }

    private DmFileDialogCompleteHandler fileDialogCompleteHandler;

    /**
     * Set a callback handler that will be invoked whenever a file dialog
     * complete event is fired.
     *
     * @param fileDialogCompleteHandler The handler that should be invoked
     * whenever a file dialog complete event occurs.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setFileDialogCompleteHandler(DmFileDialogCompleteHandler fileDialogCompleteHandler) {
        this.fileDialogCompleteHandler = fileDialogCompleteHandler;
        return this;
    }

    private DmUploadCompleteHandler uploadCompleteHandler;

    /**
     * Set a callback handler that will be invoked whenever an uploader complete
     * event is fired.
     *
     * @param uploadCompleteHandler The handler that should be invoked whenever
     * an uploader complete event occurs.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setUploadCompleteHandler(DmUploadCompleteHandler uploadCompleteHandler) {
        this.uploadCompleteHandler = uploadCompleteHandler;
        return this;
    }

    private DmUploadErrorHandler uploadErrorHandler;

    /**
     * Set a callback handler that will be invoked whenever an uploader error
     * event is fired.
     *
     * @param uploadErrorHandler The handler that should be invoked whenever an
     * uploader error event occurs.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setUploadErrorHandler(DmUploadErrorHandler uploadErrorHandler) {
        this.uploadErrorHandler = uploadErrorHandler;
        return this;
    }

    private DmUploadProgressHandler uploadProgressHandler;

    /**
     * Set a callback handler that will be invoked whenever an uploader progress
     * event is fired.
     *
     * @param uploadProgressHandler The handler that should be invoked whenever
     * an uploader progress event occurs.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setUploadProgressHandler(DmUploadProgressHandler uploadProgressHandler) {
        this.uploadProgressHandler = uploadProgressHandler;
        return this;
    }

    private DmUploadStartHandler uploadStartHandler;

    /**
     * Set a callback handler that will be invoked whenever an uploader start
     * event is fired.
     *
     * @param uploadStartHandler The handler that should be invoked whenever an
     * uploader start event occurs.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setUploadStartHandler(DmUploadStartHandler uploadStartHandler) {
        this.uploadStartHandler = uploadStartHandler;
        return this;
    }

    private DmUploadSuccessHandler uploadSuccessHandler;

    /**
     * Set a callback handler that will be invoked whenever an uploader success
     * event is fired.
     *
     * @param uploadSuccessHandler The handler that should be invoked whenever
     * an uploader success event occurs.
     * @return A reference to this {@link DmUploader} instance for convenient
     * method chaining.
     */
    public DmUploader setUploadSuccessHandler(DmUploadSuccessHandler uploadSuccessHandler) {
        this.uploadSuccessHandler = uploadSuccessHandler;
        return this;
    }

    // Delegate responsibility for managing configuration options to an anonymous helper class
    private DmConfigurable configurable = new DmConfigurable() {
    };

    public DmUploader setOption(String path, Object value) {
        configurable.setOption(path, value);
        return this;
    }

    // The current XMLHttpRequest in progress (if any), which we'll need a reference to in case the
    // user attempts to cancel an in progress upload.
    private JavaScriptObject lastXMLHttpRequest = null;

    /**
     * Cause the first file in the queue to start the uploader process
     */
    public void startUpload() {

        if (uploadURL == null) {
            throw new IllegalStateException("The 'startUpload()' method was invoked before the uploader URL was provided.  Please call the 'setUploadURL' first.");
        }

        if (nativeFilesQueued.size() > 0) {

            for (int i = 0; i < nativeFilesQueued.size(); i++) {

                JavaScriptObject nativeFile = nativeFilesQueued.get(i);

                // Initialize properties on Start
                nativeSetProperty(nativeFile, "startTime", System.currentTimeMillis());
                nativeSetProperty(nativeFile, "timeSinceLastEvent", System.currentTimeMillis());

                // SWFUpload isn't in play, so we need to keep our global stats up to date manually
                nativeSetProperty(getStats(), "in_progress", 1);

                // The SWFUploader component will automatically invoke the UploadStartHandler, but we need to fire it
                // manually for the Ajax/XMLHttpRequest Level 2 case
                nativeUpdateFileProperties(nativeFile, DmFile.Status.IN_PROGRESS.toInt());
                if (uploadStartHandler != null) {
                    uploadStartHandler.onUploadStart(
                            new DmUploadStartEvent(nativeFile.<DmFile>cast())
                    );
                }

                // Let any registered progress handlers know that we're starting at the beginning
                uploadProgressEventCallback(nativeFile.<DmFile>cast(), 0.0, (double) nativeFile.<DmFile>cast().getSize());

                nativeStartAjaxUpload(nativeFile,
                        ajaxUploadURL != null ? ajaxUploadURL : uploadURL,
                        filePostName != null ? filePostName : "FileData",
                        postParams != null ? postParams.getJavaScriptObject() : null
                );

            }
        }
    }

    private native JavaScriptObject nativeStartAjaxUpload(JavaScriptObject file, String url, String filePostName, JavaScriptObject postParams) /*-{

        var self = this;

        var xhr = new XMLHttpRequest();
            
        // Setup the event handlers we'll need to let the consuming application know what's going on
        xhr.upload.addEventListener('progress', function(e) {
           return self.@dataman.dmToolkit.dmFileUploader.client.DmUploader::uploadProgressEventCallback(Ldataman/dmToolkit/dmFileUploader/client/DmFile;DD)(file, e.loaded, e.total);
        }, false);

        xhr.addEventListener('load', function() {
           return self.@dataman.dmToolkit.dmFileUploader.client.DmUploader::uploadSuccessEventCallback(Ldataman/dmToolkit/dmFileUploader/client/DmFile;Ljava/lang/String;Ljava/lang/String;)(
               @dataman.dmToolkit.dmFileUploader.client.DmUploader::nativeUpdateFileProperties(Lcom/google/gwt/core/client/JavaScriptObject;I)(
           file,
           -4      // DmFile.Status.COMPLETE
           ),
           xhr.status + "",
           xhr.responseText
           )
        }, false);

        xhr.addEventListener('error', function() {
           return self.@dataman.dmToolkit.dmFileUploader.client.DmUploader::uploadErrorEventCallback(Ldataman/dmToolkit/dmFileUploader/client/DmFile;ILjava/lang/String;)(
               @dataman.dmToolkit.dmFileUploader.client.DmUploader::nativeUpdateFileProperties(Lcom/google/gwt/core/client/JavaScriptObject;I)(
           file,
           -3      // DmFile.Status.ERROR
           ),
           -250, // UploadErrorEvent.ErrorCode.UPLOAD_FAILED
           xhr.responseText
           )
        }, false);

        xhr.addEventListener('abort', function() {
            return self.@dataman.dmToolkit.dmFileUploader.client.DmUploader::uploadErrorEventCallback(Ldataman/dmToolkit/dmFileUploader/client/DmFile;ILjava/lang/String;)(
                @dataman.dmToolkit.dmFileUploader.client.DmUploader::nativeUpdateFileProperties(Lcom/google/gwt/core/client/JavaScriptObject;I)(
            file,
            -5      // DmFile.Status.CANCELLED
            ),
            -280, // UploadErrorEvent.ErrorCode.FILE_CANCELLED
            "Upload Aborted"
            )
        }, false);
            
            
        xhr.open('POST', url, true);
        var formData = new FormData();            
           
        formData.append('fileId',file.serverId);
        formData.append(filePostName, file);        
        
        //append any port params
        if(postParams != null) {
            for (var key in postParams) {
                formData.append(key, postParams[key]);
            }
        }

        // Kick off the multipart/form-data upload
        xhr.send(formData);            
            
        return xhr;
    }-*/;
    
    /**
     * Cancel the file upload for a specific file and remove from the queue.
     *
     * @param fileId The fileId to cancel
     * @param triggerErrorEvent if true, an uploadError event will be issued
     */
    public void cancelUpload(String fileId, boolean triggerErrorEvent) {
//        if (swfUpload != null) {
//            nativeCancelUpload(swfUpload, fileId, triggerErrorEvent);
//        } else {
        JavaScriptObject nativeFile = nativeFilesQueuedById.get(fileId);
        if (nativeFile != null) {

            boolean cancelledActiveUpload = false;

            // If the file being cancelled was currently in the process of being uploaded, then cancel the current XMLHttpRequest as well
            if (nativeFilesQueued.size() > 0 && lastXMLHttpRequest != null) {
                final DmFile currentFile = nativeFilesQueued.get(0).cast();
                if (nativeFile.<DmFile>cast().getId().equals(currentFile.getId()) && currentFile.getStatus() == DmFile.Status.IN_PROGRESS) {
                    try {
                        cancelledActiveUpload = true;
                        nativeAbortXMLHttpRequest(lastXMLHttpRequest);
                    } catch (Throwable t) {
                        // Purposefully ignoring any problems that may occur when aborting the XMLHttpRequest
                    }
                }
            }

            nativeUpdateFileProperties(nativeFile, DmFile.Status.CANCELLED.toInt());

            // Keep the global stats up to date (since SWFUpload isn't taken care of it for us in this case)
            nativeSetProperty(getStats(), "upload_cancelled", getStats().getUploadsCancelled() + 1);

            if (triggerErrorEvent) {
                nativeSetProperty(getStats(), "upload_errors", getStats().getUploadErrors() + 1);
                if (uploadErrorHandler != null) {
                    uploadErrorHandler.onUploadError(new DmUploadErrorEvent(nativeFile.<DmFile>cast(), DmUploadErrorEvent.ErrorCode.FILE_CANCELLED.toInt(), "File Cancelled"));
                }
            }

            // If we just cancelled the upload that was in progress then we need to explicitly invoke the complete
            // handler after the file cancellation (in order to let the next potential upload continue)
            if (cancelledActiveUpload) {
                uploadCompleteEventCallback(nativeFile.<DmFile>cast());
            } else {
                // If we're not cancelling the file upload that was in progress, then we need to handle
                // pulling it out of the internal queue and statistics on our own
                nativeFilesQueued.remove(nativeFile);
                nativeFilesQueuedById.remove(nativeFile.<DmFile>cast().getId());
                nativeSetProperty(getStats(), "files_queued", nativeFilesQueued.size());
                nativeSetProperty(getStats(), "in_progress", 0);
            }
        }
//        }
    }

    private static native void nativeAbortXMLHttpRequest(JavaScriptObject xmlHttpRequest) /*-{
     xmlHttpRequest.abort();
     }-*/;

//    private static native void nativeCancelUpload(JavaScriptObject swfUpload, String fileId, boolean triggerErrorEvent) /*-{
//     swfUpload.cancelUpload(fileId, triggerErrorEvent);
//     }-*/;
    /**
     * Cancel the first file in the queue.
     */
    public void cancelUpload() {
//        if (swfUpload != null) {
//            nativeCancelUpload(swfUpload);
//        } else {
        if (nativeFilesQueued.size() > 0) {
            for (int i = 0; i < nativeFilesQueued.size(); i++) {
                cancelUpload(nativeFilesQueued.get(i).<DmFile>cast().getId());
            }
        }
//        }
    }

//    private static native void nativeCancelUpload(JavaScriptObject swfUpload) /*-{
//     swfUpload.cancelUpload();
//     }-*/;
    /**
     * Cancel the the file with the supplied id. An uploadError event will be
     * issued.
     *
     * @param fileId The fileId to cancel
     */
    public void cancelUpload(String fileId) {
//        if (swfUpload != null) {
//            nativeCancelUpload(swfUpload, fileId);
//        } else {
        cancelUpload(fileId, true);
//        }
    }

//    private static native void nativeCancelUpload(JavaScriptObject swfUpload, String fileId) /*-{
//     swfUpload.cancelUpload(fileId);
//     }-*/;
    /**
     * Cancel the first file in the queue.
     *
     * @param triggerErrorEvent if true, an uploadError event will be issued
     */
    public void cancelUpload(boolean triggerErrorEvent) {
//        if (swfUpload != null) {
//            nativeCancelUpload(swfUpload, triggerErrorEvent);
//        } else {
        if (nativeFilesQueued.size() > 0) {
            for (int i = 0; i < nativeFilesQueued.size(); i++) {
                JavaScriptObject nativeFile = nativeFilesQueued.get(i);
                cancelUpload(nativeFile.<DmFile>cast().getId(), triggerErrorEvent);
            }
        }
//        }
    }

//    private static native void nativeCancelUpload(JavaScriptObject swfUpload, boolean triggerErrorEvent) /*-{
//     swfUpload.cancelUpload(null, triggerErrorEvent);
//     }-*/;
    /**
     * Stop and re-queues the file currently being uploaded. Note: this method
     * is currently only supported when in Flash/SWFUpload mode, so it's marked
     * as "private" for now until it can be more fully implemented if deemed
     * needed.
     *
     * @param triggerErrorEvent if true, an uploadError event will be issued
     * @param fileId The fileId to stop
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private void stopUpload(String fileId, boolean triggerErrorEvent) {
        // TODO: Implement AJAX equivalent
//        if (swfUpload != null) {
//            nativeStopUpload(swfUpload, fileId, triggerErrorEvent);
//        }
    }

//    private static native void nativeStopUpload(JavaScriptObject swfUpload, String fileId, boolean triggerErrorEvent) /*-{
//     swfUpload.stopUpload(fileId, triggerErrorEvent);
//     }-*/;
    //private JavaScriptObject swfUpload;
    public static boolean isAjaxUploadWithProgressEventsSupported() {
        return isFileAPISupported() && isAjaxUploadProgressEventsSupported() && isFormDataSupported();
    }

    private static native boolean isFileAPISupported() /*-{
     var inputElement = document.createElement('INPUT');
     inputElement.type = 'file';
     return 'files' in inputElement;
     }-*/;

    private static native boolean isAjaxUploadProgressEventsSupported() /*-{
     var xhr = new XMLHttpRequest();
     return !! (xhr && ('upload' in xhr) && ('onprogress' in xhr.upload));
     }-*/;

    private static native boolean isFormDataSupported() /*-{
     return !! window.FormData;
     }-*/;

    // Purposefully using concrete types here instead of a generic list to increase GWT performance
    private ArrayList<JavaScriptObject> nativeFilesQueued = new ArrayList<JavaScriptObject>();
    private HashMap<String, JavaScriptObject> nativeFilesQueuedById = new HashMap<String, JavaScriptObject>();

    private FileUpload fileUpload;

    @Override
    protected void onLoad() {

        // Make sure our entire panel fits the size that they wanted for the button
        if (this.buttonWidth >= 0) {
            this.setWidth(this.buttonWidth + "px");
        }
        if (this.buttonHeight >= 0) {
            this.setHeight(this.buttonHeight + "px");
        }

        if (ajaxUploadEnabled && isAjaxUploadWithProgressEventsSupported()) {

            // If the browser supports the XMLHttpRequest Level 2 type then we can avoid rendering the flash component
            // and just stick with a DOM/Ajax approach.
            // Use a hidden file input component to handle allowing the user to popup the file system dialog
            // (but keep it outside of the button itself so it doesn't interfere with the mouse events)
            this.add(createFileUpload());

            // Create the main element that will hold all of the inner workings of the uploader component
            Label button = new Label();
            button.setWidth("100%");
            button.setHeight("100%");
            if (this.buttonCursor != null) {
                switch (this.buttonCursor) {
                    case ARROW:
                        button.getElement().getStyle().setCursor(Style.Cursor.DEFAULT);
                        break;
                    case HAND:
                        button.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                        break;
                }
            }

            // Setup what we want to happen when someone clicks anywhere on the button
            button.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    if (buttonDisabled) {
                        return;
                    }
                    switch (buttonAction) {
                        case START_UPLOAD:
                            startUpload();
                            break;
                        case SELECT_FILES:
                            openFileDialog(fileUpload, true);
                            break;
                        case SELECT_FILE:
                        default:
                            openFileDialog(fileUpload, false);
                            break;
                    }
                }
            });

            button.addMouseOverHandler(new MouseOverHandler() {
                public void onMouseOver(MouseOverEvent event) {
                    if (buttonImageURL != null && buttonHeight >= 0 && !buttonDisabled) {
                        buttonImageElement.getStyle().setProperty("backgroundPosition", "0px -" + buttonHeight + "px");
                    }
                }
            });
            button.addMouseOutHandler(new MouseOutHandler() {
                public void onMouseOut(MouseOutEvent event) {
                    if (buttonImageURL != null && buttonHeight >= 0 && !buttonDisabled) {
                        buttonImageElement.getStyle().setProperty("backgroundPosition", "0px 0px");
                    }
                }
            });
            button.addMouseDownHandler(new MouseDownHandler() {
                public void onMouseDown(MouseDownEvent event) {
                    if (buttonImageURL != null && buttonHeight >= 0 && !buttonDisabled) {
                        buttonImageElement.getStyle().setProperty("backgroundPosition", "0px -" + (buttonHeight * 2) + "px");
                    }
                }
            });
            button.addMouseUpHandler(new MouseUpHandler() {
                public void onMouseUp(MouseUpEvent event) {
                    if (buttonImageURL != null && buttonHeight >= 0 && !buttonDisabled) {
                        buttonImageElement.getStyle().setProperty("backgroundPosition", "0px 0px");
                    }
                }
            });

            // Depending on the way they wanted the uploader button rendered, create the appropriate elements
            // in the DOM that the user will click on.
            if (this.buttonTextStyle != null) {
                buttonTextStyleElement = Document.get().createStyleElement();
                buttonTextStyleElement.setInnerText(this.buttonTextStyle);
                button.getElement().appendChild(buttonTextStyleElement);
            }
            if (this.buttonText != null) {
                buttonTextElement = Document.get().createDivElement();
                buttonTextElement.setInnerHTML(this.buttonText);
                buttonTextElement.getStyle().setWidth(100, Style.Unit.PCT);
                buttonTextElement.getStyle().setHeight(100, Style.Unit.PCT);
                if (this.buttonTextLeftPadding > Integer.MIN_VALUE) {
                    buttonTextElement.getStyle().setPaddingLeft(this.buttonTextLeftPadding, Style.Unit.PX);
                }
                if (this.buttonTextTopPadding > Integer.MIN_VALUE) {
                    buttonTextElement.getStyle().setPaddingTop(this.buttonTextTopPadding, Style.Unit.PX);
                }
                button.getElement().appendChild(buttonTextElement);
            }

            if (this.buttonImageURL != null) {
                buttonImageElement = Document.get().createDivElement();
                buttonImageElement.getStyle().setBackgroundImage("url(\"" + this.buttonImageURL + "\")");
                if (this.buttonDisabled) {
                    buttonImageElement.getStyle().setProperty("backgroundPosition", "0px -" + (buttonHeight * 3) + "px");
                } else {
                    buttonImageElement.getStyle().setProperty("backgroundPosition", "0px 0px");
                }
                buttonImageElement.getStyle().setWidth(100, Style.Unit.PCT);
                buttonImageElement.getStyle().setHeight(100, Style.Unit.PCT);
                button.getElement().appendChild(buttonImageElement);
            }

            // Add the entire button to the DOM
            this.add(button);

        }
    }

    private FileUpload createFileUpload() {
        fileUpload = new FileUpload();
        fileUpload.getElement().getStyle().setDisplay(Style.Display.NONE);
//        fileUpload.getElement().setAttribute("multiple", "multiple");

        if (fileTypes != null) {
            // Convert the format that the SWFUpload/Flash parameter expects to the W3C DOM standard
            fileUpload.getElement().setAttribute("accept", this.fileTypes.replaceAll("\\;", ",").replaceAll("\\*\\.", "."));

            // TODO: Need to consider validation of this in the file queued handler as well, as the browsers don't
            // appear to consistently support the "accept" attribute
        }

        final AbsolutePanel panel = this;

        fileUpload.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {

                JsArray selectedFiles = nativeGetSelectedFiles(fileUpload.getElement());

                // Every time a file is selected replace the FileUpload component running in the DOM so that
                // the user can continue to attempt uploading the same file multiple times (otherwise the
                // "ChangeHandler" never fires)
                panel.remove(fileUpload);
                if (buttonAction != ButtonAction.START_UPLOAD) {
                    nativeFilesQueued.clear();
                    nativeFilesQueuedById.clear();
                }
                fileUpload = null;
                panel.add(createFileUpload());

                addFilesToQueue(selectedFiles);

            }
        });
        return fileUpload;
    }

    private long lastFileId = 0;

    private String getNextFileId() {
        lastFileId++;
        return "Uploader_" + lastFileId;
    }

    /**
     * When implementing drag/drop support for file uploads, this method can be
     * used to pass into the uploader the list of native file references that
     * the user dropped onto the application to be uploaded. Will normally be
     * used in conjunction with the
     * {@link #getDroppedFiles(com.google.gwt.dom.client.NativeEvent)} method.
     * E.g.
     * <pre><code>
     * final Uploader uploader = new Uploader();
     * dropFilesArea.addDropHandler(new DropHandler() {
     *    public void onDrop(DropEvent event) {
     *       event.preventDefault();
     *       uploader.addFilesToQueue(
     *          Uploader.getDroppedFiles(event.getNativeEvent())
     *       );
     *    }
     * });
     * </code></pre>
     *
     * @param files An array of native file references to be added to the queue.
     */
    public void addFilesToQueue(JsArray files) {

        int filesQueued = 0;

        if (files != null) {

            for (int i = 0; i < files.length(); i++) {
                JavaScriptObject nativeFile = files.get(i);

                // Make sure our maximum allowable queue size upload limit has not been exceeded
                if (nativeFilesQueued.size() >= fileQueueLimit) {
                    fileQueueErrorEventCallback(nativeFile.<DmFile>cast(), DmFileQueueErrorEvent.ErrorCode.QUEUE_LIMIT_EXCEEDED.toInt(), "Exceeded file queue size limit of " + fileQueueLimit);
                    break;
                }
                if (fileUploadLimit > 0 && totalFilesUploaded >= fileUploadLimit) {

                    // Keep the global stats up to date (since SWFUpload isn't taken care of it for us in this case)
                    nativeSetProperty(getStats(), "upload_errors", getStats().getUploadErrors() + 1);

                    if (uploadErrorHandler != null) {
                        uploadErrorHandler.onUploadError(new DmUploadErrorEvent(nativeFile.<DmFile>cast(), DmUploadErrorEvent.ErrorCode.UPLOAD_LIMIT_EXCEEDED.toInt(), "Exceeded upload limit of " + fileUploadLimit));
                    }

                    break;
                }

                // Ensure the file size can be determined and that the file has some contents
                if (nativeFile.<DmFile>cast().getSize() <= 0) {
                    fileQueueErrorEventCallback(nativeFile.<DmFile>cast(), DmFileQueueErrorEvent.ErrorCode.ZERO_BYTE_FILE.toInt(), "File is zero bytes and cannot be uploaded.");
                    continue;
                }

                // Make sure the file doesn't exceed the configured size limit
                if (exceedsFileSizeLimit(nativeFile)) {
                    fileQueueErrorEventCallback(nativeFile.<DmFile>cast(), DmFileQueueErrorEvent.ErrorCode.FILE_EXCEEDS_SIZE_LIMIT.toInt(), "File size exceeds allowed limit.");
                    continue;
                }

                nativeSetProperty(nativeFile, "id", getNextFileId());

                filesQueued++;

                // Track each of the files that still need to be uploaded
                addFileToQueue(nativeFile);

            }
        }

        // If requested, notify the app each time the user has finished selecting a bunch of files
        if (fileDialogCompleteHandler != null) {
            fileDialogCompleteHandler.onFileDialogComplete(
                    new DmFileDialogCompleteEvent(
                            files != null ? files.length() : 0,
                            filesQueued,
                            nativeFilesQueued.size()
                    )
            );
        }
    }

    /**
     * A convenience method that can be used to extract the files that a user
     * dropped on the application during a GWT drop event. Useful when adding
     * drag/drop support for file uploads to your application. E.g.
     * <pre><code>
     * final Uploader uploader = new Uploader();
     * dropFilesArea.addDropHandler(new DropHandler() {
     *    public void onDrop(DropEvent event) {
     *       event.preventDefault();
     *       uploader.addFilesToQueue(
     *          Uploader.getDroppedFiles(event.getNativeEvent())
     *       );
     *    }
     * });
     * </code></pre>
     *
     * @param event The native event provided by GWT that was fired when the
     * drop occurred
     * @return An array of native file references that were dropped as part of
     * the event, which can subsequently be passed to the
     * {@link #addFileToQueue(com.google.gwt.core.client.JavaScriptObject)}
     * method.
     */
    public static native JsArray getDroppedFiles(NativeEvent event) /*-{
     return event.target.files || event.dataTransfer.files;
     }-*/;

    private void addFileToQueue(JavaScriptObject nativeFile) {
        // Track each of the files that still need to be uploaded
        nativeFilesQueued.add(nativeFile);
        nativeFilesQueuedById.put(nativeFile.<DmFile>cast().getId(), nativeFile);

        // SWFUpload isn't in play in this case, so we need to keep the global stats up to date manually
        nativeSetProperty(getStats(), "files_queued", nativeFilesQueued.size());

        // If requested, notify the app each time a new file is added to the queue
        if (fileQueuedHandler != null) {
            fileQueuedHandler.onFileQueued(new DmFileQueuedEvent(
                    nativeUpdateFileProperties(nativeFile, DmFile.Status.QUEUED.toInt()).<DmFile>cast())
            );
        }
    }

    public void setServerIdToFile(String serverResourceId, String fileUploaderId) {
        if (nativeFilesQueuedById.containsKey(fileUploaderId)) {
            (nativeFilesQueuedById.get(fileUploaderId).<DmFile>cast()).setServerResourceId(serverResourceId);
        }
    }

    private boolean exceedsFileSizeLimit(JavaScriptObject nativeFile) {
        long fileSize = nativeFile.<DmFile>cast().getSize();
        return this.fileSizeLimit != 0 && fileSize > this.fileSizeLimit;
    }

    private void openFileDialog(FileUpload fileUpload, boolean multipleFiles) {

        if (multipleFiles) {
            fileUpload.getElement().setAttribute("multiple", "true");
        } else {
            fileUpload.getElement().removeAttribute("multiple");
        }

        if (fileDialogStartHandler != null) {
            fileDialogStartHandler.onFileDialogStartEvent(new DmFileDialogStartEvent());
        }
        InputElement.as(fileUpload.getElement()).click();
    }

    @Override
    protected void onUnload() {
//        if (swfUpload != null) {
//            nativeSWFUploadDestroy(swfUpload);
//            swfUpload = null;
//        }
        buttonTextStyleElement = null;
        buttonTextElement = null;
        buttonImageElement = null;
    }

    private JavaScriptObject createNativeOptions(String placeHolderId) {
        JSONObject options = configurable.getOptions();
        if (options == null) {
            options = new JSONObject();
        }

        // Let the SWFUpload component know which element it can render itself inside of
        options.put("button_placeholder_id", new JSONString(placeHolderId));

        // Unless they've explicitly overridden it, automatically serve the SWF out of our modules directly (which
        // should be included by GWT when it compiles in all of the resources in our "public" directory)
//        if (flashURL == null) {
//            options.put("flash_url", new JSONString(GWT.getModuleBaseURL() + "swfupload.swf"));
//        }
        // For debugging the raw options that we're passing to the component on startup, uncomment the following line
        // com.google.gwt.user.client.Window.alert(options.toString());
        return options.getJavaScriptObject();
    }
    
    @SuppressWarnings({"UnusedDeclaration"})
    private boolean swfUploadLoadedEventCallback() {
        return swfUploadLoadedHandler == null || swfUploadLoadedHandler.onSWFUploadLoaded(new DmSWFUploadLoadedEvent());
    }

    @SuppressWarnings({"UnusedDeclaration"})
    private boolean fileDialogStartEventCallback() {
        return fileDialogStartHandler == null || fileDialogStartHandler.onFileDialogStartEvent(new DmFileDialogStartEvent());
    }

    @SuppressWarnings({"UnusedDeclaration"})
    private boolean fileQueuedEventCallback(DmFile file) {
        return fileQueuedHandler == null || fileQueuedHandler.onFileQueued(new DmFileQueuedEvent(file));
    }

    @SuppressWarnings({"UnusedDeclaration"})
    private boolean fileQueueErrorEventCallback(DmFile file, int errorCode, String message) {

        // Keep the global stats up to date (at least if SWFUpload isn't keep track of them for us)
//        if (swfUpload == null) {
        nativeSetProperty(getStats(), "queue_errors", getStats().getQueueErrors() + 1);
//        }

        return fileQueueErrorHandler == null || fileQueueErrorHandler.onFileQueueError(new DmFileQueueErrorEvent(file, errorCode, message));
    }

    @SuppressWarnings({"UnusedDeclaration"})
    private boolean fileDialogCompleteEventCallback(int numberOfFilesSelected, int numberOfFilesQueued, int totalFilesInQueue) {
        return fileDialogCompleteHandler == null || fileDialogCompleteHandler.onFileDialogComplete(new DmFileDialogCompleteEvent(numberOfFilesSelected, numberOfFilesQueued, totalFilesInQueue));
    }

    private boolean uploadStartEventCallback(DmFile file) {
        return uploadStartHandler == null || uploadStartHandler.onUploadStart(new DmUploadStartEvent(file));
    }

    private boolean uploadProgressEventCallback(DmFile file, double bytesComplete, double bytesTotal) {

        // If we're running in Flash mode the SWFUpload "speed" plugin takes care of this for us.  But, if
        // not we need to do our own math to keep the file statistics up to date.
//        if (swfUpload == null) {
        long now = System.currentTimeMillis();
        long bytesSinceLastEvent = (long) (bytesComplete - (nativeGetPropertyAsDouble(file, "previousBytesComplete")));
        double timeSinceLastEvent = (now - nativeGetPropertyAsDouble(file, "timeSinceLastEvent")) / 1000.0;
        double elapsedTime = (now - nativeGetPropertyAsDouble(file, "startTime")) / 1000.0;
        double currentSpeed = (bytesSinceLastEvent * 8) / timeSinceLastEvent;
        if (!Double.isInfinite(currentSpeed) && !Double.isNaN(currentSpeed)) {
            nativeAddValueToArray(file, "movingAverageHistory", currentSpeed);
        }
        nativeSetProperty(file, "bytesSinceLastEvent", bytesSinceLastEvent);
        nativeSetProperty(file, "previousBytesComplete", bytesComplete);
        nativeSetProperty(file, "timeSinceLastEvent", now);

        // consoleDebug(currentSpeed + "");
        // Set properties
        nativeSetProperty(file, "averageSpeed", (bytesComplete * 8) / elapsedTime); // Bits per seconds
        nativeSetProperty(file, "currentSpeed", currentSpeed); // Bytes per seconds since last event
        nativeSetProperty(file, "movingAverageSpeed", calculateMovingAverage(nativeGetPropertyAsArray(file, "movingAverageHistory"))); // Bytes per second
        nativeSetProperty(file, "percentUploaded", (bytesComplete / bytesTotal * 100)); // Bytes per seconds
        nativeSetProperty(file, "sizeUploaded", bytesComplete); // Bytes uploaded so far
        nativeSetProperty(file, "timeElapsed", elapsedTime); // Time since upload started
        nativeSetProperty(file, "timeRemaining", (bytesTotal - bytesComplete) / (bytesComplete / elapsedTime));

//        }
        return uploadProgressHandler == null || uploadProgressHandler.onUploadProgress(new DmUploadProgressEvent(file, (long) bytesComplete, (long) bytesTotal));
    }

    private boolean uploadErrorEventCallback(DmFile file, int errorCode, String message) {

        // If the user manually cancelled the file upload earlier (via the cancelUpload() method), then we've already invoked
        // the error handler callback if appropriate.
        if (file.getStatus() == DmFile.Status.CANCELLED) {
            return true;
        }

        // Keep the global stats up to date (at least if SWFUpload isn't keep track of them for us)
//        if (swfUpload == null) {
        nativeSetProperty(getStats(), "upload_errors", getStats().getUploadErrors() + 1);
//        }

        boolean response;
        try {
            response = uploadErrorHandler == null || uploadErrorHandler.onUploadError(new DmUploadErrorEvent(file, errorCode, message));
        } finally {
            // In the case that we're running in Ajax/DOM mode (as opposed to in SWFUpload/Flash mode) we need to explicitly
            // invoke the complete handler after each file upload error.  (When in SWFUpload/Flash
            // mode this callback will be invoked automatically by the SWFUpload library.)
//            if (swfUpload == null) {
            try {
                uploadCompleteEventCallback(file);
            } finally {
                // Similarly, we need to handle the requeue error logic manually
                if (requeueOnError && errorCode != DmFile.Status.CANCELLED.toInt()) {
                    addFileToQueue(file);
                }
            }
//            }
        }

        return response;
    }

    private boolean uploadSuccessEventCallback(DmFile file, String responseReceived, String serverData) {
        boolean response = false;

//        if (swfUpload == null) {
        // Only call the success handler if the response code is one of the expected type, otherwise call the error handler
        if (httpSuccess == null) {
            httpSuccess = new long[]{200};
        }
        boolean success = false;
        for (long code : httpSuccess) {
            if ((code + "").equals(responseReceived)) {
                success = true;
            }
        }

        if (success) {
            try {
                // If the user manually cancelled the file upload earlier (via the cancelUpload() method), then don't
                // allow the success handler to be invoked (but still allow the complete handler to run)
                if (file.getStatus() != DmFile.Status.CANCELLED) {
                    totalFilesUploaded++;

                    // If the file upload was super quick, we may not have gotten any progress events.  So, let
                    // anyone who cares know that we've made it to a 100% successfully with this file
                    uploadProgressEventCallback(file, file.getSize(), file.getSize());

                    // Keep the global stats up to date (since SWFUpload isn't in play in this case)
                    nativeSetProperty(getStats(), "successful_uploads", getStats().getSuccessfulUploads() + 1);

                    response = uploadSuccessHandler == null || uploadSuccessHandler.onUploadSuccess(new DmUploadSuccessEvent(file, serverData, responseReceived));
                }
            } finally {
                // In the case that we're running in Ajax/DOM mode (as opposed to in SWFUpload/Flash mode) we need to explicitly
                // invoke the complete handler after each file is uploaded successfully.  (When in SWFUpload/Flash
                // mode this callback will be invoked automatically by the Uploader library.)
                uploadCompleteEventCallback(file);
            }
        } else {
            uploadErrorEventCallback(file, DmUploadErrorEvent.ErrorCode.HTTP_ERROR.toInt(), "Unsuccessful server response code of: " + responseReceived);
        }
//        } else {
//            response = uploadSuccessHandler == null || uploadSuccessHandler.onUploadSuccess(new DmUploadSuccessEvent(file, serverData, responseReceived));
//        }

        return response;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    private boolean uploadCompleteEventCallback(DmFile file) {
        if (nativeFilesQueuedById.containsKey(file.getId())) {
            JavaScriptObject nativeFile = nativeFilesQueuedById.get(file.getId());
            nativeFilesQueued.remove(nativeFile);
            nativeFilesQueuedById.remove(file.getId());
        }

        // Keep the global stats up to date (at least if SWFUpload isn't keep track of them for us)
//        if (swfUpload == null) {
        nativeSetProperty(getStats(), "files_queued", nativeFilesQueued.size());
        nativeSetProperty(getStats(), "in_progress", 0);
//        }

        return uploadCompleteHandler == null || uploadCompleteHandler.onUploadComplete(new DmUploadCompleteEvent(file));
    }

//    private static native boolean nativeSWFUploadDestroy(JavaScriptObject swfUpload) /*-{
//     return swfUpload.destroy();
//     }-*/;
    private static native JsArray nativeGetSelectedFiles(Element fileInputElement) /*-{
     return fileInputElement.files;
     }-*/;

    // We're using native objects instead of Java objects in order to keep the rest of the code compatible with
    // the native types returned by SWFUpload
    private static native JavaScriptObject nativeUpdateFileProperties(JavaScriptObject file, int fileStatus) /*-{
     file.modificationdate = file.lastModifiedDate;
     file.filestatus = fileStatus;
     return file;
     }-*/;

    private static native void nativeSetProperty(JavaScriptObject obj, String key, double value) /*-{
     obj[key] = value;
     }-*/;

    private static native void nativeSetProperty(JavaScriptObject obj, String key, String value) /*-{
     obj[key] = value;
     }-*/;

    private static native void nativeAddValueToArray(JavaScriptObject obj, String key, double value) /*-{
     if (!obj[key]) {
     obj[key] = new Array();
     }
     obj[key].push(value);
     }-*/;

    private static native double nativeGetPropertyAsDouble(JavaScriptObject obj, String key) /*-{
     return obj[key] ? obj[key] : 0.0;
     }-*/;

    private static native JsArrayNumber nativeGetPropertyAsArray(JavaScriptObject obj, String key) /*-{
     return obj[key] ? obj[key] : new Array();
     }-*/;

    /**
     * Implementation of SWFUpload Speed plug-in's moving average calculation
     * based on the set of data points available ported directly from the
     * original 2.9 implementation
     *
     * @param history An array of values calculated so far
     * @return The moving average
     */
    private static double calculateMovingAverage(JsArrayNumber history) {
        double[] vals = new double[history.length()];
        long size = history.length();
        double sum = 0.0;
        double mSum = 0.0;
        long mCount = 0;

        // Check for sufficient data
        if (size >= 8) {
            // Clone the array and Calculate sum of the values
            for (int i = 0; i < size; i++) {
                vals[i] = history.get(i);
                sum += vals[i];
            }
            double mean = sum / size;

            // Calculate variance for the set
            double varianceTemp = 0.0;
            for (int i = 0; i < size; i++) {
                varianceTemp += Math.pow((vals[i] - mean), 2);
            }
            double variance = varianceTemp / size;
            double standardDev = Math.sqrt(variance);

            // Standardize the Data
            for (int i = 0; i < size; i++) {
                vals[i] = (vals[i] - mean) / standardDev;
            }

            // Calculate the average excluding outliers
            double deviationRange = 2.0;

            for (int i = 0; i < size; i++) {
                if (vals[i] <= deviationRange && vals[i] >= -deviationRange) {
                    mCount++;
                    mSum += history.get(i);
                }
            }

        } else {
            // Calculate the average (not enough data points to remove outliers)
            mCount = size;
            for (int i = 0; i < size; i++) {
                mSum += history.get(i);
            }
        }

        return mSum / mCount;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    private native void consoleDebug(String msg) /*-{
     $wnd.console.debug(msg);
     }-*/;

}
