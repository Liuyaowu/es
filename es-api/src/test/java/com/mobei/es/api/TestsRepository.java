package com.mobei.es.api;

import com.mobei.es.api.dao.EmpRespository;
import com.mobei.es.api.entity.Emp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.*;

@SpringBootTest
class TestsRepository {

    @Autowired
    private EmpRespository empRespository;

    @Test
    void testSave() throws IOException {
        Emp emp = new Emp();
        emp.setId(UUID.randomUUID().toString());
        emp.setName("张三丰");
        emp.setBir(new Date());
        emp.setAge(24);
        emp.setAddress("CSC");
        emp.setContent("HELLO");
        empRespository.save(emp);
    }

    @Test
    void testSave2() throws IOException {
        Emp emp = new Emp();
        emp.setId(UUID.randomUUID().toString());
        emp.setName("张四丰");
        emp.setBir(new Date());
        emp.setAge(25);
        emp.setAddress("CSC");
        emp.setContent("HELLO");
        empRespository.save(emp);
    }

    @Test
    void testSave22() throws IOException {
        Emp emp = new Emp();
//        emp.setId(UUID.randomUUID().toString());
//        emp.setName("张四丰");
//        emp.setBir(new Date());
//        emp.setAge(25);
//        emp.setAddress("CSC");
//        emp.setContent("HELLO");
        Emp.DD d1 = new Emp.DD();
        d1.setId("d1");
        d1.setDName("dName1");
        d1.setDAge("1");
        Emp.DD d2 = new Emp.DD();
        d2.setId("d2");
        d2.setDName("dName2");
        d2.setDAge("2");
        List<Emp.DD> dds = new ArrayList<>();
        dds.add(d1);
        dds.add(d2);
        emp.setDdList(dds);

        empRespository.save(emp);
    }

    /**
     * 既可以完成更新又可以完成保存,id存在则更新,否则保存
     *  如果要更新先查询在保存
     *
     * @throws IOException
     */
    @Test
    void testUpdate() throws IOException {
        Emp emp = new Emp();
        emp.setId("e8b080e8-27a0-4504-9457-c1bb7fb0d263");
        emp.setName("改名啊");
        emp.setBir(new Date());
        emp.setAge(25);
        emp.setAddress("CSC");
        emp.setContent("HELLO");
        empRespository.save(emp);
    }

    @Test
    void testDelete() throws IOException {
        empRespository.deleteById("e8b080e8-27a0-4504-9457-c1bb7fb0d263");
    }

    @Test
    void testDeleteAll() throws IOException {
        empRespository.deleteAll();
    }

    @Test
    void testFindOne() throws IOException {
        Optional<Emp> byId = empRespository.findById("e8b080e8-27a0-4504-9457-c1bb7fb0d263");
        if (byId.isPresent()) {
            System.out.println(byId.get());
        }
    }

    @Test
    void testFindBy() throws IOException {
        List<Emp> empList = empRespository.findByName("张四丰");
        System.out.println(empList);
    }

    @Test
    void testFindAll() throws IOException {
//        Iterable<Emp> all = empRespository.findAll();
        List orders;
//        Sort sort = Sort.by("age");
        Sort sort = Sort.by(Sort.Order.asc("age"));
        Iterable<Emp> all = empRespository.findAll(sort);
        System.out.println(all);
    }

    @Test
    void testFindByPage() throws IOException {
//        Emp emp = new Emp();
//        emp.setId("e8b080e8-27a0-4504-9457-c1bb7fb0d263");
//        Sort sort;
//        Pageable pageable = new PageRequest(1, 10, Sort.by(Sort.Order.asc("age")));
//        empRespository.searchSimilar(emp, new String[]{"id"}, pageable);
//        empRespository.search(QueryBuilders.matchAllQuery(), PageRequest.of(1, 20));
    }
}
