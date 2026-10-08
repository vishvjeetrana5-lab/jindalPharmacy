package dataman.dmToolkit.dmSuggestBox.client;

import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;

class DmSuggestBox_Dsg extends TextBox {

    final PopupPanel ppSuggestion = new PopupPanel(true);
    final ListBox lbxSuggestion = new ListBox(true);

    public DmSuggestBox_Dsg() {
        super();
        this.setWidth("100%");
        ppSuggestion.setHeight("100px");

        lbxSuggestion.setWidth("100%");
        lbxSuggestion.setHeight("100%");
        ppSuggestion.add(lbxSuggestion);
    }
}
