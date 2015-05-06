package com.an.wm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.mm.dao.GoodsDao;
import com.an.sys.entity.Organization;
import com.an.utils.PoiUtils;
import com.an.utils.Util;
import com.an.wm.dao.InventoryDao;
import com.an.wm.entity.Item;
import com.an.wm.entity.Location;
import com.an.wm.entity.MaterialUom;
import com.an.wm.entity.WorkBill;
import com.an.wm.service.InventoryService;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 库存查询/导入
 *
 * @author Karas 2012-3-8
 */
@Controller
@RequestMapping("/wm")
public class InventoryController {

    private static final Logger logger = LoggerFactory
            .getLogger(InventoryController.class);

    @Autowired
    private InventoryDao stockDao;

    @Autowired
    private InventoryService stockService;

    @Autowired
    private GoodsDao goodsDao;

    @RequestMapping(value = {"/inventory/list"}, method = RequestMethod.GET)
    public Map<?, ?> query(WebRequest request) throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        result.put("list", stockDao.selectList(mParam));
        result.put("count", stockDao.count(mParam));
        return result;
    }

    /**
     * 通过主键ID查询商品分类详情
     */
    @RequestMapping(value = "/inventory/{id}", method = RequestMethod.GET)
    public Item load(@PathVariable("id") int id)
            throws BadRequestException {
        return stockDao.selectOne(id);
    }

    /**
     * 通过主键ID查询商品分类详情
     */
    @RequestMapping(value = "/inventory/summary", method = RequestMethod.GET)
    public Map<?, ?> summary(WebRequest request)
            throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        result.put("list", stockDao.summaryList(mParam));
        result.put("count", stockDao.summaryCount(mParam));
        return result;
    }

    /**
     * 导入库存
     */
    @RequestMapping(value = "/inventory/import", method = RequestMethod.POST)
    public Collection<Map<String, String>> upload(HttpServletRequest request) throws Exception {
        InputStream is;
        List<Map<String, String>> result = new ArrayList<>();
        String filename = request.getHeader("X-File-Name");
        try {
            is = request.getInputStream();
            String regExp = ".+\\.(.+)$";
            String errorType = "exe,com,cgi,jsp,sh";
            Pattern p = Pattern.compile(regExp);
            Matcher m = p.matcher(filename);
            boolean contain = m.find();
            if (!contain || errorType.indexOf(m.group(1)) > 0) {
                throw new BadRequestException("wrong type file");
            }
            int version = (filename.endsWith("xlsx") ? 2007 : 2003);

            Workbook wb = (version == 2003) ? new HSSFWorkbook(is) : new XSSFWorkbook(is);

            Sheet sheet = wb.getSheetAt(0);
            Row header = sheet.getRow(0);
            int id = 0;
            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue;
                Item item = new Item();
                try {
                    // #{lotCode,jdbcType=BIGINT},
                    // #{usageType,jdbcType=VARCHAR},
                    // #{storageType,jdbcType=VARCHAR},
                    // #{weight,jdbcType=NUMERIC}, now(),
                    // #{volume,jdbcType=NUMERIC},
                    // #{pallet.deviceCode,jdbcType=VARCHAR},
                    // #{pallet.barcode,jdbcType=VARCHAR},
                    WorkBill bill = new WorkBill();
                    bill.setType("CS");
                    bill.setBillCode(PoiUtils.getString(row.getCell(1)));
                    item.setBill(bill);
                    item.setReceiptDate(PoiUtils.getDate(row.getCell(2)));
                    item.setPn(PoiUtils.getString(row.getCell(3)));

//                    Item goods = goodsDao.selectByCode(item.getPn());
                    if (item == null)
                        throw new Exception("未找到商品：" + item.getPn());
                    item.setCostPrice(item.getCostPrice());
                    item.setBarcode(PoiUtils.getString(row.getCell(4)));
                    item.setName(PoiUtils.getString(row.getCell(5)));
                    item.setRealQuantity(PoiUtils.getNumeric(row.getCell(6)));
                    item.setAdjustQuantity(PoiUtils.getNumeric(row.getCell(6)));
                    MaterialUom uom = new MaterialUom();
                    uom.setCode(PoiUtils.getString(row.getCell(7)));
                    item.setUom(uom);

                    Location local = new Location();
                    local.setSectionCode(PoiUtils.getString(row.getCell(8)));
                    local.setBarcode(PoiUtils.getString(row.getCell(9)));
                    item.setLocation(local);

                    Organization org = new Organization();
                    org.setOrgCode(PoiUtils.getString(row.getCell(10)));
                    org.setOrgType("partner");
//                    item.setBelongTo(org);

                    item.setProductDate(PoiUtils.getDate(row.getCell(11)));
                    JSONObject lotAttr = new JSONObject();
                    int i = 11;
                    while (i < header.getLastCellNum() - 1) {
                        i++;
                        if (!"".equals(PoiUtils.getString(row.getCell(i))))
                            lotAttr.put(PoiUtils.getString(header.getCell(i)),
                                    PoiUtils.getString(row.getCell(i)));
                    }
                    item.setBatchAttr(lotAttr);
                    stockService.inventory(item);
                } catch (Exception e) {
                    logger.info(e.getMessage(), e);
                    Map<String, String> x = new HashMap<>();
                    x.put("code", "" + (++id));
                    x.put("error", e.getMessage());
                    result.add(x);
                }
            }

            Map<String, String> x = new HashMap<>();
            x.put("code", "OK");
            x.put("error", "导入完成，共导入" + (sheet.getLastRowNum() - id) + "条记录,失败"
                    + id + "条");
            result.add(x);
            return result;
        } catch (Exception e) {
            throw new BadRequestException(e);
        }
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
    public ModelAndView otherRequestException(Exception e) {
        logger.error(e.getMessage(), e);
        return new ErrorModelAndView(e);
    }

}
