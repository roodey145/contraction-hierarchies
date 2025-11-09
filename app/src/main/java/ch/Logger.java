package ch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/*
 * Note: I always submit this util script to use for experiments
 */
public class Logger {
    private final PrintWriter pw;
    public Logger(String dataFileName) throws FileNotFoundException, UnsupportedEncodingException {
        File file = new File(dataFileName + ".csv");
        pw = new PrintWriter(file, "UTF-8");
    }

    public void println(String line) {
        pw.println(line);
    }

    public void close() {
        pw.close();
    }
}
