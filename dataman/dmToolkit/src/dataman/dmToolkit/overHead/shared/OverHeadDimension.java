package dataman.dmToolkit.overHead.shared;

public class OverHeadDimension {

    int width, height, overheadWidth, atRateWidth, amountWidth;
    boolean ohColIsExpandable = false;

    public OverHeadDimension() {

    }

    public OverHeadDimension(int width, int height, int overheadWidth, int atRateWidth, int amountWidth, boolean ohColIsExpandable) {
        this.width = width;
        this.height = height;
        this.overheadWidth = overheadWidth;
        this.atRateWidth = atRateWidth;
        this.amountWidth = amountWidth;
        this.ohColIsExpandable = ohColIsExpandable;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getOverheadWidth() {
        return overheadWidth;
    }

    public void setOverheadWidth(int overheadWidth) {
        this.overheadWidth = overheadWidth;
    }

    public int getAtRateWidth() {
        return atRateWidth;
    }

    public void setAtRateWidth(int atRateWidth) {
        this.atRateWidth = atRateWidth;
    }

    public int getAmountWidth() {
        return amountWidth;
    }

    public void setAmountWidth(int amountWidth) {
        this.amountWidth = amountWidth;
    }

    public boolean isOHColIsExpandable() {
        return ohColIsExpandable;
    }

    public void setOHColIsExpandable(boolean ohColIsExpandable) {
        this.ohColIsExpandable = ohColIsExpandable;
    }

}
