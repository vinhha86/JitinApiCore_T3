package vn.gpay.jitin.core.stockin;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IStockInDService extends Operations<StockInD>{

	List<StockInD> findBy_InvoiceAndSku(Long material_invoiceid_link, Long skuid_link);

	List<StockInD> from_vendor_findBy_PContractAndSku(Long pcontractid_link, Long stockid_link, Long skuid_link);

	List<StockInD> getByStockinId(Long stockinid_link);
	
	List<StockInD> getbyStockinID_StockinDID(Long stockinid_link, Long stockindid_link);
	
	void recal_Totalcheck(Long stockindid_link);
	
	public List<Long> getStockinIdBySkuId(Long skuid_link);

	List<StockInD> pfrom_packing_findBy_PContractAndSku(Long pcontractid_link, Long stockid_link, Long skuid_link);

	List<StockInD> pfrom_packing_findBy_PContract(Long pcontractid_link, Long stockid_link);

	List<StockInD> from_vendor_findBy_PContract(Long pcontractid_link, Long stockid_link);

}
