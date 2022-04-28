package vn.gpay.jitin.core.api.invoice;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.invoice.Invoice;

public class InvoiceResponse extends ResponseBase{
	public List<Invoice> data;
	public Integer totalCount;
}
