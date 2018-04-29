package com.qican.ifarmmanager.listener;

public interface PopwindowListener<Window, InfoType> {
    void infoChanged(Window w, InfoType obj);
}
