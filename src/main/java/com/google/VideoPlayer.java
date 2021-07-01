package com.google;

import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private List<Video> videos;
  private boolean isPlaying = false;
  private Video playingVideo;
  private boolean isPaused = false;
  private List<VideoPlaylist> playlists = new ArrayList<>();

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    videos = videoLibrary.getVideos();
    Collections.sort(videos);

    System.out.println("Here's a list of all available videos:");

    for (Video video : videos) {
      System.out.println(video.getDisplayInfo());
    }
  }

  public void playVideo(String videoId) {
    boolean foundVideo = false;
    Video previousVideo = null;
    videos = videoLibrary.getVideos();
    isPaused = false;

    for (Video video : videos) {
      if (videoId.equals(video.getVideoId())) {
        foundVideo = true;
        previousVideo = playingVideo;
        playingVideo = video;
        break;
      }

    }

    if (playingVideo.isFlagged()) {
      System.out.println("Cannot play video: Video is currently flagged (reason: " + playingVideo.getReason() + ")");
    } else {
      if (isPlaying && foundVideo) {
        System.out.println("Stopping video: " + previousVideo.getTitle());
      }
  
      if (foundVideo) {
        isPlaying = true;
        System.out.println("Playing video: " + playingVideo.getTitle());
      } else {
        System.out.println("Cannot play video: Video does not exist");
      }
    }  
  }

  public void stopVideo() {
    if (isPlaying) {
      System.out.println("Stopping video: " + playingVideo.getTitle());
      isPlaying = false;
    } else {
      System.out.println("Cannot stop video: No video is currently playing.");
    }
  }

  public void playRandomVideo() {
    videos = videoLibrary.getVideos();
    List<Video> filteredVideos = new ArrayList<>();
    for (Video video : videos) {
      if(!video.isFlagged()) {
        filteredVideos.add(video);
      }
    }

    if(filteredVideos.isEmpty()) {
      System.out.println("No videos available");
    } else {
      int random = (int) (Math.random() * filteredVideos.size());
      playVideo(videos.get(random).getVideoId());
    }
    
  }

  public void pauseVideo() {
    if (isPlaying) {
      if (isPaused) {
        System.out.println("Video already paused: " + playingVideo.getTitle());
      } else {
        System.out.println("Pausing video: " + playingVideo.getTitle());
        isPaused = true;
      }
    } else {
      System.out.println("Cannot pause video: No video is currently playing");
    }

  }

  public void continueVideo() {
    if (isPlaying) {
      if (isPaused) {
        isPaused = false;
        System.out.println("Continuing video: " + playingVideo.getTitle());
      } else {
        System.out.println("Cannot continue video: Video is not paused");
      }
    } else {
      System.out.println("Cannot continue video: No video is currently playing");
    }
  }

  public void showPlaying() {
    String info;
    if (isPlaying) {
      if (playingVideo.isFlagged()) {
        isPlaying = false;
        System.out.println("No video is currently playing");
      } else {
        info = playingVideo.getDisplayInfo();
        if (isPaused) {
          info += " - PAUSED";
        }
        System.out.println("Currently playing: " + info);
      }
    } else {
      System.out.println("No video is currently playing");
    }

  }

  public boolean playlistExists(String playlistName) {
    boolean playlistExists = false;
    for (VideoPlaylist playlist : playlists) {
      if (playlist.getName().toLowerCase().equals(playlistName.toLowerCase())) {
        playlistExists = true;
        break;
      }
    }
    return playlistExists;
  }

  public void createPlaylist(String playlistName) {
    if (playlists.size() > 0) {

      if (playlistExists(playlistName)) {
        System.out.println("Cannot create playlist: A playlist with the same name already exists");

      } else {
        playlists.add(new VideoPlaylist(playlistName));
        System.out.println("Successfully created new playlist: " + playlistName);
      }
    } else {
      playlists.add(new VideoPlaylist(playlistName));
      System.out.println("Successfully created new playlist: " + playlistName);
    }
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    boolean playlistExists = false;
    boolean videoExists = false;
    boolean sameVideoInList = true;
    VideoPlaylist videoPlaylist = null;
    Video videoToAdd = null;
    videos = videoLibrary.getVideos();

    for (VideoPlaylist playlist : playlists) {
      if (playlist.getName().toLowerCase().equals(playlistName.toLowerCase())) {
        videoPlaylist = playlist;
        playlistExists = true;
        break;
      }
    }

    for (Video video : videos) {
      if (video.getVideoId().equals(videoId)) {
        videoExists = true;
        videoToAdd = video;
        break;
      }
    }

    if (videoPlaylist != null && !videoPlaylist.chechDuplicate(videoId)) {
      sameVideoInList = false;
      videoPlaylist.addVideo(videoToAdd);
    }

    if (playlistExists) {

      if (videoExists) {
        if (sameVideoInList) {
          System.out.println("Cannot add video to " + playlistName + ": Video already added");
        } else {
          if (videoToAdd.isFlagged()) {
            System.out.println("Cannot add video to " + playlistName + ": Video is currently flagged (reason: " + videoToAdd.getReason() + ")");
          } else {
            System.out.println("Added video to " + playlistName + ": " + videoToAdd.getTitle());
          }
        }
      } else {
        System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
      }
    } else {
      System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
    }
  }

  public void showAllPlaylists() {
    Collections.sort(playlists);
    if (playlists.size() > 0) {
      System.out.println("Showing all playlists:");
      for (VideoPlaylist videoPlaylist : playlists) {
        System.out.println("  " + videoPlaylist.getName());
      }
    } else {
      System.out.println("No playlists exist yet");
    }
  }

  public void showPlaylist(String playlistName) {
    if (playlistExists(playlistName)) {
      System.out.println("Showing playlist: " + playlistName);

      for (VideoPlaylist videoPlaylist : playlists) {
        if (videoPlaylist.getName().toLowerCase().equals(playlistName.toLowerCase())) {
          System.out.print(videoPlaylist.getDisplayInfo());
        }
      }
    } else {
      System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
    }

  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    boolean playlistExists = false;
    boolean videoExists = false;
    boolean sameVideoInList = false;
    VideoPlaylist videoPlaylist = null;
    Video videoToRemove = null;
    videos = videoLibrary.getVideos();

    for (VideoPlaylist playlist : playlists) {
      if (playlist.getName().toLowerCase().equals(playlistName.toLowerCase())) {
        videoPlaylist = playlist;
        playlistExists = true;
        break;
      }
    }

    for (Video video : videos) {
      if (video.getVideoId().equals(videoId)) {
        videoExists = true;
        videoToRemove = video;
        break;
      }
    }

    if (videoPlaylist != null && videoPlaylist.chechDuplicate(videoId)) {
      sameVideoInList = true;
      videoPlaylist.removeVideo(videoToRemove);
    }

    if (playlistExists) {

      if (videoExists) {
        if (sameVideoInList) {
          System.out.println("Removed video from " + playlistName + ": " + videoToRemove.getTitle());
        } else {
          System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
        }
      } else {
        System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
      }
    } else {
      System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
    }
  }

  public void clearPlaylist(String playlistName) {
    boolean isCleared = false;
    for (VideoPlaylist videoPlaylist : playlists) {
      if (videoPlaylist.getName().toLowerCase().equals(playlistName.toLowerCase())) {
        videoPlaylist.removeAll();
        isCleared = true;
      }
    }
    if (isCleared) {
      System.out.println("Successfully removed all videos from " + playlistName);
    } else {
      System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
    }
  }

  public void deletePlaylist(String playlistName) {
    boolean isDeleted = false;
    VideoPlaylist playlistToDelete = null;
    for (VideoPlaylist videoPlaylist : playlists) {
      if (videoPlaylist.getName().toLowerCase().equals(playlistName.toLowerCase())) {
        playlistToDelete = videoPlaylist;
        isDeleted = true;
      }
    }

    playlists.remove(playlistToDelete);

    if (isDeleted) {
      System.out.println("Deleted playlist: " + playlistName);
    } else {
      System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
    }

  }

  public void searchVideos(String searchTerm) {
    Scanner scan = new Scanner(System.in);
    int numberToPlay;
    Video videoToPlay = null;

    videos = videoLibrary.getVideos();
    List<Video> searchResult = new ArrayList<>();
    int number = 1;

    for (Video video : videos) {
      if (video.getTitle().toLowerCase().contains(searchTerm.toLowerCase())) {
        if(!video.isFlagged()) {
          searchResult.add(video);
        }
      }
    }

    if (searchResult.isEmpty()) {
      System.out.println("No search results for " + searchTerm);

    } else {
      System.out.println("Here are the results for " + searchTerm + ":");

      Collections.sort(searchResult);
      for (Video video : searchResult) {
        System.out.print("  " + number + ") ");
        System.out.println(video.getDisplayInfo());
        number++;
      }

      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
      System.out.println("If your answer is not a valid number, we will assume it's a no.");

      try {
        numberToPlay = scan.nextInt();
        scan.nextLine();

        if (numberToPlay - 1 < searchResult.size()) {
          if (numberToPlay - 1 >= 0) {
            videoToPlay = searchResult.get(numberToPlay - 1);
            playVideo(videoToPlay.getVideoId());
          }
        }

      } catch (InputMismatchException ex) {
        
      }

      scan.close();
    }

  }

  public void searchVideosWithTag(String videoTag) {
    Scanner scan = new Scanner(System.in);
    int numberToPlay;
    Video videoToPlay = null;

    videos = videoLibrary.getVideos();
    List<Video> searchResult = new ArrayList<>();
    int number = 1;

    if (videoTag.charAt(0) == '#') {
      for (Video video : videos) {
        if (video.getTags().contains(videoTag)) {
          if (!video.isFlagged()) {
            searchResult.add(video);
          }     
        }
      }
  
      if (searchResult.isEmpty()) {
        System.out.println("No search results for " + videoTag);
  
      } else {
        System.out.println("Here are the results for " + videoTag + ":");
  
        Collections.sort(searchResult);
        for (Video video : searchResult) {
          System.out.print("  " + number + ") ");
          System.out.println(video.getDisplayInfo());
          number++;
        }
  
        System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
        System.out.println("If your answer is not a valid number, we will assume it's a no.");
  
        try {
          numberToPlay = scan.nextInt();
          scan.nextLine();
  
          if (numberToPlay - 1 < searchResult.size()) {
            if (numberToPlay - 1 >= 0) {
              videoToPlay = searchResult.get(numberToPlay - 1);
              playVideo(videoToPlay.getVideoId());
            }
          }
  
        } catch (InputMismatchException ex) {
          
        }
  
        scan.close();
      }
    } else {
      System.out.println("No search results for " + videoTag);
    }
    
  }

  public void flagVideo(String videoId) {
    boolean alreadyFlagged = false;
    boolean videoEixsts = false;
    String flaggedVideoName = "";
    String reason = "";
    videos = videoLibrary.getVideos();

    for (Video video : videos) {
      if (video.getVideoId().equals(videoId)) {
        videoEixsts = true;
        if (video.isFlagged())  {
          alreadyFlagged = true;
        } else {
          video.setFlag();
          reason = video.getReason();
          flaggedVideoName = video.getTitle();
          
        }
      }
    }

    if (videoEixsts) {
      if (alreadyFlagged) {
        System.out.println("Cannot flag video: Video is already flagged");
      } else {
        if (playingVideo !=null && playingVideo.getVideoId().equals(videoId)) {
          System.out.println("Stopping video: " + flaggedVideoName);
        }
        System.out.println("Successfully flagged video: " + flaggedVideoName + " (reason: " + reason + ")");
      }

    } else {
      System.out.println("Cannot flag video: Video does not exist");
    }
  }

  public void flagVideo(String videoId, String reason) {
    boolean alreadyFlagged = false;
    boolean videoEixsts = false;
    String flaggedVideoName = "";
    videos = videoLibrary.getVideos();

    for (Video video : videos) {
      if (video.getVideoId().equals(videoId)) {
        videoEixsts = true;
        if (video.isFlagged())  {
          alreadyFlagged = true;
        } else {
          video.setFlag(reason);
          reason = video.getReason();
          flaggedVideoName = video.getTitle();
          
        }
      }
    }

    if (videoEixsts) {
      if (alreadyFlagged) {
        System.out.println("Cannot flag video: Video is already flagged");
      } else {
        if (playingVideo !=null && playingVideo.getVideoId().equals(videoId)) {
          System.out.println("Stopping video: " + flaggedVideoName);
        }
        System.out.println("Successfully flagged video: " + flaggedVideoName + " (reason: " + reason + ")");
      }

    } else {
      System.out.println("Cannot flag video: Video does not exist");
    }
  }

  public void allowVideo(String videoId) {
    boolean videoEixsts = false;
    Video videoToAllow = null;
    videos = videoLibrary.getVideos();

    for (Video video : videos) {
      if (video.getVideoId().equals(videoId)) {
        videoEixsts = true;
        videoToAllow = video;
      }
    }

    if (videoEixsts) {
      if (videoToAllow.isFlagged()) {
        System.out.println("Successfully removed flag from video: " + videoToAllow.getTitle());
      } else {
        System.out.println("Cannot remove flag from video: Video is not flagged");
      }

    } else {
      System.out.println("Cannot remove flag from video: Video does not exist");
    }
  }
}