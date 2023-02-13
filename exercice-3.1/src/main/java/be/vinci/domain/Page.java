package be.vinci.domain;

public interface Page {
    String getTitle();

    void setTitle(String title);

    String getUri();

    void setUri(String uri);

    String getContent();

    void setContent(String content);

    int getAuthorId();

    void setAuthorId(int authorId);

    String getStatus();

    void setStatus(String status);

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();

    int getId();

    void setId(int id);
}
