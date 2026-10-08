package dataman.radiology;

import aarogya.projectMenu.radiologyDoc.shared.RDRequestCommands;
import aarogya.projectMenu.radiologyDoc.jvm.RadiologyDocumentRequest_Mdl;
import aarogya.projectMenu.radiologyDoc.jvm.RadiologyDocumentUploadRequest_Mdl;
import aarogya.projectMenu.self.shared.ProjectRuleCode;
import com.google.gson.reflect.TypeToken;
import dataman.dmPlugin.CFunctions;
import dataman.dmPlugin.server.NanoHTTPD;
import dataman.dmPlugin.server.router.RouterNanoHTTPD;
import dataman.stPlugin.STFunctions;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.docx4j.Docx4J;
import org.docx4j.dml.CTBlip;
import org.docx4j.model.structure.HeaderFooterPolicy;
import org.docx4j.model.structure.SectionWrapper;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.FooterPart;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.relationships.RelationshipsPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.*;

public class RadiologyDocCommands extends RouterNanoHTTPD.DefaultHandler {

    @Override
    public NanoHTTPD.Response post(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        NanoHTTPD.Response response = null;
        try {
            Map<String, List<String>> queryParams = NanoHTTPD.decodeParameters(session.getQueryParameterString());
            String userAgent = session.getHeaders().get("user-agent");
            if (queryParams.containsKey("cmd") && queryParams.containsKey("request")) {
                String cmdValue = queryParams.get("cmd").get(0);
                String requestModelJson = queryParams.get("request").get(0);
                RadiologyDocumentRequest_Mdl requestModel = CFunctions.getObjectFromJson(requestModelJson, new TypeToken<RadiologyDocumentRequest_Mdl>() {
                });
                RDRequestCommands command = RDRequestCommands.getCommandFrom(cmdValue);
                RadiologyDocumentUploadRequest_Mdl uploadRequestModel = null;
                if (command == RDRequestCommands.UploadContent) {
                    uploadRequestModel = uploadFileContent(requestModel, userAgent);
                } else if (command == RDRequestCommands.UploadContentWithHeader) {
                    uploadRequestModel = uploadFileContentWithHeader(requestModel, userAgent);
                } else if (command == RDRequestCommands.Download) {
                    downloadFile(requestModel, userAgent);
                } else if (command == RDRequestCommands.Save) {
                    STFunctions.removeAndKillAllRadiologyReportsProcesses(requestModel);
                }
                String responseJsonString = "";
                if ((command == RDRequestCommands.UploadContent) || (command == RDRequestCommands.UploadContentWithHeader)) {
                    responseJsonString = CFunctions.getJsonFromObject(uploadRequestModel, new TypeToken<RadiologyDocumentUploadRequest_Mdl>() {
                    });
                } else if ((command == RDRequestCommands.Save) || (command == RDRequestCommands.Download)) {
                    responseJsonString = CFunctions.getJsonFromObject(requestModel, new TypeToken<RadiologyDocumentRequest_Mdl>() {
                    });
                }
                response = NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), responseJsonString);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            response.addHeader("Access-Control-Allow-Origin", "*");
        }
        return response;
    }

    @Override
    public String getText() {
        return "RADIOLOGY_REPORTS";
    }

    @Override
    public NanoHTTPD.Response.IStatus getStatus() {
        return NanoHTTPD.Response.Status.OK;
    }

    @Override
    public String getMimeType() {
        return "application/json";
    }

    private void downloadFile(RadiologyDocumentRequest_Mdl requestModel, String userAgent) throws Exception {
        try {
            saveReportFile(requestModel, userAgent);

            STFunctions.putRadiologyReportProcess(requestModel);
        } finally {
        }
    }

    private RadiologyDocumentUploadRequest_Mdl uploadFileContentWithHeader(RadiologyDocumentRequest_Mdl requestModel, String userAgent) throws Exception {
        RadiologyDocumentUploadRequest_Mdl uploadRequestModel = null;
        try {

            //===download the header template and saved content from mongo and merge them
            saveReportFile(requestModel, userAgent);

            //===now the file will be uploaded to server
            requestModel.ruleCode = ProjectRuleCode.PATHOLOGY_TEST.getRuleCode();
            RadiologyReportUploader uploader = new RadiologyReportUploader(requestModel, userAgent, new File(requestModel.physicalFilePath));
            uploadRequestModel = uploader.runAfterProcess();
//            Thread thread = new Thread(uploader);
//            thread.start();
        } finally {

        }
        return uploadRequestModel;
    }

    private RadiologyDocumentUploadRequest_Mdl uploadFileContent(RadiologyDocumentRequest_Mdl requestModel, String userAgent) throws Exception {
        RadiologyDocumentUploadRequest_Mdl uploadRequestModel = null;
        RadiologyDocumentRequest_Mdl storedModel = null;
        try {
            storedModel = STFunctions.getStoredRadiologyRequestModel(requestModel);
            storedModel.contentDocumentId = requestModel.contentDocumentId;
            storedModel.contentVoucherLineItemSerialNo = requestModel.contentVoucherLineItemSerialNo;
            if (storedModel == null) {
                throw new Exception("Requested Model is not found in the processes...");
            }

            File contentFile = new File(storedModel.temporaryContentDownloadedFilePath);
            File mergedFile = new File(storedModel.physicalFilePath);

            if (!contentFile.exists()) {
                throw new Exception("Unable to locate requested content file...");
            }

            if (!mergedFile.exists()) {
                throw new Exception("Unable to locate requested merged document...");
            }

            File newContentFile = File.createTempFile("rpt", ".docx");

            STFunctions.copyFile(contentFile, newContentFile);

            org.docx4j.openpackaging.packages.WordprocessingMLPackage newContentDocument = WordprocessingMLPackage.load(newContentFile);

            removeAllPreviousContentsFromContentDocument(newContentDocument);

            WordprocessingMLPackage mergedDocument = WordprocessingMLPackage.load(mergedFile);

            replaceNewDocumentContentsWithMergedContent(newContentDocument, mergedDocument);

            newContentDocument.save(newContentFile);///this line may throw error as content file is also opened in the wordprocessingmlpackage

            RadiologyReportUploader uploader = new RadiologyReportUploader(storedModel, userAgent, newContentFile);
//            Thread thread = new Thread(uploader);
//            thread.start();
            uploadRequestModel = uploader.runAfterProcess();
        } finally {
            uploadRequestModel.requestModel = storedModel;
        }
        return uploadRequestModel;
    }

    private void replaceNewDocumentContentsWithMergedContent(WordprocessingMLPackage newContentDocument, WordprocessingMLPackage mergedDocument) throws Exception {
        try {
            List nbody = mergedDocument.getMainDocumentPart().getJAXBNodesViaXPath("//w:body", false);
            for (Object b : nbody) {
                List filhos = ((org.docx4j.wml.Body) b).getContent();
                for (Object k : filhos) {
                    newContentDocument.getMainDocumentPart().addObject(k);
                }
            }

            List nblips = mergedDocument.getMainDocumentPart().getJAXBNodesViaXPath("//a:blip", false);
            for (Object el : nblips) {
                try {
                    CTBlip blip = (CTBlip) el;

                    RelationshipsPart parts = mergedDocument.getMainDocumentPart().getRelationshipsPart();
                    Relationship rel = parts.getRelationshipByID(blip.getEmbed());
                    RelationshipsPart docRels = newContentDocument.getMainDocumentPart().getRelationshipsPart();

                    rel.setId(null);
                    docRels.addRelationship(rel);
                    blip.setEmbed(rel.getId());

                    newContentDocument.getMainDocumentPart().addTargetPart(mergedDocument.getParts().getParts().get(new PartName("/word/" + rel.getTarget())));

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } finally {

        }
    }


    private void removeAllPreviousContentsFromContentDocument(WordprocessingMLPackage newContentDocument) throws Exception {
        try {
            List<Object> listElements = new ArrayList<>();
            List obody = newContentDocument.getMainDocumentPart().getContent();
            for (Object b : obody) {
                for (Object k : obody) {
                    listElements.add(k);
                }
            }

            for (int i = 0; i < listElements.size(); i++) {
                obody.remove(listElements.get(i));
            }
        } finally {

        }
    }

    private void saveReportFile(RadiologyDocumentRequest_Mdl requestModel, String userAgent) throws Exception {
        CountDownLatch countDownLatch;
        try {
            countDownLatch = new CountDownLatch(2);
//            Thread headerThread = new Thread(new RadiologyReportDownloader(requestModel,true, userAgent, countDownLatch));
//            Thread contentThread = new Thread(new RadiologyReportDownloader(requestModel,false, userAgent, countDownLatch));
//            headerThread.start();
//            contentThread.start();
            RadiologyReportDownloader downloader = new RadiologyReportDownloader(requestModel, true, userAgent, countDownLatch);
            downloader.run();
            downloader = new RadiologyReportDownloader(requestModel, false, userAgent, countDownLatch);
            downloader.run();

            countDownLatch.await();
        } finally {
            countDownLatch = null;
            mergeTwoWordDocuments(requestModel);
        }
    }

    private void mergeTwoWordDocuments(RadiologyDocumentRequest_Mdl requestModel) throws Exception {
        File headerFile = null, contentFile = null, outputFile = null;
        try {
            headerFile = new File(requestModel.temporaryHeaderDownloadedFilePath);
            contentFile = new File(requestModel.temporaryContentDownloadedFilePath);

            outputFile = File.createTempFile("rpt", ".docx");

            WordprocessingMLPackage headerWordDocument = Docx4J.load(headerFile);//WordprocessingMLPackage.load(headerFile);
            WordprocessingMLPackage contentWordDocument = Docx4J.load(contentFile);//WordprocessingMLPackage.load(contentFile);


            for (SectionWrapper sw : headerWordDocument.getDocumentModel().getSections()) {
                if (sw.getHeaderFooterPolicy() != null) {
                    ObjectFactory objectFactory = ObjectFactory.get();

                    MainDocumentPart contentMainDocumentPart = contentWordDocument.getMainDocumentPart();

                    HeaderFooterPolicy headerFooterPolicy = sw.getHeaderFooterPolicy();

                    HeaderPart defaultHeader = headerFooterPolicy.getDefaultHeader();
                    FooterPart defaultFooter = headerFooterPolicy.getDefaultFooter();

                    HeaderPart contentHeaderPart = new HeaderPart();
                    contentHeaderPart.setJaxbElement(defaultHeader.getJaxbElement());
                    Relationship contentHeaderRel = contentMainDocumentPart.addTargetPart(contentHeaderPart);

                    FooterPart contentFooterPart = new FooterPart();
                    contentFooterPart.setJaxbElement(defaultFooter.getJaxbElement());
                    Relationship contentFooterRel = contentMainDocumentPart.addTargetPart(contentFooterPart);

                    HeaderReference contentHeaderRef = objectFactory.createHeaderReference();
                    contentHeaderRef.setId(contentHeaderRel.getId());
                    contentHeaderRef.setType(HdrFtrRef.DEFAULT);

                    FooterReference contentFooterRef = objectFactory.createFooterReference();
                    contentFooterRef.setId(contentFooterRel.getId());
                    contentFooterRef.setType(HdrFtrRef.DEFAULT);

                    SectionWrapper contentSectionWrapper = contentWordDocument.getDocumentModel().getSections().get(contentWordDocument.getDocumentModel().getSections().size() - 1);
                    SectPr contentSectPr = contentSectionWrapper.getSectPr();
                    if (contentSectPr == null) {
                        contentSectPr = objectFactory.createSectPr();
                        contentMainDocumentPart.addObject(contentSectPr);
                        contentWordDocument.getDocumentModel().getSections().get(0).setSectPr(contentSectPr);
                    }

                    contentSectPr.getEGHdrFtrReferences().add(contentHeaderRef);
                    contentSectPr.getEGHdrFtrReferences().add(contentFooterRef);
                }
            }

//            List blips = contentWordDocument.getMainDocumentPart().getJAXBNodesViaXPath("//a:blip", false);
//            for (Object el : blips) {
//                try {
//                    CTBlip blip = (CTBlip) el;
//
//                    RelationshipsPart parts = contentWordDocument.getMainDocumentPart().getRelationshipsPart();
//                    Relationship rel = parts.getRelationshipByID(blip.getEmbed());
//                    RelationshipsPart docRels = headerWordDocument.getMainDocumentPart().getRelationshipsPart();
//
//                    rel.setId(null);
//                    docRels.addRelationship(rel);
//                    blip.setEmbed(rel.getId());
//
//                    headerWordDocument.getMainDocumentPart().addTargetPart(contentWordDocument.getParts().getParts().get(new PartName("/word/" + rel.getTarget())));
//                } catch (Exception ex) {
//                }
//            }

            //headerWordDocument.save(outputFile);
//            contentWordDocument.save(outputFile);
            findNreplaceWithFinalDocument(contentWordDocument, requestModel, outputFile);
            requestModel.physicalFilePath = outputFile.getAbsolutePath();
            System.out.println("Merged word document is wrotten on : " + requestModel.physicalFilePath);

        } finally {
            if (headerFile != null) {
                headerFile.delete();
            }
//            if (contentFile != null) {
//                contentFile.delete();
//            }
        }
    }

    private void findNreplaceWithFinalDocument(WordprocessingMLPackage wordDocument, RadiologyDocumentRequest_Mdl requestModel, File outputFile) throws Exception {
        try {
            DocumentFindAndReplace findNreplace = new DocumentFindAndReplace(wordDocument);
            findNreplace.searchAndReplace(requestModel.getFindNreplaceParams());
            findNreplace.saveDocumentToFilePath(outputFile);

            System.out.println("Find and Replace is done on the path : " + requestModel.physicalFilePath);
        } finally {

        }
    }
}
