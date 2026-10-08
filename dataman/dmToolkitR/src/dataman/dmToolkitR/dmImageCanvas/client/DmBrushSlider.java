package dataman.dmToolkitR.dmImageCanvas.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import dataman.dmToolkitR.dmLabel.client.DmLabel;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.extras.slider.client.ui.Slider;
import org.gwtbootstrap3.extras.slider.client.ui.base.constants.HandleType;

public class DmBrushSlider {

    public interface DmBrushSliderCloseCallback {

        public void brushSliderClosing(int sliderValue);
    }
    Modal modal = new Modal();
    private ModalHeader modalHeader = new ModalHeader();
    private ModalBody modalBody = new ModalBody();
    private Slider slider;
    private DmBrushSliderCloseCallback callback;
    private Button btnClose;
    private DmLabel sliderCurrentValue;

    public DmBrushSlider(DmBrushSliderCloseCallback closeCallback, int initialValue) {
        super();
        this.callback = closeCallback;
        slider = new Slider();
        sliderCurrentValue = new DmLabel("" + initialValue);
        sliderCurrentValue.getElement().getStyle().setFontSize(17, Style.Unit.PX);
        btnUI();
        slider.setMin(0);
        slider.setMax(20);
        slider.setStep(1);
        slider.setValue(new Double(initialValue));
        slider.setWidth("100%");
        slider.setHandle(HandleType.ROUND);
        modal.setDataBackdrop(ModalBackdrop.STATIC);
        modal.setSize(ModalSize.SMALL);
        modalHeader.add(btnClose);
        modalHeader.setTitle("Brush Size");
        modalHeader.setClosable(false);
        modalBody.add(sliderCurrentValue);
        modalBody.add(slider);
        modal.add(modalHeader);
        modal.add(modalBody);

    }

    private void btnUI() {

        this.btnClose = new Button();
        Anchor an = new Anchor();
        an.addStyleName("fa fa-close");
        this.btnClose.getElement().appendChild(an.getElement());
        btnClose.addStyleName("close");
        this.btnClose.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                closePopup();
            }
        });

        slider.addValueChangeHandler(new ValueChangeHandler<Double>() {
            @Override
            public void onValueChange(ValueChangeEvent<Double> event) {

                sliderCurrentValue.setText("" + slider.getValue());
            }
        });
    }

    private void closePopup() {
        if (this.callback != null) {
            this.callback.brushSliderClosing(DmShared.vNull(slider.getValue()).intValue());
        }
        modal.hide();
    }

    public void show() {
        modal.show();
    }
}
