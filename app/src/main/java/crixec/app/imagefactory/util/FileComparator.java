package crixec.app.imagefactory.util;
import java.util.Comparator;
import java.io.File;

public class FileComparator implements Comparator<Object>
{
	@Override  
	public int compare(Object obj, Object obj2)
	{
        return (!((File) obj).isDirectory() || ((File) obj2).isDirectory()) ? (((File) obj).isDirectory() || !((File) obj2).isDirectory()) ? ((File) obj).getName().compareToIgnoreCase(((File) obj2).getName()) < 0 ? -1 : 1 : 1 : -1;
    }
}
