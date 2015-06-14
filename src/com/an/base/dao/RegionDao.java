package com.an.base.dao;

import com.an.base.entity.Region;
import com.an.core.model.BaseDao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RegionDao extends BaseDao<Region, Integer> {

	public RegionDao() {
		super();
		namespace = "RegionMapper";
	}
	
    public Region selectInit(Integer id) {
        if (id != null && id > 0) {
            return selectOne(id);
        } else {
            return new Region();
        }
    }

	@Transactional
	public int save(Region region) {
		if (region.getId() == null || region.getId() <= 0) {
			insert(region);
		}
		
		//节点修改
		if (region.getPath() != null && !region.getPath().isEmpty() && region.getStep() > 1) {
			String path[] = region.getPath().split(",");
			Integer oldParentId = Integer.parseInt(path[path.length - 1]);
			//如果变更了父节点，检查原父节点是否变为叶子节点并更新
			if (!region.getParent().equals(oldParentId)) {
				Region oldParent = selectOne(oldParentId);
				Map<String, Object> param = new HashMap<>();
				param.put("parent", oldParentId);
				if (count(param) == 1) {//当前只有一个子节点了（即当前正被移走的节点）
					oldParent.setLeaf(true);
					update(oldParent);
				}
			}
		}

		//检查新父节点是不是叶子节点，如果是，则更新为非叶子节点
		if (region.getParent() != null && region.getParent() > 0) {
			Region parent = selectOne(region.getParent());
			if (parent.isLeaf()) {
				parent.setLeaf(false);
				update(parent);
			}

			region.setStep(parent.getStep() + 1);
			region.setPath(parent.getPath() + parent.getId() + ",");
			region.setCode(parent.getCode() + String.format("%04d", region.getId()));
			region.setDepth(parent.getDepth() + String.format("%02d", region.getSort()) + String.format("%04d", region.getId()));
		} else {
			region.setStep(1);
			region.setPath(",");
			region.setCode(String.format("%04d", region.getId()));
			region.setDepth(String.format("%02d", region.getSort()) + String.format("%04d", region.getId()));
		}
		int rst = update(region);
		updateChildren(region);
		return rst;
	}

	/**
	 * 递归更新子节点
	 * @param parent
	 */
	public void updateChildren(Region parent) {
		//更新当前节点的直接子节点
		sqlSession.update(namespace + ".updateChildren", parent);
		Map<String, Object> param = new HashMap<>();
		param.put("parent", parent.getId());
		List<Region> children = selectList(param);
		for (Region r : children) {
			if (!r.isLeaf()) {
				updateChildren(r);
			}
		}
	}

	public List<Map<String, Object>> selectKV(Map<String, Object> params) {
		return sqlSession.selectList(namespace + ".selectKV", params);
	}

	public int updateBusinessStatus(Region region) {
		return sqlSession.update(namespace + ".updateBusinessStatus", region);
	}

	@Transactional
	public int delete(Integer id) {
		sqlSession.update(namespace + ".deleteChildren", id);
		return super.delete(id);
	}

}
