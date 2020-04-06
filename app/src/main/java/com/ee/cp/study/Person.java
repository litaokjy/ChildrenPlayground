package com.ee.cp.study;

import androidx.annotation.NonNull;

public class Person implements Cloneable {
    private int age;
    private boolean sex;
    private String name;

    @Override
    public Object clone() {
        Person person;
        try {
            person = (Person) super.clone();
            person.age = this.age;
            person.sex = this.sex;
            person.name = this.name;
            return person;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return "Person{" + "name='" + name + ", sex=" + sex + ", age=" + age + '}';
    }
}
