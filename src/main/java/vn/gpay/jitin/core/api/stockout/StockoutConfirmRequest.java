package vn.gpay.jitin.core.api.stockout;

import vn.gpay.jitin.core.base.RequestBase;
import vn.gpay.jitin.core.stockout.StockOut;

public class StockoutConfirmRequest extends RequestBase{
	public StockOut stockout;
	public Long stockoutId;
	public Long approver_userid_link;
	public Long unapprover_userid_link;
	public Long receiver_userid_link;
	public Long productid_link;
	public Long pcontractid_link;
	public Boolean isAutoChecked; // true: xuất điều chuyển: tạo mới stockin pklist để status là OK
}
