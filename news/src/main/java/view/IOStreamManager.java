package view;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by bruse on 16/3/22.
 */
public class IOStreamManager {

    public static void closeInputStreams(InputStream... inputs) {
        for (InputStream input : inputs) {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    // JUST close
                }
            }
        }
    }

    public static void closeOutputStreams(OutputStream... outputs) {
        for (OutputStream output : outputs) {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    // JUST close
                }
            }
        }
    }

}

