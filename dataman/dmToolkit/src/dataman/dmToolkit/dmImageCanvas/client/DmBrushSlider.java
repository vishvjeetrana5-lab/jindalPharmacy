/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.dmToolkit.dmImageCanvas.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.widget.core.client.Slider;

/**
 *
 * @author abhishek
 */
public class DmBrushSlider extends PopupPanel {

    public interface DmBrushSliderCloseCallback {

        public void brushSliderClosing(int sliderValue);
    }

    private Slider slider;

    private DmBrushSliderCloseCallback callback;

    private VerticalPanel mainPanel;
    private Button cmdClose;    

    public DmBrushSlider(DmBrushSliderCloseCallback closeCallback,int initialValue) {
        super(false, true);
        this.callback = closeCallback;

        prepareUI();
        
        this.slider.setValue(initialValue, true, true);

        setWidget(this.mainPanel);
    }

    private void prepareUI() {
        this.mainPanel = new VerticalPanel();

        this.cmdClose = new Button();
        Anchor an = new Anchor();
        an.addStyleName("fa fa-close");
        an.getElement().getStyle().setColor("red");
        this.cmdClose.getElement().appendChild(an.getElement());

        this.cmdClose.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                closePopup();
            }
        });

        this.mainPanel.add(this.cmdClose);

        slider = new Slider(true);
        slider.setIncrement(1);
        slider.setMaxValue(100);
        slider.setMinValue(1);

        this.mainPanel.add(this.slider);
    }

    private void closePopup() {
        if (this.callback != null) {
            this.callback.brushSliderClosing(this.slider.getValue());
        }
        this.hide(true);
    }

}
