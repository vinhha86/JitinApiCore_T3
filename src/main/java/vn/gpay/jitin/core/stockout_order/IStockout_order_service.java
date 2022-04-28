package vn.gpay.jitin.core.stockout_order;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import vn.gpay.jitin.core.base.Operations;

public interface IStockout_order_service extends Operations<Stockout_order>{
	List<Stockout_order> getby_porder(Long porderid_link);
	
	Page<Stockout_order> findBySearch(Date stockoutorderdate_from, Date stockoutorderdate_to, int page, int limit, Integer status, List<Long> listStockoutOrderId, Integer stockouttypeid_link);

	Page<Stockout_order> findBySearch_KeHoachSanXuat(Date stockoutorderdate_from, Date stockoutorderdate_to, int page, int limit, Integer status, List<Long> listStockoutOrderId, Integer stockouttypeid_link, Long porder_grantid_link);
	
	List<Long> getStockoutOrderByOrgFromId(Long orgid_from_link);
}
