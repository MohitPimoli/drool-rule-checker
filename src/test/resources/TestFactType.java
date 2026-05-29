package com.test.fixture;

/**
 * Minimal Java fixture class for property-based testing of Drools language support.
 * Provides getters, a boolean accessor, and a public field so that binding resolution
 * and dot-completion have a real PsiClass target.
 */
public class TestFactType {

    public String name;

    private String resourceId;
    private int age;
    private boolean active;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
