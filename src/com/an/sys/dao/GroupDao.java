package com.an.sys.dao;

import com.an.core.model.BaseDao;
import com.an.sys.entity.Group;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class GroupDao extends BaseDao<Group, Integer> {
    public GroupDao() {
        super();
        namespace = "SYS.GroupMapper";
    }

    public int save(Group node) {
        if (node.getId() == null || node.getId() <= 0) {
            return insert(node);
        }else{
            return update(node);
        }

//        if (node.getPath() != null && !node.getPath().isEmpty()
//                && node.getStep() > 1) {
//            String path[] = node.getPath().split(",");
//            Integer oldPid = Integer.parseInt(path[path.length - 1]);
//
//            if (node.getParent() != oldPid) {
//                Group oldParent = selectOne(oldPid);
//                Map<String, Object> param = new HashMap<>();
//                param.put("parent", oldPid);
//                if (count(param) == 1) {
//                    oldParent.setLeaf(true);
//                    update(oldParent);
//                }
//            }
//        }
//        if (node.getParent() != null && node.getParent() > 0) {
//            Group parent = selectOne(node.getParent());
//            if (parent.isLeaf()) {
//                parent.setLeaf(false);
//                update(parent);
//            }
//
//            node.setStep(parent.getStep() + 1);
//            node.setPath(parent.getPath() + parent.getId() + ",");
//            node.setDepth(parent.getDepth()
//                    + String.format("%02d", node.getSort())
//                    + String.format("%08d", node.getId()));
//        } else {
//            node.setStep(1);
//            node.setPath(",");
//            node.setDepth(String.format("%02d", node.getSort())
//                    + String.format("%08d", node.getId()));
//        }
//        int rst = update(node);
//        sqlSession.update(namespace + ".updateLeaf", node);
//        return rst;
    }

    public Collection<Map<Integer, String>> selectKV(String type, boolean own) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("own", own);
        return sqlSession.selectList(namespace + ".selectKV", params);
    }


    public Group selectUserByCode(Integer depNo) {
        return sqlSession.selectOne(namespace + ".selectUserByCode", depNo);
    }

    public Group selectByCode(String depCode) {
        return sqlSession.selectOne(namespace + ".selectByCode", depCode);
    }

}
