/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2020. All rights reserved.
 * Generated by the CloudDB ObjectType compiler.  DO NOT EDIT!
 */
package com.canbazdev.hmskitsproject1.data.model;

import com.huawei.agconnect.cloud.database.CloudDBZoneObject;
import com.huawei.agconnect.cloud.database.Text;
import com.huawei.agconnect.cloud.database.annotations.DefaultValue;
import com.huawei.agconnect.cloud.database.annotations.EntireEncrypted;
import com.huawei.agconnect.cloud.database.annotations.NotNull;
import com.huawei.agconnect.cloud.database.annotations.Indexes;
import com.huawei.agconnect.cloud.database.annotations.PrimaryKeys;

import java.util.Date;

/**
 * Definition of ObjectType PostInfo.
 *
 * @since 2022-07-20
 */
@PrimaryKeys({"id"})
public final class PostInfo extends CloudDBZoneObject {
    private Integer id;

    private String author;

    private String address;

    private String text;

    private Date date;

    private String image;

    public PostInfo() {
        super(PostInfo.class);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

}
