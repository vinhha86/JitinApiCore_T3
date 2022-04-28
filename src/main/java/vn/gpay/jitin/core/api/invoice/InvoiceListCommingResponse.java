package vn.gpay.jitin.core.api.invoice;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.invoice.InvoiceList;

public class InvoiceListCommingResponse extends ResponseBase {
	public List<InvoiceList> data;
}
