package Model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class Note {
    private IntegerProperty noteId;
    private ObjectProperty<LocalDateTime> creationDate;
    private StringProperty note;

    public Note(){
        this.creationDate = new SimpleObjectProperty<LocalDateTime>(LocalDateTime.now());
        this.noteId = new SimpleIntegerProperty();
        this.note = new SimpleStringProperty();
    }
    public Note(String note){
        this.note = new SimpleStringProperty(note);
        this.noteId = new SimpleIntegerProperty();
        this.creationDate = new SimpleObjectProperty<LocalDateTime>(LocalDateTime.now());
    }

    public int getNoteId() {
        return noteId.get();
    }

    public void setNoteId(int noteId) {
        this.noteId.set(noteId);
    }

    public LocalDateTime getCreationDate() {
        return creationDate.get();
    }

    public String getNote() {
        return note.get();
    }

    public void setNote(String note) {
        this.note = new SimpleStringProperty(note);
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate.set(creationDate);
    }
}
