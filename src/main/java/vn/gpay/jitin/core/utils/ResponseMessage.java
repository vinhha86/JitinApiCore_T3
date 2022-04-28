package vn.gpay.jitin.core.utils;

import java.util.HashMap;

public class ResponseMessage {

	public static int KEY_RC_SUCCESS                = 200;
    public static int KEY_RC_BAD_REQUEST            = 400;
    public static int KEY_RC_AUTHEN_ERROR           = 401;
    public static int KEY_RC_UN_AUTHORIZED          = 403;
    public static int KEY_RC_RS_NOT_FOUND           = 404;
    public static int KEY_RC_CREATE_INVCHECK_FAIL   = 300;
    public static int KEY_RC_SERVER_ERROR           = 500;
    public static int KEY_RC_EXCEPTION              = 900;
    public static int KEY_RC_IMEI_NOT_EXSIST        = 901;
    public static int KEY_RC_IMEI_NOT_PERMISSION    = 902;
    public static int KEY_RC_APPROVE_FAIL           = 903;
    public static int KEY_RC_ACTIVE_FAIL            = 904;
    
    //Cac loi lien quan Nhap kho
    public static int KEY_STOCKIN_NOSKU          	= 1000;
    public static String MES_STOCKIN_NOSKU    		= "Phiếu nhập kho không có dòng hàng nào";
    public static int KEY_STOCKIN_WRONGEPC          = 1001;
    public static String MES_STOCKIN_WRONGEPC    	= "Chíp đã tồn tại ở một kho khác, không thể nhập kho";
    public static String MES_STOCKIN_SKU_NOTEXIST  	= "SKU không tồn tại";
    public static String MES_STOCKIN_SKU_EMPTY  	= "Chíp không có SKU";
    public static int KEY_STOCKIN_WRONGSTATUS      	= 1002;
    public static String MES_STOCKIN_WRONGSTATUS   	= "Trạng thái của phiếu không hợp lệ";
    
    //Cac loi lien quan Xuat kho
    public static int KEY_STOCKOUT_NOSKU          	= 2000;
    public static String MES_STOCKOUT_NOSKU    		= "Phiếu xuất kho không có dòng hàng nào";
    public static int KEY_STOCKOUT_WRONGEPC         = 2001;
    public static String MES_STOCKOUT_WRONGEPC    	= "Chíp không tồn tại trong kho, không thể xuất kho";
    public static int KEY_STOCKOUT_WRONGSTATUS      = 2002;
    public static String MES_STOCKOUT_WRONGSTATUS   = "Trạng thái của phiếu không hợp lệ";
    public static int KEY_STOCKOUT_NOT_FOUND      	= 2003;
    public static String MES_STOCKOUT_NOT_FOUND   	= "Phiếu không tồn tại";
    public static int KEY_STOCKOUT_EPC_NOTCHECK     = 2004;
    public static String MES_STOCKOUT_EPC_NOTCHECK  = "Có sản phẩm/nguyên liệu chưa kiểm tra trước khi xuất kho";
    
    //Các lỗi liên quan SKU có đầu 7
    public static int KEY_RC_SKU_INVALID            = 700;
    public static String MES_RC_SKU_INVALID         = "SKU Invalid";
    
    //Các lỗi liên quan EPC có đầu 8
    public static int KEY_RC_EPC_INVALID            = 800;
    public static String MES_RC_EPC_INVALID         = "EPC Invalid";
    
    public static String MES_RC_SUCCESS   = "OK - Everything Worked";
    public static String MES_RC_BAD_REQUEST   = "Bad Request - Invalid Parameter or Data Integrity Issue";
    public static String MES_RC_AUTHEN_ERROR = "Authentication Error";
    public static String MES_RC_UN_AUTHORIZED     = "Unauthorized Request";
    public static String MES_RC_RS_NOT_FOUND     = "Resource Not Found";
    public static String MES_RC_SERVER_ERROR     = "Platform Internal Server Error";
    public static String MES_RC_EXCEPTION     = "Exception request API";
    public static String MES_RC_IMEI_NOT_EXSIST     = "Imei not exisit";
    public static String MES_RC_IMEI_NOT_PERMISSION     = "Imei not permission";
    public static String MES_RC_CREATE_INVCHECK_FAIL    = "Create invcheck fail";
    
    
    public static String MES_RC_APPROVE_FAIL      = "Approve Fail !";
    public static String MES_RC_ACTIVE_FAIL     = "ACTIVE Fail !";
    
    //saving
    public static int KEY_RC_PHONE_OR_ID_EXISTS     = 903;    
    public static String MES_KEY_RC_PHONE_OR_ID_EXISTS   = "Mobile or Identify card is Exists";
   
    
    public static String getMessage(int code){      
        HashMap<Integer,String> hMes = new HashMap<>();
        hMes.put(KEY_RC_SUCCESS, MES_RC_SUCCESS);
        hMes.put(KEY_RC_BAD_REQUEST, MES_RC_BAD_REQUEST);
        hMes.put(KEY_RC_AUTHEN_ERROR, MES_RC_AUTHEN_ERROR);
        hMes.put(KEY_RC_UN_AUTHORIZED, MES_RC_UN_AUTHORIZED);
        hMes.put(KEY_RC_RS_NOT_FOUND, MES_RC_RS_NOT_FOUND);
        hMes.put(KEY_RC_SERVER_ERROR, MES_RC_SERVER_ERROR);
        hMes.put(KEY_RC_EXCEPTION, MES_RC_EXCEPTION);
        hMes.put(KEY_RC_IMEI_NOT_EXSIST, MES_RC_IMEI_NOT_EXSIST);
        hMes.put(KEY_RC_IMEI_NOT_PERMISSION, MES_RC_IMEI_NOT_PERMISSION);
        hMes.put(KEY_RC_PHONE_OR_ID_EXISTS, MES_KEY_RC_PHONE_OR_ID_EXISTS);
        hMes.put(KEY_RC_APPROVE_FAIL, MES_RC_APPROVE_FAIL);
        hMes.put(KEY_RC_ACTIVE_FAIL, MES_RC_ACTIVE_FAIL);
        hMes.put(KEY_RC_CREATE_INVCHECK_FAIL, MES_RC_CREATE_INVCHECK_FAIL);
        hMes.put(KEY_RC_EPC_INVALID, MES_RC_EPC_INVALID);
        
        hMes.put(KEY_STOCKIN_NOSKU, MES_STOCKIN_NOSKU);
        hMes.put(KEY_STOCKIN_WRONGEPC, MES_STOCKIN_WRONGEPC);
        hMes.put(KEY_STOCKIN_WRONGSTATUS, MES_STOCKIN_WRONGSTATUS);
        
        hMes.put(KEY_STOCKOUT_NOSKU, MES_STOCKOUT_NOSKU);
        hMes.put(KEY_STOCKOUT_WRONGEPC, MES_STOCKOUT_WRONGEPC);
        hMes.put(KEY_STOCKOUT_WRONGSTATUS, MES_STOCKOUT_WRONGSTATUS);
        hMes.put(KEY_STOCKOUT_NOT_FOUND, MES_STOCKOUT_NOT_FOUND);
        hMes.put(KEY_STOCKOUT_EPC_NOTCHECK, MES_STOCKOUT_EPC_NOTCHECK);
        return hMes.get(code);
    }
}
