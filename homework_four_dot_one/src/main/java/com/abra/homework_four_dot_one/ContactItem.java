package com.abra.homework_four_dot_one;

public class ContactItem {
    private int iconId;
    private String name;
    private String info;
    private int iconBackground;

    public ContactItem(int iconId, String name, String info, int iconBackground) {
        this.iconId = iconId;
        this.name = name;
        this.info = info;
        this.iconBackground = iconBackground;
    }

    public int getIconId() {
        return iconId;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public int getIconBackground() {
        return iconBackground;
    }
}
