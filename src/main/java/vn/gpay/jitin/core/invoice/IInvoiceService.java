package vn.gpay.jitin.core.invoice;

import java.util.Date;
import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IInvoiceService extends Operations<Invoice>{

	public List<Invoice> findByInvoicenumber(String invoicenumber);
	
	public List<Invoice> findInvoiceBySkuCode(Long orgid_link,String invoicenumber,String skucode);
	
	public List<Invoice> InvoiceListComming(Long orgid_link,String stockcode,String orgfrom_code,String invoicenumber,Date shipdateto_from, Date shipdateto_to,int status);
	
	public List<InvoiceD> findInvoiceDBySkuCode(String invoicenumber,String skucode);
	
	public void updateStatusByInvoicenumber(String invoicenumber);
	
	public List<Invoice> findInvoiceActivate(Long orgid_link);

	List<Invoice> InvoiceList_ByPContractAndSKU(Long pcontractid_link, Long skuid_link);
	
	public List<Invoice> getInvoiceBySearch(
			String custom_declaration,
			Date invoicedate_from,
			Date invoicedate_to,
			String invoicenumber,
			Long org_prodviderid_link,
			Integer status
			);
}
