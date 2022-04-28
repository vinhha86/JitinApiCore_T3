package vn.gpay.jitin.core.api.invoice;

import java.util.List;

import vn.gpay.jitin.core.base.RequestBase;
import vn.gpay.jitin.core.invoice.Invoice;

public class InvoiceCreateRequest extends RequestBase{
	public String msgtype;
	public String token;
	public List<Invoice> data;
}
