package com.example.boot.model.kuGou;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 2018/12/27 14:13
 * 走路呼呼带风
 * 歌曲列表分页信息
 */
@Data
public class MusicList implements Serializable{

    private Integer page;           //第几页
    private Integer pagesize;       //每页条数
    private Integer total;          //总条数
    private List<MusicBody> lists;  //分页列表
}
