package com.an.wm.service;

import com.an.core.exception.BadRequestException;
import com.an.mm.dao.GoodsDao;
import com.an.utils.TestUtil;
import com.an.wm.dao.InventoryDao;
import com.an.wm.dao.ItemDao;
import com.an.wm.entity.Item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 库存调整服务
 *
 * @author karas
 */

@Service
public class InventoryService {

    @Autowired
    private InventoryDao invDao;

    @Autowired
    private GoodsDao goodsDao;

//    @Autowired
//    private InventoryLogDao logDao;

    @Autowired
    private ItemDao itemDao;

    /**
     * 收货
     * @param item 收货明细
     * @return 库存记录
     * @throws BadRequestException
     */
    @Transactional
    public Item receipt(Item item) throws BadRequestException {
        // 验证单据明细的有效性
        validateItem(item);
        if (item.getBelong() == null)
            item.setBelong(item.getBill().getBelong());

        // 改变默认参数
        if (item.getUsageType() == null)
            item.setUsageType("0");

        if (item.getReceiptDate() == null)
            item.setReceiptDate(new Date());

        // 更新移动平均价
        if (item.getCostPrice() != null)
            updateMovingAveragePrice(item);

        // 增加库存
        return append(item);
    }

    /**
     * 调拨出库入库
     * @param item
     * @throws BadRequestException
     */
    @Transactional
    public void transit(Item item) throws BadRequestException {
        validateItem(item);
        reduce(item);
        append(item);
    }

    /**
     * 发货出库
     * @param item
     * @throws BadRequestException
     */
    @Transactional
    public void issue(Item item) throws BadRequestException {
        validateItem(item);
        reduce(item);
    }

    /**
     * 库存盘点
     *
     * @param item
     * @throws BadRequestException
     */
    @Transactional
    public void inventory(Item item) throws BadRequestException {

        // TODO: 对比系统库存，产生关联到stockId的差异
        // TODO: 根据差异，调整总库存
        // TODO: 生成盘点snapshot
        // TODO: 按时间点，根据stockId调整库存变动记录snapshot

        invDao.save(item);
        snapshot(item, item.getBillId());

    }

    /**
     * 库内物动
     *
     * @param item
     * @return
     * @throws BadRequestException
     */
    @Transactional
    public Item move(Item item) throws BadRequestException {

        Item stock = invDao.selectOne(item.getStockId());
        stock.setAdjustQuantity(item.getRealQuantity().negate());
        stock.setRealQuantity(stock.getRealQuantity().subtract(item.getRealQuantity()));
        invDao.save(stock);
        stock.setTarget(item.getTarget());
        snapshot(stock, item.getBillId());

        stock.setLocation(item.getTarget());
        stock.setStockId(null);
        stock.setBill(item.getBill());
        stock.setAdjustQuantity(item.getRealQuantity());
        stock.setRealQuantity(item.getRealQuantity());
        invDao.save(stock);
        stock.setTarget(item.getLocation());
        snapshot(stock, item.getBillId());

        return stock;
    }

    /**
     * 废弃
     *
     * @param item
     * @throws BadRequestException
     */
    @Transactional
    public void trash(Item item) throws BadRequestException {
        item.setUsageType("trash");
        split(item, "");
    }

    /**
     * 锁定库存
     *
     * @param item
     * @return
     * @throws BadRequestException
     */
    @Transactional
    public Item lock(Item item) throws BadRequestException {
        item.setUsageType("locked");
        split(item, "normal");
        return item;
    }

    /**
     * 增加库存记录、数量
     *
     * @param item
     * @return
     */
    private Item append(Item item) {
        item.setAdjustQuantity(item.getRealQuantity());
        item.setStatus("t");
        item.setLocation(item.getTarget());
        invDao.save(item);
        snapshot(item, item.getBillId());
        
        
        return item;
    }

    private void merge(Item item) {

    }

