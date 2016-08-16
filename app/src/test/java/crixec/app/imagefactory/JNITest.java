package crixec.app.imagefactory;

import org.junit.Test;

import crixec.app.imagefactory.util.libmif.Callback;
import crixec.app.imagefactory.core.Invoker;

/**
 * Created by crixec on 16-8-11.
 */
public class JNITest implements Callback{
    @Test
    public void main(){
        System.load("/home/crixec/workspace/ImageFactory/app/src/main/libs/x86_64/libmif.so");
        String[] params = new String[]{"unpackbootimg",
                "-i",
                "/home/crixec/workspace/tests/recovery.img",
                "-o", "/home/crixec/workspace/tests/recovery.img_unpacked"
        };

        Invoker.invoke(params, this);
    }

    @Override
    public void onStdout(String output) {
        System.out.println("STDOUT:" + output);
    }

    @Override
    public void onStderr(String output) {
        System.out.println("STDERR:" + output);
    }
}
