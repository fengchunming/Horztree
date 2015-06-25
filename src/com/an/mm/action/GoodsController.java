package com.an.mm.action;

import com.an.base.dao.RegionDao;
import com.an.base.entity.Region;
import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.mm.dao.CategoryDao;
import com.an.mm.dao.GoodsDao;
import com.an.mm.entity.Goods;
import com.an.mm.entity.Picture;
import com.an.sys.entity.Setting;
import com.an.utils.FileUtil;
import com.an.utils.ImageUtil;
import com.an.utils.Period;
import com.an.utils.Upload;
import com.an.utils.Util;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品管理
 *
 * @author Karas 2012-03-08
 */

@Controller
@RequestMapping("/mm")
public class GoodsController {

	private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

	@Autowired
	private GoodsDao goodsDao;
	
	@Autowired
	private RegionDao regionDao;
	
	@Autowired
	private CategoryDao categoryDao;

	/**
	 * 查询商品列表
	 * 
	 * @param request
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/goods/list", method = RequestMethod.GET)
	public Map<?, ?> query(WebRequest request) throws BadRequestException {
		Map<String, Object> mParam = Util.GetRequestMap(request);
		Map<String, Object> result = new HashMap<>();
		result.put("list", goodsDao.selectList(mParam));
		result.put("count", goodsDao.count(mParam));
		return result;
	}

	/**
	 * 商品详情
	 * 
	 * @param id
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/goods/{id}", method = RequestMethod.GET)
	public Goods load(@PathVariable("id") int id) throws BadRequestException {
		return goodsDao.selectOne(id);
	}

	/**
	 * 新增商品-保存
	 * 
	 * @param goods
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/goods", method = RequestMethod.POST)
	@Transactional
	public Goods insert(@RequestBody Goods goods) throws BadRequestException {
		if (goods.getCategoryId() != null && goods.getCategoryId() > 0) {
			String categoryCode = categoryDao.selectOne(goods.getCategoryId()).getCode();
			goods.setCategoryCode(categoryCode);
		}
		if (goodsDao.save(goods) != 1) {
			throw new BadRequestException("保存失败！");
		} else {
			return goods;
		}
	}

	/**
	 * 修改商品-保存
	 * 
	 * @param goods
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/goods/{id}", method = RequestMethod.PUT)
	@Transactional
	public Goods update(@RequestBody Goods goods) throws BadRequestException {
		if (goods.getCategoryId() != null && goods.getCategoryId() > 0) {
			String categoryCode = categoryDao.selectOne(goods.getCategoryId()).getCode();
			goods.setCategoryCode(categoryCode);
		}
		if (goodsDao.save(goods) != 1) {
			throw new BadRequestException("保存失败！");
		} else {
			return goods;
		}
	}

	/**
	 * 商品图片上传
	 * 
	 * @param request
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/goods/upload", method = RequestMethod.POST)
	public Picture upload(HttpServletRequest request) throws BadRequestException {
		try {
			String filename = Upload.htmlUpload(request, Setting.resourcePath);
			String source = Setting.resourcePath + filename;
			String s = source.substring(0, source.lastIndexOf("/") + 1) + "s_" + source.substring(source.lastIndexOf("/") + 1);
			String xs = source.substring(0, source.lastIndexOf("/") + 1) + "xs_" + source.substring(source.lastIndexOf("/") + 1);

			ImageUtil.fill(source, s, 220, 220);
			ImageUtil.fill(source, xs, 90, 90);
			return new Picture(filename);
		} catch (Exception e) {
			throw new BadRequestException(e);
		}
	}

	/**
	 * 删除商品
	 * 
	 * @param id
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/goods/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") int id) throws BadRequestException {
		if (goodsDao.delete(id) <= 0)
			throw new BadRequestException("删除失败");
	}

	/**
	 * 查询商品发布信息（库存等）
	 * 
	 * @param request
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/inventory/list", method = RequestMethod.GET)
	public Map<?, ?> queryInventory(WebRequest request) throws BadRequestException {
		Map<String, Object> mParam = Util.GetRequestMap(request);
		Map<String, Object> result = new HashMap<>();
		result.put("list", goodsDao.selectInventory(mParam));
		result.put("count", goodsDao.countInventory(mParam));
		return result;
	}

	/**
	 * 查询低库存
	 * 
	 * @param request
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/inventory/low", method = RequestMethod.GET)
	public Map<?, ?> queryLowerInventory(WebRequest request) throws BadRequestException {
		Map<String, Object> mParam = Util.GetRequestMap(request);
		mParam.put("own", true);
		mParam.put("low", true);
		Map<String, Object> result = new HashMap<>();
//		result.put("list", goodsDao.selectInventory(mParam));
		result.put("count", goodsDao.countInventory(mParam));
		return result;
	}

	/**
	 * 保存商品发布信息（库存等）
	 * @param goods
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/inventory/{id}", method = RequestMethod.PUT)
	@Transactional
	public Goods saveInventory(@RequestBody Goods goods) throws BadRequestException {
		if (goodsDao.updateGoodsInventory(goods) < 1) {//可能为2
			throw new BadRequestException("保存失败！");
		} else {
			return goods;
		}
	}
    /**
     * 批量导入库存
     * @param request
     * @return
     * @throws Exception
     */
	@RequestMapping(value = "/inventory/import", method = RequestMethod.POST)
	@Transactional
	public Map<String, Object> saveBatchInventory(HttpServletRequest request) throws Exception {
		String realPath = request.getServletContext().getRealPath("");
		String configFilePath = realPath + File.separator + "template" + File.separator + "GoodsRegionConfig.xml";
		List<Goods> list = FileUtil.importData(request, configFilePath);
		List<Goods> delList = new ArrayList<Goods>();
		List<Goods> reservedList = new ArrayList<Goods>();
		list = getIdByCode(list);
		// 删除保留位 是 “否” 的记录，更新保留位是 “是” 的记录
		for (Goods goods : list) {
			if ("否".equals(goods.getIsReserved().trim())) {
				delList.add(goods);
			} else {
				reservedList.add(goods);
			}
		}

		Map<String, Object> map = new HashMap<String, Object>();
		int result1 = 2;
		int result2 = 2;
		if (reservedList.size() > 0) {
			result1 = goodsDao.batchUpdateGoodsInventory(reservedList);
		}
		if (delList.size() > 0) {
			result2 = goodsDao.batchDeleteGoodsInventory(delList);
		}
		if (result1 < 1 || result2 < 1) {
			throw new BadRequestException("导入失败！");
		} else {
			map.put("msg", "导入成功");
			return map;
		}
	}

