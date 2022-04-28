package vn.gpay.jitin.core.api.invcheck;

import java.util.Date;

import vn.gpay.jitin.core.base.RequestBase;

public class InvcheckListRequest extends RequestBase{

	public String stockcode;
	public String orgfrom_code;
	public Date invdateto_from;
	public Date invdateto_to;
	public Integer status;
}
