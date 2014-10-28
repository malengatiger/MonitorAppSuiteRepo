package com.com.boha.monitor.library.dto;

import java.io.Serializable;

/**
 * Created by aubreyM on 2014/04/24.
 */
public class VideoClipDTO implements Serializable, Comparable<VideoClipDTO> {

    private long videoDate, length, dateUploaded;
    private String uriString,
            comment,
            filePath,
            youTubeID,
            tournamentName, golfGroupName;
    private int tournamentID, videoClipID;
    private int golfGroupID, sortType;

    public String getGolfGroupName() {
        return golfGroupName;
    }

    public void setGolfGroupName(String golfGroupName) {
        this.golfGroupName = golfGroupName;
    }

    public long getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(long dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public long getVideoDate() {
        return videoDate;
    }

    public void setVideoDate(long videoDate) {
        this.videoDate = videoDate;
    }

    public String getYouTubeID() {
        return youTubeID;
    }

    public void setYouTubeID(String youTubeID) {
        this.youTubeID = youTubeID;
    }

    public int getVideoClipID() {
        return videoClipID;
    }

    public void setVideoClipID(int videoClipID) {
        this.videoClipID = videoClipID;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public String getUriString() {
        return uriString;
    }

    public void setUriString(String uriString) {
        this.uriString = uriString;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getTournamentID() {
        return tournamentID;
    }

    public void setTournamentID(int tournamentID) {
        this.tournamentID = tournamentID;
    }

    public int getGolfGroupID() {
        return golfGroupID;
    }

    public void setGolfGroupID(int golfGroupID) {
        this.golfGroupID = golfGroupID;
    }


    @Override
    public int compareTo(VideoClipDTO v) {

        switch (sortType) {
            case 0:
                if (videoDate == v.videoDate) return 0;
                if (videoDate < v.videoDate) return 1;
                if (videoDate > v.videoDate) return -1;
                break;
            case SORT_BY_DATE_DESC:
                if (videoDate == v.videoDate) return 0;
                if (videoDate < v.videoDate) return 1;
                if (videoDate > v.videoDate) return -1;
                break;
            case SORT_BY_DATE_ASC:
                if (videoDate == v.videoDate) return 0;
                if (videoDate < v.videoDate) return -1;
                if (videoDate > v.videoDate) return 1;
                break;
            case SORT_BY_TOURNAMENT:
                return tournamentName.compareTo(v.tournamentName);

        }
        return 0;
    }

    public static  final int SORT_BY_DATE_DESC = 1, SORT_BY_DATE_ASC = 2, SORT_BY_TOURNAMENT = 3;


}
