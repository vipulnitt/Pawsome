package com.example.pawsome2;

public class Data {
    String name;
    String url;
    String temprament;
    String  bred_for;
    String origin;
    String Height;
    String Weight;
    String image_id;
    int id;
    public Data(){}
    public Data(String name, String url, String temprament, String bred_for, String origin, String Height, String Weight){
        this.name=name;
        this.url=url;
        this.temprament=temprament;
        this.origin =origin;
        this.Height =Height;
        this.Weight =Weight;
        this.bred_for=bred_for;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getTemprament() {
        return temprament;
    }

    public void setTemprament(String temprament) {
        this.temprament = temprament;
    }

    public String getBred_for() {
        return bred_for;
    }

    public void setBred_for(String bred_for) {
        this.bred_for = bred_for;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
