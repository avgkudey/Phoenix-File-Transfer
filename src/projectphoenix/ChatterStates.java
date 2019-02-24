/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectphoenix;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author KASUN
 */
public class ChatterStates {
    private IntegerProperty msgCount;
      
       public final void setMsgCount(int fileCount) {
        this.msgCountProperty().set(fileCount);
    }

    public final IntegerProperty msgCountProperty() {
        if (msgCount == null) {
            msgCount = new SimpleIntegerProperty(0);
        }
        return msgCount;
    }
}
