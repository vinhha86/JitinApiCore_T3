package vn.gpay.jitin.core.api.invcheck;

import vn.gpay.jitin.core.base.RequestBase;

public class InvcheckCreateRequest extends RequestBase{
	public Long bossid;
	public Long orgfrom_code;
	public Long productcode;
	public String extrainfo;
}
