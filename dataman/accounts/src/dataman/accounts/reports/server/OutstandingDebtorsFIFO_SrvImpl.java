package dataman.accounts.reports.server;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.reports.shared.OutstandingDebtorsFIFO_Model;
import dataman.accounts.reports.shared.OutstandingDebtorsFIFO_Srv;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class OutstandingDebtorsFIFO_SrvImpl extends DmRSSPrivate implements OutstandingDebtorsFIFO_Srv {

    @Override
    public DmResultPageing<List<OutstandingDebtorsFIFO_Model.TxtHAccountName>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<OutstandingDebtorsFIFO_Model.TxtHAccountName>> rstTemp = new DmResultPageing<>();
        List<OutstandingDebtorsFIFO_Model.TxtHAccountName> lstTemp = new ArrayList<>();
        OutstandingDebtorsFIFO_Model.TxtHAccountName listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select 'o' As Tick,SG.SubCode,SG.Name,Sg.ManualCode,isNull(CT.CityName,'') as City,AG.GroupName, "
                    + " isNull(SG.Zone,'') as Zone From SubGroup SG Left Join "
                    + " AcGroup AG On AG.GroupCode=SG.GroupCode Left Join "
                    + " City CT On SG.CityCode=CT.CityCode Left Join "
                    + " ZoneMast ZM On ZM.Code=SG.Zone"
                    + " LEFT JOIN subgroupsitelist SL ON SG.subcode = SL.subcode"
                    + " Where  SL.site_Code =:site_Code ");
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new OutstandingDebtorsFIFO_Model.TxtHAccountName();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("City")));
                listNode.setGroupName(DmShared.xNull(rsTemp.getString("GroupName")));
                listNode.setZone(DmShared.xNull(rsTemp.getString("Zone")));

                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSiteName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select Sm.Code,Sm.Name From Sitemast Sm where code in (" + DmServer.getSsnSiteList(this) + ")  ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountGroup(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select AG.GroupCode,AG.GroupName From AcGroup  AG  ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("GroupCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("GroupName")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportFor(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            rstTemp.setData(lstTemp);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Debit");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("C");
            listNode.setSharedKeyFieldName("Credit");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("B");
            listNode.setSharedKeyFieldName("Both");
            lstTemp.add(listNode);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String strCondition1 = "", sqlQuery;
        String strDate = "", strCondDt = "", strContitionReportFor = "";
        Double D1 = 0.0;
        try {
            OutstandingDebtorsFIFO_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strDate = DmServer.convertDate(filter.get("asOnDate").getText()).toString();
            D1 = DmShared.vNull(filter.get("interval").getTag());
            strCondition1 = " Where LG.V_Date < = '" + DmServer.convertDate(filter.get("asOnDate").getText()) + "'  ";
            strCondDt = " Where LG.V_Date < = '" + DmServer.convertDate(filter.get("asOnDate").getText()) + "'  ";

            if (!DmShared.xNull(filter.get("accountGroup").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " And (isNull(CAST(SG.GroupCode AS VARCHAR),'') In (Select isNull(CAST(AGP.GroupCode AS VARCHAR),'') From AcGroupPath AGP Where AGP.GroupUnder In ( " + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + ")) Or SG.GroupCode In ( " + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + " )) ";
            }

            if (!DmShared.xNull(filter.get("accountName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " And LG.SubCode In ( " + DmServer.getParamList(":accountName", filter.get("accountName").getTag()) + ") ";
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " And LG.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
            } else {
                strCondition1 += " And LG.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") ";
            }

            if (!DmShared.xNull(filter.get("reportFor").getTag()).trim().equalsIgnoreCase("D")) {
                strContitionReportFor = " Where isNull(Status,'')<>'ADV' And isNull(PendingAmt,0)>0 ";
            } else if (!DmShared.xNull(filter.get("reportFor").getTag()).trim().equalsIgnoreCase("D")) {
                strContitionReportFor = " Where (isNull(Status,'')='ADV') Or (isNull(Status,'')<>'ADV' And isNull(PendingAmt,0)<0) ";
            } else {
                strContitionReportFor = " Where isNull(PendingAmt,0)<>0 ";
            }

            sqlQuery = " DECLARE  "
                    + "	@TempRecord TABLE (DocId  NVARCHAR(20),RecId  NVARCHAR(20),V_Date  NVARCHAR(30),subcode NVARCHAR(30),  PartyName NVARCHAR(500),BilAmt FLOAT,PendingAmt FLOAT, "
                    + "	         STATUS  NVARCHAR(20),Site_Code  NVARCHAR(20),PartyCity  NVARCHAR(200),Narration  VARCHAR(MAX),V_type  NVARCHAR(20) );  "
                    + "          SET NOCOUNT ON; "
                    + "          DECLARE @SubCode VARCHAR(100); "
                    + "		 DECLARE @Party VARCHAR(200);DECLARE @PCity VARCHAR(200);  DECLARE @Cr FLOAT;DECLARE @Adv FLOAT;DECLARE @SiteCode VARCHAR(100)   "
                    + "	         DECLARE CurrTempPayment CURSOR FOR   "
                    + "			SELECT LG.SubCode, MAX(Sg.name) AS PartyName,MAX(CT.CityName) AS PCity,ISNULL(SUM(AmtCr),0) AS AmtCr,   "
                    + "			CASE WHEN ISNULL(SUM(AmtCr),0)> ISNULL(SUM(AmtDr),0) THEN (ISNULL(SUM(AmtCr),0) - ISNULL(SUM(AmtDr),0)) ELSE  0   END AS Advance ,    "
                    + "			MAX(LG.Site_Code) AS SiteCode FROM Ledger LG  "
                    + "			LEFT JOIN SubGroup SG ON SG.SubCode =LG.SubCode  "
                    + "                 LEFT JOIN SubGroupNature SGN ON SG.Nature = SGN.code "
                    + "			LEFT JOIN City CT ON SG.CityCode  =CT.CityCode  "
                    + strCondition1 + " and SGN.manualCode='CT' "
                    + " GROUP BY LG.SubCode "
                    + " OPEN CurrTempPayment   "
                    + "	FETCH NEXT FROM CurrTempPayment INTO @SubCode,@Party,@PCity,@Cr,@Adv,@SiteCode ;  "
                    + "    WHILE @@FETCH_STATUS =0   "
                    + "	BEGIN   DECLARE @CrAmt FLOAT  "
                    + "		DECLARE @tempval FLOAT  "
                    + "		DECLARE @DocId NVARCHAR(20) "
                    + "		DECLARE @RecId NVARCHAR(20)  "
                    + "		DECLARE @V_date NVARCHAR(20) "
                    + "		DECLARE @Supplier NVARCHAR(20) "
                    + "		DECLARE @PartyName NVARCHAR(300) "
                    + "		DECLARE @DrAmt FLOAT   "
                    + "		DECLARE @Site NVARCHAR(30) "
                    + "		DECLARE @City NVARCHAR(100) "
                    + "		DECLARE @Narr VARCHAR(MAX) "
                    + "		DECLARE @VType NVARCHAR(1000)  SET @tempval=0   "
                    + "		DECLARE curr_TempAdjust CURSOR  "
                    + "	 FOR  "
                    + "		SELECT  ISNULL(CAST(LG.DocId AS VARCHAR),'') AS DocId,ISNULL(CAST(LG.RecId AS VARCHAR),'') AS RecId, "
                    + "		ISNULL(LG.V_date,'') AS V_date,ISNULL(LG.SubCode,'') AS Subcode,ISNULL(SG.Name,'') AS PartyName, "
                    + "		ISNULL(Lg.AmtDr,0) AS AmtDr,ISNULL(Lg.Site_Code,0) AS Site_Code ,ISNULL(Ct.CityName,'') AS City, "
                    + "		ISNULL(Lg.Narration,'') AS Narr,ISNULL(Lg.V_type,'') AS V_type    "
                    + "		FROM Ledger LG LEFT JOIN SubGroup SG ON  SG.SubCode=LG.SubCode  "
                    + "		LEFT JOIN City CT ON Ct.CityCode =Sg.CityCode    "
                    + strCondDt + " and isNull(Lg.AmtDr,0) <>0  AND LG.SubCode = @SubCode   order by Lg.V_Date  "
                    + " SET @CrAmt=@Cr  OPEN curr_TempAdjust   "
                    + "				FETCH NEXT FROM curr_TempAdjust INTO @DocId,@RecId,@V_date,@Supplier,@PartyName,@DrAmt,@Site,@City,@Narr,@VType   "
                    + "				WHILE @@FETCH_STATUS =0  "
                    + "				BEGIN IF   @DrAmt< @CrAmt  "
                    + "				 BEGIN  SET @CrAmt=@CrAmt-@DrAmt END  "
                    + "				 ELSE  "
                    + "				 BEGIN  DECLARE @Status NVARCHAR(20)   "
                    + "				 IF  @DrAmt<> @DrAmt -@CrAmt SET  @Status='A'   "
                    + "				 INSERT INTO  @TempRecord VALUES (@DocId,@RecId,@V_date,@Supplier,@PartyName,@DrAmt,@DrAmt -@CrAmt,@Status,@Site,@City,@Narr,@VType)   "
                    + "				 SET  @CrAmt = 0  "
                    + "				 SET @Status=''  "
                    + "				 END "
                    + "				 FETCH NEXT  "
                    + "				 FROM curr_TempAdjust INTO @DocId,@RecId,@V_date,@Supplier,@PartyName,@DrAmt,@Site,@City,@Narr,@VType;  END  "
                    + "				 CLOSE curr_TempAdjust; DEALLOCATE curr_TempAdjust;   "
                    + "				 IF   @Adv<>0  INSERT INTO  @TempRecord VALUES ('','','01/feb/1980', @SubCode,@Party,0,@Adv,'Adv',@SiteCode,@PCity,'Advance Payment ','');   "
                    + "			FETCH NEXT FROM CurrTempPayment INTO @SubCode,@Party,@PCity,@Cr,@Adv,@SiteCode ; END   "
                    + " "
                    + "  CLOSE CurrTempPayment;  "
                    + "  DEALLOCATE CurrTempPayment;"
                    + " SELECT *, "
                    + " (CASE WHEN DateDiff(Day,V_Date,'" + strDate + "'   )>= 0 AND  DateDiff(Day,V_Date,'" + strDate + "' )<=" + D1 + " THEN  PendingAmt Else 0 end ) AS AmtDay1, "
                    + " (CASE WHEN DateDiff(Day,V_Date,'" + strDate + "' )>" + D1 + " THEN  PendingAmt ELSE 0 end) AS AmtDay2, " + D1 + " As Days "
                    + " FROM @TempRecord ";
            sqlQuery += strContitionReportFor;

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            if (!DmShared.xNull(filter.get("accountGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("accountGroup", DmShared.xNull(filter.get("accountGroup").getTag()), true);
            }

            if (!DmShared.xNull(filter.get("accountName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("accountName", DmShared.xNull(filter.get("accountName").getTag()), true);
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("siteName", DmShared.xNull(filter.get("siteName").getTag()));
            } else {
                psTemp.setStringList("siteName", DmShared.xNull(DmServer.getSsnSiteList(this)));
            }

            rsTemp = psTemp.executeQuery();

            jasperReport = DmServer.compileJasper("AC_Outstanding_FIFO_Dr");
            jasperPrint = DmServer.fillJasper(this, "Outstanding Debtors FIFO", jasperReport, new HashMap(), filter, rsTemp);
            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setStatus(false);
            rstTemp.setErrorMessage(e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

}
