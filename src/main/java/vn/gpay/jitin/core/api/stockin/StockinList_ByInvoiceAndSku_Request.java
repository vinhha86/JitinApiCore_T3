package vn.gpay.jitin.core.api.stockin;

import vn.gpay.jitin.core.base.RequestBase;

public class StockinList_ByInvoiceAndSku_Request extends RequestBase{
	public Long pcontractid_link;
	public Long pcontract_poid_link = null;
	public Long porderid_link = null;
	public Long stockid_link =  null;
	public Long material_invoiceid_link;
	public Long skuid_link;
}
