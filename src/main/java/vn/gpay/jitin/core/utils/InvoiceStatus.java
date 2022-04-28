package vn.gpay.jitin.core.utils;
public class InvoiceStatus {
	public static int INVOICE_STATUS_OK                			= 0;//Dự kiến ngày về, chưa có Invoice NK
    public static int INVOICE_STATUS_INVOICED            		= 1;//Đã có Số Invoice và ngày hàng về cảng
    public static int INVOICE_STATUS_STOCKED            		= 2;//Đã lập phiếu nhập kho
}
