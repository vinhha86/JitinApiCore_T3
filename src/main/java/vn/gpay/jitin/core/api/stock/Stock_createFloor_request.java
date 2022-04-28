package vn.gpay.jitin.core.api.stock;

import vn.gpay.jitin.core.base.RequestBase;

public class Stock_createFloor_request extends RequestBase{
	public Long orgid_link;
	public String spaceepc;
	public String spaceepc_old;
	public String spacename;
	public String spacename_old;
	public Integer floorid;
	public Integer floorid_old;
	public Long rowid_link;
	public boolean isCreateNew;
}
