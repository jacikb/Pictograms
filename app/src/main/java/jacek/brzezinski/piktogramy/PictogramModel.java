package jacek.brzezinski.piktogramy;

import android.app.Application;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

public class PictogramModel {
    private int id;
    private String name;
    private String path;
    private int position;
    private Boolean isActive;

    //constructor
    public PictogramModel(int id, String name, String path, int position, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.position = position;
        this.isActive = isActive;
    }

    public PictogramModel() {
    }

    public void createNew(String name, String path, int position) {
        id = -1;
        this.name = name;
        this.path = path;
        this.position = position;
        this.isActive = true;
    }


    @Override
    public String toString() {
        return "CustomerModel{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", path='" + path + "'" +
                ", position=" + position +
                ", isActive=" + (isActive ? 1 : 0) +
                '}';
    }

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
