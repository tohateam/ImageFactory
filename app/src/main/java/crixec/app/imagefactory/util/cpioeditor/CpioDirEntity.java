package crixec.app.imagefactory.util.cpioeditor;

/**
 * Created by crixec on 16-8-22.
 */
public class CpioDirEntity extends CpioEntity
{

    public CpioDirEntity()
    {

    }

    public CpioDirEntity(String type, String target, String gid, String uid)
    {
        setType(type);
        setType(target);
        setGid(gid);
        setUid(uid);
    }
    @Override
    public String getSource()
    {
        // TODO: Implement this method
        return "";
    }

    @Override
    public String toString()
    {
        // TODO: Implement this method
        return String.format("%s %s %s %s %s", getType(), getTarget(), getPermission(), getGid(), getUid());
    }
}