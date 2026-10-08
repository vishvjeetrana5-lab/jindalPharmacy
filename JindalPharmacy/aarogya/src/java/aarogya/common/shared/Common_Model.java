package aarogya.common.shared;

import com.google.gwt.resources.client.ImageResource;

public class Common_Model {

//    public static final String strWidgetName = "KickOff";
//    public static final String strStyleFileName = "KickOff_Style.css";
    public static class dmImgResourse {

        private String name;
        private ImageResource img;

        public dmImgResourse(String name, ImageResource img) {
            setName(name);
            setImg(img);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ImageResource getImg() {
            return img;
        }

        public void setImg(ImageResource img) {
            this.img = img;
        }

    }
}
