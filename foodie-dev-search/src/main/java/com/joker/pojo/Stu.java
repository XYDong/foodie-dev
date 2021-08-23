package com.joker.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "stu", type = "_doc")
public class Stu {

    @Id
    private Long stuId;

    @Field(store = true)
    private String name;

    @Field(store = true)
    private Integer age;

    @Field(store = true)
    private Float money;

    @Field(store = true,type = FieldType.Keyword)
    private String sign;

    @Field(store = true)
    private String desc;

}
