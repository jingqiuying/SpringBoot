package cn.wolfcode.web.modules.custinfo.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.wolfcode.web.commons.entity.ExcelExportEntityWrapper;
import cn.wolfcode.web.commons.entity.LayuiPage;
import cn.wolfcode.web.commons.utils.CityUtils;
import cn.wolfcode.web.commons.utils.LayuiTools;
import cn.wolfcode.web.commons.utils.PoiExportHelper;
import cn.wolfcode.web.commons.utils.SystemCheckUtils;
import cn.wolfcode.web.modules.BaseController;
import cn.wolfcode.web.modules.linkmane.entity.TbCustLinkman;
import cn.wolfcode.web.modules.linkmane.service.ITbCustLinkmanService;
import cn.wolfcode.web.modules.log.LogModules;
import cn.wolfcode.web.modules.sys.entity.SysDict;
import cn.wolfcode.web.modules.sys.entity.SysUser;
import cn.wolfcode.web.modules.sys.form.LoginForm;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.wolfcode.web.modules.custinfo.entity.TbCustomer;
import cn.wolfcode.web.modules.custinfo.service.ITbCustomerService;

import link.ahsj.core.annotations.AddGroup;
import link.ahsj.core.annotations.SameUrlData;
import link.ahsj.core.annotations.SysLog;
import link.ahsj.core.annotations.UpdateGroup;
import link.ahsj.core.entitys.ApiModel;
import link.ahsj.core.exception.AppServerException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ????????????????????????
 * @since 2022-06-20
 */
@Log4j2
@Controller
@RequestMapping("custinfo")
public class TbCustomerController extends BaseController {

    @Autowired
    private ITbCustomerService entityService;
    @Autowired
    private ITbCustLinkmanService custLinkmanService;

    private static final String LogModule = "TbCustomer";

    @GetMapping("/list.html")
    public ModelAndView list(ModelAndView mv) {
        //???????????????
        mv.addObject("citys", CityUtils.citys);
        mv.setViewName("cust/custinfo/list");
        return mv;
//        return "cust/custinfo/list";
    }

    @RequestMapping("/add.html")
    @PreAuthorize("hasAuthority('cust:custinfo:add')")
    public ModelAndView toAdd(ModelAndView mv) {
        //???????????????
        mv.addObject("citys", CityUtils.citys);
        mv.setViewName("cust/custinfo/add");
        return mv;
    }

    @GetMapping("/{id}.html")
    @PreAuthorize("hasAuthority('cust:custinfo:update')")
    public ModelAndView toUpdate(@PathVariable("id") String id, ModelAndView mv) {
        //???????????????
        mv.addObject("citys", CityUtils.citys);
        mv.setViewName("cust/custinfo/update");
        mv.addObject("obj", entityService.getById(id));
        mv.addObject("id", id);
        return mv;
    }

    @RequestMapping("list")
    @PreAuthorize("hasAuthority('cust:custinfo:list')")
    public ResponseEntity page(LayuiPage layuiPage,String parameterName,String cityId,String openStatus) {
        SystemCheckUtils.getInstance().checkMaxPage(layuiPage);
        IPage page = new Page<>(layuiPage.getPage(), layuiPage.getLimit());
        IPage page1 = entityService.lambdaQuery()
                .eq(StringUtils.isNotBlank(openStatus),TbCustomer::getOpenStatus,openStatus)
                .eq(StringUtils.isNotBlank(cityId),TbCustomer::getProvince,cityId)

                .like(StringUtils.isNotBlank(parameterName),TbCustomer::getCustomerName,parameterName)
                .or()
                .like(StringUtils.isNotBlank(parameterName),TbCustomer::getLegalLeader,parameterName)
                .page(page);
        List<TbCustomer> records = page.getRecords();
        for (TbCustomer record : records) {
            String cityValue = CityUtils.getCityValue(record.getProvince());
            record.setProvinceName(cityValue);
        }
        return ResponseEntity.ok(LayuiTools.toLayuiTableModel(page1));
    }

    @SameUrlData
    @PostMapping("save")
    @SysLog(value = LogModules.SAVE, module =LogModule)
    @PreAuthorize("hasAuthority('cust:custinfo:add')")
    public ResponseEntity<ApiModel> save(@Validated({AddGroup.class}) @RequestBody TbCustomer entity, HttpServletRequest request) {
        entity.setInputTime(LocalDateTime.now(ZoneOffset.of("+16")));
        SysUser loginUser = (SysUser) request.getSession().getAttribute(LoginForm.LOGIN_USER_KEY);
        entity.setInputUserId(loginUser.getUserId());
        //????????????????????????
        //???????????????????????????????????????
        Integer count = entityService.lambdaQuery().eq(TbCustomer::getCustomerName, entity.getCustomerName()).count();
        if (count>0){//????????????
            throw new AppServerException("????????????");
        }
        entityService.save(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SameUrlData
    @SysLog(value = LogModules.UPDATE, module = LogModule)
    @PutMapping("update")
    @PreAuthorize("hasAuthority('cust:custinfo:update')")
    public ResponseEntity<ApiModel> update(@Validated({UpdateGroup.class}) @RequestBody TbCustomer entity,HttpServletRequest request) {
        SysUser loginUser = (SysUser) request.getSession().getAttribute(LoginForm.LOGIN_USER_KEY);
        entity.setInputUserId(loginUser.getUserId());
        entity.setUpdateTime(LocalDateTime.now(ZoneOffset.of("+16")));

        //????????????????????????

        //???????????????????????????????????????
        Integer count = entityService.lambdaQuery().eq(TbCustomer::getCustomerName, entity.getCustomerName())
                .ne(TbCustomer::getId,entity.getId())
                .count();
        if (count>0){//????????????
            throw new AppServerException("????????????");
        }
        entityService.updateById(entity);
        return ResponseEntity.ok(ApiModel.ok());
    }

    @SysLog(value = LogModules.DELETE, module = LogModule)
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('cust:custinfo:delete')")
    public ResponseEntity<ApiModel> delete(@PathVariable("id") String id) {

        entityService.removeById(id);
        //????????????????????????????????????
        custLinkmanService.lambdaUpdate().eq(TbCustLinkman::getCustId,id).remove();
        return ResponseEntity.ok(ApiModel.ok());
    }

//    @SysLog(value = LogModules.EXPORT, module = LogModules.DICT)
//    @PostMapping("export")
//    @PreAuthorize("hasAuthority('dataDict:export')")
//    public void export(HttpServletResponse response,String parameterName,String custId) throws IOException {
//       //???????????????????????????
//        entityService.lambdaQuery()
//                .eq(StringUtils.isNotBlank(custId))
//
//        ExcelExportEntityWrapper wrapper = new ExcelExportEntityWrapper();
//        wrapper.entity(SysDict.DICE_NAME, "diceName", 20)
//                .entity(SysDict.DICE_CODE, "diceCode", 20)
//                .entity(SysDict.DICE_VALUE, "?????????", 20)
//                .entity("??????", "disable", 20)
//                .entity(SysDict.DESCRIPTION, "description", 20);
//
//        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), wrapper.getResult(), dictList);
//        PoiExportHelper.exportExcel(response, "??????????????????", workbook);
//    }

}
