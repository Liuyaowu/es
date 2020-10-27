package com.mobei.es.api.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
//用在类上,将Emp对象映射成ES中的一条json,前提是ES中没有该索引,会自动创建
@Document(indexName = "ems")
public class Emp {
    @Id//用来将对象中的id属性与文档中_id一一对象
    private String id;
    @Field(type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Integer)
    private Integer age;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private Date bir;
    @Field(type = FieldType.Text)
    private String content;
    @Field(type = FieldType.Text)
    private String address;
}
