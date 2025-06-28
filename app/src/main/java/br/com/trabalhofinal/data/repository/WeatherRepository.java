package br.com.trabalhofinal.data.repository;

import br.com.trabalhofinal.data.api.WeatherService;
import br.com.trabalhofinal.data.model.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WeatherRepository {


    private static final String BASE_URL = "https://api.hgbrasil.com/";
    private static final String API_KEY = "cefb7e38";
    private WeatherService weatherService;

    public WeatherRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherService = retrofit.create(WeatherService.class);
    }

    public void getWeatherByLocation(double lat, double lon, WeatherCallback callback) {
        Call<WeatherResponse> call = weatherService.getWeatherByGPS(lat, lon, API_KEY);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Erro na resposta da API");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public interface WeatherCallback {
        void onSuccess(WeatherResponse response);
        void onFailure(String error);
    }

}
