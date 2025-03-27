package com.example.lab4_guess;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab4_guess.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView infoTextView;
    private EditText inputEditText;
    private Button guessButton;
    private Button exitButton;
    private Spinner difficultySpinner;

    private int secretNumber;
    private int maxNumber = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация элементов UI
        infoTextView = findViewById(R.id.infoTextView);
        inputEditText = findViewById(R.id.inputEditText);
        guessButton = findViewById(R.id.guessButton);
        exitButton = findViewById(R.id.exitButton);
        difficultySpinner = findViewById(R.id.difficultySpinner);

        // Настройка адаптера для Spinner (уровень сложности)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.difficulty_levels,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapter);

        // Слушатель для изменения уровня сложности
        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        maxNumber = 10;
                        break;
                    case 1:
                        maxNumber = 50;
                        break;
                    case 2:
                        maxNumber = 100;
                        break;
                }
                newGame(); // Начать новую игру при изменении уровня сложности
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Обработчик для кнопки выхода
        exitButton.setOnClickListener(v -> finish());

        // Обработчик для кнопки "Угадать"
        guessButton.setOnClickListener(this::onClick);

        // Начать игру с дефолтным уровнем сложности
        newGame();
    }

    // Метод для начала новой игры
    private void newGame() {
        Random random = new Random();
        secretNumber = random.nextInt(maxNumber + 1); // Загадать число от 0 до maxNumber
        infoTextView.setText(getString(R.string.try_to_guess) + " (0-" + maxNumber + ")");
    }

    // Метод обработки нажатия на кнопку "Угадать"
    public void onClick(View v) {
        String inputText = inputEditText.getText().toString();
        if (inputText.isEmpty()) {
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
            return;
        }

        int userGuess = Integer.parseInt(inputText);
        if (userGuess < 0 || userGuess > maxNumber) {
            Toast.makeText(this, "Введите число от 0 до " + maxNumber + "!", Toast.LENGTH_SHORT).show();
            inputEditText.setText(""); // Очищаем поле ввода
            return;
        }

        // Проверка на правильность предположения
        if (userGuess > secretNumber) {
            infoTextView.setText(getString(R.string.ahead));
        } else if (userGuess < secretNumber) {
            infoTextView.setText(getString(R.string.behind));
        } else {
            infoTextView.setText(getString(R.string.hit) + " " + getString(R.string.play_more) + "?");
            newGame(); // Начинаем новую игру после победы
        }

        // Очищаем поле ввода после каждой попытки
        inputEditText.setText("");
    }

    // Метод для завершения игры
    public void exitGame(View view) {
        finish();
    }
}
