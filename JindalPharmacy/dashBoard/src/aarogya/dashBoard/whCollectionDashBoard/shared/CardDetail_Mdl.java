package aarogya.dashBoard.whCollectionDashBoard.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmToolkit.dmChart.shared.DmChart_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CardDetail_Mdl implements Serializable, IsSerializable {

    private String count;
    private List<DmChart_Mdl> listChardData = new ArrayList<DmChart_Mdl>();

    public CardDetail_Mdl() {
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<DmChart_Mdl> getListChardData() {
        return listChardData;
    }
}
