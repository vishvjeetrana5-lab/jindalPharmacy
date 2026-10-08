package dataman.dmToolkit.self.shared;

import com.google.gwt.user.client.ui.IsWidget;
import java.util.Objects;

public class ViaListWidget {

    String name;
    IsWidget isWidget;

    public ViaListWidget(String name, IsWidget isWidget) {
        this.name = name;
        this.isWidget = isWidget;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IsWidget getIsWidget() {
        return isWidget;
    }

    public void setIsWidget(IsWidget isWidget) {
        this.isWidget = isWidget;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.isWidget);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ViaListWidget other = (ViaListWidget) obj;
        if (!Objects.equals(this.isWidget, other.isWidget)) {
            return false;
        }
        return true;
    }
}
