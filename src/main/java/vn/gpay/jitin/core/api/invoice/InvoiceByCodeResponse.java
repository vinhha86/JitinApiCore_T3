package vn.gpay.jitin.core.api.invoice;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.invoice.Invoice;
import vn.gpay.jitin.core.packinglist.PackingList;

public class InvoiceByCodeResponse extends ResponseBase{

	public List<Invoice> data;
	public List<PackingList> epcs;
}
