package vn.gpay.jitin.core.stockout_order;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IStockout_order_d_service extends Operations<Stockout_order_d> {
	List<Stockout_order_d> getby_Stockout_order(Long Stockout_order_id_link);
	
	List<Stockout_order_d> getByStockoutOrderId(Long stockoutorderid_link);

	List<Stockout_order_d> findBy_PContractAndSku(Long pcontractid_link, Long pcontract_poid_link, Long porderid_link,Long stockid_link, Long skuid_link);
}
