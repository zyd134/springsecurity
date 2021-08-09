package com.zhang.config;

import com.zhang.handler.MyAccessDeniedHandler;
import com.zhang.handler.MyAuthenticationFailureHandler;
import com.zhang.handler.MyAuthenticationSuccessHandler;
import com.zhang.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * springSecurity配置类
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyAccessDeniedHandler accessDeniedHandler;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private PersistentTokenRepository persistentTokenRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //表单提交
        http.formLogin()
                //自定义入参
                .usernameParameter("username")
                .passwordParameter("password")
                //当发现/login时认为是登录，必须和表单提交的地址一样，去执行UserDetailsServiceImpl
                .loginProcessingUrl("/login")
                //自定义登录页面
                .loginPage("/showLogin")
                //登录成功后跳转页面，post请求
                .successForwardUrl("/toMain")
                //自定义登录成功处理器
//                .successHandler(new MyAuthenticationSuccessHandler("/main.html"))
                //登录失败后跳转的页面。post请求
                .failureForwardUrl("/toError");
//                .failureHandler(new MyAuthenticationFailureHandler("/error.html"))；


        //授权认证
        http.authorizeRequests()
                //login.html不需要被认证
                .antMatchers("/showLogin").permitAll()
//                .antMatchers("/login.html").access("permitAll")
                //error.html不需要被认证
                .antMatchers("/error.html").permitAll()
                //放行静态资源
//                .antMatchers("/css/**","/js/**","/images/**").permitAll()
                //放行后缀.png
//                .antMatchers("/**/*.png").permitAll()
//                .regexMatchers(".+[.]png").permitAll()
                //指定请求方法
//                .regexMatchers(HttpMethod.GET,"/zhang/demo").permitAll()
                //mvc匹配
//                .mvcMatchers("/demo").servletPath("/zhang").permitAll()
                //权限控制
                //基于权限
//                .antMatchers("/main1.html").hasAnyAuthority("admin")
//                .antMatchers("main1.html").hasAnyAuthority("admin","admiN")
                //基于角色
//                .antMatchers("/main1.html").hasRole("abc")
//                .antMatchers("/main1.html").access("hasRole('abc')")
//                .antMatchers("main1.html").hasAnyRole("abC","abc")
                //基于IP地址
//                .antMatchers("/main1.html").hasIpAddress("127.0.0.1")
                //所有请求都必须被认证，必须登录之后被访问
                .anyRequest().authenticated();
                //自定义access方法
//                .anyRequest().access("@myServiceImpl.hasPermission(request,authentication)");

        //异常处理
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        //记住我
        http.rememberMe()
                //设置数据源
                .tokenRepository(persistentTokenRepository)
                //超时时间
                .tokenValiditySeconds(60)
                //自定义登录逻辑
                .userDetailsService(userDetailsService);

        //退出
        http.logout()
//                .logoutUrl("/user/logout")
                //退出成功跳转的页面
                .logoutSuccessUrl("/login.html");

        //关闭crf防护
//        http.csrf().disable();
    }

    @Bean
    public PasswordEncoder getPw(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        //设置数据源
        jdbcTokenRepository.setDataSource(dataSource);
        //自动建表，第一次启动时开启，第二次启动时注释掉
//        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

}
