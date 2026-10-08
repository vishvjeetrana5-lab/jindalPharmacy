package dataman.accounts.self.shared;

public class AcEnum {

    public enum PageName {

        BalanceSheet("BalanceSheet", 0),
        ProfitLoss("ProfitLoss", 1),
        AcGroup("AcGroup", 2),
        TrialBalance("TrialBalance", 3),
        DetailTrialBalance("DetailTrialBalance", 4),
        Ledger("Ledger", 5),
        BalanceSheetCompare("BalanceSheetCompare", 6),
        ProfitLossCompare("ProfitLossCompare", 7),
        AcGroupCompare("AcGroupCompare", 8),
        TrialBalanceCompare("TrialBalanceCompare", 9);

        private final int pageValue;
        private final String pageType;

        public int getPageValue() {
            return pageValue;
        }

        public String getPageType() {
            return pageType;
        }

        private PageName(String type, int value) {
            this.pageValue = value;
            this.pageType = type;
        }

        public static PageName pageName(int pageValue) {
            PageName rtnPage = null;

            if (PageName.BalanceSheet.getPageValue() == pageValue) {
                rtnPage = PageName.BalanceSheet;
            } else if (PageName.AcGroup.getPageValue() == pageValue) {
                rtnPage = PageName.AcGroup;
            } else if (PageName.DetailTrialBalance.getPageValue() == pageValue) {
                rtnPage = PageName.DetailTrialBalance;
            } else if (PageName.Ledger.getPageValue() == pageValue) {
                rtnPage = PageName.Ledger;
            } else if (PageName.ProfitLoss.getPageValue() == pageValue) {
                rtnPage = PageName.ProfitLoss;
            } else if (PageName.TrialBalance.getPageValue() == pageValue) {
                rtnPage = PageName.TrialBalance;
            } else if (PageName.TrialBalanceCompare.getPageValue() == pageValue) {
                rtnPage = PageName.TrialBalanceCompare;
            } else if (PageName.BalanceSheetCompare.getPageValue() == pageValue) {
                rtnPage = PageName.BalanceSheetCompare;
            } else if (PageName.AcGroupCompare.getPageValue() == pageValue) {
                rtnPage = PageName.AcGroupCompare;
            }
            return rtnPage;
        }
    }

    public enum CodeType {

        Group("Group", 0),
        SubGroup("SubGroup", 1);

        private final int pageValue;
        private final String pageType;

        public int getPageValue() {
            return pageValue;
        }

        public String getPageType() {
            return pageType;
        }

        private CodeType(String type, int value) {
            this.pageValue = value;
            this.pageType = type;
        }

        public static CodeType codeType(int pageValue) {
            CodeType rtnPage = null;

            if (CodeType.Group.getPageValue() == pageValue) {
                rtnPage = CodeType.Group;
            } else if (CodeType.SubGroup.getPageValue() == pageValue) {
                rtnPage = CodeType.SubGroup;
            }
            return rtnPage;
        }
    }
}
