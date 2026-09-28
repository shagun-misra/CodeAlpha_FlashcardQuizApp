package com.example.codealpha_flashcardquizapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvCounter, tvQuestion, tvAnswer;
    private Button btnShowAnswer, btnPrevious, btnNext, btnAddCard, btnEditCard, btnDeleteCard;

    private List<Flashcard> flashcards;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCounter = findViewById(R.id.tvCounter);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvAnswer = findViewById(R.id.tvAnswer);
        btnShowAnswer = findViewById(R.id.btnShowAnswer);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnAddCard = findViewById(R.id.btnAddCard);
        btnEditCard = findViewById(R.id.btnEditCard);
        btnDeleteCard = findViewById(R.id.btnDeleteCard);

        flashcards = new ArrayList<>();
        flashcards.add(new Flashcard("What is an Economy?", "A system that provides people with the means to work and earn a living."));
        flashcards.add(new Flashcard("What is Opportunity Cost?", "The value of the next best alternative foregone."));
        flashcards.add(new Flashcard("Why is the PPF concave to origin?", "Due to increasing Marginal Opportunity Cost (MOC)."));

        updateCardUI();

        btnShowAnswer.setOnClickListener(v -> tvAnswer.setVisibility(View.VISIBLE));

        btnNext.setOnClickListener(v -> {
            if (currentIndex < flashcards.size() - 1) {
                currentIndex++;
                updateCardUI();
            }
        });

        btnPrevious.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                updateCardUI();
            }
        });

        btnAddCard.setOnClickListener(v -> showCardDialog(false));

        btnEditCard.setOnClickListener(v -> {
            if (!flashcards.isEmpty()) {
                showCardDialog(true);
            }
        });

        btnDeleteCard.setOnClickListener(v -> {
            if (!flashcards.isEmpty()) {
                flashcards.remove(currentIndex);
                if (currentIndex >= flashcards.size() && currentIndex > 0) {
                    currentIndex--;
                }
                updateCardUI();
                Toast.makeText(MainActivity.this, "Card deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCardUI() {
        if (flashcards.isEmpty()) {
            tvCounter.setText("0 of 0");
            tvQuestion.setText("No flashcards left. Tap '+ Add' to create one.");
            tvAnswer.setVisibility(View.GONE);
            btnShowAnswer.setEnabled(false);
            btnPrevious.setEnabled(false);
            btnNext.setEnabled(false);
            btnEditCard.setEnabled(false);
            btnDeleteCard.setEnabled(false);
            return;
        }

        btnShowAnswer.setEnabled(true);
        btnEditCard.setEnabled(true);
        btnDeleteCard.setEnabled(true);

        Flashcard currentCard = flashcards.get(currentIndex);
        tvCounter.setText("Card " + (currentIndex + 1) + " of " + flashcards.size());
        tvQuestion.setText(currentCard.getQuestion());
        tvAnswer.setText(currentCard.getAnswer());
        tvAnswer.setVisibility(View.GONE);

        btnPrevious.setEnabled(currentIndex > 0);
        btnNext.setEnabled(currentIndex < flashcards.size() - 1);
    }

    private void showCardDialog(boolean isEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isEdit ? "Edit Flashcard" : "Add Flashcard");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        final EditText etQuestion = new EditText(this);
        etQuestion.setHint("Enter question");
        layout.addView(etQuestion);

        final EditText etAnswer = new EditText(this);
        etAnswer.setHint("Enter answer");
        layout.addView(etAnswer);

        if (isEdit) {
            Flashcard current = flashcards.get(currentIndex);
            etQuestion.setText(current.getQuestion());
            etAnswer.setText(current.getAnswer());
        }

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String q = etQuestion.getText().toString().trim();
            String a = etAnswer.getText().toString().trim();

            if (q.isEmpty() || a.isEmpty()) {
                Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isEdit) {
                Flashcard current = flashcards.get(currentIndex);
                current.setQuestion(q);
                current.setAnswer(a);
            } else {
                flashcards.add(new Flashcard(q, a));
                currentIndex = flashcards.size() - 1;
            }
            updateCardUI();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
