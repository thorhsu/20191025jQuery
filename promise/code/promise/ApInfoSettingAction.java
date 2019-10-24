package com.ebizprise.ctbc.project.action.checkList;

import com.ebizprise.ctbc.project.exception.ParseExcelException;
import com.ebizprise.ctbc.project.mappers.basic.setting.SysEmployeeMapper;
import com.ebizprise.ctbc.project.mappers.checkList.DbTypeMapper;
import com.ebizprise.ctbc.project.model.basic.setting.SysEmployee;
import com.ebizprise.ctbc.project.model.checkList.*;
import com.ebizprise.ctbc.project.model.common.ExtJsDataTablePaging;
import com.ebizprise.ctbc.project.otherdb.method.scheduleApplist.ApplicationListService;
import com.ebizprise.ctbc.project.service.UserService;
import com.ebizprise.ctbc.project.service.checkList.ApInfoService;
import com.ebizprise.ctbc.project.thread.EbizThreadPool;
import com.ebizprise.ctbc.project.thread.ThreadSc02BatchInsert;
import com.ebizprise.ctbc.project.util.Constant;
import com.ebizprise.ctbc.project.util.ExcelUtil;
import com.ebizprise.ctbc.project.util.ExceptionRecordUtil;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DuplicateKeyException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Namespace("/checkList/appListSetting")
@ParentPackage(value="json-default")
@Results({
    @Result(name= Constant.INIT_PAGE, type="dispatcher", location="/WEB-INF/jsp/checkList/appInfoSetting/apInfoSetting_init.jsp"),
    @Result(name=Constant.EDIT_PAGE, type="dispatcher", location="/WEB-INF/jsp/checkList/appInfoSetting/apInfoSetting_edit.jsp"),
    @Result(name= Constant.SUCCESS_JSON, type="json", params={"contentType","applicatoin/json","root","result"}),
    @Result(name= Constant.SUCCESS_TEXT, type="json", params={"contentType","text/html","root","result"}),
    @Result(name= Constant.SUCCESS_FILEOUTPUT, type="stream", params={"contentType","application/octet-stream",
                                                                      "inputName","fileInputStream",
                                                                      "contentDisposition","attachment;filename=\"${outFileName}\"",
                                                                      "bufferSize","1024"})
})
public class ApInfoSettingAction extends ActionSupport {
	
    private static final Logger logger = LoggerFactory.getLogger(ApInfoSettingAction.class);

    @Autowired
    private ApInfoService apInfoSvc;
    @Autowired
    private ApplicationListService applicationListService;
    @Autowired
    private DbTypeMapper dbTypeMap;
    @Autowired
    private UserService userSvc;
    @Autowired
    private SysEmployeeMapper sysEmployeeMapper;

    private ApInfo apInfo;
    private Long id;
    private List<Long> ids;
    private List<DbType> dbTypeList;
    
    private String querySc02Id;
    private String queryApName;
    private String queryApId;
    
    private Long applicationListId;

    private String saveJsonStr;

    // upload
    private File file;
    private String fileFileName;
    // output
    private InputStream fileInputStream;
    private String outFileName;

    private Map<String, Object> result;

    // ----------
 
