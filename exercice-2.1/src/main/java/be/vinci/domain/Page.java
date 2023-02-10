package be.vinci.domain;

import java.util.Arrays;

public class Page {

    private int id;
    private String title;
    private String uri;
    private String content;
    private int authorId;
    private String status;
    private final static String[] POSSIBLE_STATUS = { "hidden", "published" };

    public Page() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = Arrays.stream(POSSIBLE_STATUS).filter(possibleStatus -> possibleStatus.equals(status)).findFirst()
                .orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Page page = (Page) o;

        return id == page.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Page [id=" + id + ", title=" + title + ", uri=" + uri + ", content=" + content + ", publicationStatus="
                + status + ", authorId=" + authorId + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
