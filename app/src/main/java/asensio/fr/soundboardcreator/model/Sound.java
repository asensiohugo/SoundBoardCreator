package asensio.fr.soundboardcreator.model;

import android.net.Uri;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "sound")
public class Sound {


    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;
    @DatabaseField(columnName = "uri", canBeNull = false)
    private String uriToString;

    public Sound() {
    }

    public Sound(String name, String uriToSring) {
        this.name = name;
        this.uriToString = uriToSring;
    }

    public long getId() {
        return id;
    }

    public String getUri() {
        return uriToString;
    }

    public void setUri(Uri uri) {
        this.uriToString = uriToString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Sound{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", uriToString='" + uriToString + '\'' +
                '}';
    }

}
