package cz.prasoprd.msglogger;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import org.javacord.api.AccountType;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import cz.prasoprd.msglogger.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    public static MainActivity clazz;
    MySharedPreferences token;
    DiscordApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        token = new MySharedPreferences(MainActivity.this);
        if (!token.getText().isEmpty()) {
            api = new DiscordApiBuilder().setAccountType(AccountType.CLIENT).setToken(token.getText()).login().join();
        }
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(v -> {
            if (api != null) {
                Snackbar.make(v,"Already logged in as " + api.getYourself().getDiscriminatedName(), Snackbar.LENGTH_LONG).show();
            }
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("Enter your Discord token:");
            EditText editText = new EditText(MainActivity.this);
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            dialog.setView(editText);
            dialog.setPositiveButton("Ok", (dialog1, which) -> {
                String text = editText.getText().toString();
                if (!text.isEmpty()) {
                    token.setText(editText.getText().toString());
                    Messages.createNotification(token.getText(),"Token");
                }
            });
            dialog.setNegativeButton("Cancel", (dialog12, which) -> dialog12.cancel());
            dialog.show();
        });
        if (api != null) {
            new Thread(() -> {
                api.addMessageDeleteListener(event -> {
                    if (event.getMessage().isPresent() && event.getMessageAuthor().isPresent() && !event.getMessageAuthor().get().isYourself() && event.getMessageContent().isPresent() && !event.getMessageContent().get().isEmpty()) {
                        if (event.getMessage().get().isServerMessage() && event.getServer().isPresent() && event.getServerTextChannel().isPresent()) {
                            String text = event.getMessageContent().get();
                            String name = event.getMessageAuthor().get().getDiscriminatedName();
                            String channel = event.getServerTextChannel().get().getName();
                            String server = event.getServer().get().getName();
                            Messages.addDelete("\"" + text + "\" by " + name + " in " + channel + " from " + server);
                        } else if (event.getMessage().get().isPrivateMessage()) {
                            String text = event.getMessageContent().get();
                            String name = event.getMessageAuthor().get().getDiscriminatedName();
                            Messages.addDelete("\"" + text + "\" by " + name);
                        }
                    }
                });
                api.addMessageEditListener(event -> {
                    if (event.getMessage().isPresent() && event.getMessageAuthor().isPresent() && !event.getMessageAuthor().get().isYourself() && event.getOldContent().isPresent() && !event.getOldContent().get().isEmpty()) {
                        if (event.getMessage().get().isServerMessage() && event.getServer().isPresent() && event.getServerTextChannel().isPresent()) {
                            String text = event.getOldContent().get();
                            String name = event.getMessageAuthor().get().getDiscriminatedName();
                            String channel = event.getServerTextChannel().get().getName();
                            String server = event.getServer().get().getName();
                            Messages.addEdit("\"" + text + "\" by " + name + " in " + channel + " from " + server);
                        } else if (event.getMessage().get().isPrivateMessage()) {
                            String text = event.getOldContent().get();
                            String name = event.getMessageAuthor().get().getDiscriminatedName();
                            Messages.addEdit("\"" + text + "\" by " + name);
                        }
                    }
                });
            }).start();
        }
        clazz = this;
    }
}
