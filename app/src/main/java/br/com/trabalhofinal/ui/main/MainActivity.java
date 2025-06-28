package br.com.trabalhofinal.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.trabalhofinal.R;
import br.com.trabalhofinal.data.local.database.AppDatabase;
import br.com.trabalhofinal.data.local.model.WeatherHistory;
import br.com.trabalhofinal.data.model.Results;
import br.com.trabalhofinal.data.model.WeatherResponse;
import br.com.trabalhofinal.data.repository.WeatherRepository;
import br.com.trabalhofinal.ui.adapters.WeatherHistoryAdapter;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private TextView textViewCidade, textViewTemperatura, textViewDescricao, textViewDataHora, textViewDescricaoLua;
    private ImageView imageViewIcon, imageViewFaseLua;
    private ImageButton imageButtonRefresh;
    private ImageButton imageButtonDuvidas;
    private ImageButton imageButtonSobre;
    private RecyclerView recyclerViewHistorico;
    private WeatherRepository repository;
    private FusedLocationProviderClient fusedLocationClient;
    private Handler handler = new Handler();
    private Runnable updateTimeRunnable;
    private AppDatabase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dataBase = AppDatabase.getDatabase(this);

        // Mapeamento dos elementos da tela
        textViewCidade = findViewById(R.id.textViewCidade);
        textViewTemperatura = findViewById(R.id.textViewTemperatura);
        textViewDescricao = findViewById(R.id.textViewDescricao);
        textViewDataHora = findViewById(R.id.textViewDataHora);
        imageViewIcon = findViewById(R.id.imageViewIcon);
        textViewDescricaoLua = findViewById(R.id.textViewDescricaoLua);
        imageViewFaseLua = findViewById(R.id.imageViewFaseLua);
        recyclerViewHistorico = findViewById(R.id.recyclerViewHistorico);
        repository = new WeatherRepository();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        startUpdatingTime();
        verificarPermissaoLocalizacao();
    }

    private void verificarPermissaoLocalizacao() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            buscarLocalizacaoAtual();
        }
    }

    private void buscarLocalizacaoAtual() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Configura a solicitação de localização
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true); // Isso força o diálogo a aparecer se o GPS estiver desligado

        SettingsClient client = LocationServices.getSettingsClient(this);
        client.checkLocationSettings(builder.build())
                .addOnSuccessListener(this, locationSettingsResponse -> {
                    // GPS ativo, agora tenta pegar a localização
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(MainActivity.this, location -> {
                                if (location != null) {
                                    double latitude = location.getLatitude();
                                    double longitude = location.getLongitude();
                                    buscarClima(latitude, longitude);
                                } else {
                                    // Se não tiver lastLocation, solicita atualização ativa
                                    LocationRequest lr = LocationRequest.create()
                                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                            .setInterval(1000)
                                            .setFastestInterval(500)
                                            .setNumUpdates(1);

                                    fusedLocationClient.requestLocationUpdates(lr, new LocationCallback() {
                                        @Override
                                        public void onLocationResult(LocationResult locationResult) {
                                            Location freshLocation = locationResult.getLastLocation();
                                            if (freshLocation != null) {
                                                double latitude = freshLocation.getLatitude();
                                                double longitude = freshLocation.getLongitude();
                                                buscarClima(latitude, longitude);
                                            } else {
                                                Toast.makeText(MainActivity.this,
                                                        getString(R.string.local_indisponivel_msg),
                                                        Toast.LENGTH_LONG).show();
                                            }
                                            fusedLocationClient.removeLocationUpdates(this);
                                        }
                                    }, Looper.getMainLooper());
                                }
                            });
                })
                .addOnFailureListener(this, e -> {
                    if (e instanceof ResolvableApiException) {
                        try {
                            ((ResolvableApiException) e).startResolutionForResult(MainActivity.this, 1001);
                        } catch (IntentSender.SendIntentException sendEx) {
                            sendEx.printStackTrace();
                        }
                    }
                });
    }

    private void navegarSobre() {
        Intent intent = new Intent(this, SobreActivity.class);
        startActivity(intent);
    }

   private void navegarDuvidas(){
        Intent intent = new Intent(this, DuvidasActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (resultCode == RESULT_OK) {
                buscarLocalizacaoAtual();
            } else {
                Toast.makeText(this,
                        getString(R.string.ative_localizacao_msg),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void buscarClima(double latitude, double longitude) {
        repository.getWeatherByLocation(latitude, longitude, new WeatherRepository.WeatherCallback() {
            @Override
            public void onSuccess(WeatherResponse response) {
                Results results = response.results;
                runOnUiThread(() -> {
                    preencherDadosClima(results);
                    preencherFaseLua(results);
                    salvarNoHistorico(results);
                    carregarHistorico();
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this,
                        getString(R.string.erro_prefix, error),
                        Toast.LENGTH_LONG).show());
            }
        });
    }

    private void salvarNoHistorico(Results results) {
        new Thread(() -> {
            String dataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
            WeatherHistory history = new WeatherHistory(0, results.city_name, dataHora, results.temp + "°C");
            dataBase.weatherHistoryDao().insert(history);
        }).start();
    }

    private void carregarHistorico() {
        new Thread(() -> {
            dataBase.weatherHistoryDao().deleteAllExceptLastFive();
            List<WeatherHistory> lista = dataBase.weatherHistoryDao().getAll();
            runOnUiThread(() -> {
                WeatherHistoryAdapter adapter = new WeatherHistoryAdapter(lista);
                recyclerViewHistorico.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewHistorico.setAdapter(adapter);
            });
        }).start();
    }

    @SuppressLint({"SetTextI18n", "StringFormatMatches"})
    private void preencherDadosClima(Results results) {
        textViewCidade.setText(getString(R.string.cidade_prefix, results.city_name));
        textViewTemperatura.setText(getString(R.string.temperatura_format, results.temp));
        textViewDescricao.setText(results.description);
        int iconResId = getWeatherIconResource(results.condition_slug);
        imageViewIcon.setImageResource(iconResId);
    }

    @SuppressLint("SetTextI18n")
    private void preencherFaseLua(Results results) {
        textViewDescricaoLua.setText(traduzirFaseLua(results.moon_phase));
        int iconeLua = getMoonIcon(results.moon_phase);
        imageViewFaseLua.setImageResource(iconeLua);
    }

    private int getWeatherIconResource(String conditionSlug) {
        switch (conditionSlug) {
            case "clear_day": return R.drawable.clear_day;
            case "clear_night": return R.drawable.clear_night;
            case "cloudly_day": return R.drawable.cloudly_day;
            case "cloudly_night": return R.drawable.cloudly_night;
            case "cloud": return R.drawable.cloud;
            case "rain": return R.drawable.rain;
            case "storm": return R.drawable.storm;
            case "snow": return R.drawable.snow;
            case "fog": return R.drawable.fog;
            default: return R.drawable.none_day;
        }
    }

    private String traduzirFaseLua(String moonPhase) {
        switch (moonPhase) {
            case "new": return getString(R.string.moon_new);
            case "waxing_crescent": return getString(R.string.moon_waxing_crescent);
            case "first_quarter": return getString(R.string.moon_first_quarter);
            case "waxing_gibbous": return getString(R.string.moon_waxing_gibbous);
            case "full": return getString(R.string.moon_full);
            case "waning_gibbous": return getString(R.string.moon_waning_gibbous);
            case "last_quarter": return getString(R.string.moon_last_quarter);
            case "waning_crescent": return getString(R.string.moon_waning_crescent);
            default: return getString(R.string.moon_unknown);
        }
    }

    private int getMoonIcon(String moonPhase) {
        switch (moonPhase) {
            case "new": return R.drawable.moon_new;
            case "waxing_crescent": return R.drawable.waxing_crescent;
            case "first_quarter": return R.drawable.first_quarter;
            case "waxing_gibbous": return R.drawable.waxing_gibbous;
            case "full": return R.drawable.full;
            case "waning_gibbous": return R.drawable.waning_gibbous;
            case "last_quarter": return R.drawable.last_quarter;
            case "waning_crescent": return R.drawable.waning_crescent;
            default: return R.drawable.moon_new;
        }
    }

    private void startUpdatingTime() {
        updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String currentTime = sdf.format(new Date());
                textViewDataHora.setText("🕒 " + currentTime);
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(updateTimeRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && updateTimeRunnable != null) {
            handler.removeCallbacks(updateTimeRunnable);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                buscarLocalizacaoAtual();
            } else {
                Toast.makeText(this,
                        getString(R.string.permissao_negada_msg),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            buscarLocalizacaoAtual();
            return true;
        }
        else if (id == R.id.action_faq) {
            navegarDuvidas();
            return true;
        }
        else if (id == R.id.action_about) {
            navegarSobre();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}