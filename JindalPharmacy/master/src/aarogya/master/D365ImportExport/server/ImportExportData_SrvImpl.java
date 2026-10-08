package aarogya.master.D365ImportExport.server;

import aarogya.master.D365ImportExport.shared.ImportExportData_Srv;
import aarogya.master.D365ImportExport.shared.MedicineMaster_Mdl;
import aarogya.selfToolkit.D365_To_His_DataSync.server.ItemMasterSync;
import aarogya.selfToolkit.D365_To_His_DataSync.server.CommanScheduler;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ImportExportData_SrvImpl extends DmRSSPrivate implements ImportExportData_Srv {

    @Override
    public DmResult<String> exportCancelIPDBills(String postingFromDate) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        Connection cnTempStaging = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            String stagingDB = DmServer.executeScalar("select bc_databaseName from enviroCompany", cnTemp);
            cnTempStaging = DmServer.getConnection(stagingDB);
            spBegin = new DmSavePoint(cnTempStaging);
            CommanScheduler.exportCancelbill(cnTemp, cnTempStaging, postingFromDate);
            spBegin.commit();
            cnTemp.close();
            cnTempStaging.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
                if (e instanceof DmException) {
                    dmrTemp.setErrorNumber(((DmException) e).getErrorNo());
                    dmrTemp.setValue1(((DmException) e).getErrorSource());
                    dmrTemp.setValue2(((DmException) e).getErrorSourceCode());
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null) {
                    cnTemp.close();
                }
                if (cnTempStaging != null) {
                    cnTempStaging.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<String> getTpaSettlementBills(String postingFromDate) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        Connection cnTempStaging = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            String stagingDB = DmServer.executeScalar("select bc_databaseName from enviroCompany", cnTemp);
            cnTempStaging = DmServer.getConnection(stagingDB);
            spBegin = new DmSavePoint(cnTempStaging);
            CommanScheduler.exportCancelbill(cnTemp, cnTempStaging, postingFromDate);
            spBegin.commit();
            cnTemp.close();
            cnTempStaging.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
                if (e instanceof DmException) {
                    dmrTemp.setErrorNumber(((DmException) e).getErrorNo());
                    dmrTemp.setValue1(((DmException) e).getErrorSource());
                    dmrTemp.setValue2(((DmException) e).getErrorSourceCode());
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null) {
                    cnTemp.close();
                }
                if (cnTempStaging != null) {
                    cnTempStaging.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<String> exportPaymentDetails(String postingFromDate) {

        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        Connection cnTempCompany = null;
        Connection cnTempStaging = null;
        Connection stagingCnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            cnTempCompany = DmServer.getConnection(DmServer.dbCompany);
            String transactioDB = DmServer.executeScalar(" select top 1 centralData_Path From company order by v_Prefix desc ", cnTempCompany);
            cnTempCompany.close();
            String stagingDB = DmServer.executeScalar("select bc_databaseName from enviroCompany", cnTemp);
            cnTempStaging = DmServer.getConnection(stagingDB);
            spBegin = new DmSavePoint(cnTempStaging);
            CommanScheduler.postHisPaymentDetails(cnTemp, cnTempStaging, transactioDB, postingFromDate);
            spBegin.commit();
            dmrTemp.setStatus(true);

        } catch (Exception e) {

            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }

            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));

        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null) {
                    cnTemp.close();
                }
                if (cnTempCompany != null) {
                    cnTemp.close();
                }
                if (cnTempStaging != null) {
                    cnTemp.close();
                }
                if (stagingCnTemp != null) {
                    stagingCnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<String> exportUnBilledService(String postingFromDate) {

        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        Connection cnTempCompany = null;
        Connection cnTempStaging = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            cnTempCompany = DmServer.getConnection(DmServer.dbCompany);
            String transactioDB = DmServer.executeScalar(" select top 1 centralData_Path From company order by v_Prefix desc ", cnTempCompany);
            cnTempCompany.close();
            String stagingDB = DmServer.executeScalar("select bc_databaseName from enviroCompany", cnTemp);
            cnTempStaging = DmServer.getConnection(stagingDB);
            spBegin = new DmSavePoint(cnTempStaging);
            CommanScheduler.postUnbilledIPDServiceBill(cnTemp, cnTempStaging, transactioDB, postingFromDate, "");
            spBegin.commit();
            dmrTemp.setStatus(true);

        } catch (Exception e) {

            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }

            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));

        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null) {
                    cnTemp.close();
                }
                if (cnTempCompany != null) {
                    cnTemp.close();
                }
                if (cnTempStaging != null) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<String> exportStockTransfer(String postingFromDate) {

        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        Connection cnTempCompany = null;
        Connection cnTempStaging = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            cnTempCompany = DmServer.getConnection(DmServer.dbCompany);
            String transactioDB = DmServer.executeScalar(" select top 1 centralData_Path From company order by v_Prefix desc ", cnTempCompany);
            cnTempCompany.close();
            String stagingDB = DmServer.executeScalar("select bc_databaseName from enviroCompany", cnTemp);
            cnTempStaging = DmServer.getConnection(stagingDB);
            spBegin = new DmSavePoint(cnTempStaging);

//            int cnt = DmShared.iNull(DmServer.executeScalar("select count(documentNumber) as cnt from His_stockTransferHeader where v_Date = '" + DmShared.dNull(postingFromDate) + "'", cnTempStaging)).intValue();
//            if (cnt > 0) {
//                throw new DmException(0, "Data is already posted for the selected date.");
//            }
            CommanScheduler.commanPostStockDataToD365(CommanScheduler.getStockTransferData(cnTemp, postingFromDate), cnTempStaging, cnTemp, "Transfer");
            spBegin.commit();
            dmrTemp.setStatus(true);

        } catch (Exception e) {

            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }

            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));

        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null) {
                    cnTemp.close();
                }
                if (cnTempCompany != null) {
                    cnTemp.close();
                }
                if (cnTempStaging != null) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<String> exportStockComsumption(String postingFromDate) {

        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        Connection cnTempCompany = null;
        Connection cnTempStaging = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            cnTempCompany = DmServer.getConnection(DmServer.dbCompany);
            String transactioDB = DmServer.executeScalar(" select top 1 centralData_Path From company order by v_Prefix desc ", cnTempCompany);
            cnTempCompany.close();
            String stagingDB = DmServer.executeScalar("select bc_databaseName from enviroCompany", cnTemp);
            cnTempStaging = DmServer.getConnection(stagingDB);
            spBegin = new DmSavePoint(cnTempStaging);

//            int cnt = DmShared.iNull(DmServer.executeScalar("select count(documentNumber) as cnt from His_stockConsumptionHeader where v_Date = '" + DmShared.dNull(postingFromDate) + "'", cnTempStaging)).intValue();
//            if (cnt > 0) {
//                throw new DmException(0, "Data is already posted for the selected date.");
//            }
            CommanScheduler.commanPostStockDataToD365(CommanScheduler.getStockcomsumptionData(cnTemp, postingFromDate), cnTempStaging, cnTemp, "Consumption");
            spBegin.commit();
            dmrTemp.setStatus(true);

        } catch (Exception e) {

            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }

            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));

        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null) {
                    cnTemp.close();
                }
                if (cnTempCompany != null) {
                    cnTemp.close();
                }
                if (cnTempStaging != null) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<String> exportPharmacyBillStock(String postingFromDate) {

        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        Connection cnTempCompany = null;
        Connection cnTempStaging = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBeginStaging = null;
        DmSavePoint spBegin = null;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            cnTempCompany = DmServer.getConnection(DmServer.dbCompany);
            String transactioDB = DmServer.executeScalar(" select top 1 centralData_Path From company order by v_Prefix desc ", cnTempCompany);
            cnTempCompany.close();
            String stagingDB = DmServer.executeScalar("select bc_databaseName from enviroCompany", cnTemp);
            cnTempStaging = DmServer.getConnection(stagingDB);
            spBeginStaging = new DmSavePoint(cnTempStaging);
            spBegin = new DmSavePoint(cnTemp);
            CommanScheduler.insertPharmacySale(cnTempStaging,cnTemp, postingFromDate, transactioDB);
            spBeginStaging.commit();
            spBegin.commit();
            dmrTemp.setStatus(true);

        } catch (Exception e) {

            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
                if (spBeginStaging != null) {
                    spBeginStaging.rollBack();
                }
            } catch (Exception ex) {
            }

            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));

        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null) {
                    cnTemp.close();
                }
                if (cnTempCompany != null) {
                    cnTemp.close();
                }
                if (cnTempStaging != null) {
                    cnTempStaging.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<String> exportOpdPosting(String postingFromDate) {
        DmResult<String> dmrTemp = new DmResult();
        String sqlQuery = "";
        Connection cnTemp = null;
        Connection cnTempStaging = null;
        DmSavePoint spBegin = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            String stagingDB = DmServer.executeScalar("select bc_databaseName from enviroCompany", cnTemp);
            cnTempStaging = DmServer.getConnection(stagingDB);
            spBegin = new DmSavePoint(cnTempStaging);
            CommanScheduler.exportRevenue(cnTemp, cnTempStaging, postingFromDate, "opdRevenu");
            spBegin.commit();
            cnTemp.close();
            cnTempStaging.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (cnTemp != null) {
                    cnTemp.rollback();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {

            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    public DmRSSPrivate forRssTemp() {
        return this;
    }

    @Override
    public DmResult<String> getItemDetails() {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        Connection cnTempCompany = null;
        DmSavePoint stagingSpBegin = null;
        Connection stagingCnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        List<MedicineMaster_Mdl> finalList = new ArrayList<>();
        String sqlQuery = "";
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            cnTempCompany = DmServer.getConnection(DmServer.dbCompany);
            String databaseName = DmServer.executeScalar(" select bc_databaseName from enviroCompany ", cnTemp);
            String transactioDB = DmServer.executeScalar(" select centralData_Path From company where v_Prefix = " + DmServer.getSsnVoucherPrefix(this) + " ", cnTempCompany);
            cnTempCompany.close();
            stagingCnTemp = DmServer.getConnection(databaseName);
            spBegin = new DmSavePoint(cnTemp);
            stagingSpBegin = new DmSavePoint(stagingCnTemp);

            /*------------------------Data transfer from d365 to his-----------------*/
            ItemMasterSync.syncItemMaster(cnTemp, stagingCnTemp, transactioDB);

            dmrTemp.setStatus(true);
            cnTemp.commit();
            stagingCnTemp.commit();
            cnTemp.close();
            stagingCnTemp.close();
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
                if (stagingSpBegin != null) {
                    stagingSpBegin.rollBack();
                }

            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null) {
                    cnTemp.close();
                }
                if (cnTempCompany != null) {
                    cnTempCompany.close();
                }
                if (stagingCnTemp != null) {
                    stagingCnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<String> getIpdRevenue(String postingFromDate) {
        DmResult<String> dmrTemp = new DmResult();
        String sqlQuery = "";
        Connection cnTemp = null;
        Connection cnTempStaging = null;
        DmSavePoint spBegin = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            String stagingDB = DmServer.executeScalar("select bc_databaseName from enviroCompany", cnTemp);
            cnTempStaging = DmServer.getConnection(stagingDB);
            spBegin = new DmSavePoint(cnTempStaging);
            CommanScheduler.exportRevenue(cnTemp, cnTempStaging, postingFromDate, "ipdRevenu");
            spBegin.commit();
            cnTemp.close();
            cnTempStaging.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (cnTemp != null) {
                    cnTemp.rollback();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {

            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<String> syncDepartmentMaster() {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        Connection stagingCnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery = "";
        DmSavePoint stagingSpBegin = null;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            String databaseName = DmServer.executeScalar(" select bc_databaseName from enviroCompany ", cnTemp);

            stagingCnTemp = DmServer.getConnection(databaseName);
            spBegin = new DmSavePoint(cnTemp);
            stagingSpBegin = new DmSavePoint(stagingCnTemp);
            /*------------------------Data transfer from d365 to his-----------------*/
            CommanScheduler.syncDepartmentMaster(cnTemp, stagingCnTemp);

            dmrTemp.setStatus(true);
            cnTemp.commit();
            stagingCnTemp.commit();
            cnTemp.close();
            stagingCnTemp.close();

        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
                if (stagingSpBegin != null) {
                    stagingSpBegin.rollBack();
                }

            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null) {
                    cnTemp.close();
                }
                if (stagingCnTemp != null) {
                    stagingCnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<String> updateIndentStatus() {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        Connection stagingCnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery = "";
        DmSavePoint stagingSpBegin = null;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            String databaseName = DmServer.executeScalar(" select bc_databaseName from enviroCompany ", cnTemp);

            stagingCnTemp = DmServer.getConnection(databaseName);
            spBegin = new DmSavePoint(cnTemp);
            stagingSpBegin = new DmSavePoint(stagingCnTemp);

            /*------------------------Update Indent Status-----------------*/


            dmrTemp.setStatus(true);
            cnTemp.commit();
            stagingCnTemp.commit();
            cnTemp.close();
            stagingCnTemp.close();

        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
                if (stagingSpBegin != null) {
                    stagingSpBegin.rollBack();
                }

            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null) {
                    cnTemp.close();
                }
                if (stagingCnTemp != null) {
                    stagingCnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

}
