package dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import dto.BoardDto;

@Dao
public interface BoardDao {
    @Query("SELECT * FROM board")
    List<BoardDto> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BoardDto boardDto);

    @Update
    void update(BoardDto boardDto);

    @Delete()
    void delete(BoardDto boardDto);

    @Query("DELETE from board")
    void deleteAll();

    @Query("SELECT * FROM board WHERE classification = :desiredClassification")
    List<BoardDto> searchByClassification(String desiredClassification);
}


