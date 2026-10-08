package aarogya.nabh.self.shared;

import aarogya.projectMenu.self.shared.pmNABH;
import dataman.dmBase.self.shared.UserMenu;
import java.util.ArrayList;
import java.util.List;

public class NABH_Model {

    pmNABH pmMenu = new pmNABH();

    public List<UserMenu> getMenu() {
        List<UserMenu> menuList = new ArrayList<>();
        List<UserMenu> mainMenuList;

        menuList.add(new UserMenu("Transactions"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getNABH_Navigation(mainMenuList);
        getIndicators_1_20(mainMenuList);
        getIndicators_21_40(mainMenuList);
        getIndicators_41_60(mainMenuList);
        getIndicators_61_70(mainMenuList);

        menuList.add(new UserMenu("Reports"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getReports(mainMenuList);

        return menuList;
    }

//<editor-fold defaultstate="collapsed" desc="NABH Navigation">
    private void getNABH_Navigation(List<UserMenu> menuList) {
        menuList.add(new UserMenu("General"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.NABH_NAVIGATION.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.PATIENT_INFECTION_ENTRY.caption));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Indicators 1-20">
    private void getIndicators_1_20(List<UserMenu> menuList) {
        menuList.add(new UserMenu("Indicators 1-20"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.Indicator_1.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.Indicator_2.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.Indicator_3.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.Indicator_4.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.Indicator_5.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.Indicator_6.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.Indicator_7.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.Indicator_8.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.Indicator_9.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.Indicator_10.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.Indicator_11.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.Indicator_12.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.Indicator_13.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.Indicator_14.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.Indicator_15.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.Indicator_16.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.Indicator_17.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.Indicator_18.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.Indicator_19.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.Indicator_20.caption));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Indicators 21-40">
    private void getIndicators_21_40(List<UserMenu> menuList) {
        menuList.add(new UserMenu("Indicators 21-40"));

        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 21"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 22"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 23"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 24"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 25"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 26"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 27"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 28"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 29"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 30"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 31"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 32"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 33"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 34"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 35"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 36"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 37"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 38"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 39"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 40"));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Indicators 41-60">
    private void getIndicators_41_60(List<UserMenu> menuList) {
        menuList.add(new UserMenu("Indicators 41-60"));

        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 41"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 42"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 43"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 44"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 45"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 46"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 47"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 48"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 49"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 50"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 51"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 52"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 53"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 54"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 55"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 56"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 57"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 58"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 59"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 60"));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Indicators 61-70">
    private void getIndicators_61_70(List<UserMenu> menuList) {
        menuList.add(new UserMenu("Indicators 61-70"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 61"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 62"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 63"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 64"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 65"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 66"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 67"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 68"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 69"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("Indicator 70"));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Reports">
    private void getReports(List<UserMenu> menuList) {
        menuList.add(new UserMenu("General Reports"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.NABH_INDICATORS_SUMMARY.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.INDICATOR_10_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.INDICATOR_13_REPORT.caption));
    }
//</editor-fold>

}
