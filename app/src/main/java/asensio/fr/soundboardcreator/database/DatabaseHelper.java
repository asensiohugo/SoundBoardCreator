package asensio.fr.soundboardcreator.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import asensio.fr.soundboardcreator.model.Sound;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    /**
     * CONSTANT
     */
    private static final String DB_NAME = "soundDatabase.sqlite";
    private static final int DB_VERSION = 1;
    /**
     * A/D
     */
    private Dao<Sound, Integer> soundDAO = null;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, com.j256.ormlite.support.ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Sound.class);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, com.j256.ormlite.support.ConnectionSource connectionSource, int i, int i2) {
        try {
            TableUtils.dropTable(connectionSource, Sound.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter/Setter
     */
    public Dao<Sound, Integer> getSoundDao() {
        if (soundDAO == null) {
            try {
                soundDAO = getDao(Sound.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return soundDAO;
    }
}