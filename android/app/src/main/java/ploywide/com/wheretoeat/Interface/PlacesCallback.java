package ploywide.com.wheretoeat.Interface;

import java.util.List;

import ploywide.com.wheretoeat.model.PlacesItem;

public interface PlacesCallback {

    void success(List<PlacesItem> placesItems);

    void showError(String error);
}
