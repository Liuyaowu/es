package com.mobei.es.api;

import com.alibaba.fastjson.JSON;
import com.mobei.es.api.entity.User;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class EsApiApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 1、测试创建索引
     *
     * @throws IOException
     */
    @Test
    void contextLoads() throws IOException {
        // 1. 创建索引请求
        CreateIndexRequest createIndexRequest =
                new CreateIndexRequest("mobei_index");
        // 2.客户端执行请求
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);
    }

    /**
     * 2、测试获取索引,判断是否存在
     *
     * @throws IOException
     */
    @Test
    void testExistIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("mobei_index");
        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    /**
     * 3、测试删除索引
     *
     * @throws IOException
     */
    @Test
    void testDeleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("mobei_index");
        AcknowledgedResponse response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(response.isAcknowledged());
    }

    /**
     * 4、测试添加文档
     *
     * @throws IOException
     */
    @Test
    void testAddDoc() throws IOException {
        // 创建对象
        User user = new User("漠北之夜", 30);

        // 创建请求
        IndexRequest request = new IndexRequest("mobei_index");
        request.id("1");
        request.timeout(TimeValue.timeValueSeconds(1));
//        request.timeout("1s");

        // 数据放入请求:json格式
        request.source(JSON.toJSONString(user), XContentType.JSON);

        // 客户端发送请求
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
        System.out.println(response.toString());
    }

    /**
     * 5、测试获取文档,判断是否存在
     *
     * @throws IOException
     */
    @Test
    void testIsExistDoc() throws IOException {
        GetRequest request = new GetRequest("mobei_index","1");

        // 不获取返回的_source的上下文了
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none_");

        boolean exists = restHighLevelClient.exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    /**
     * 6、测试获取文档信息
     *
     * @throws IOException
     */
    @Test
    void testGetDoc() throws IOException {
        GetRequest request = new GetRequest("mobei_index","1");

        // 这里如果加上以下内容获取到对象为空
//        request.fetchSourceContext(new FetchSourceContext(false));
//        request.storedFields("_none_");

        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        System.out.println(response.getSourceAsString());
        System.out.println(response);
    }

    /**
     * 7、测试更新文档
     *
     * @throws IOException
     */
    @Test
    void testUpdateDoc() throws IOException {
        UpdateRequest request = new UpdateRequest("mobei_index", "1");

//        request.timeout(TimeValue.timeValueSeconds(1));
        request.timeout("1s");

        User user = new User("漠北之夜更新", 30);
        // 数据放入请求:json格式
        request.doc(JSON.toJSONString(user), XContentType.JSON);

        // 客户端发送请求
        UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
        System.out.println(response.toString());
    }

    /**
     * 8、测试删除文档
     *
     * @throws IOException
     */
    @Test
    void testDelDoc() throws IOException {
        DeleteRequest request = new DeleteRequest("mobei_index", "1");
        request.timeout("1s");
        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }

    /**
     * 9、测试批量插入文档
     *
     * @throws IOException
     */
    @Test
    void testBulkRequest() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");
        ArrayList<User> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new User("mobei" + i, i));
        }

        for (int i = 0; i < 10; i++) {
            bulkRequest.add(
                    new IndexRequest("buld_request_index")
                            //如果不指定id会使用默认的id
                    .id("" + (i + 1))
                    .source(JSON.toJSONString(list.get(i)), XContentType.JSON)
            );
        }

        BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(response);
        System.out.println(response.hasFailures());
    }

    /**
     * 查询
     *
     * @throws IOException
     */
    @Test
    void testSearch() throws IOException {
        SearchRequest request = new SearchRequest("mobei_index");

        //构建搜索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 设置高亮
//        searchSourceBuilder.highlighter();

        //精确匹配
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "张三");
        searchSourceBuilder.query(termQueryBuilder);

        //匹配所有
//        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();

        // 分页
//        searchSourceBuilder.from();
//        searchSourceBuilder.size();

        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        request.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(response.getHits()));
        System.out.println("===============");
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

}
