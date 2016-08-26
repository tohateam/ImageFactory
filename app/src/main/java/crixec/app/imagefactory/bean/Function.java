package crixec.app.imagefactory.bean;

/**
 * Created by crixec on 16-8-23.
 */
public class Function {
    String name;
    String description;

    public Function(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
