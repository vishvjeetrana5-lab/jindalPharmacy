package dataman.dmToolkit.self.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface DmToolkit_Res extends ClientBundle {

    public static final DmToolkit_Res rsObj = GWT.create(DmToolkit_Res.class);

    @Source("DmToolkit_Style.css")
    dmCss dcStyle();

    @Source("dataman/dmToolkit/self/resources/image/Add.png")
    ImageResource imgAdd();

    @Source("dataman/dmToolkit/self/resources/image/Cancel.png")
    ImageResource imgCancel();

    @Source("dataman/dmToolkit/self/resources/image/Card.png")
    ImageResource imgCard();

    @Source("dataman/dmToolkit/self/resources/image/Delete.png")
    ImageResource imgDelete();

    @Source("dataman/dmToolkit/self/resources/image/Edit.png")
    ImageResource imgEdit();

    @Source("dataman/dmToolkit/self/resources/image/Exit.png")
    ImageResource imgExit();

    @Source("dataman/dmToolkit/self/resources/image/Find.png")
    ImageResource imgFind();

    @Source("dataman/dmToolkit/self/resources/image/First.png")
    ImageResource imgFirst();

    @Source("dataman/dmToolkit/self/resources/image/Info.png")
    ImageResource imgInfo();

    @Source("dataman/dmToolkit/self/resources/image/Last.png")
    ImageResource imgLast();

    @Source("dataman/dmToolkit/self/resources/image/Next.png")
    ImageResource imgNext();

    @Source("dataman/dmToolkit/self/resources/image/Previous.png")
    ImageResource imgPrevious();

    @Source("dataman/dmToolkit/self/resources/image/Print.png")
    ImageResource imgPrint();

    @Source("dataman/dmToolkit/self/resources/image/Refresh.png")
    ImageResource imgRefresh();

    @Source("dataman/dmToolkit/self/resources/image/Save.png")
    ImageResource imgSave();

    @Source("dataman/dmToolkit/self/resources/image/ProgressBar.gif")
    ImageResource imgProgressBar();

    public interface dmCss extends CssResource {

        @ClassName("dmTopControl")
        String strDmTopControl();
    }
}
