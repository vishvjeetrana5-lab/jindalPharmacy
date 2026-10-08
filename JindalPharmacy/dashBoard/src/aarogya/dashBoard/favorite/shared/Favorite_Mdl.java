package aarogya.dashBoard.favorite.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Favorite_Mdl extends KeyField implements Serializable, IsSerializable {

    public final static String allModule = "All Module";
    public final static String favModule = "Favorite Module";
    public final static String allMenu = "All Menu";
    public final static String favMenu = "Favorite Menu";
    public final static String grdModule = "Module";
    public final static String grdMenu = "Menu";

    private String allModuleName;
    private String allMenuName;
    private String allMenuText;
    private String favModuleName;
    private String favMenuName;
    private String favMenuText;
    private String grdType;

    public Favorite_Mdl() {
    }

    public String getAllModuleName() {
        return allModuleName;
    }

    public void setAllModuleName(String allModuleName) {
        this.allModuleName = allModuleName;
    }

    public String getAllMenuName() {
        return allMenuName;
    }

    public void setAllMenuName(String allMenuName) {
        this.allMenuName = allMenuName;
    }

    public String getFavModuleName() {
        return favModuleName;
    }

    public void setFavModuleName(String favModuleName) {
        this.favModuleName = favModuleName;
    }

    public String getFavMenuName() {
        return favMenuName;
    }

    public void setFavMenuName(String favMenuName) {
        this.favMenuName = favMenuName;
    }

    public String getAllMenuText() {
        return allMenuText;
    }

    public void setAllMenuText(String allMenuText) {
        this.allMenuText = allMenuText;
    }

    public String getFavMenuText() {
        return favMenuText;
    }

    public void setFavMenuText(String favMenuText) {
        this.favMenuText = favMenuText;
    }

    public String getGrdType() {
        return grdType;
    }

    public void setGrdType(String grdType) {
        this.grdType = grdType;
    }

}
