package com.google;

import java.util.Collections;
import java.util.List;

/** A class used to represent a video. */
class Video implements Comparable<Video>{

  private final String title;
  private final String videoId;
  private final List<String> tags;
  private boolean flag = false;
  private String reason;

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
  }

  public boolean isFlagged(){
    return this.flag;
  }

  public String getReason() {
    return this.reason;
  }

  public void setFlag() {
    this.flag = true;
    this.reason = "Not supplied";
  }

  

  public void setFlag(String reason) {
    this.flag = true;
    this.reason = reason;
  }

  public void allow() {
    this.flag = false;
    this.reason = null;
  }


  /** Returns formatted video info. */
  String getDisplayInfo() {
    String displayInfo = "";
    displayInfo += title;

    displayInfo += " (" + videoId + ") ";
    displayInfo += "[";
    for(int i = 0; i < tags.size(); i++) {
      if(i == tags.size() - 1) {
        displayInfo += tags.get(i);
      } else {
        displayInfo += tags.get(i);
        displayInfo += " ";
      }

    }
    displayInfo += "]";

    if (isFlagged()) {
      displayInfo += " - FLAGGED (reason: " + this.reason + ")";
    }

    return displayInfo;
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }
  /** Implement Comparable to get lexicographical order. */
  @Override
  public int compareTo(Video other) {
    return this.title.compareTo(other.title);
  }
}
