package com.chenxk.www.materialdesign_demo;

import java.io.Serializable;

/**
 * 新闻实体对象
 */
public class NewsBean implements Serializable{

    /** 新闻id */
    public int id;

    /** 新闻图片 */
    public int newsImage;

    /** 新闻标题 */
    public String newsTitle;
}