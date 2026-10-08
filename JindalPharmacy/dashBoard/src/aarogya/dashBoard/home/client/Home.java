package aarogya.dashBoard.home.client;

import aarogya.dashBoard.advancesPendingForBillSettlement.client.AdvancesPendingForBillSettlementCard;
import aarogya.dashBoard.billDueNotPaid.client.BillDueNotPaidCard;
import aarogya.dashBoard.billPendingForApproval.client.BillPendingForApprovalCard;
import aarogya.dashBoard.billPendingForGRN.client.BillPendingForGRNCard;
import aarogya.dashBoard.dashBoardCard.client.DashBoardCard;
import aarogya.dashBoard.fastMovingMedicine.client.FastMovingMedicineCard;
import aarogya.dashBoard.inventoryPendingPO.client.InventoryPendingPOCard;
import aarogya.dashBoard.inventoryIndentPendingForApproval.client.InventoryIndentPendingForApprovalCard;
import aarogya.dashBoard.inventoryPendingIndent.client.InventoryPendingIndentCard;
import aarogya.dashBoard.medicineStockOut.client.MedicineStockOutCard;
import aarogya.dashBoard.nearExpiryMedicine.client.NearExpiryMedicineCard;
import aarogya.dashBoard.nonSoldMedicine.client.NonSoldMedicineCard;
import aarogya.dashBoard.requisitionInventory.client.RequisitionInventoryCard;
import aarogya.dashBoard.requisitionPharmacy.client.RequisitionPharmacyCard;
import aarogya.dashBoard.totalCashCollection.client.TotalCashCollectionCard;
import aarogya.dashBoard.totalRefundForDate.client.TotalRefundForDateCard;
import aarogya.dashBoard.walkThrough.client.WalkThroughCard;
import aarogya.dashBoard.whCollectionDashBoard.client.WHCollectionDashBoardCard;
import aarogya.projectMenu.self.shared.pmDashBoard;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;

public class Home extends Home_Dsg {

    private pmDashBoard pmMenu = new pmDashBoard();

    public Home(List<UserMenu> menuList) {
        super();
        setDashboardList(menuList);
    }

    private void setDashboardList(List<UserMenu> listUserMenu) {
        List<UserMenu> userMenus;

        userMenus = listUserMenu;
        if (userMenus != null && userMenus.size() > 0) {
            userMenus = userMenus.get(0).getUserMenu();
            if (userMenus != null && userMenus.size() > 0) {
                userMenus = userMenus.get(0).getUserMenu();
                for (UserMenu userMenu : userMenus) {
                    if (!DmShared.xNull(userMenu.getMenuName()).trim().equalsIgnoreCase("")) {
                        initializeCards(DmShared.xNull(userMenu.getMenuName()).trim());
                    }
                }
            }
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
    }

    private void initializeCards(String cardName) {
        DashBoardCard dashBoardCard;

        if (pmMenu.ITEM_REQUISITION.caption.equalsIgnoreCase(cardName)) {
            dashBoardCard = new RequisitionInventoryCard();
            dashBoardCard.initialze();
            addCard(dashBoardCard.asWidget());
        } else if (pmMenu.MEDICINE_REQUISITION.caption.equalsIgnoreCase(cardName)) {
            dashBoardCard = new RequisitionPharmacyCard();
            dashBoardCard.initialze();
            addCard(dashBoardCard.asWidget());
        } else if (pmMenu.WALK_THROUGH.caption.equalsIgnoreCase(cardName)) {
            dashBoardCard = new WalkThroughCard();
            dashBoardCard.initialze();
            addCard(dashBoardCard.asWidget());
        } else if (pmMenu.INVENTORY_PENDING_PO.caption.equalsIgnoreCase(cardName)) {
            dashBoardCard = new InventoryPendingPOCard();
            dashBoardCard.initialze();
            addCard(dashBoardCard.asWidget());
        } else if (pmMenu.PENDING_INDENT_FOR_APPROVAL.caption.equalsIgnoreCase(cardName)) {
            dashBoardCard = new InventoryIndentPendingForApprovalCard();
            dashBoardCard.initialze();
            addCard(dashBoardCard.asWidget());
        } else if (pmMenu.PENDING_INDENT.caption.equalsIgnoreCase(cardName)) {
            dashBoardCard = new InventoryPendingIndentCard();
            dashBoardCard.initialze();
            addCard(dashBoardCard.asWidget());
        } else if (pmMenu.NEAR_EXPIRY_MEDICINE.caption.equalsIgnoreCase(cardName)) {
            dashBoardCard = new NearExpiryMedicineCard();
            dashBoardCard.initialze();
            addCard(dashBoardCard.asWidget());
        } else if (pmMenu.MEDICINE_STOCK_OUT.caption.equalsIgnoreCase(cardName)) {
            dashBoardCard = new MedicineStockOutCard();
            dashBoardCard.initialze();
            addCard(dashBoardCard.asWidget());
        } else if (pmMenu.NON_SOLD_MEDICINE.caption.equalsIgnoreCase(cardName)) {
            dashBoardCard = new NonSoldMedicineCard();
            dashBoardCard.initialze();
            addCard(dashBoardCard.asWidget());
        } else if (pmMenu.FAST_MOVING_MEDICINE.caption.equalsIgnoreCase(cardName)) {
            dashBoardCard = new FastMovingMedicineCard();
            dashBoardCard.initialze();
            addCard(dashBoardCard.asWidget());
        } //
        else if (pmMenu.TOTAL_CASH_COLLECTION.caption.equalsIgnoreCase(cardName)) {
            dashBoardCard = new TotalCashCollectionCard();
            dashBoardCard.initialze();
            addCard(dashBoardCard.asWidget());
        } else if (pmMenu.TOTAL_REFUND_FOR_DATE.caption.equalsIgnoreCase(cardName)) {
            dashBoardCard = new TotalRefundForDateCard();
            dashBoardCard.initialze();
            addCard(dashBoardCard.asWidget());
        } else if (pmMenu.BILL_PENDING_FOR_GRN.caption.equalsIgnoreCase(cardName)) {
            dashBoardCard = new BillPendingForGRNCard();
            dashBoardCard.initialze();
            addCard(dashBoardCard.asWidget());
        } else if (pmMenu.BILL_PENDING_FOR_APPROVAL.caption.equalsIgnoreCase(cardName)) {
            dashBoardCard = new BillPendingForApprovalCard();
            dashBoardCard.initialze();
            addCard(dashBoardCard.asWidget());
        } else if (pmMenu.BILL_DUE_NOT_PAID.caption.equalsIgnoreCase(cardName)) {
            dashBoardCard = new BillDueNotPaidCard();
            dashBoardCard.initialze();
            addCard(dashBoardCard.asWidget());
        } else if (pmMenu.ADV_PENDING_FOR_SETTLEMENT.caption.equalsIgnoreCase(cardName)) {
            dashBoardCard = new AdvancesPendingForBillSettlementCard();
            dashBoardCard.initialze();
            addCard(dashBoardCard.asWidget());
        }else if (pmMenu.WHOLE_SALE_COLLECTION_DASHBOARD.caption.equalsIgnoreCase(cardName)) {
            dashBoardCard = new WHCollectionDashBoardCard();
            dashBoardCard.initialze();
            addCard(dashBoardCard.asWidget());
        }

    }

    private void addCard(Widget widget) {
        widget.addStyleName("dashBoard-cardStyle dashBoard-card-collapse");
        flwCardContainer.add(widget);
    }

}
