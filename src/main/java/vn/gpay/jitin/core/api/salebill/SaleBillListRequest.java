package vn.gpay.jitin.core.api.salebill;

import java.util.Date;

import vn.gpay.jitin.core.base.RequestBase;

public class SaleBillListRequest extends RequestBase{

	public Long orgbillid_link;
	public String billcode;
	public Date salebilldate_from;
	public Date salebilldate_to;
}
