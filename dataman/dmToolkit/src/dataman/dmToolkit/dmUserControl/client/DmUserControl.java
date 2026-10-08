package dataman.dmToolkit.dmUserControl.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.HashMap;

public class DmUserControl extends DmUserControl_Dsg implements IsWidget {

    HashMap<String, DmTextBlog> textBlogs = new HashMap<String, DmTextBlog>();
    HashMap<String, DmControlBlog> controlBlogs = new HashMap<String, DmControlBlog>();

    public DmControlBlog addGeneralBlog(IsWidget isWidget) {
        DmControlBlog controlBlog = new DmControlBlog(isWidget);

        hrtMainPnl.add(controlBlog.getControlBlogContainer());

        return controlBlog;
    }

    public void addControlBlog(String caption, IsWidget isWidget) {
        String key = DmShared.xNull(caption).trim().toLowerCase();
        DmControlBlog controlBlog = null;

        if (isWidget != null) {
            controlBlog = new DmControlBlog(isWidget);
            hrtMainPnl.add(controlBlog.getControlBlogContainer());
        }

        controlBlogs.put(key, controlBlog);
    }

    public void addGap(int gapPercentage) {
        FlowPanel flwTemp = new FlowPanel();
        flwTemp.setWidth(String.valueOf(gapPercentage) + "%");
        hrtMainPnl.add(flwTemp);
        hrtMainPnl.setCellWidth(flwTemp, String.valueOf(gapPercentage) + "%");
    }

    public void addTextBlog(String caption, int width, boolean leftBoder, boolean rightBorder) {
        String key = DmShared.xNull(caption).trim().toLowerCase();
        DmTextBlog textBlog;
        VerticalPanel verticalPanel;

        if (textBlogs.containsKey(key)) {
            setTextBlog(caption, "");
        } else {
            verticalPanel = new VerticalPanel();
            textBlog = new DmTextBlog(caption, "", width, leftBoder, rightBorder);
            verticalPanel.add(textBlog.getCaptionLabel());
            verticalPanel.add(textBlog.getValueLabel());
            hrtMainPnl.add(verticalPanel);
            textBlogs.put(key, textBlog);
        }
    }

    public void setTextBlog(String caption, String value) {
        String key = DmShared.xNull(caption).trim().toLowerCase();
        DmTextBlog textBlog;

        textBlog = textBlogs.get(key);
        textBlog.setValue(value);
    }

    public String getTextBlog(String caption) {
        String key = DmShared.xNull(caption).trim().toLowerCase();
        DmTextBlog textBlog;

        textBlog = textBlogs.get(key);
        return textBlog.getValue();
    }

    public void setFocus() {
//        if (btnNew.isAttached()) {
//            btnNew.setFocus(true);
//        } else if (btnEdit.isAttached()) {
//            btnEdit.setFocus(true);
//        } else if (btnDelete.isAttached()) {
//            btnDelete.setFocus(true);
//        } else if (btnCard.isAttached()) {
//            btnCard.setFocus(true);
//        } else if (btnFind.isAttached()) {
//            btnFind.setFocus(true);
//        } else if (btnSave.isAttached()) {
//            btnSave.setFocus(true);
//        }
    }

    public String[] getUserControlKeys() {
        return controlBlogs.keySet().toArray(new String[controlBlogs.size()]);
    }

    public void disableAll() {
        String[] keys = getUserControlKeys();
        DmControlBlog controlBlog = null;

        if (keys != null) {
            for (int i = 0; i < keys.length; i++) {
                controlBlog = controlBlogs.get(keys[i]);
                if (controlBlog != null) {
                    controlBlog.setVisible(false);
                }
            }
        }
    }

    public void setVisible(String[] keys) {

        if (keys != null) {
            for (int i = 0; i < keys.length; i++) {
                setVisible(keys[i].toString());
            }
        }
    }

    public void setVisible(String caption) {
        String key = DmShared.xNull(caption).trim().toLowerCase();
        DmControlBlog controlBlog = null;

        if (controlBlogs != null) {
            controlBlog = controlBlogs.get(key);
            if (controlBlog != null) {
                controlBlog.setVisible(true);
            }
        }
    }

    public DmUserControl() {
        super();
    }

    @Override
    public Widget asWidget() {
        return hrtMainPnl;
    }
}
