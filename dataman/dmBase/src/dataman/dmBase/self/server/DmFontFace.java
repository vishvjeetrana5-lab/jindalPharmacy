package dataman.dmBase.self.server;

import java.awt.Font;
import java.io.File;
import java.util.Objects;

public class DmFontFace {

    private File fontFile;
    private Font font;

    public DmFontFace(File fontFile) throws Exception {
        this.fontFile = fontFile;
        this.font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
    }
    
    public String getFontFamilyName(){
        return this.font.getFamily();
    }
    
    public String getFontName(){
        return this.font.getFontName();
    }
    
    public boolean isBold(){
        return getFontName().toLowerCase().contains("bold");
    }
    
    public boolean isItalic(){
        return getFontName().toLowerCase().contains("italic");
    }
    
    public File getFontFile(){
        return this.fontFile;
    }

    @Override
    public boolean equals(Object obj) {
        boolean bool=false;
        if(obj instanceof DmFontFace){
            bool= ((DmFontFace)obj).getFontName().equalsIgnoreCase(getFontName());
        }
        return bool;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.font);
        return hash;
    }
    
    
}
