import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;

public class HelloShiro {

    public void shiroHello() {

        //  读取ini配置文件，创建工厂类
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro.ini");

        //  构建 SecurityManager 安全管理器
        SecurityManager securityManager = factory.getInstance();

        //  启用 SecurityManager
        SecurityUtils.setSecurityManager(securityManager);

        //  获取 Subject 主体
        Subject subject = SecurityUtils.getSubject();

        //  构建用户名密码
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("jay", "123");

        //  登录
        subject.login(usernamePasswordToken);

        //  判断是否登录成功
        System.out.println("登录状态：" + subject.isAuthenticated());
    }
}
