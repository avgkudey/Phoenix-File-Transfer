package projectphoenix;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class ErrorWriter {

    public static void writer(String error) {

        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("C:\\Users\\Public\\errorlog.txt", true), "UTF-8"));
            BufferedWriter buffer = new BufferedWriter(writer);
            buffer.append(error);
            buffer.newLine();
            buffer.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
