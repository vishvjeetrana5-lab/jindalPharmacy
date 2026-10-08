package aarogya.mis.reports.server;

import aarogya.mis.reports.shared.ConsultantSharingFilter_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConsultantSharingTemp111_Server {

    public List<ConsultantSharingFilter_Mdl> getSharingRules111(String consultantCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<ConsultantSharingFilter_Mdl> listNode = new ArrayList<ConsultantSharingFilter_Mdl>();
        ConsultantSharingFilter_Mdl node;
        String sqlQuery;

        try {
            sqlQuery = "Select	csm.applicableFrom, csm.applicableTo, csm.organizationCode, csm.moduleName, "
                    + "         csm.serviceCode, csm.serviceGroupCode, csm.serviceCategoryCode, "
                    + "         csm.headerConsultantIsAmount, csm.headerConsultantAmount, csm.headerReferredIsAmount, csm.headerReferredAmount, "
                    + "         csm.lineConsultantIsAmount, csm.lineConsultantAmount, csm.lineReferredIsAmount, csm.lineReferredAmount "
                    + "		from consultantSharingMast as csm "
                    + "		where csm.consultantCode = :consultantCode "
                    + "         order by csm.organizationCode desc, csm.moduleName desc, csm.serviceCode desc, csm.serviceGroupCode desc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("consultantCode", DmShared.iNull(consultantCode).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new ConsultantSharingFilter_Mdl();
                node.setApplicableFrom(rsTemp.getDate("applicableFrom"));
                node.setApplicableTo(rsTemp.getDate("applicableTo"));
                node.setOrganizationCode(DmShared.iNull(rsTemp.getString("organizationCode")).longValue());
                node.setModuleName(DmShared.xNull(rsTemp.getString("moduleName")).trim());
                node.setServiceCode(DmShared.iNull(rsTemp.getString("serviceCode")).intValue());
                node.setServiceGroupCode(DmShared.iNull(rsTemp.getString("serviceGroupCode")).intValue());
                node.setServiceCategoryCode(DmShared.iNull(rsTemp.getString("serviceCategoryCode")).intValue());
                node.setHeaderConsultantIsAmount(DmShared.iNull(rsTemp.getString("headerConsultantIsAmount")).intValue());
                node.setHeaderReferredIsAmount(DmShared.iNull(rsTemp.getString("headerReferredIsAmount")).intValue());
                node.setLineConsultantIsAmount(DmShared.iNull(rsTemp.getString("lineConsultantIsAmount")).intValue());
                node.setLineReferredIsAmount(DmShared.iNull(rsTemp.getString("lineReferredIsAmount")).intValue());
                node.setHeaderConsultantAmount(DmShared.vNull(rsTemp.getString("headerConsultantAmount")).doubleValue());
                node.setHeaderReferredAmount(DmShared.vNull(rsTemp.getString("headerReferredAmount")).doubleValue());
                node.setLineConsultantAmount(DmShared.vNull(rsTemp.getString("lineConsultantAmount")).doubleValue());
                node.setLineReferredAmount(DmShared.vNull(rsTemp.getString("lineReferredAmount")).doubleValue());
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception ex) {
            throw new DmException(0, ex.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return listNode;
    }

    public List<ConsultantSharingFilter_Mdl> getBestFit1111(List<ConsultantSharingFilter_Mdl> filterList, ResultSet rsTemp) {
        List<ConsultantSharingFilter_Mdl> listTemp = null;

        if (filterList != null) {
            try {
                Date serviceDate = rsTemp.getDate("serviceDate");

                /*=================< 1 >=================*/
                listTemp = filterList.stream().filter(x -> (x.getApplicableFrom().getTime() <= serviceDate.getTime()
                        && x.getApplicableTo().getTime() >= serviceDate.getTime())).collect(Collectors.toList());

                if (listTemp != null && listTemp.size() > 0) {
                    Long organizationCode = DmShared.iNull(rsTemp.getString("organizationCode")).longValue();
                    Integer serviceCode = DmShared.iNull(rsTemp.getString("serviceCode")).intValue();
                    Integer categoryCode = DmShared.iNull(rsTemp.getString("categoryCode")).intValue();
                    Integer groupCode = DmShared.iNull(rsTemp.getString("groupCode")).intValue();
                    String moduleName = DmShared.xNull(rsTemp.getString("moduleName")).trim();

                    listTemp = null;
                    /*=================< 2 >=================*/
                    listTemp = filterList.stream().filter(x -> (DmShared.iNull(x.getOrganizationCode()).longValue() == organizationCode)
                            && (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(moduleName))
                            && (DmShared.iNull(x.getServiceCode()).intValue() == serviceCode)
                    ).collect(Collectors.toList());

                    if (listTemp == null || listTemp.size() == 0) {
                        /*=================< 3 >=================*/
                        listTemp = filterList.stream().filter(x -> (DmShared.iNull(x.getOrganizationCode()).longValue() == organizationCode)
                                && (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(moduleName))
                                && (DmShared.iNull(x.getServiceGroupCode()).intValue() == groupCode)
                        ).collect(Collectors.toList());

                        if (listTemp == null || listTemp.size() == 0) {
                            /*=================< 4 >=================*/
                            listTemp = filterList.stream().filter(x -> (DmShared.iNull(x.getOrganizationCode()).longValue() == organizationCode)
                                    && (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(moduleName))
                                    && (DmShared.iNull(x.getServiceCategoryCode()).intValue() == categoryCode)
                            ).collect(Collectors.toList());

                            if (listTemp == null || listTemp.size() == 0) {
                                /*=================< 5 >=================*/
                                listTemp = filterList.stream().filter(x -> (DmShared.iNull(x.getOrganizationCode()).longValue() == organizationCode)
                                        && (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(moduleName))
                                        && (DmShared.iNull(x.getServiceCode()).intValue() == 0) && (DmShared.iNull(x.getServiceGroupCode()).intValue() == 0)
                                        && (DmShared.iNull(x.getServiceCategoryCode()).intValue() == 0)
                                ).collect(Collectors.toList());

                                if (listTemp == null || listTemp.size() == 0) {
                                    /*=================< 6 >=================*/
                                    listTemp = filterList.stream().filter(x -> (DmShared.iNull(x.getOrganizationCode()).longValue() == organizationCode)
                                            && (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(""))
                                            && (DmShared.iNull(x.getServiceCode()).intValue() == serviceCode)
                                    ).collect(Collectors.toList());

                                    if (listTemp == null || listTemp.size() == 0) {
                                        /*=================< 7 >=================*/
                                        listTemp = filterList.stream().filter(x -> (DmShared.iNull(x.getOrganizationCode()).longValue() == organizationCode)
                                                && (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(""))
                                                && (DmShared.iNull(x.getServiceGroupCode()).intValue() == groupCode)
                                        ).collect(Collectors.toList());

                                        if (listTemp == null || listTemp.size() == 0) {
                                            /*=================< 8 >=================*/
                                            listTemp = filterList.stream().filter(x -> (DmShared.iNull(x.getOrganizationCode()).longValue() == organizationCode)
                                                    && (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(""))
                                                    && (DmShared.iNull(x.getServiceCategoryCode()).intValue() == categoryCode)
                                            ).collect(Collectors.toList());

                                            if (listTemp == null || listTemp.size() == 0) {
                                                /*=================< 9 >=================*/
                                                listTemp = filterList.stream().filter(x -> (DmShared.iNull(x.getOrganizationCode()).longValue() == 0)
                                                        && (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(moduleName))
                                                        && (DmShared.iNull(x.getServiceCode()).intValue() == serviceCode)
                                                ).collect(Collectors.toList());

                                                if (listTemp == null || listTemp.size() == 0) {
                                                    /*=================< 10 >=================*/
                                                    listTemp = filterList.stream().filter(x -> (DmShared.iNull(x.getOrganizationCode()).longValue() == 0)
                                                            && (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(moduleName))
                                                            && (DmShared.iNull(x.getServiceGroupCode()).intValue() == groupCode)
                                                    ).collect(Collectors.toList());

                                                    if (listTemp == null || listTemp.size() == 0) {
                                                        /*=================< 11 >=================*/
                                                        listTemp = filterList.stream().filter(x -> (DmShared.iNull(x.getOrganizationCode()).longValue() == 0)
                                                                && (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(moduleName))
                                                                && (DmShared.iNull(x.getServiceCategoryCode()).intValue() == categoryCode)
                                                        ).collect(Collectors.toList());

                                                        if (listTemp == null || listTemp.size() == 0) {
                                                            /*=================< 11 >=================*/
                                                            listTemp = filterList.stream().filter(x -> (DmShared.iNull(x.getOrganizationCode()).longValue() == 0)
                                                                    && (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(""))
                                                                    && (DmShared.iNull(x.getServiceCode()).intValue() == serviceCode)
                                                            ).collect(Collectors.toList());

                                                            if (listTemp == null || listTemp.size() == 0) {
                                                                /*=================< 12 >=================*/
                                                                listTemp = filterList.stream().filter(x -> (DmShared.iNull(x.getOrganizationCode()).longValue() == 0)
                                                                        && (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(""))
                                                                        && (DmShared.iNull(x.getServiceGroupCode()).intValue() == groupCode)
                                                                ).collect(Collectors.toList());

                                                                if (listTemp == null || listTemp.size() == 0) {
                                                                    /*=================< 13 >=================*/
                                                                    listTemp = filterList.stream().filter(x -> (DmShared.iNull(x.getOrganizationCode()).longValue() == 0)
                                                                            && (DmShared.xNull(x.getModuleName()).trim().equalsIgnoreCase(""))
                                                                            && (DmShared.iNull(x.getServiceCategoryCode()).intValue() == categoryCode)
                                                                    ).collect(Collectors.toList());
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new DmException(0, e.getMessage());
            }
        }

        return listTemp;
    }
}
