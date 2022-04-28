package vn.gpay.jitin.core.utils;
public class RFPrintStockinStatus {
	public static int RFPRINT_STOCKIN_STATUS_NEW                	= 0;//Mới tạo, chưa in
    public static int RFPRINT_STOCKIN_STATUS_OK            			= 1;//Đã in OK
    public static int RFPRINT_STOCKIN_STATUS_ERR            		= -1;//In lỗi
    
    public static final int     ZB_OK               = 0;
    public static final String  ZB_OK_MSG           = "Printer ready";
    
    public static final int     ZB_INJOB            = 1;
    public static final String  ZB_MSG_INJOB        = "Printer started";    
    
    public static final int     ZB_ERR_OUTPAPER     = 21;
    public static final String  ZB_MSG_OUTPAPER     = "Out of paper";  
    public static final String  ZB_MSG_OUTPAPER_VN  = "Hết tem!!! Đề nghị thay cuộn tem mới";
    
    public static final int     ZB_ERR_OUTRIBBON     = 22;
    public static final String  ZB_MSG_OUTRIBBON     = "Out of ribbon";     
    public static final String  ZB_MSG_OUTRIBBON_VN  = "Hết cuộn mực!!! Đề nghị thay cuộn mực mới";
    
    public static final int     ZB_ERR_UNKNOWN     = 99;
    public static final String  ZB_MSG_UNKNOWN     = "Unknown Error";  
    public static final String  ZB_MSG_UNKNOWN_VN  = "Không xác định! Đề nghị liên hệ IT";
}
