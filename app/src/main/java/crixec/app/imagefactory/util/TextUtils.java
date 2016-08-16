package crixec.app.imagefactory.util;

/**
 * Created by Crixec on 2016/8/14.
 */
public class TextUtils {
    public static String arrayToString(Object[] objs) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : objs) {
            sb.append(obj + " ");
        }
        return sb.toString();
    }
}
