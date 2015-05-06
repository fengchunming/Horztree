package com.an.core.model;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * DAO基类
 */
public abstract class BaseDao<T, E> {

    @Autowired
    protected SqlSession sqlSession;

    /**
     * 命名空间
     */
    protected String namespace;


    public void setSqlSession(SqlSession session) {
        sqlSession = session;
    }

    /**
     * 根据params参数来统计总数
     *
     * @param params 查询条件
     * @return 记录数
     */
    public int count(Map<String, ?> params) {
        return sqlSession.selectOne(namespace + ".countByExample", params);
    }

    /**
     * 根据params来选择记录
     *
     * @param params 查询条件
     * @return 记录列表
     */
    public List<T> selectList(Map<String, ?> params) {
        return sqlSession.selectList(namespace + ".selectByExample", params);
    }

    /**
     * 根据主键来查找记录
     *
     * @param id 主键
     * @return 选择的记录
     */
    public T selectOne(E id) {
        return sqlSession.selectOne(namespace + ".selectByPrimaryKey", id);
    }

    /**
     * 新增记录
     *
     * @param object 需要插入的数据
     * @return 影响的记录数
     */
    public int insert(T object) {
        return sqlSession.insert(namespace + ".insert", object);
    }

    /**
     * 根据主键来更新记录
     *
     * @param object 需要插入的数据，含主键
     * @return 影响的行数
     */
    public int update(T object) {
        return sqlSession.update(namespace + ".updateByPrimaryKey", object);
    }

    /**
     * 根据id来删除记录
     *
     * @param id 需要删除的主键
     * @return 删除的记录数
     */
    public int delete(E id) {
        return sqlSession.delete(namespace + ".deleteByPrimaryKey", id);
    }

    /**
     * 验证编号唯一性
     *
     * @param params
     * @return
     */
    public int isReasonableCode(Map<String, ?> params) {
        return sqlSession.selectOne(namespace + ".isReasonable", params);
    }

}
