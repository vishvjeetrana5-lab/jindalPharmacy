package dataman.dmToolkit.dmDateBox.client;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.DateBox;

abstract class DmDateBox_Dsg extends DateBox {

    final DateTimeFormat dateFormat;

    public DmDateBox_Dsg() {
        super();
        this.dateFormat = DateTimeFormat.getFormat("dd-MMM-yyyy");
        this.setWidth("100%");
        this.getElement().setAttribute("border-color", "lightGray");
        this.getElement().setClassName("gwt-TextBox");
        super.setFormat(new DateBox.DefaultFormat(dateFormat));
        super.getDatePicker().setYearArrowsVisible(true);
        super.getDatePicker().setYearAndMonthDropdownVisible(true);
    }

}
