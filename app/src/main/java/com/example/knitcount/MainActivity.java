package com.example.knitcount;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    private Counter counter;
    private TextView counterText;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        counterText = findViewById(R.id.counterText);
        TextView counterLabel = findViewById(R.id.counterLabel);

        Button incrementButton = findViewById(R.id.incrementButton);
        Button decrementButton = findViewById(R.id.decrementButton);
        Button resetButton = findViewById(R.id.resetButton);

        preferences =
                getSharedPreferences("counter_preferences", MODE_PRIVATE);


        String savedName = preferences.getString("counter_name", "Rows");
        int savedValue = preferences.getInt("counter_value", 0);

        counter = new Counter(savedName, savedValue);
        counterLabel.setText(counter.getName());
        counterText.setText(String.valueOf(counter.getValue()));

        counterLabel.setOnClickListener(view -> {
            EditText nameInput = new EditText(this);
            nameInput.setText(counter.getName());
            nameInput.selectAll();

            new AlertDialog.Builder(this)
                    .setTitle("Counter name")
                    .setView(nameInput)
                    .setPositiveButton("Save", (dialog, which) -> {
                        String newName = nameInput.getText().toString().trim();

                        if (!newName.isEmpty()) {
                            counter.setName(newName);
                            counterLabel.setText(counter.getName());

                            preferences.edit()
                                    .putString("counter_name", counter.getName())
                                    .apply();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        incrementButton.setOnClickListener(view -> {
            counter.increment();
            updateCounter();
        });

        decrementButton.setOnClickListener(view -> {
            counter.decrement();
            updateCounter();
        });

        resetButton.setOnClickListener(view -> {
            counter.reset();
            updateCounter();
        });

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (view, insets) -> {
                    Insets systemBars =
                            insets.getInsets(WindowInsetsCompat.Type.systemBars());

                    view.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );
    }

    private void updateCounter() {
        int currentValue = counter.getValue();

        counterText.setText(String.valueOf(currentValue));

        preferences.edit()
                .putInt("counter_value", currentValue)
                .apply();
    }
}