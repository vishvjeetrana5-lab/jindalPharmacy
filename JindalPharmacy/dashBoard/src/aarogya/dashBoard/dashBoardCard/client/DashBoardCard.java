package aarogya.dashBoard.dashBoardCard.client;

import aarogya.projectMenu.self.shared.pmDashBoard;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;

public abstract class DashBoardCard extends DashBoardCard_Dsg implements IsWidget, ClickHandler {

    public abstract void initialze();

    public abstract void anchorClicked(ClickEvent event);

    public DashBoardCard() {
        super();
        manageEvent();
    }

    private void manageEvent() {
        lblName.addClickHandler(this);
        expandAnchor.addClickHandler(this);
        collapseAnchor.addClickHandler(this);
        externalLinkAnchor.addClickHandler(this);
    }

    public FlowPanel getWidget() {
        return cardFlowPanel;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(this.expandAnchor)) {
            cardFlowPanel.removeStyleName("dashBoard-card-collapse");
            cardFlowPanel.addStyleName("dashBoard-card-expand");

//  cardFlowPanel.addStyleName("dashBoard-card-expandLarg");
            expandCollapseFlowPanel.clear();
            expandCollapseFlowPanel.add(collapseAnchor);
            cardFlowPanel.remove(countSection());
            cardFlowPanel.add(chartSection());
            anchorClicked(event);

        } else if (event.getSource().equals(this.collapseAnchor)) {
            cardFlowPanel.removeStyleName("dashBoard-card-expand");
            cardFlowPanel.addStyleName("dashBoard-card-collapse");

            expandCollapseFlowPanel.clear();
            expandCollapseFlowPanel.add(expandAnchor);
            cardFlowPanel.remove(chartSection());
            cardFlowPanel.add(countSection());
            anchorClicked(event);

        } else if (event.getSource().equals(this.externalLinkAnchor)) {
            anchorClicked(event);
        } else if (event.getSource().equals(this.lblName)) {
            initialze();
        }
    }
}
