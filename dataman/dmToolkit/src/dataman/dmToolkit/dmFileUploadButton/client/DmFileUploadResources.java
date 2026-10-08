/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.dmToolkit.dmFileUploadButton.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 *
 * @author abhishek
 */
interface DmFileUploadResources extends ClientBundle {

    @Source("attachment.png")
    ImageResource PAPERCLIP();

    public DmFileUploadResources RESOURCES = GWT.create(DmFileUploadResources.class);
}