    private void split(Item item, String usage) {
        // 转换到SKU单位数量
        BigDecimal skuQty = item.getRealQuantity().multiply(item.getUom().getPackQuantity());

        Collection<Item> stocks = loadStocks(item, usage);
        for (Item stock : stocks) {
            // 跳过无效库存记录
            if (stock.getRealQuantity().compareTo(new BigDecimal(0)) <= 0)
                continue;

            // 库存足够扣减
            if (stock.getRealQuantity()
                    .multiply(stock.getUom().getPackQuantity())
                    .compareTo(skuQty) > 0) {

                // 转换库存单位数量
                BigDecimal stockQty = stock.getRealQuantity().multiply(
                        stock.getUom().getPackQuantity());
                stockQty = stockQty.subtract(skuQty);

                Item newOne = (Item) stock.clone();
                newOne.setUsageType(item.getUsageType());
                newOne.setId(null);
                newOne.setStockId(null);
                newOne.setRealQuantity(skuQty);
                newOne.setAdjustQuantity(skuQty);
                newOne.setUom(stock.getSkuUom());
                newOne.setTarget(item.getTarget());
                invDao.save(newOne);
                
                snapshot(newOne, item.getBillId());

                // 如果剩余数量可以整除原库存单位
                if (stockQty.divide(stock.getUom().getPackQuantity(), 0,
                        BigDecimal.ROUND_CEILING).compareTo(
                        stockQty.divide(stock.getUom().getPackQuantity(),
                                0, BigDecimal.ROUND_FLOOR)) == 0) {

                    // 把剩余数量转换成原库存单位
                    stock.setRealQuantity(stockQty.divide(stock.getUom()
                            .getPackQuantity(), 0, BigDecimal.ROUND_CEILING));
                    stock.setAdjustQuantity(skuQty
                            .divide(stock.getUom().getPackQuantity(), 4,
                                    BigDecimal.ROUND_HALF_UP).negate());
                } else {

                    // 转换成SKU单位
                    stock.setUom(stock.getSkuUom());
                    stock.setRealQuantity(stockQty);
                    stock.setAdjustQuantity(skuQty.negate());
                }
                // 足够扣减，归零
                skuQty = new BigDecimal(0);

            } else { // 库存不足扣减
                stock.setAdjustQuantity(stock.getRealQuantity().negate());
                skuQty = skuQty.subtract(stock.getRealQuantity().multiply(
                        stock.getUom().getPackQuantity()));
                stock.setRealQuantity(new BigDecimal(0));
                stock.setUsageType(item.getUsageType());
            }

            snapshot(stock, item.getBillId());
            invDao.save(stock);
            if (skuQty.compareTo(new BigDecimal(0)) == 0) {
                break;
            }
        }
    }

    /**
     * 减少库存数量、记录
     *
     * @param item
     * @throws BadRequestException
     */
    private void reduce(Item item) throws BadRequestException {
        // 转换到SKU单位数量
        BigDecimal skuQty = item.getRealQuantity().multiply(item.getUom().getPackQuantity());

        Collection<Item> stocks = loadStocks(item, "normal");
        for (Item stock : stocks) {
            // 跳过无效库存记录
            if (stock.getRealQuantity().compareTo(new BigDecimal(0)) <= 0)
                continue;

            // 库存足够扣减
            if (stock.getRealQuantity().multiply(stock.getUom().getPackQuantity()).compareTo(skuQty) > 0) {

                // 转换库存单位数量
                BigDecimal stockQty = stock.getRealQuantity().multiply(stock.getUom().getPackQuantity());
                stockQty = stockQty.subtract(skuQty);

                // 如果剩余数量可以整除原库存单位
                if (stockQty.divide(stock.getUom().getPackQuantity(), 0, BigDecimal.ROUND_CEILING).compareTo(
                        stockQty.divide(stock.getUom().getPackQuantity(), 0, BigDecimal.ROUND_FLOOR)) == 0) {

                    // 把剩余数量转换成原库存单位
                    stock.setRealQuantity(stockQty.divide(stock.getUom().getPackQuantity(), 0, BigDecimal.ROUND_CEILING));
                    stock.setAdjustQuantity(skuQty.negate());
                } else {
                    // 转换成SKU单位
                    stock.setUom(stock.getSkuUom());
                    stock.setRealQuantity(stockQty);
                    stock.setAdjustQuantity(skuQty.divide(stock.getUom().getPackQuantity(), 4, BigDecimal.ROUND_HALF_UP).negate());
                }

                // 足够扣减，归零
                skuQty = new BigDecimal(0);

            } else { // 库存不足扣减
                stock.setAdjustQuantity(stock.getRealQuantity().negate());
                skuQty = skuQty.subtract(stock.getRealQuantity().multiply(stock.getUom().getPackQuantity()));
                stock.setRealQuantity(new BigDecimal(0));
            }
            stock.setTarget(item.getTarget());
            snapshot(stock, item.getBillId());
            invDao.save(stock);
            if (skuQty.compareTo(new BigDecimal(0)) == 0) {
                break;
            }
        }
    }

