package com.an.mm.dao;

import com.an.mm.entity.Category;
import com.an.core.model.BaseDao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CategoryDao extends BaseDao<Category, Integer> {

	public CategoryDao() {
		super();
		namespace = "MM.CategoryMapper";
	}

	@Transactional
	public int save(Category category) {
		if (category.getId() == null || category.getId() <= 0) {
			insert(category);
		}

		//节点修改
		if (category.getPath() != null && !category.getPath().isEmpty() && category.getStep() > 1) {
			String path[] = category.getPath().split(",");
			Integer oldParentId = Integer.parseInt(path[path.length - 1]);
			//如果变更了父节点，检查原父节点是否变为叶子节点并更新
			if (!category.getParent().equals(oldParentId)) {
				Category oldParent = selectOne(oldParentId);
				Map<String, Object> param = new HashMap<>();
				param.put("parent", oldParentId);
				if (count(param) == 1) {//当前只有一个子节点了（即当前正被移走的节点）
					oldParent.setLeaf(true);
					update(oldParent);
				}
			}
		}

		//检查新父节点是不是叶子节点，如果是，则更新为非叶子节点
		if (category.getParent() != null && category.getParent() > 0) {
			Category parent = selectOne(category.getParent());
			if (parent.isLeaf()) {
				parent.setLeaf(false);
				update(parent);
			}

			category.setStep(parent.getStep() + 1);
			category.setPath(parent.getPath() + parent.getId() + ",");
			category.setDepth(parent.getDepth() + String.format("%02d", category.getSort()) + String.format("%02d", category.getId()));
			category.setCode(parent.getCode() + String.format("%02d", category.getId()));
		} else {//根节点
			category.setStep(1);
			category.setPath(",");
			category.setDepth(String.format("%02d", category.getSort()) + String.format("%02d", category.getId()));
			category.setCode(String.format("%02d", category.getId()));
		}
		int rst = update(category);
		updateChildren(category);
		return rst;
	}
	
	/**
	 * 递归更新子节点
	 * @param parent
	 */
	public void updateChildren(Category parent) {
		//更新当前节点的直接子节点
		sqlSession.update(namespace + ".updateChildren", parent);
		Map<String, Object> param = new HashMap<>();
		param.put("parent", parent.getId());
		List<Category> children = selectList(param);
		for (Category c : children) {
			if (!c.isLeaf()) {
				updateChildren(c);
			}
		}
	}

	public List<Map<String, Object>> selectKV(Map<String, String> param) {
		return sqlSession.selectList(namespace + ".selectKV", param);
	}

	@Transactional
	public int delete(Integer id) {
		sqlSession.update(namespace + ".deleteChildren", id);
		return super.delete(id);
	}

}
