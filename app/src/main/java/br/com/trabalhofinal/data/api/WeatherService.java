package br.com.trabalhofinal.data.api;

import br.com.trabalhofinal.data.model.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("weather")
    Call<WeatherResponse> getWeatherByGPS(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("key") String apiKey
    );
}