    /**
     * 更新移动平均价
     *
     * @param item
     */
    private void updateMovingAveragePrice(Item item) {
        if (item.getCostPrice() == null)
            return;
        BigDecimal qty = item.getRealQuantity().multiply(
                item.getUom().getPackQuantity() == null ? new BigDecimal(1) : item.getUom().getPackQuantity());
        Item goods = itemDao.selectByCode(item.getPn());
        //TODO item.getSummary() != null??
        if(goods.getAvgPrice() != null) {
            goods.setAvgPrice(qty
                    .multiply(item.getCostPrice())
                    .add(item.getSummary().multiply(
                            goods.getAvgPrice()))
                    .divide(qty.add(item.getSummary()), 4,
                            BigDecimal.ROUND_HALF_DOWN));
        }else{
            goods.setAvgPrice(item.getCostPrice());
        }
        itemDao.updateMovingAveragePrice(goods);
    }


    private Collection<Item> loadStocks(Item item, String usage) {
        Map<String, Object> param = new HashMap<>();
        if (item.getStockId() != null && item.getStockId() > 0) {
            param.put("stockId", item.getStockId());
        } else {
            param.put("usage", usage);
//            param.put("lotCode", item.getBatchCode());
//            param.put("section", item.getLocation().getSectionCode());
//            param.put("location", item.getLocation().getBarcode());
//            param.put("goodsCode", item.getPn());
            param.put("itemId", item.getItemId());
            param.put("warehouse", item.getLocation().getWarehouse());
            param.put("_BY", "batch_code");
        }
        TestUtil.printMap(param);
        return invDao.selectList(param);
    }

    /**
     * 库存快照，每当库存有变动时发生
     *
     * @param stock 库存
     */
    private void snapshot(Item stock, Integer bill) {
        Map<String,Object> param = new HashMap<>();
//      goodsDao.updateStock();Integer groupId, String pn, Integer itemId
//      logDao.save(stock);
        Integer groupId = stock.getTarget().getWarehouse();
        String pn = stock.getPn();
        Integer itemId = bill;
        goodsDao.updateStock(groupId, pn, itemId);
    }

    /**
     * 数据检查
     *
     * @param item
     * @throws BadRequestException
     */
    private void validateItem(Item item) throws BadRequestException {
        if (item.getRealQuantity() == null || item.getRealQuantity().floatValue() <= 0) {
            throw new BadRequestException("商品数量不可小于0:" + item.getId() + ":" + item.getPn() + item.getName());
        }
        if (item.getPn() == null || item.getPn().isEmpty()) {
            throw new BadRequestException("商品编号不可为空:" + item.getId() + ":" + item.getPn() + item.getName());
        }
        if (item.getUom() == null) {
            throw new BadRequestException("商品单位不可为空:" + item.getId() + ":" + item.getPn() + item.getName());
        }
    }
}
