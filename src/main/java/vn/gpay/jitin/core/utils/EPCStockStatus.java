package vn.gpay.jitin.core.utils;
public class EPCStockStatus {
	//Dùng để update Stockid_link trong bảng Warehouse
	public static long EPCSTOCK_ONLOGISTIC	= -1;//Chíp đã xuất kho và đang trên đường
    public static long EPCSTOCK_SOLDOUT		= -2;//Chíp đã bán cho khách hàng
}
