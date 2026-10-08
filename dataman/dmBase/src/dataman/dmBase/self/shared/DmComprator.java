/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataman.dmBase.self.shared;


import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.Comparator;

/**
 *
 * @author hp
 */

public class DmComprator implements Serializable,IsSerializable{

    public static final Comparator<String> CASE_INSENSITIVE_ORDER
            = new DmCaseInsensitiveComparator();

}
