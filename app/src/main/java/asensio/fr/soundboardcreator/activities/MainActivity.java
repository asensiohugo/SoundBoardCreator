package asensio.fr.soundboardcreator.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import asensio.fr.soundboardcreator.R;
import asensio.fr.soundboardcreator.database.DatabaseHelper;
import asensio.fr.soundboardcreator.model.Sound;

public class MainActivity extends AppCompatActivity {

    // PENSER A AUTORISER L'APPLICATION A ACCEDER AU STOCKAGE DANS LES PARAMETRES DU TELEPHONE

    private static final int REQUEST_CODE = 43;
    private static final String TAG = "MainActivity";
    private MediaPlayer mediaPlayer;

    private DatabaseHelper helper;
    private Dao<Sound, Integer> soundDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.helper = new DatabaseHelper(this);
        try {
            this.soundDao = helper.getSoundDao();
        } catch (Exception e) {
            throw new AssertionError(e.getMessage());
        }

        try {
            List<Sound> soundList = soundDao.queryForAll();
            //Log.d(TAG, "onCreate: " + soundList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Context context = getApplicationContext();
        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.list);
        //Log.d("main", "reclyclerview =" + recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration itemDecor = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        recyclerView.setAdapter(new SoundListAdapter());

        this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_sound_button) {
            startSearch();
        }
        return super.onOptionsItemSelected(item);
    }

    private void startSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("audio/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                //Toast.makeText(this, "Uri :" + uri, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this, AddSoundActivity.class);
                intent.putExtra("URI", uri.toString());
                startActivity(intent);
            }
        }
    }

    private class SoundListAdapter extends RecyclerView.Adapter<SoundListAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sound_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

            try {
                final List<Sound> sounds = soundDao.queryForAll();
                final Sound sound = sounds.get(position);

                holder.soundName.setText(sound.getName());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(sound.getUri());
                        if (!mediaPlayer.isPlaying()) {
                            try {
                                mediaPlayer.reset();
                                mediaPlayer.setDataSource(getApplicationContext(), uri);
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            mediaPlayer.stop();
                        }
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        PopupMenu popup = new PopupMenu(view.getContext(), view);
                        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                if (menuItem.getItemId() == R.id.edit) {
                                    Intent intent = new Intent(getApplicationContext(), EditSoundActivity.class);
                                    intent.putExtra("soundId", sound.getId());
                                    startActivity(intent);
                                }
                                if (menuItem.getItemId() == R.id.delete) {
                                    try {
                                        soundDao.delete(sound);
                                        notifyDataSetChanged();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                                return false;
                            }
                        });
                        popup.show();
                        return true;
                    }
                });


            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            try {
                return soundDao.queryForAll().size();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public final TextView soundName;

            public ViewHolder(View itemView) {
                super(itemView);
                soundName = itemView.findViewById(R.id.sound_name_text);
            }

        }


    }
}

