package crixec.app.imagefactory.util.cpioeditor;

/**
 * Created by crixec on 16-8-22.
 */

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CpioListGenerator implements Closeable
{
    File listFile;
    FileWriter fw;

    private CpioListGenerator(File listFile)
    {
        this.listFile = listFile;
    }
    public static CpioListGenerator toFile(File file){
        return new CpioListGenerator(file);
    }
    public void prepare() throws IOException{
        fw = new FileWriter(listFile);
    }
    public synchronized void write(CpioEntity entity) throws IOException{
        fw.write(entity.toString() + "\n");
        fw.flush();
    }
    public void writeAll(List<CpioEntity> entities) throws IOException{
        clear();
        for(CpioEntity entity : entities){
            write(entity);
        }
    }
    public void clear() throws IOException{
        fw.close();
        fw = new FileWriter(listFile);
    }

    @Override
    public void close() throws IOException
    {
        // TODO: Implement this method
        fw.close();
    }


}
