package com.google;

import java.util.ArrayList;
import java.util.List;

/** A class used to represent a Playlist */
class VideoPlaylist implements Comparable<VideoPlaylist> {
    private String name;
    private List<Video> videos;
  
    public VideoPlaylist() {}
  
    public VideoPlaylist(String name) {
      this.name = name;
      this.videos = new ArrayList<Video>();
    }
  
    public String getName() {
      return this.name;
    }
  
    public List<Video> getVideos() {
      return this.videos;
    }

    @Override 
    public int compareTo(VideoPlaylist other) {
      return this.name.compareTo(other.getName());
    }

    public boolean chechDuplicate(String videoId) {
        boolean duplicateVideo = false;
        for(Video video : this.getVideos()) {
            if (video.getVideoId().equals(videoId)) {
                duplicateVideo = true;
                break;
            }
        }
        return duplicateVideo;     
    }

    public void addVideo(Video video) {
        this.videos.add(video);
    }

    public void removeVideo(Video video) {
      this.videos.remove(video);
    }

    public void removeAll() {
      this.videos.removeAll(videos);
    }


    public String getDisplayInfo() {
        String displayInfo = "";
        if (this.videos.size() > 0) {
          for (Video video : videos) {
            displayInfo += "  " + video.getDisplayInfo() + "\n";
          }

        } else {
          displayInfo += "  No videos here yet\n";
        }
   
        return displayInfo;
    }
}
