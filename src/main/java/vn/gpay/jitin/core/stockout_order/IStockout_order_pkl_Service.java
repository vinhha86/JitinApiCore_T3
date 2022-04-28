package vn.gpay.jitin.core.stockout_order;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IStockout_order_pkl_Service extends Operations<Stockout_order_pkl>{

	List<Stockout_order_pkl> getByStockoutOrderDId(Long stockoutorderdid_link);
	
	List<Stockout_order_pkl> getByLotnumberAndPackageId(Long stockoutorderdid_link, String lotnumber, Integer packageid);
	
	List<Stockout_order_pkl> getByStockoutOrderDId_lotnumber(Long stockoutorderdid_link, String lotnumber);
	
	List<Stockout_order_pkl> getByStockoutOrderDId_epc(Long stockoutorderdid_link, String epc);
}
