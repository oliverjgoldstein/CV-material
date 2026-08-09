package solution;

import java.io.File;

// This class looks at the last full stop in a string of a file name and then compares it to the extension txt.
// It returns everything from the last full stop onwards.
public class Utils {

    public final static String txt = "txt";

    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}