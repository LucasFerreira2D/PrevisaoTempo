package br.com.trabalhofinal.data.local.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weather_history")
public class WeatherHistory {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String cityName;
    public String dateTime;
    public String temperature;

    public WeatherHistory(int id, String cityName, String dateTime, String temperature) {
        this.id = id;
        this.cityName = cityName;
        this.dateTime = dateTime;
        this.temperature = temperature;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
