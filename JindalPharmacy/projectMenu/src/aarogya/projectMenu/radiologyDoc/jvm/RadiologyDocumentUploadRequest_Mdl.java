/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.projectMenu.radiologyDoc.jvm;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author abhishek
 */
public class RadiologyDocumentUploadRequest_Mdl {

    @SerializedName("rModel")
    public RadiologyDocumentRequest_Mdl requestModel;

    @SerializedName("content")
    public String documentContent;

    @SerializedName("fileLineId")
    public String mongoFileLineId;

}
