package be.vinci.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class NewsImpl implements News {
  private int id;
  private String title;
  private String shortDescription;
  private String content;
  private final static String[] POSSIBLE_PUBLICATION_STATUSES = { "hidden", "published" };
  private String publicationStatus;
  private User author;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  private Date creationDateTime;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  private Date endDateTime;
  private Page associatedPage;
  private int positionInPage;

  public NewsImpl() {
    this.creationDateTime = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime(this.creationDateTime);
    cal.add(Calendar.MONTH, 1);
    this.endDateTime = cal.getTime();
  }

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String getShortDescription() {
    return shortDescription;
  }

  @Override
  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  @Override
  public String getContent() {
    return content;
  }

  @Override
  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public String getPublicationStatus() {
    return publicationStatus;
  }

  @Override
  public void setPublicationStatus(String publicationStatus) {
    this.publicationStatus = Arrays.stream(POSSIBLE_PUBLICATION_STATUSES)
        .filter(possibleStatus -> possibleStatus.equals(publicationStatus)).findFirst().orElse(null);
  }

  @Override
  public User getAuthor() {
    return author;
  }

  @Override
  public void setAuthor(User author) {
    this.author = author;
  }

  @Override
  public Date getCreationDateTime() {
    return creationDateTime;
  }

  @Override
  public void setCreationDateTime(Date creationDateTime) {
    this.creationDateTime = creationDateTime;
  }

  @Override
  public Date getEndDateTime() {
    return endDateTime;
  }

  @Override
  public void setEndDateTime(Date endDateTime) {
    this.endDateTime = endDateTime;
  }

  @Override
  public Page getAssociatedPage() {
    return associatedPage;
  }

  @Override
  public void setAssociatedPage(Page associatedPage) {
    this.associatedPage = associatedPage;
  }

  @Override
  public Integer getPositionInPage() {
    return positionInPage;
  }

  @Override
  public void setPositionInPage(Integer positionInPage) {
    this.positionInPage = positionInPage;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    NewsImpl other = (NewsImpl) obj;
    if (id != other.id)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "NewsImpl [id=" + id + ", title=" + title + ", shortDescription=" + shortDescription + ", content="
        + content + ", publicationStatus=" + publicationStatus + ", endDateTime=" + endDateTime
        + ", associatedPage=" + associatedPage + "]";
  }

}
