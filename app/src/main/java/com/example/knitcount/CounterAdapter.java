package com.example.knitcount;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CounterAdapter
        extends RecyclerView.Adapter<CounterAdapter.CounterViewHolder> {

    private final List<Counter> counters;
    private final CounterActionListener listener;

    public interface CounterActionListener {
        void onCounterChanged(Counter counter);
        void onCounterDeleted(Counter counter);
    }

    public CounterAdapter(
            List<Counter> counters,
            CounterActionListener listener
    ) {
        this.counters = counters;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CounterViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View counterView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_counter, parent, false);

        return new CounterViewHolder(counterView);
    }

    @Override
    public void onBindViewHolder(
            @NonNull CounterViewHolder holder,
            int position
    ) {
        Counter counter = counters.get(position);

        displayCounter(holder, counter);

        holder.incrementButton.setOnClickListener(view -> {
            counter.increment();
            displayCounter(holder, counter);

            listener.onCounterChanged(counter);
        });

        holder.decrementButton.setOnClickListener(view -> {
            counter.decrement();
            displayCounter(holder, counter);

            listener.onCounterChanged(counter);
        });

        holder.resetButton.setOnClickListener(view -> {
            counter.reset();
            displayCounter(holder, counter);

            listener.onCounterChanged(counter);
        });

        holder.counterLabel.setOnClickListener(view -> {
            showRenameDialog(holder, counter);
        });

        holder.deleteButton.setOnClickListener(view -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Delete counter")
                    .setMessage(
                            "Delete \"" + counter.getName() + "\"?"
                    )
                    .setPositiveButton("Delete", (dialog, which) -> {

                        int currentPosition =
                                holder.getBindingAdapterPosition();

                        if (currentPosition != RecyclerView.NO_POSITION) {

                            Counter deletedCounter =
                                    counters.get(currentPosition);

                            listener.onCounterDeleted(deletedCounter);

                            counters.remove(currentPosition);

                            notifyItemRemoved(currentPosition);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void displayCounter(
            CounterViewHolder holder,
            Counter counter
    ) {
        holder.counterLabel.setText(counter.getName());

        holder.counterText.setText(
                String.valueOf(counter.getValue())
        );
    }

    private void showRenameDialog(
            CounterViewHolder holder,
            Counter counter
    ) {
        EditText nameInput =
                new EditText(holder.itemView.getContext());

        nameInput.setText(counter.getName());
        nameInput.selectAll();

        new AlertDialog.Builder(holder.itemView.getContext())
                .setTitle("Counter name")
                .setView(nameInput)
                .setPositiveButton("Save", (dialog, which) -> {

                    String newName = nameInput
                            .getText()
                            .toString()
                            .trim();

                    if (!newName.isEmpty()) {
                        counter.setName(newName);

                        holder.counterLabel.setText(
                                counter.getName()
                        );

                        listener.onCounterChanged(counter);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return counters.size();
    }

    static class CounterViewHolder
            extends RecyclerView.ViewHolder {

        final TextView counterLabel;
        final TextView counterText;
        final Button incrementButton;
        final Button decrementButton;
        final Button resetButton;
        final ImageButton deleteButton;

        CounterViewHolder(@NonNull View itemView) {
            super(itemView);

            counterLabel =
                    itemView.findViewById(R.id.counterLabel);

            counterText =
                    itemView.findViewById(R.id.counterText);

            incrementButton =
                    itemView.findViewById(R.id.incrementButton);

            decrementButton =
                    itemView.findViewById(R.id.decrementButton);

            resetButton =
                    itemView.findViewById(R.id.resetButton);

            deleteButton =
                    itemView.findViewById(R.id.deleteButton);
        }
    }
}