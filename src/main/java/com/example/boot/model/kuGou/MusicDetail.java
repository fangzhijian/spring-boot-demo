package com.example.boot.model.kuGou;

import lombok.Data;

import java.io.Serializable;

/**
 * 2018/12/27 14:47
 * 走路呼呼带风
 * 歌曲详情
 */
@Data
public class MusicDetail implements Serializable{
    private String audio_name;  //歌曲全名,一般是歌手加歌名
    private String author_name; //歌手名字(艺名)
    private String song_name;   //歌曲名
    private String album_name;  //唱片集名称
    private Long timelength;    //时长,单位毫秒
    private Long filesize;      //歌曲大小,单位b
    private String lyrics;      //歌词
    private String img;         //封面图片
    private String play_url;    //歌曲url

}
