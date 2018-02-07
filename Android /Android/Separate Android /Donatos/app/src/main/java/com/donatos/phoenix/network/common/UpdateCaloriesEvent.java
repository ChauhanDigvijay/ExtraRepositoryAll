package com.donatos.phoenix.network.common;

public class UpdateCaloriesEvent {
    private MenuItem menuItem;

    public UpdateCaloriesEvent(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public MenuItem getMenuItem() {
        return this.menuItem;
    }
}
