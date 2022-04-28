package vn.gpay.jitin.core.stock;

import java.util.List;

import vn.gpay.jitin.core.api.stock.Stock_menu_request;
import vn.gpay.jitin.core.base.Operations;

public interface IStockrowService extends Operations<Stockrow>{

	public List<Stockrow> findStockrowByOrgID(long id);
	
	public List<StockTree> createTree(List<Stockrow> stockrow_list, List<Long> listorgId, Stock_menu_request entity);
	public List<StockTree> createStockProductTree(List<Stockrow> stockrow_list, List<Long> listorgId, Stock_menu_request entity);
	
	List<Stockrow> findStockrowByOrgIDAndCode(Long orgid_link, String code);
	
	List<Stockrow> findStockrowByOrgIDAndCodeAndNotId(Long orgid_link, String code, Long id);
	
	List<Stockrow> findStockRowByOrgtype(Integer orgtypeid_link);
}
