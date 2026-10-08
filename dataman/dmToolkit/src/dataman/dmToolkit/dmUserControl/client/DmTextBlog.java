package dataman.dmToolkit.dmUserControl.client;

import com.google.gwt.user.client.ui.InlineLabel;

public class DmTextBlog {

    InlineLabel captionLabel = new InlineLabel();
    InlineLabel valueLabel = new InlineLabel();

    public DmTextBlog(String caption, String value, int width, boolean leftBoder, boolean rightBorder) {
        captionLabel.getElement().setAttribute("style", captionLabel.getElement().getAttribute("style")
                + ";width:" + String.valueOf(width) + "px;max-width:" + String.valueOf(width) + "px"
                + ";color: white;padding-right: 9px;height: 12px;text-align: center"
                + ";background-color: rgb(81, 99, 149);font-family: arial;font-weight: bold;font-size: 9px;padding-left: 1px;"
                + (leftBoder ? ";border-left: 1px solid black;" : "")
                + (rightBorder ? ";border-right: 1px solid black;" : "")
        );
        captionLabel.setStyleName("ellipsis");

        valueLabel.getElement().setAttribute("style", valueLabel.getElement().getAttribute("style")
                + ";width:" + String.valueOf(width) + "px;max-width:" + String.valueOf(width) + "px"
                + ";padding-right: 9px;height: 13px;text-align: center"
                + ";background-color: white;font-family: arial;font-weight: bold;font-size: 10px;padding-left: 1px;"
                + (leftBoder ? ";border-left: 1px solid black;" : "")
                + (rightBorder ? ";border-right: 1px solid black;" : "")
        );
        valueLabel.setStyleName("ellipsis");

        setCaption(caption);
        setValue(value);
    }

    public InlineLabel getCaptionLabel() {
        return captionLabel;
    }

    public InlineLabel getValueLabel() {
        return valueLabel;
    }

    public String getCaption() {
        return captionLabel.getText();
    }

    public void setCaption(String caption) {
        captionLabel.setText(caption);
    }

    public String getValue() {
        return valueLabel.getText();
    }

    public void setValue(String value) {
        valueLabel.setText(value);
    }
}
