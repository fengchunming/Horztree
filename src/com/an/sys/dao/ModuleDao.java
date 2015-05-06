package com.an.sys.dao;

import com.an.core.model.BaseDao;
import com.an.sys.entity.Module;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ModuleDao extends BaseDao<Module, Integer> {
    public ModuleDao() {
        super();
        namespace = "ModuleMapper";
    }

    public Module selectInit(Integer primaryKey) {
        if (primaryKey != null && primaryKey > 0)
            return selectOne(primaryKey);
        else
            return new Module();
    }

    public int save(Module node) {
        if (node.getId() == null || node.getId() <= 0) {
            insert(node);
        }

        if (node.getPath() != null && !node.getPath().isEmpty()
                && node.getStep() > 1) {
            String path[] = node.getPath().split(",");
            Integer oldPid = Integer.parseInt(path[path.length - 1]);

            if (!node.getParent().equals(oldPid)) {
                Module oldParent = selectOne(oldPid);
                Map<String, Object> param = new HashMap<>();
                param.put("parent", oldPid);
                if (count(param) == 1) {
                    oldParent.setLeaf(true);
                    update(oldParent);
                }
            }
        }

        if (node.getParent() != null && node.getParent() > 0) {
            Module parent = selectOne(node.getParent());
            if (parent.isLeaf()) {
                parent.setLeaf(false);
                update(parent);
            }

            node.setStep(parent.getStep() + 1);
            node.setPath(parent.getPath() + parent.getId() + ",");
            node.setDepth(parent.getDepth()
                    + String.format("%02d", node.getSort())
                    + String.format("%08d", node.getId()));
        } else {
            node.setStep(1);
            node.setPath(",");
            node.setDepth(String.format("%02d", node.getSort())
                    + String.format("%08d", node.getId()));
        }
        int rst = update(node);
        sqlSession.update(namespace + ".updateLeaf", node);
        return rst;
    }

    public Collection<Map<Integer, String>> selectKV() {
        return sqlSession.selectList(namespace + ".selectKV");
    }

}
