package vn.gpay.jitin.core.warehouse_check;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IWarehouseCheckService  extends Operations<WarehouseCheck>{
	
	List<WarehouseCheck> findByStockoutOrderAndSku(Long stockoutorderid_link, Long skuid_link);
	
	List<WarehouseCheck> findBy_Warehouse_StockoutOrder(Long stockoutorderid_link, Long warehouseid_link);
}
