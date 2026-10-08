package aarogya.common.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class KeyCommunicator {

    private short byteLength = 332;

    public KeyParam getMyScoket(KeyParam keyPara) {

        Socket socket = null;
        DataInputStream input = null;
        DataOutputStream out = null;
        KeyParam keyOut = new KeyParam();
        // establish a connection 
        try {   // initialize socket and input output streams 

            socket = new Socket("127.0.0.1", 8801); // Ip and Port where Daman Validator exe is running

//            KeyParam keyPara = new KeyParam();
            keyPara.Trcode = 1;
            keyPara.Len = byteLength;

//            keyPara.LicenceKey = createSpace("CCCCUJPID-DGUIC4CB8-DH", 22);
//            keyPara.ClientCode = createSpace("286590", 10);
//            keyPara.PartyName = createSpace("HOTEL HILLTOP", 50);
//            keyPara.City = createSpace("BOKARO", 40);
//            keyPara.GSTIN = createSpace("20AEZPJ1872912Y", 15);
//            keyPara.MacAddress = createSpace("", 40);
//            keyPara.ServerID = createSpace("", 40);
//            keyPara.ProductName = createSpace("AATITHYA", 40);
//            keyPara.ModList = new short[24];
            byte[] arr = convetObjectToByteArr(keyPara);
            OutputStream os = socket.getOutputStream();

            os.write(arr);

            Thread.sleep(1000);

            InputStream stream = socket.getInputStream();
            byte[] data = new byte[byteLength];
            int count = stream.read(data);
            keyOut = convertByteArrayToObject(data);

            stream.close();
            os.close();
            socket.close();
        } catch (Exception ex) {
            String error = ex.toString();
        }
        if (keyOut.RetVal != 0 && keyOut.RetVal != -1) {
            System.out.println("\nRetVal:" + keyOut.RetVal);
            System.out.println("\nExpiryDay:" + keyOut.ExpiryDay);
            System.out.println("\nExpiryMonth:" + keyOut.ExpiryMonth);
            System.out.println("\nExpiryYear:" + keyOut.ExpiryYear);
            System.out.println("\nModule1:" + keyOut.Module1);
            System.out.println("\nModule2:" + keyOut.Module2);
            System.out.println("\nModule3:" + keyOut.Module3);
            System.out.println("\nModule4:" + keyOut.Module4);
            System.out.println("\nModule5:" + keyOut.Module5);

            for (short i = 0; i < 24; i++) {
                System.out.println("\n Mod List : " + keyOut.ModList[i]);
            }

            System.out.println("\nClientCode:" + new String(keyOut.ClientCode));
            System.out.println("\nPartyName:" + new String(keyOut.PartyName));
            System.out.println("\nCity:" + new String(keyOut.City));
            System.out.println("\nGSTIN:" + new String(keyOut.GSTIN));
            System.out.println("\nMacAddress:" + new String(keyOut.MacAddress));
            System.out.println("\nServerID:" + new String(keyOut.ServerID));
            System.out.println("\nProductName:" + new String(keyOut.ProductName));
            System.out.println("\nLicenceKey:" + new String(keyOut.LicenceKey));
        }
        return keyOut;
    }

    public char[] createSpace(String val, int len) {
        for (int i = val.length(); i < len; i++) {
            val = val + " ";
        }
        return val.toCharArray();
    }

    public byte[] convetObjectToByteArr(KeyParam keyout) {
        byte[] arr = new byte[byteLength];

        ByteBuffer buffer_len = ByteBuffer.allocate(2);
        buffer_len.order(ByteOrder.LITTLE_ENDIAN);
        buffer_len.putShort(keyout.Len);
        System.arraycopy(buffer_len.array(), 0, arr, 0, 2);

        ByteBuffer buffer_trcode = ByteBuffer.allocate(2);
        buffer_trcode.order(ByteOrder.LITTLE_ENDIAN);
        buffer_trcode.putShort(keyout.Trcode);
        System.arraycopy(buffer_trcode.array(), 0, arr, 2, 2);

        byte[] bytesKey = Charset.forName("UTF-8").encode(CharBuffer.wrap(keyout.LicenceKey)).order(ByteOrder.LITTLE_ENDIAN).array();
        System.arraycopy(bytesKey, 0, arr, 4, 22);//10

        ByteBuffer buffer_RetVal = ByteBuffer.allocate(2);
        buffer_RetVal.order(ByteOrder.LITTLE_ENDIAN);
        buffer_RetVal.putShort(keyout.RetVal);
        System.arraycopy(buffer_RetVal.array(), 0, arr, 26, 2);

        byte[] bytesClientCode = Charset.forName("UTF-8").encode(CharBuffer.wrap(keyout.ClientCode)).order(ByteOrder.LITTLE_ENDIAN).array();
        System.arraycopy(bytesClientCode, 0, arr, 28, 10);//10

        byte[] PartyName = Charset.forName("UTF-8").encode(CharBuffer.wrap(keyout.PartyName)).order(ByteOrder.LITTLE_ENDIAN).array();
        System.arraycopy(PartyName, 0, arr, 38, 50);//50

        byte[] City = Charset.forName("UTF-8").encode(CharBuffer.wrap(keyout.City)).order(ByteOrder.LITTLE_ENDIAN).array();
        System.arraycopy(City, 0, arr, 88, 40);//40

        byte[] GSTIN = Charset.forName("UTF-8").encode(CharBuffer.wrap(keyout.GSTIN)).order(ByteOrder.LITTLE_ENDIAN).array();
        System.arraycopy(GSTIN, 0, arr, 128, 15);//15

        byte[] MacAddress = Charset.forName("UTF-8").encode(CharBuffer.wrap(keyout.MacAddress)).order(ByteOrder.LITTLE_ENDIAN).array();
        System.arraycopy(MacAddress, 0, arr, 143, 40);//40

        byte[] ServerID = Charset.forName("UTF-8").encode(CharBuffer.wrap(keyout.ServerID)).order(ByteOrder.LITTLE_ENDIAN).array();
        System.arraycopy(ServerID, 0, arr, 183, 40);//40

        byte[] ProductName = Charset.forName("UTF-8").encode(CharBuffer.wrap(keyout.ProductName)).order(ByteOrder.LITTLE_ENDIAN).array();
        System.arraycopy(ProductName, 0, arr, 223, 40);//40

        ByteBuffer buffer_ExpiryDay = ByteBuffer.allocate(2);
        buffer_ExpiryDay.order(ByteOrder.LITTLE_ENDIAN);
        buffer_ExpiryDay.putShort(keyout.RetVal);
        System.arraycopy(buffer_ExpiryDay.array(), 0, arr, 263, 2);

        ByteBuffer buffer_ExpiryMonth = ByteBuffer.allocate(2);
        buffer_ExpiryMonth.order(ByteOrder.LITTLE_ENDIAN);
        buffer_ExpiryMonth.putShort(keyout.RetVal);
        System.arraycopy(buffer_ExpiryMonth.array(), 0, arr, 265, 2);

        ByteBuffer buffer_ExpiryYear = ByteBuffer.allocate(2);
        buffer_ExpiryYear.order(ByteOrder.LITTLE_ENDIAN);
        buffer_ExpiryYear.putShort(keyout.RetVal);
        System.arraycopy(buffer_ExpiryYear.array(), 0, arr, 267, 2);

        byte[] siteCode = Charset.forName("UTF-8").encode(CharBuffer.wrap(keyout.SiteCode)).order(ByteOrder.LITTLE_ENDIAN).array();
        System.arraycopy(siteCode, 0, arr, 269, 5);//5

        ByteBuffer buffer_Module1 = ByteBuffer.allocate(2);
        buffer_Module1.order(ByteOrder.LITTLE_ENDIAN);
        buffer_Module1.putShort(keyout.RetVal);
        System.arraycopy(buffer_Module1.array(), 0, arr, 274, 2);

        ByteBuffer buffer_Module2 = ByteBuffer.allocate(2);
        buffer_Module2.order(ByteOrder.LITTLE_ENDIAN);
        buffer_Module2.putShort(keyout.RetVal);
        System.arraycopy(buffer_Module2.array(), 0, arr, 276, 2);

        ByteBuffer buffer_Module3 = ByteBuffer.allocate(2);
        buffer_Module3.order(ByteOrder.LITTLE_ENDIAN);
        buffer_Module3.putShort(keyout.RetVal);
        System.arraycopy(buffer_Module3.array(), 0, arr, 278, 2);

        ByteBuffer buffer_Module4 = ByteBuffer.allocate(2);
        buffer_Module4.order(ByteOrder.LITTLE_ENDIAN);
        buffer_Module4.putShort(keyout.RetVal);
        System.arraycopy(buffer_Module4.array(), 0, arr, 280, 2);

        ByteBuffer buffer_Module5 = ByteBuffer.allocate(2);
        buffer_Module5.order(ByteOrder.LITTLE_ENDIAN);
        buffer_Module5.putShort(keyout.RetVal);
        System.arraycopy(buffer_Module5.array(), 0, arr, 282, 2);

        ByteBuffer byteBufModList = ByteBuffer.allocate(2 * 24);

        for (int i = 0; i < 24; i++) {
            byteBufModList.order(ByteOrder.LITTLE_ENDIAN);
            byteBufModList.putShort(keyout.ModList[i]);
            i++;
        }
        System.arraycopy(byteBufModList.array(), 0, arr, 284, 48);

        return arr;

    }

    public KeyParam convertByteArrayToObject(byte[] readData) {
        KeyParam keyout = new KeyParam();

        byte[] arrLen = Arrays.copyOfRange(readData, 0, 2);
        short saLenth = ByteBuffer.wrap(arrLen).order(ByteOrder.LITTLE_ENDIAN).getShort();
        keyout.Len = saLenth;

        byte[] arrTrCode = Arrays.copyOfRange(readData, 2, 4);
        short shTrCode = ByteBuffer.wrap(arrTrCode).order(ByteOrder.LITTLE_ENDIAN).getShort();
        keyout.Trcode = shTrCode;

        byte[] arrKey = Arrays.copyOfRange(readData, 4, 26);
        String strKey = new String(arrKey);
        keyout.LicenceKey = strKey.toCharArray();

        byte[] arrRetVal = Arrays.copyOfRange(readData, 26, 28);
        short shRetVal = ByteBuffer.wrap(arrRetVal).order(ByteOrder.LITTLE_ENDIAN).getShort();
        keyout.RetVal = shRetVal;

        byte[] arrClientCode = Arrays.copyOfRange(readData, 28, 38);
        String strClientCode = new String(arrClientCode);
        keyout.ClientCode = strClientCode.toCharArray();

        byte[] arrPartyName = Arrays.copyOfRange(readData, 38, 88);
        String strarrPartyName = new String(arrPartyName);
        keyout.PartyName = strarrPartyName.toCharArray();

        byte[] arrCity = Arrays.copyOfRange(readData, 88, 128);
        String strCity = new String(arrCity);
        keyout.City = strCity.toCharArray();

        byte[] arrGSTIN = Arrays.copyOfRange(readData, 128, 143);
        String strGSTIN = new String(arrGSTIN);
        keyout.GSTIN = strGSTIN.toCharArray();

        byte[] arrMacAddress = Arrays.copyOfRange(readData, 143, 183);
        String strMacAddress = new String(arrMacAddress);
        keyout.MacAddress = strMacAddress.toCharArray();

        byte[] arrServerId = Arrays.copyOfRange(readData, 183, 223);
        String strServerId = new String(arrServerId);
        keyout.ServerID = strServerId.toCharArray();

        byte[] arrProductName = Arrays.copyOfRange(readData, 223, 263);
        String strProductName = new String(arrProductName);
        keyout.ProductName = strProductName.toCharArray();

        byte[] arrExpiryDay = Arrays.copyOfRange(readData, 263, 265);
        short shExpiryDay = ByteBuffer.wrap(arrExpiryDay).order(ByteOrder.LITTLE_ENDIAN).getShort();
        keyout.ExpiryDay = shExpiryDay;

        byte[] arrExpiryMonth = Arrays.copyOfRange(readData, 265, 267);
        short shExpiryMonth = ByteBuffer.wrap(arrExpiryMonth).order(ByteOrder.LITTLE_ENDIAN).getShort();
        keyout.ExpiryMonth = shExpiryMonth;

        byte[] arrExpiryYear = Arrays.copyOfRange(readData, 267, 269);
        short shExpiryYear = ByteBuffer.wrap(arrExpiryYear).order(ByteOrder.LITTLE_ENDIAN).getShort();
        keyout.ExpiryYear = shExpiryYear;

        byte[] arrSiteCode = Arrays.copyOfRange(readData, 269, 274);
        String strSiteCode = new String(arrSiteCode);
        keyout.SiteCode = strSiteCode.toCharArray();

        byte[] arrModule1 = Arrays.copyOfRange(readData, 274, 276);
        short shModule1 = ByteBuffer.wrap(arrModule1).order(ByteOrder.LITTLE_ENDIAN).getShort();
        keyout.Module1 = shModule1;

        byte[] arrModule2 = Arrays.copyOfRange(readData, 276, 278);
        short shModule2 = ByteBuffer.wrap(arrModule2).order(ByteOrder.LITTLE_ENDIAN).getShort();
        keyout.Module2 = shModule2;

        byte[] arrModule3 = Arrays.copyOfRange(readData, 278, 280);
        short shModule3 = ByteBuffer.wrap(arrModule3).order(ByteOrder.LITTLE_ENDIAN).getShort();
        keyout.Module3 = shModule3;

        byte[] arrModule4 = Arrays.copyOfRange(readData, 280, 282);
        short shModule4 = ByteBuffer.wrap(arrModule4).order(ByteOrder.LITTLE_ENDIAN).getShort();
        keyout.Module4 = shModule4;

        byte[] arrModule5 = Arrays.copyOfRange(readData, 282, 284);
        short shModule5 = ByteBuffer.wrap(arrModule5).order(ByteOrder.LITTLE_ENDIAN).getShort();
        keyout.Module5 = shModule5;

        // to turn bytes to shorts as either big endian or little endian.
        byte[] arrModList = Arrays.copyOfRange(readData, 284, readData.length);
        short[] modList = new short[24];
        ByteBuffer.wrap(arrModList).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(modList);
        keyout.ModList = modList;
        return keyout;
    }
}
