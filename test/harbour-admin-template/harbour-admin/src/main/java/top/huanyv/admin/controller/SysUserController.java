package top.huanyv.admin.controller;

import lombok.extern.slf4j.Slf4j;
import top.huanyv.admin.domain.entity.SysUser;
import top.huanyv.admin.domain.vo.LayUIPageVo;
import top.huanyv.admin.domain.vo.user.UserPageVo;
import top.huanyv.admin.service.SysUserService;
import top.huanyv.admin.utils.*;
import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.jdbc.core.pagination.Page;
import top.huanyv.bean.utils.BeanUtil;
import top.huanyv.bean.utils.StringUtil;
import top.huanyv.webmvc.annotation.*;
import top.huanyv.webmvc.annotation.argument.Body;
import top.huanyv.webmvc.annotation.argument.Form;
import top.huanyv.webmvc.annotation.argument.Param;
import top.huanyv.webmvc.annotation.argument.Path;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.action.ActionResult;
import top.huanyv.webmvc.security.LoginService;
import top.huanyv.webmvc.security.SubjectHolder;
import top.huanyv.webmvc.security.User;
import top.huanyv.webmvc.security.digest.PasswordDigester;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author huanyv
 * @date 2023/2/21 19:48
 */
@Bean
@Route("/admin/user")
@Slf4j
public class SysUserController {

    @Inject
    private SysUserService sysUserService;

    @Inject
    private PasswordDigester passwordDigester;

    @Inject
    private LoginService loginService;

    @Get
    public ActionResult page(@Form PageDto pageDto, @Form SysUser user) {
        Page<SysUser> page = sysUserService.getPageUser(pageDto.getPage(), pageDto.getLimit(), user);
        List<UserPageVo> userListVos = BeanUtil.copyBeanList(page.getData(), UserPageVo.class);
        return new LayUIPageVo(page.getTotal(), userListVos);
    }

    @Post
    public ActionResult add(@Body SysUser sysUser, HttpRequest req) {
        boolean bool = sysUserService.addUser(sysUser);
        return RestResult.condition(bool, "添加成功", "添加失败");
    }

    @Put
    public ActionResult update(@Body SysUser sysUser) {
        boolean b = sysUserService.updateUser(sysUser);
        return RestResult.condition(b, "修改成功", "修改失败");
    }

    @Delete("/{ids}")
    public ActionResult remove(@Path("ids") String ids) {
        boolean b = sysUserService.deleteUserById(ids);
        return RestResult.condition(b, "删除成功", "删除失败");
    }

    /**
     * 更新用户对应的角色
     *
     * @param userId  用户id
     * @param roleIds 角色id
     * @return {@link ActionResult}
     */
    @Post("/updateRoles")
    public ActionResult updateRoles(@Param("userId") Long userId, @Param("roleIds") String roleIds) {
        Long[] ids = Util.parseIds(roleIds);
        boolean bool = sysUserService.updateRoles(userId, ids);
        return RestResult.condition(bool, "更新成功", "更新失败");
    }

    @Post("/updatePassword")
    public ActionResult updatePassword(@Param("oldPassword") String oldPwd
            , @Param("newPassword1") String newPwd1, @Param("newPassword2") String newPwd2) {
        if (!StringUtil.hasText(oldPwd) || !StringUtil.hasText(newPwd1) || !StringUtil.hasText(newPwd2)) {
            return RestResult.error("输入不可为空！");
        }
        String loginUsername = LoginUtil.getLoginUsername();
        User loginUser = loginService.loadUserByUsername(loginUsername);
        if (!passwordDigester.match(oldPwd, loginUser.getPassword())) {
            return RestResult.error("旧密码不正确！");
        }
        if (!Objects.equals(newPwd1, newPwd2)) {
            return RestResult.error("新密码两次不相同！");
        }
        // 修改密码
        SysUser sysUser = new SysUser();
        sysUser.setUserId(Long.parseLong(loginUser.getId()));
        sysUser.setPassword(passwordDigester.digest(newPwd1));
        boolean bool = sysUserService.updateUser(sysUser);
        return RestResult.condition(bool, "修改成功！", "修改失败！");
    }

    /**
     * 修改当前用户个人资料
     *
     * @param sysUser 系统用户
     * @return {@link ActionResult}
     */
    @Post("/updateInfo")
    public ActionResult updateInfo(@Body SysUser sysUser) {
        User subject = SubjectHolder.getStrategy().getSubject();
        long id = Long.parseLong(subject.getId());
        sysUser.setUserId(id);
        boolean b = sysUserService.updateUser(sysUser);
        return RestResult.condition(b, "保存成功", "保存失败");
    }
}
