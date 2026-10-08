package dataman.dmToolkit.razorPay.server;

import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.razorPay.shared.param.RazorPayPosPmtGatewayParam;
import dataman.dmToolkit.razorPay.shared.param.RazorPayPosPmtGatewayParam_Vld;
import dataman.dmToolkit.razorPay.shared.pmtCancelReq.PmtCancelPushTo_Mdl;
import dataman.dmToolkit.razorPay.shared.pmtCancelReq.PmtCancelRequest_Mdl;
import dataman.dmToolkit.razorPay.shared.pmtCancelRes.PmtCancelResponse_Mdl;
import dataman.dmToolkit.razorPay.shared.pmtIniReq.PmtIniRequest_Mdl;
import dataman.dmToolkit.razorPay.shared.pmtIniReq.PushTo_Mdl;
import dataman.dmToolkit.razorPay.shared.pmtIniRes.PmtIniResponse_Mdl;
import dataman.dmToolkit.razorPay.shared.pmtIntegration.shared.PmtGatewayDetail_Mdl;
import dataman.dmToolkit.razorPay.shared.pmtIntegration.shared.RazorPayEnum.Error_Codes_Status;
import dataman.dmToolkit.razorPay.shared.pmtIntegration.shared.RazorPayEnum.PMT_INTEGRATION_STATUS;
import dataman.dmToolkit.razorPay.shared.pmtStatusReq.PmtStatusRequest_Mdl;
import dataman.dmToolkit.razorPay.shared.pmtStatusRes.PmtStatusResponse_Mdl;

public class RazorPayPOS_Server {

    private static void validateParamData(RazorPayPosPmtGatewayParam pmtGatewayParam) throws Exception {
        if (pmtGatewayParam == null) {
            throw new Exception("Payment Param can not be null.");
        }
        RazorPayPosPmtGatewayParam_Vld.validateParamData(pmtGatewayParam);
    }

