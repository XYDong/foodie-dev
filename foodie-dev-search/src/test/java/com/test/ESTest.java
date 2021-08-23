package com.test;

import com.joker.Application;
import com.joker.pojo.Stu;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ESTest {

    @Autowired
    private ElasticsearchTemplate esTemplate;


    /**
     * 不建议使用elasticsearchTemplate 对索引进行管理（创建索引，更新映射，删除索引）
     * 索引就像是数据库或者数据库中的表，平时不会频繁通过Java去创建和修改数据库或表，只会针对数据做CRUD的操作
     * 在ES中也是同理，我们尽量使用ElasticsearchTemplate 对文档进行CRUD操作
     * 1. 属性（FieldType）类型不灵活
     * 2. 主分片和副分片数无法设置
     */
    @Test
    public void createIndexStu(){
        Stu stu = new Stu();
        stu.setStuId(1002L);
        stu.setName("白小飞");
        stu.setAge(18);
        stu.setMoney(18.8f);
        stu.setSign("I'm spider man");
        stu.setDesc("I wish i am spider man");
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(stu).build();
        esTemplate.index(indexQuery);
    }

    @Test
    public void deleteIndexStu() {
        esTemplate.deleteIndex(Stu.class);
    }


    /*==============================分隔线======================================*/

    @Test
    public void updateStuDoc() {
        Map<String,Object> source = new HashMap<>();
        source.put("sign","I am not super man");
        source.put("money",88.6f);
        source.put("age",33);
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.source(source);
        UpdateQuery updateQuery = new UpdateQueryBuilder()
                .withClass(Stu.class)
                .withId("1002")
                .withIndexRequest(indexRequest)
                .build();
        esTemplate.update(updateQuery);
    }

    @Test
    public void getStuDoc() {
        GetQuery query = new GetQuery();
        query.setId("1002");

        Stu stu = esTemplate.queryForObject(query, Stu.class);
        System.out.println(stu);
    }
    @Test
    public void deleteStuDoc() {
        esTemplate.delete(Stu.class, "1002");
    }


}
