package dto;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "board")
public class BoardDto {
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "username")
    private String userName;

    @ColumnInfo(name = "text")
    private String text;
    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    private byte[] imagePath;

    private String classification;

    public BoardDto(Long id, String password, String title, String userName, String text, String date, byte[] imagePath, String classification) {
        this.id = id;
        this.password = password;
        this.title = title;
        this.userName = userName;
        this.text = text;
        this.date = date;
        this.imagePath = imagePath;
        this.classification = classification;
    }

    public BoardDto() {
        this(null, "", "", "", "", null, null, null);
    }

    public BoardDto(long id) {
        this.id = id;
        this.password = "";
        this.title = "";
        this.userName = "";
        this.text = "";
        this.date = null;
        this.imagePath = null;
        this.classification = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public byte[] getImagePath() {
        return imagePath;
    }

    public void setImagePath(byte[] imagePath) {
        this.imagePath = imagePath;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
}


