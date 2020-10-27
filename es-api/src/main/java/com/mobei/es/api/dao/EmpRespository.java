package com.mobei.es.api.dao;

import com.mobei.es.api.entity.Emp;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EmpRespository extends ElasticsearchRepository<Emp, String> {

    /**
     * 根据姓名查询
     *
     * @param name
     * @return
     */
    List<Emp> findByName(String name);

    /**
     * 根据年龄查询
     *
     * @param age
     * @return
     */
    List<Emp> findByAge(Integer age);

    /**
     * 根据姓名和年龄查
     *
     * @param name
     * @param age
     * @return
     */
    List<Emp> findByNameAndAddress(String name, Integer age);

    /**
     * 根据姓名或者年龄查
     *
     * @param name
     * @param age
     * @return
     */
    List<Emp> findByNameOrAddress(String name, Integer age);

    /**
     * 查询年龄大于等于23
     *
     * @param age
     * @return
     */
    List<Emp> findByAgeGreaterThanEqual(Integer age);

}
