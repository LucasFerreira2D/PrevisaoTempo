package br.com.trabalhofinal.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import br.com.trabalhofinal.data.local.model.WeatherHistory;

@Dao
public interface WeatherHistoryDao {

    @Insert
    void insert(WeatherHistory history);

    @Query("SELECT * FROM weather_history ORDER BY id DESC LIMIT 5")
    List<WeatherHistory> getAll();

    // deleta tudo menos os últimos 5
    @Query(" DELETE FROM weather_history WHERE id NOT IN (SELECT id FROM weather_history ORDER BY id DESC LIMIT 5)")
    void deleteAllExceptLastFive();
}
