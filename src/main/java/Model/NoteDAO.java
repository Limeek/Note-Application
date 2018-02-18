package Model;

import Controllers.NoteDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class NoteDAO {
    //метод достающий заметки из ResultSet'а в ObservableList
    public static ObservableList<Note> getNotesFromRS(ResultSet rs) throws SQLException{
        ObservableList<Note> notes = FXCollections.observableArrayList();
        while (rs.next()){
            Note note = new Note();
            note.setNote(rs.getString("note"));
            note.setNoteId(rs.getInt("Id"));
            note.setCreationDate(rs.getTimestamp("creationDate").toLocalDateTime());
            notes.add(note);
        }
        return notes;
    }
    //Метод, который достает из БД все заметки
    public static ObservableList<Note> findAllNotes() throws SQLException{
        String sqlStmt = "select * from notes order by creationDate DESC";
        try{
            ResultSet rsNotes = NoteDB.executeQuery(sqlStmt);
            ObservableList<Note> notes = getNotesFromRS(rsNotes);
            return notes;
        }
        catch (SQLException sqlEx){
            sqlEx.printStackTrace();
            System.out.println("Error while selecting all notes");
            throw sqlEx;
        }
    }
    //Метод вставляет запись в БД
    public static void insertNote(String note, LocalDateTime creationDate) throws SQLException{
        String sqlStmt = "INSERT INTO notes(NOTE,creationDate)\n"+
                 "VALUES('" + note + "',+'"+ creationDate +"');";
        try {
            NoteDB.executeUpdate(sqlStmt);
        }
        catch (SQLException sqlEx){
            sqlEx.printStackTrace();
            System.out.println("Error while inseting Note");
            throw sqlEx;
        }
    }
}
