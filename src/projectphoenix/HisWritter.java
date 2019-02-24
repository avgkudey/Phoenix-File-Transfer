/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectphoenix;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class HisWritter {

    public static void writter(String msg) {

        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("C:\\Users\\Public\\historyreceive.txt", true), "UTF-8"));
            BufferedWriter buffer = new BufferedWriter(writer);
            buffer.append(msg);
            buffer.newLine();
            buffer.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
