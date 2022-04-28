package vn.gpay.jitin.core.api.invoice;

import java.util.Date;

import vn.gpay.jitin.core.base.RequestBase;

public class InvoiceListPageRequest extends RequestBase{
	public String custom_declaration;
	public Date invoicedate_from;
	public Date invoicedate_to;
	public String invoicenumber;
	public Long org_prodviderid_link;
	public Integer status;
	public Integer limit;
	public Integer page;
}
