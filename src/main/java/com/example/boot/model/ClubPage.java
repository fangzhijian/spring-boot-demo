package com.example.boot.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 2018/7/24 9:55
 * 走路呼呼带风
 * 只用于获取start和pageSize
 */
@Data
public class ClubPage<T> implements Serializable{

    private List<T> list = Collections.emptyList();

    private Integer count;

    private Integer pageNumber = 15 ;

    private Integer pageSize = 1;

    private Integer start = 0;

    public ClubPage(){

    }

    public ClubPage(Integer count,Integer pageSize,Integer pageNumber){
        this.count = count;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        setPageSize(pageSize);
        setPageNumber(pageNumber);
    }

    public Integer getStart() {
        return getPageSize()*(getPageNumber()-1);
    }


    public void setPageNumber(Integer pageNumber) {
        pageNumber =  pageNumber == null?1:pageNumber;
        int lastPageNo  = count/pageSize;
        int lastPageCount = count%pageSize;

        if( lastPageCount > 0 ){
            lastPageNo++;
        }
        if (pageNumber>lastPageNo){
            pageNumber = lastPageNo;
        }
        if (pageNumber<1){
            pageNumber = 1;
        }
        this.pageNumber = pageNumber;
    }


    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize == null?15:pageSize;
    }


}
