package crixec.app.imagefactory.bean;

/**
 * Created by crixec on 16-8-24.
 */
public class AboutItem {
    String text1;
    String text2;
    String text3;
    String url;

    public AboutItem(String text1, String text2, String text3, String url) {
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }


    public String getText1() {
        return text1;
    }


    public String getText2() {
        return text2;
    }

    public String getText3() {
        return text3;
    }

}