    public ApInfo getApInfo() {
        return apInfo;
    }
	public void setApInfo(ApInfo apInfo) {
        this.apInfo = apInfo;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public List<Long> getIds() {
        return ids;
    }
    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
    public List<DbType> getDbTypeList() {
        return dbTypeList;
    }
    public void setDbTypeList(List<DbType> dbTypeList) {
        this.dbTypeList = dbTypeList;
    }
    
    public String getQuerySc02Id() {
		return querySc02Id;
	}
	public void setQuerySc02Id(String querySc02Id) {
		this.querySc02Id = querySc02Id;
	}
	public String getQueryApName() {
		return queryApName;
	}
	public void setQueryApName(String queryApName) {
		this.queryApName = queryApName;
	}
	public String getQueryApId() {
		return queryApId;
	}
	public void setQueryApId(String queryApId) {
		this.queryApId = queryApId;
	}
	
	public Long getApplicationListId() {
		return applicationListId;
	}
	public void setApplicationListId(Long applicationListId) {
		this.applicationListId = applicationListId;
	}
    
	public String getSaveJsonStr() {
        return saveJsonStr;
    }
	public void setSaveJsonStr(String saveJsonStr) {
        this.saveJsonStr = saveJsonStr;
    }
	
	public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }
    public String getFileFileName() {
        return fileFileName;
    }
    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }
    public InputStream getFileInputStream() {
        return fileInputStream;
    }
    public void setFileInputStream(InputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }

    public String getOutFileName() {
        return outFileName;
    }
    public void setOutFileName(String outFileName) {
        this.outFileName = outFileName;
    }

    public Map<String, Object> getResult() {
        return result;
    }
    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    // --------

    /**
     *
     * @return
     */
    @Action("init")
    public String init(){
        return Constant.INIT_PAGE;
    }


    /**
     *
     * @return
     */
    @Action("edit")
    public String edit(){

        if(id != null){
            apInfo = apInfoSvc.selectById(id);
        }

        dbTypeList = dbTypeMap.selectAll();

        return Constant.EDIT_PAGE;
    }

    /**
     *
     * @return
     */
    @Action("selectGrid")
    public String selectGrid(){

        HttpServletRequest request = ServletActionContext.getRequest();
        ExtJsDataTablePaging paging = new ExtJsDataTablePaging(request, ApInfo.class);

        List<ApInfo> list = apInfoSvc.selectAllForGrid(paging, querySc02Id, queryApName, queryApId);
        result = paging.toView(list).toMap();

        return Constant.SUCCESS_JSON;
    }
    
    /**
     * @return
     */
    @Action("selectApplicationListBySc02Id")
    public String selectApplicationListBySc02Id(){

        result = new HashMap<>();

        List<ApplicationList> data =
        		applicationListService.selectApplicationListBySc02IdForApInfoSelect(apInfo.getSc02Id());

        result.put("data", data);

        return Constant.SUCCESS_JSON;
    }

    /**
     *
     * @return
     */
    @Action("selectRoleGroupBySc02Id")
    public String selectRoleGroupBySc02Id(){

        result = new HashMap<>();

        List<RoleGroupNotice> data =
                apInfoSvc.selectRoleGroupBySc02Id(apInfo.getSc02Id());

        result.put("data", data);

        return Constant.SUCCESS_JSON;
    }

    /**
     *
     * @return
     */
    @Action("selectGroupFuncBySc02Id")
    public String selectGroupFuncBySc02Id(){

        result = new HashMap<>();

        List<GroupFunctionNotice> data =
                apInfoSvc.selectGroupFuncBySc02Id(apInfo.getSc02Id());

        result.put("data", data);

        return Constant.SUCCESS_JSON;
    }
    
    /**
     * @return
     */
    @Action("selectApEmpBySc02Id")
    public String selectApEmpBySc02Id(){

        result = new HashMap<>();
 
        List<SysEmployee> data =
                apInfoSvc.selectApEmpBySc02Id_pageInsert(apInfo.getSc02Id());

        result.put("data", data);

        return Constant.SUCCESS_JSON;
    }
    
    /**
     * @return
     */
    @Action("selectBuEmpBySc02Id")
    public String selectBuEmpBySc02Id(){

        result = new HashMap<>();
 
        List<SysEmployee> data =
                apInfoSvc.selectBuEmpBySc02Id_pageInsert(apInfo.getSc02Id());

        result.put("data", data);

        return Constant.SUCCESS_JSON;
    }
    
    /**
     * @return
     */
    @Action("getApplicationListById")
    public String getApplicationListById(){

        result = new HashMap<>();

        ApplicationList data =
        		applicationListService.selectByPrimaryKey(applicationListId);

        result.put("data", data);

        return Constant.SUCCESS_JSON;
    }

    /**
     *
     * @return
     */
    @Action("deleteApInfo")
    public String deleteApInfo(){
        result = new HashMap<>();

        if(CollectionUtils.isEmpty(ids)){
            result.put("result", Boolean.FALSE);
            result.put("message", "請勾選資料");
            return Constant.SUCCESS_JSON;
        }
        try {
            List<ApInfo> usedApInfos = apInfoSvc.delete(ids);
            result.put("result", Boolean.TRUE);
            String message;
            if(CollectionUtils.isNotEmpty(usedApInfos)){
                StringBuffer sb = new StringBuffer();
                for(ApInfo a:usedApInfos){
                    sb.append(a.getSc02Id()).append(";");
                }
                message = getText("ap.system.setting.delete.used", new String[]{sb.toString()});
            }else{
                message = getText("global.save.success");
            }
            result.put("message", message);
        } catch (Exception e) {
            logger.error(ExceptionRecordUtil.record(e));
            result.put("result", Boolean.FALSE);
            result.put("message", getText("global.save.fail"));
        }

        return Constant.SUCCESS_JSON;
    }

    /**
     *
     * @return
     */
    @Action("save")
    public String save(){

        result = new HashMap<>();
        try {
            // validate
            if(apInfo == null || apInfo.isRequiredEmpty()){
                result.put("result", Boolean.FALSE);
                result.put("message", "請輸入必要資訊");
                return Constant.SUCCESS_JSON;
            }
            apInfoSvc.save(apInfo, this);
            result.put("result", Boolean.TRUE);
            result.put("message", getText("global.save.success"));
            result.put("apInfo", apInfo);
        } catch (DuplicateKeyException e){
            result.put("result", Boolean.FALSE);
            result.put("message", e.getMessage());
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            result.put("result", Boolean.FALSE);
            result.put("message", getText("global.save.fail"));
        }
        return Constant.SUCCESS_JSON;
    }


    /**
     *
     * @return
     */
    @Action("export")
    public String export(){

        try {
            InputStreamResource isr = apInfoSvc.getExport(querySc02Id, queryApName, queryApId, this);
            outFileName = URLEncoder.encode(isr.getDescription(), "UTF-8");
            fileInputStream = isr.getInputStream();
        } catch (Exception e) {
            logger.error(ExceptionRecordUtil.record(e));
        }

        return Constant.SUCCESS_FILEOUTPUT;
    }

    /**
     *
     * @return
     */
    @Action("upload")
    public String upload(){
        result = new HashMap<>();

        // 檢查當前使用的email
        String currentCN = userSvc.getCurrentUser().getEmpId();
        SysEmployee emp = sysEmployeeMapper.getEmployeeByCn(currentCN);
        if(emp == null){
            result = new HashMap<>();
            result.put("message", "系統找不到您的人員資訊");
            return Constant.SUCCESS_TEXT;
        }
        if(EmailValidator.getInstance().isValid(emp.getMail()) == false){
            result = new HashMap<>();
            result.put("message", "您的mail為錯誤格式，請連絡相關單位");
            return Constant.SUCCESS_TEXT;
        }
        
        try {
        	batchInsert();
        	result.put("message", "【系統資料匯入作業中，待匯入作業完成後，匯入結果會email至您的信箱】"); 
        }catch(Exception e) {
        	result.put("message", "【匯入失敗】");
            e.printStackTrace();
        }
        /*舊的寫法*/
//        try {
//        	batchInsert();
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//        
//        result.put("message", "【系統資料匯入作業中，待匯入作業完成後，匯入結果會email至您的信箱】");
        return Constant.SUCCESS_TEXT;
    }
    
    private void batchInsert() throws IOException, ParseExcelException {
    	Workbook excel = ExcelUtil.getWorkBook(new FileInputStream(file), fileFileName);
    	ThreadSc02BatchInsert threadSc02BatchInsert = new ThreadSc02BatchInsert();
    	threadSc02BatchInsert.setExcel(excel);
    	threadSc02BatchInsert.setCurrentCN(userSvc.getCurrentUser().getEmpId());
    	threadSc02BatchInsert.setMessage(this);
    	EbizThreadPool.INSTANCE.getThreadPool().execute(threadSc02BatchInsert);
    }
}
