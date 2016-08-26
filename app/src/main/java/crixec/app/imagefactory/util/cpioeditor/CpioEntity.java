package crixec.app.imagefactory.util.cpioeditor;

/**
 * Created by crixec on 16-8-22.
 */
public class CpioEntity
{
    private String type;
    private String target;
    private String source;
    private String permission;
    private String gid;
    private String uid;

    public CpioEntity()
    {

    }

    public CpioEntity(String type, String target, String source, String permission, String gid, String uid)
    {
        this.type = type;
        this.target = target;
        this.source = source;
        this.permission = permission;
        this.gid = gid;
        this.uid = uid;
    }

    public void setPermission(String permission)
    {
        this.permission = permission;
    }

    public String getPermission()
    {
        return permission;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

    public void setTarget(String target)
    {
        this.target = target;
    }

    public String getTarget()
    {
        return target;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public String getSource()
    {
        return source;
    }

    public void setGid(String gid)
    {
        this.gid = gid;
    }

    public String getGid()
    {
        return gid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getUid()
    {
        return uid;
    }

    @Override
    public String toString()
    {
        // TODO: Implement this method
        return String.format("%s %s %s %s %s %s", type, target, source, permission, gid, uid);
    }

}

