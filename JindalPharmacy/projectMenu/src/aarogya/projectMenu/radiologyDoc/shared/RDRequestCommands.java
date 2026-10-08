package aarogya.projectMenu.radiologyDoc.shared;

public enum RDRequestCommands {
    Download("download"),
    UploadContent("uploadContent"),
    Save("save"),
    UploadContentWithHeader("uploadContentWithHeader");

    private String cmdValue;

    public String toString() {
        return cmdValue;
    }

    public static RDRequestCommands getCommandFrom(String value) {
        RDRequestCommands cmd = null;
        if (value.equalsIgnoreCase(RDRequestCommands.Download.toString())) {
            cmd = Download;
        } else if (value.equalsIgnoreCase(RDRequestCommands.UploadContent.toString())) {
            cmd = UploadContent;
        } else if (value.equalsIgnoreCase(RDRequestCommands.UploadContentWithHeader.toString())) {
            cmd = UploadContentWithHeader;
        } else if (value.equalsIgnoreCase(RDRequestCommands.Save.toString())) {
            cmd = Save;
        }
        return cmd;
    }

    private RDRequestCommands(String value) {
        this.cmdValue = value;
    }
}
