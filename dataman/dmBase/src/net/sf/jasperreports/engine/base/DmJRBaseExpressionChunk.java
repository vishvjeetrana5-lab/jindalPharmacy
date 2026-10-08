package net.sf.jasperreports.engine.base;

public class DmJRBaseExpressionChunk extends JRBaseExpressionChunk {

    public void setChunk(byte paramType, String txtParamName) {
        type = paramType;
        text = txtParamName;
    }
}
