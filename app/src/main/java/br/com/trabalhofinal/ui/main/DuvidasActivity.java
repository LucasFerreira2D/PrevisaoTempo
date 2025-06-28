package br.com.trabalhofinal.ui.main;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

import br.com.trabalhofinal.R;

public class DuvidasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_duvidas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.duvidas_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Amarra a Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbarDuvidas);
        setSupportActionBar(toolbar);

        // 2. Habilita o Up Button (setinha)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // 3. Captura o clique na setinha e finaliza a Activity
        toolbar.setNavigationOnClickListener(v -> finish());

    }
}