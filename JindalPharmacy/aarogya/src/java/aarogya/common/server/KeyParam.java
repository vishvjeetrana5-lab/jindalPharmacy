package aarogya.common.server;

public class KeyParam {

    public short Len;
    public short Trcode;
    public short RetVal;
    public char[] LicenceKey = new char[22];

    public char[] ClientCode = new char[10];
    public char[] PartyName = new char[50];
    public char[] City = new char[40];
    public char[] GSTIN = new char[15];
    public char[] MacAddress = new char[40];
    public char[] ServerID = new char[40];
    public char[] ProductName = new char[40];

    public short ExpiryDay;
    public short ExpiryMonth;
    public short ExpiryYear;
    public char[] SiteCode = new char[5];
    public short Module1;
    public short Module2;
    public short Module3;
    public short Module4;
    public short Module5;
    public short ModList[];

}
