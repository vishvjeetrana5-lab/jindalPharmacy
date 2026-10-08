package aarogya.inventory.purchaseOrder.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class Tolerance_Mdl implements Serializable, IsSerializable {

    private String tl_ExcessPer;
    private String tl_ExcessQty;
    private String tl_ShortagePer;
    private String tl_ShortageQty;
    private String orderQuantity;
    private String orderQuantityWithExcess;
    private String orderQuantityWithShortage;

    public Tolerance_Mdl() {
    }

    public String getTl_ExcessPer() {
        return tl_ExcessPer;
    }

    public void setTl_ExcessPer(String tl_ExcessPer) {
        this.tl_ExcessPer = tl_ExcessPer;
    }

    public String getTl_ExcessQty() {
        return tl_ExcessQty;
    }

    public void setTl_ExcessQty(String tl_ExcessQty) {
        this.tl_ExcessQty = tl_ExcessQty;
    }

    public String getTl_ShortagePer() {
        return tl_ShortagePer;
    }

    public void setTl_ShortagePer(String tl_ShortagePer) {
        this.tl_ShortagePer = tl_ShortagePer;
    }

    public String getTl_ShortageQty() {
        return tl_ShortageQty;
    }

    public void setTl_ShortageQty(String tl_ShortageQty) {
        this.tl_ShortageQty = tl_ShortageQty;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getOrderQuantityWithExcess() {
        return orderQuantityWithExcess;
    }

    public void setOrderQuantityWithExcess(String orderQuantityWithExcess) {
        this.orderQuantityWithExcess = orderQuantityWithExcess;
    }

    public String getOrderQuantityWithShortage() {
        return orderQuantityWithShortage;
    }

    public void setOrderQuantityWithShortage(String orderQuantityWithShortage) {
        this.orderQuantityWithShortage = orderQuantityWithShortage;
    }

}
