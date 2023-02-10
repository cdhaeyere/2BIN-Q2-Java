package be.vinci;

import java.util.Arrays;

public class Text {

    private int id;
    private String content;
    private String level;
    private final static String[] levels = {"easy", "medium", "hard"};

    public Text() {
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getLevel() {
        return level;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLevel(String level) {
        this.level = Arrays.stream(levels).filter(l -> l.equals(level)).findFirst().orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Text text = (Text) o;

        return id == text.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Text{" + "id=" + id + ", content=" + content + ", level=" + level + '}';
    }
}
