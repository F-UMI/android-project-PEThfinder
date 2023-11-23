package database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import android.content.Context;

import com.example.pethfinder.LocalDateTimeConverter;

import dao.BoardDao;
import dto.BoardDto;

@Database(entities = {BoardDto.class}, version = 2)
@TypeConverters({LocalDateTimeConverter.class})
public abstract class BoardDB extends RoomDatabase {
    public abstract BoardDao boardDao();

    private static BoardDB INSTANCE;

    public static BoardDB getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (BoardDB.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                BoardDB.class, "board.db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}



