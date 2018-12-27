package com.example.boot.model.kuGou;

import lombok.Data;

import java.io.Serializable;

/**
 * 2018/12/27 14:17
 * 走路呼呼带风
 * 歌曲列表信息
 */
@Data
public class MusicBody implements Serializable{
    private String AlbumID;         //唱片集id,用于请求歌曲详情
    /**
     *  FileHash、HQFileHash、SQFileHash、ResFileHash
     * 分别对应标准、高品、无损、原声，这里我们使用免费的
     */
    private String FileHash;        //标准音质文件hash,用于请求歌曲详情
    private String SingerName;      //歌手名字(艺名)
    private String SongName;        //歌曲名,包含h5标签,如斜体、加粗
    private String AlbumName;       //唱片集名称
    private String ExtName;         //音乐格式
    private Long FileSize;          //歌曲大小,单位b

}
