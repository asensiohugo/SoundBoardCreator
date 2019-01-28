package asensio.fr.soundboardcreator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import asensio.fr.soundboardcreator.R;
import asensio.fr.soundboardcreator.database.DatabaseHelper;
import asensio.fr.soundboardcreator.model.Sound;

public class EditSoundActivity extends AppCompatActivity {
    private DatabaseHelper helper;
    private Dao<Sound, Integer> soundDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsound);

        this.helper = new DatabaseHelper(this);
        try {
            this.soundDao = helper.getSoundDao();
        } catch (Exception e) {
            throw new AssertionError(e.getMessage());
        }

        final long id = this.getIntent().getLongExtra("soundId",-1);

        final EditText sound_edit_text = this.findViewById(R.id.soundName);
        Button sound_button = this.findViewById(R.id.valid_button);

        sound_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sound_edit_text.getText() != null){
                    try {
                        Log.d("Edit","id = "+ id);
                        Sound sound = soundDao.queryForId(Math.toIntExact(id));
                        sound.setName(sound_edit_text.getText().toString());
                        soundDao.update(sound);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

}
