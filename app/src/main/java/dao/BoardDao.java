package dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dto.Board;

@Dao
public interface BoardDao {
    @Query("SELECT * FROM Board")
    List<Board> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Board board);

    @Update
    void update(Board board);

    @Delete()
    void delete(Board board);

    @Query("DELETE from Board")
    void deleteAll();
}