	/**
	 * 导出现有库存
	 * @param request
	 * @param webRequest
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/inventory/export", method = RequestMethod.GET)
	@Transactional
	public void exportInventory(HttpServletRequest request, WebRequest webRequest, HttpServletResponse response) throws Exception {
		String realPath = request.getServletContext().getRealPath("");
		String templatePath = realPath + File.separator + "template" + File.separator + "inventory_template.xlsx";
		Map<String, Object> beans = new HashMap<>();
		Map<String, Object> mParam = Util.GetRequestMap(webRequest);
		List<Goods> list = goodsDao.selectInventory(mParam);
		Integer regionId = Integer.parseInt(mParam.get("regionId").toString());
		Region region = regionDao.selectOne(regionId);
		for (Goods goods : list) {
			goods.setRegionCode(region.getCode());
			goods.setRegionName(region.getFullName());
		}
		beans.put("title", region.getFullName());
		beans.put("date", Util.CurrentTime("yyyy-MM-dd"));
		beans.put("list", list);
		beans.put("isReserved", "是");
		String currentTime = Period.getSystemTime();
		String exportFileName = "【" + beans.get("title") + "】" + "仓库库存统计表_" + currentTime + ".xlsx";
		XLSTransformer transformer = new XLSTransformer();
		Workbook workbook = transformer.transformXLS(new FileInputStream(new File(templatePath)), beans);

		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(exportFileName, "UTF-8"));
		response.setContentType("application/octet-stream; charset=utf-8");
		OutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		outputStream.flush();
		outputStream.close();

	}
	/**
	 * 异常处理
	 */
	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ModelAndView badRequestException(Exception e) {
		logger.error(e.getMessage(), e);
		return new ErrorModelAndView(e);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ModelAndView otherException(Exception e) {
		logger.warn(e.getMessage(), e);
		return new ErrorModelAndView(e);
	}
	
	private List<Goods> getIdByCode(List<Goods> list) {
		for (Goods goods : list) {
			goods.setCode(goods.getCode().trim());
			goods.setRegionCode(goods.getRegionCode().trim());
		}
		List<Goods> goodsIdList = goodsDao.selectGoodsIdList(list);
		// 查出goods 表中 code对应的id 放入list
		for (Goods i : list) {
			for (Goods j : goodsIdList) {
				if (i.getCode().equals(j.getCode())) {
					i.setId(j.getId());
					break;
				}
			}
		}
		List<Goods> regionIdList = goodsDao.selectRegionIdList(list);
		for (Goods i : list) {
			for (Goods j : regionIdList) {
				if (i.getRegionCode().equals(j.getRegionCode())) {
					i.setRegionId(j.getRegionId());
					break;
				}
			}
		}
		return list;
	}

}
