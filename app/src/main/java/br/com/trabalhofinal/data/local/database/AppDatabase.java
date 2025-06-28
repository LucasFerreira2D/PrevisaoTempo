package br.com.trabalhofinal.data.local.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import br.com.trabalhofinal.data.local.dao.WeatherHistoryDao;
import br.com.trabalhofinal.data.local.model.WeatherHistory;

@Database(entities = {WeatherHistory.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;
    public abstract WeatherHistoryDao weatherHistoryDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "weather-db")
                            //.fallbackToDestructiveMigration() // Se quiser apagar e recriar em caso de erro de versão
                            .addCallback(roomCallback)
                            .build();

                    Log.d("AppDatabase", "Banco de dados criado!");
                }
            }
        }
        return INSTANCE;
    }

    private static final Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.d("AppDatabase", "onCreate chamado - Banco de dados inicializado.");
        }
    };
}