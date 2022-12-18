package jacek.brzezinski.piktogramy;

import android.app.Application;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;


public class PictogramModel {
    public static final int TREE_ROOT = 0;

    public static final int ROLE_PICTOGRAM = 0;
    public static final int ROLE_DIR = 1;
    public static final int ROLE_BACK = -1;

    private int id;
    private int parent;
    private int role;
    private String name;
    private String path;
    private Boolean resource;
    private int position;
    private Boolean isActive;

    //constructor
    public PictogramModel(int id, int parent, int role, String name, String path, Boolean resource, int position, Boolean isActive) {
        this.id = id;
        this.parent = parent;
        this.role = role;
        this.name = name;
        this.path = path;
        this.resource = resource;
        this.position = position;
        this.isActive = isActive;
    }

    public PictogramModel() {
    }

    public void createNew(int parent, String name, String path, Boolean resource, int position) {
        id = -1;
        this.parent = parent;
        this.role = PictogramModel.ROLE_PICTOGRAM;
        this.name = name;
        this.path = path;
        this.resource = resource;
        this.position = position;
        this.isActive = true;
    }

    public PictogramModel setRoleBack(int parent) {
        this.parent = parent;
        this.role = PictogramModel.ROLE_BACK;
        this.resource = true;
        this.path = "p_back";
        this.name = "Back";
        return this;
    }

    @Override
    public String toString() {
        return this.name + (this.path.length() > 0 ? " (" + this.path + ")" : "");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
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

    public Boolean isResource() {
        return resource;
    }

    public void setResource(Boolean resource) {
        this.resource = resource;
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