    public static PmtGatewayDetail_Mdl initiatePayment(PmtGatewayDetail_Mdl mrTemp, RazorPayPosPmtGatewayParam pmtGatewayParam) throws Exception {
        try {
            validateParamData(pmtGatewayParam);
            if (mrTemp == null) {
                throw new Exception("Payment request data can not be null.");
            }
            if (DmShared.xNull(mrTemp.getId()).equalsIgnoreCase("")) {
                throw new Exception("Payment Id is mandatory.");
            }
            if (DmBaseShared.xNull(mrTemp.getDeviceId()).equalsIgnoreCase("")) {
                throw new Exception("Payment Device Id is mandatory.");
            }
            if (DmBaseShared.vNull(mrTemp.getAmount()).doubleValue() == 0d) {
                throw new Exception("Payment Amount is mandatory.");
            }

            PmtIniRequest_Mdl request = new PmtIniRequest_Mdl();
            request.setUsername(String.valueOf(pmtGatewayParam.getUsername()));
            request.setAmount(DmBaseServer.vNull(mrTemp.getAmount(), "0.00"));
            request.setAppKey(pmtGatewayParam.getAppKey());
            request.setMode(pmtGatewayParam.getMode());
            request.setCustomerName(DmBaseShared.xNull(mrTemp.getRefName()));
            request.setCustomerEmail(DmBaseShared.xNull(mrTemp.getEmail()));
            request.setCustomerMobileNumber(DmBaseShared.xNull(mrTemp.getMobile()));
            request.setExternalRefNumber(String.valueOf(mrTemp.getId()));

            PushTo_Mdl push = new PushTo_Mdl();
            push.setDeviceId(mrTemp.getDeviceId());
            request.setPushTo(push);

            //call Start Api
            PmtIniResponse_Mdl response_Mdl = RazorPayPosAPI_Server.sendInitiateTransactions(request, pmtGatewayParam.getUrlInitiate());

            if (response_Mdl == null) {
                throw new Exception("Payment response data can not be null.");
            }
            if (!response_Mdl.getSuccess()) {
                throw new Exception(DmBaseShared.xNull(response_Mdl.getMessage()));
            }
            mrTemp.setGtwAckNo(DmBaseShared.xNull(response_Mdl.getP2pRequestId()));
            mrTemp.setGtwStatusCode(DmBaseShared.xNull(response_Mdl.getErrorCode()));
            mrTemp.setGtwStatusMsg(DmBaseShared.xNull(response_Mdl.getMessage()));
            mrTemp.setStatus(getStatus(DmBaseShared.xNull(response_Mdl.getMessageCode())));

            mrTemp.setResponseString(DmBaseShared.xNull(response_Mdl.getResponseString()));

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return mrTemp;
    }

    public static PmtGatewayDetail_Mdl checkPaymentStaus(PmtGatewayDetail_Mdl mrTemp, RazorPayPosPmtGatewayParam pmtGatewayParam) throws Exception {
        try {
            validateParamData(pmtGatewayParam);
            if (mrTemp == null) {
                throw new Exception("Payment Request Type can not be null.");
            }
            if (DmShared.xNull(mrTemp.getId()).equalsIgnoreCase("")) {
                throw new Exception("Payment Id is mandatory.");
            }
            if (DmBaseShared.xNull(mrTemp.getGtwAckNo()).equalsIgnoreCase("")) {
                throw new Exception("Payment gateway Ack. No. is mandatory.");
            }
            if (DmBaseShared.xNull(mrTemp.getDeviceId()).equalsIgnoreCase("")) {
                throw new Exception("Payment Device Id is mandatory.");
            }

            PmtStatusRequest_Mdl status = new PmtStatusRequest_Mdl();
            status.setAppKey(pmtGatewayParam.getAppKey());
            status.setUsername(String.valueOf(pmtGatewayParam.getUsername()));
            status.setOrigP2pRequestId(mrTemp.getGtwAckNo());

            //call Start Api
            PmtStatusResponse_Mdl response_Mdl = RazorPayPosAPI_Server.checkStatusApi(status, pmtGatewayParam.getUrlStatus());

            if (response_Mdl == null) {
                throw new Exception("Payment response data can not be null.");
            }
            mrTemp.setGtwPmtMode(DmBaseShared.xNull(response_Mdl.getPaymentCardType()));
            mrTemp.setGtwBankTrnNo(DmBaseShared.xNull(response_Mdl.getRrNumber()));
            mrTemp.setGtwTrnId(DmBaseShared.xNull(response_Mdl.getTxnId()));
            mrTemp.setGtwStatusCode(DmBaseShared.xNull(response_Mdl.getMessageCode()));
            mrTemp.setGtwStatusMsg(DmBaseShared.xNull(response_Mdl.getMessage()));
            mrTemp.setStatus(getStatus(DmBaseShared.xNull(response_Mdl.getMessageCode())));
            mrTemp.setRespMdl(response_Mdl.getRespMdl());

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return mrTemp;
    }

    public static PmtGatewayDetail_Mdl cancelPayment(PmtGatewayDetail_Mdl mrTemp, RazorPayPosPmtGatewayParam pmtGatewayParam) throws Exception {
        try {
            validateParamData(pmtGatewayParam);
            if (mrTemp == null) {
                throw new Exception("Payment Request Type can not be null.");
            }
            if (DmShared.xNull(mrTemp.getId()).equalsIgnoreCase("")) {
                throw new Exception("Payment Id is mandatory.");
            }
            if (DmBaseShared.xNull(mrTemp.getGtwAckNo()).equalsIgnoreCase("")) {
                throw new Exception("Payment gateway Ack. No. is mandatory.");
            }
            if (DmBaseShared.xNull(mrTemp.getDeviceId()).equalsIgnoreCase("")) {
                throw new Exception("Payment Device Id is mandatory.");
            }
            if (DmBaseShared.vNull(mrTemp.getAmount()).doubleValue() == 0d) {
                throw new Exception("Payment Amount is mandatory.");
            }

            PmtCancelRequest_Mdl cancel = new PmtCancelRequest_Mdl();
            cancel.setAppKey(pmtGatewayParam.getAppKey());
            cancel.setUsername(String.valueOf(pmtGatewayParam.getUsername()));
            cancel.setOrigP2pRequestId(mrTemp.getGtwAckNo());

            PmtCancelPushTo_Mdl push = new PmtCancelPushTo_Mdl();
            push.setDeviceId(mrTemp.getDeviceId());
            cancel.setPushTo(push);

            //call Start Api
            PmtCancelResponse_Mdl response_Mdl = RazorPayPosAPI_Server.cancelTransactionApi(cancel, pmtGatewayParam.getUrlCancel());

            if (response_Mdl == null) {
                throw new Exception("Payment response data can not be null.");
            }
            mrTemp.setStatus(getStatus(DmBaseShared.xNull(response_Mdl.getMessageCode())));
            mrTemp.setRespMdl(response_Mdl.getRespMdl());

        } catch (Exception e) {
            e.printStackTrace();
            throw new DmException(0, e.getMessage());
        }

        return mrTemp;
    }

    private static int getStatus(String wlStatus) throws Exception {
        int status = PMT_INTEGRATION_STATUS.PENDING.getId();

        Error_Codes_Status value = Error_Codes_Status.getValueOf(wlStatus);

        if (value != null) {
            switch (value) {
                case P2P_DEVICE_RECEIVED:
                case P2P_DEVICE_SENT:
                case P2P_STATUS_QUEUED:
                    status = PMT_INTEGRATION_STATUS.PROCESS.getId();
                    break;
                case P2P_DEVICE_TXN_DONE:
                    status = PMT_INTEGRATION_STATUS.SUCCESS.getId();
                    break;
                case P2P_DEVICE_CANCELED:
                case P2P_STATUS_IN_CANCELED_FROM_EXTERNAL_SYSTEM:
                    status = PMT_INTEGRATION_STATUS.FAILED.getId();
                    break;
                default:
                    break;
            }
        }
        return status;
    }
}
