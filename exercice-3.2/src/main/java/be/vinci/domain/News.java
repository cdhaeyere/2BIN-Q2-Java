package be.vinci.domain;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = NewsImpl.class)
public interface News {

  Integer getId();

  void setId(Integer id);

  String getTitle();

  void setTitle(String title);

  String getShortDescription();

  void setShortDescription(String shortDescription);

  String getContent();

  void setContent(String content);

  String getPublicationStatus();

  void setPublicationStatus(String publicationStatus);

  User getAuthor();

  void setAuthor(User author);

  Date getCreationDateTime();

  void setCreationDateTime(Date creationDateTime);

  Date getEndDateTime();

  void setEndDateTime(Date endDateTime);

  Page getAssociatedPage();

  void setAssociatedPage(Page associatedPage);

  Integer getPositionInPage();

  void setPositionInPage(Integer positionInPage);

}
