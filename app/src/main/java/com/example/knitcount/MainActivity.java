package com.example.knitcount;

import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import androidx.room.Room;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private final List<Counter> counters = new ArrayList<>();
    private CounterAdapter counterAdapter;
    private AppDatabase database;
    private CounterDao counterDao;
    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        database = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "knitcount_database"
        ).build();

        counterDao = database.counterDao();

        RecyclerView countersRecyclerView =
                findViewById(R.id.countersRecyclerView);

        Button addCounterButton =
                findViewById(R.id.addCounterButton);


        counterAdapter = new CounterAdapter(
                counters,
                new CounterAdapter.CounterActionListener() {

                    @Override
                    public void onCounterChanged(Counter counter) {
                        executor.execute(() -> {
                            counterDao.update(counter);
                        });
                    }

                    @Override
                    public void onCounterDeleted(Counter counter) {
                        executor.execute(() -> {
                            counterDao.delete(counter);
                        });
                    }
                }
        );

        countersRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        countersRecyclerView.setAdapter(counterAdapter);

        executor.execute(() -> {
            List<Counter> savedCounters = counterDao.getAll();

            runOnUiThread(() -> {
                counters.addAll(savedCounters);
                counterAdapter.notifyDataSetChanged();
            });
        });

        addCounterButton.setOnClickListener(view -> {
            int counterNumber = counters.size() + 1;

            Counter newCounter = new Counter(
                    0,
                    "Counter " + counterNumber,
                    0
            );

            executor.execute(() -> {
                long newId = counterDao.insert(newCounter);
                newCounter.setId(newId);

                runOnUiThread(() -> {
                    counters.add(newCounter);

                    int newPosition = counters.size() - 1;

                    counterAdapter.notifyItemInserted(newPosition);
                    countersRecyclerView.scrollToPosition(newPosition);
                });
            });
        });

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (view, insets) -> {
                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

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
}