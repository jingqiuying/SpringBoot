package cn.wolfcode.web.modules.sys.conctroller;

import cn.wolfcode.web.modules.home.service.IHomeService;
import cn.wolfcode.web.modules.sys.service.MenuService;
import cn.wolfcode.web.modules.sys.form.LoginForm;
import cn.wolfcode.web.modules.sys.entity.SysMenu;
import cn.wolfcode.web.modules.sys.entity.SysUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Eastern unbeaten
 * @version Version 1.0
 * @date 2019/4/1 16:22
 * @email chenshiyun2011@163.com
 */
@Controller
@Log4j2
public class IndexController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private IHomeService homeService;


    @GetMapping(value = {"", "/", "index"})
    public ModelAndView index(ModelAndView mv, HttpServletRequest request,String menuType) {
        if(!"horizontal".equals(menuType)){
            menuType = "vertical";
        }
        SysUser loginUser = (SysUser) request.getSession().getAttribute(LoginForm.LOGIN_USER_KEY);
        List<SysMenu> sysMenus = menuService.queryUserLeftMenu(loginUser);

        mv.setViewName("index");
        mv.addObject("sysMenus", sysMenus);
        // 菜单显示方式 横向：horizontal 竖向：vertical
        mv.addObject("menuType", menuType);
        // 是否显示水印
        mv.addObject("watermark", true);
        return mv;
    }


    @GetMapping("/main.html")
    public ModelAndView mainPage(ModelAndView mv) {
        mv.addObject("opertionLoginCount",homeService.getOperationLogCount());
        mv.addObject("custCount",homeService.getCustInfoCount());
        mv.addObject("loginCount",homeService.getLoginCount());
        mv.addObject("userCount",homeService.getUserCount());
        log.debug(mv.getModelMap());
        mv.setViewName("main");
        return mv;
    }
}
