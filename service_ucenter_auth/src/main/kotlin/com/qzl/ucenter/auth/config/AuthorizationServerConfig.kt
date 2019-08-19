package com.qzl.ucenter.auth.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.bootstrap.encrypt.KeyProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory

import javax.annotation.Resource
import javax.sql.DataSource
import java.security.KeyPair


@Configuration
@EnableAuthorizationServer
internal class AuthorizationServerConfig : AuthorizationServerConfigurerAdapter() {
    @Autowired
    private val dataSource: DataSource? = null
    //jwt令牌转换器
    @Autowired
    private val jwtAccessTokenConverter: JwtAccessTokenConverter? = null
    @Autowired
    var userDetailsService: UserDetailsService? = null
    @Autowired
    var authenticationManager: AuthenticationManager? = null
    @Autowired
    var tokenStore: TokenStore? = null
    @Autowired
    private val customUserAuthenticationConverter: CustomUserAuthenticationConverter? = null

    @Resource(name = "keyProp")
    private val keyProperties: KeyProperties? = null

    //读取密钥的配置
    @Bean("keyProp")
    fun keyProperties(): KeyProperties {
        return KeyProperties()
    }


    //客户端配置
    @Bean
    fun clientDetails(): ClientDetailsService {
        return JdbcClientDetailsService(dataSource!!)
    }

    @Throws(Exception::class)
    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.jdbc(this.dataSource).clients(this.clientDetails())
        /*clients.inMemory()
                .withClient("XcWebApp")//客户端id
                .secret("$2a$10$9bEpZ/hWRQxyr5hn5wHUj.jxFpIrnOmBcWlE/g/0Zp3uNxt9QTh/S")//密码，要保密
                .accessTokenValiditySeconds(60)//访问令牌有效期
                .refreshTokenValiditySeconds(60)//刷新令牌有效期
                //授权客户端请求认证服务的类型authorization_code：根据授权码生成令牌，
                // client_credentials:客户端认证，refresh_token：刷新令牌，password：密码方式认证
                .authorizedGrantTypes("authorization_code", "client_credentials", "refresh_token", "password")
                .scopes("app");//客户端范围，名称自定义，必填*/
    }

    //token的存储方法
    //    @Bean
    //    public InMemoryTokenStore tokenStore() {
    //        //将令牌存储到内存
    //        return new InMemoryTokenStore();
    //    }
    //    @Bean
    //    public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory){
    //        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
    //        return redisTokenStore;
    //    }
    @Bean
    @Autowired
    fun tokenStore(jwtAccessTokenConverter: JwtAccessTokenConverter): TokenStore {
        return JwtTokenStore(jwtAccessTokenConverter)
    }

    @Bean
    fun jwtAccessTokenConverter(customUserAuthenticationConverter: CustomUserAuthenticationConverter): JwtAccessTokenConverter {
        val converter = JwtAccessTokenConverter()
        val keyPair = KeyStoreKeyFactory(keyProperties!!.keyStore.location, keyProperties.keyStore.secret.toCharArray())
                .getKeyPair(keyProperties.keyStore.alias, keyProperties.keyStore.password.toCharArray())
        converter.setKeyPair(keyPair)
        //配置自定义的CustomUserAuthenticationConverter
        val accessTokenConverter = converter.accessTokenConverter as DefaultAccessTokenConverter
        accessTokenConverter.setUserTokenConverter(customUserAuthenticationConverter)
        return converter
    }

    //授权服务器端点配置
    @Throws(Exception::class)
    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        /*Collection<TokenEnhancer> tokenEnhancers = applicationContext.getBeansOfType(TokenEnhancer.class).values();
        TokenEnhancerChain tokenEnhancerChain=new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(new ArrayList<>(tokenEnhancers));

        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setReuseRefreshToken(true);
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setTokenStore(tokenStore);
        defaultTokenServices.setAccessTokenValiditySeconds(1111111);
        defaultTokenServices.setRefreshTokenValiditySeconds(1111111);
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);

        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                        //.tokenStore(tokenStore);
                .tokenServices(defaultTokenServices);*/
        endpoints.accessTokenConverter(jwtAccessTokenConverter)
                .authenticationManager(authenticationManager)//认证管理器
                .tokenStore(tokenStore)//令牌存储
                .userDetailsService(userDetailsService)//用户信息service
    }

    //授权服务器的安全配置
    @Throws(Exception::class)
    override fun configure(oauthServer: AuthorizationServerSecurityConfigurer) {
        //        oauthServer.checkTokenAccess("isAuthenticated()");//校验token需要认证通过，可采用http basic认证
        oauthServer.allowFormAuthenticationForClients()
                .passwordEncoder(BCryptPasswordEncoder())
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
    }


}

