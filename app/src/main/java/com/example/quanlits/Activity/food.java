package com.example.quanlits.Activity;

public class food {
    public static final int TYPE_TRASUA = 0;
    public static final int TYPE_CAFE = 1;
    public static final int TYPE_BANH = 2;

    private int imageResId;
    private String name;
    private int type;

    public food(int imageResId, String name, int type) {
        this.imageResId = imageResId;
        this.name = name;
        this.type = type;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }
}
