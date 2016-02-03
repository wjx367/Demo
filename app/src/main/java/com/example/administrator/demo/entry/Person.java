package com.example.administrator.demo.entry;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/3.
 */
public class Person implements Serializable{
    private int id;
    private String name;
    private String pass;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + ", pass=" + pass
                + "]";
    }
}
