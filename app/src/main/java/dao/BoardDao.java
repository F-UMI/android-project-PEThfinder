package dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import dto.Board;

@Dao
public interface BoardDao {
    @Query("SELECT * FROM Board")
    List<Board> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Board board);

    @Query("DELETE from Board")
    void deleteAll();
}


