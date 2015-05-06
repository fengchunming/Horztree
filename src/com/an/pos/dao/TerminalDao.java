package com.an.pos.dao;

import com.an.core.model.BaseDao;
import com.an.pos.entity.Terminal;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

@Repository
public class TerminalDao extends BaseDao<Terminal, Integer> {

    public TerminalDao() {
        super();
        namespace = "TerminalMapper";
    }

    public Terminal selectByCode(String code) {
        return sqlSession.selectOne(namespace + ".selectByCode", code);
    }

    public int save(Terminal terminal) {
        if (terminal.getTerminalId() > 0) {
            return update(terminal);
        } else {
            return insert(terminal);
        }
    }

    public Collection<Map<Integer, String>> selectKV() {
        return sqlSession.selectList(namespace + ".selectTerminalKV");
    }

}
