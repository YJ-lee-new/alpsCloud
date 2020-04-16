package com.alps.common.oauth2.security;

import com.alps.common.oauth2.config.AlpsCommonProperties;
import com.alps.common.oauth2.utils.BeanConvertUtils;
import com.alps.common.oauth2.utils.ReflectionUtils;
import com.alps.common.oauth2.utils.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * 认证信息帮助类
 *
 * @author YJ .lee
 */
@Slf4j
public class AlpsHelper {

    /**
     * 获取认证用户信息
     *
     * @return
     */
    public static AlpsUserDetails getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication instanceof OAuth2Authentication) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
            OAuth2Request clientToken = oAuth2Authentication.getOAuth2Request();
            if (!oAuth2Authentication.isClientOnly()) {
                if (authentication.getPrincipal() instanceof AlpsUserDetails) {
                    return (AlpsUserDetails) authentication.getPrincipal();
                }
                if (authentication.getPrincipal() instanceof Map) {
                    return BeanConvertUtils.mapToObject((Map) authentication.getPrincipal(), AlpsUserDetails.class);
                }
            } else {
                AlpsUserDetails alpsUser = new AlpsUserDetails();
                alpsUser.setClientId(clientToken.getClientId());
                alpsUser.setAuthorities(clientToken.getAuthorities());
                return alpsUser;
            }
        }
        return null;
    }


    /**
     * 更新AlpsUser
     *
     * @param alpsUser
     */
    public static void updateAlpsUser(TokenStore tokenStore, AlpsUserDetails alpsUser) {
        if (alpsUser == null) {
            return;
        }
        Assert.notNull(alpsUser.getClientId(), "客户端ID不能为空");
        Assert.notNull(alpsUser.getUsername(), "用户名不能为空");
        // 动态更新客户端生成的token
        Collection<OAuth2AccessToken> accessTokens = tokenStore.findTokensByClientIdAndUserName(alpsUser.getClientId(), alpsUser.getUsername());
        if (accessTokens != null && !accessTokens.isEmpty()) {
            for (OAuth2AccessToken accessToken : accessTokens) {
                // 由于没有set方法,使用反射机制强制赋值
                OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(accessToken);
                if (oAuth2Authentication != null) {
                    Authentication authentication = oAuth2Authentication.getUserAuthentication();
                    ReflectionUtils.setFieldValue(authentication, "principal", alpsUser);
                    // 重新保存
                    tokenStore.storeAccessToken(accessToken, oAuth2Authentication);
                }
            }
        }
    }


    /***
     * 更新客户端权限
     * @param tokenStore
     * @param clientId
     * @param authorities
     */
    public static void updateAlpsClientAuthorities(TokenStore tokenStore, String clientId, Collection<? extends GrantedAuthority> authorities) {
        if (authorities == null) {
            return;
        }
        // 动态更新客户端生成的token
        Collection<OAuth2AccessToken> accessTokens = tokenStore.findTokensByClientId(clientId);
        if (accessTokens != null && !accessTokens.isEmpty()) {
            Iterator<OAuth2AccessToken> iterator = accessTokens.iterator();
            while (iterator.hasNext()) {
                OAuth2AccessToken token = iterator.next();
                OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(token);
                if (oAuth2Authentication != null && oAuth2Authentication.isClientOnly()) {
                    // 只更新客户端权限
                    // 由于没有set方法,使用反射机制强制赋值
                    ReflectionUtils.setFieldValue(oAuth2Authentication, "authorities", authorities);
                    // 重新保存
                    tokenStore.storeAccessToken(token, oAuth2Authentication);
                }
            }
        }
    }


    /**
     * 获取认证用户Id
     *
     * @return
     */
    public static Long getUserId() {
        return getUser().getUserId();
    }

    /**
     * 是否拥有权限
     *
     * @param authority
     * @return
     */
    public static Boolean hasAuthority(String authority) {
        AlpsUserDetails auth = getUser();
        if (auth == null) {
            return false;
        }
        if (AuthorityUtils.authorityListToSet(auth.getAuthorities()).contains(authority)) {
            return true;
        }
        return false;
    }

    /**
     * 构建token转换器
     *
     * @return
     */
    public static DefaultAccessTokenConverter buildAccessTokenConverter() {
        AlpsUserConverter userAuthenticationConverter = new AlpsUserConverter();
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(userAuthenticationConverter);
        return accessTokenConverter;
    }

    /**
     * 构建jwtToken转换器
     *
     * @param properties
     * @return
     */
    public static JwtAccessTokenConverter buildJwtTokenEnhancer(AlpsCommonProperties properties) throws Exception {
        JwtAccessTokenConverter converter = new AlpsJwtAccessTokenEnhancer();
        converter.setSigningKey(properties.getJwtSigningKey());
        converter.afterPropertiesSet();
        return converter;
    }

    /**
     * 构建自定义远程Token服务类
     *
     * @param properties
     * @return
     */
    public static RemoteTokenServices buildRemoteTokenServices(AlpsCommonProperties properties) {
        // 使用自定义系统用户凭证转换器
        DefaultAccessTokenConverter accessTokenConverter = buildAccessTokenConverter();
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setCheckTokenEndpointUrl(properties.getTokenInfoUri());
        tokenServices.setClientId(properties.getClientId());
        tokenServices.setClientSecret(properties.getClientSecret());
        tokenServices.setAccessTokenConverter(accessTokenConverter);
        log.info("buildRemoteTokenServices[{}]", tokenServices);
        return tokenServices;
    }

    /**
     * 构建资源服务器JwtToken服务类
     *
     * @param properties
     * @return
     */
    public static ResourceServerTokenServices buildJwtTokenServices(AlpsCommonProperties properties) throws Exception {
        // 使用自定义系统用户凭证转换器
        DefaultAccessTokenConverter accessTokenConverter = buildAccessTokenConverter();
        AlpsJwtTokenService tokenServices = new AlpsJwtTokenService();
        // 这里的签名key 保持和认证中心一致
        JwtAccessTokenConverter converter = buildJwtTokenEnhancer(properties);
        JwtTokenStore jwtTokenStore = new JwtTokenStore(converter);
        tokenServices.setTokenStore(jwtTokenStore);
        tokenServices.setJwtAccessTokenConverter(converter);
        tokenServices.setDefaultAccessTokenConverter(accessTokenConverter);
        log.info("buildJwtTokenServices[{}]", tokenServices);
        return tokenServices;
    }

    /**
     **构建资源服务器RedisToken服务类
     *
     * @return
     */
    public static ResourceServerTokenServices buildRedisTokenServices(RedisConnectionFactory redisConnectionFactory) throws Exception {
        AlpsRedisTokenService tokenServices = new AlpsRedisTokenService();
        // 这里的签名key 保持和认证中心一致
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        tokenServices.setTokenStore(redisTokenStore);
        log.info("buildRedisTokenServices[{}]", tokenServices);
        return tokenServices;
    }


    /**
               *       认证服务器原始方式创建AccessToken
     * @param endpoints
     * @param postParameters
     * @return
     * @throws Exception
     */
    public static OAuth2AccessToken createAccessToken(AuthorizationServerEndpointsConfiguration endpoints, Map<String, String> postParameters) throws Exception {
        String clientId = postParameters.get("client_id");
        String clientSecret = postParameters.get("client_secret");
        Assert.notNull(clientId, "client_id not null");
        Assert.notNull(clientSecret, "client_secret not null");
        clientId = clientId.trim();
        ClientDetailsService clientDetailsService = (ClientDetailsService) ReflectionUtils.getFieldValue(endpoints,"clientDetailsService");
        PasswordEncoder passwordEncoder = SpringContextHolder.getBean(PasswordEncoder.class);
        // 验证客户端
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
        if (clientDetails == null) {
            throw new NoSuchClientException("No client with requested id:" + clientId);
        }
        if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
            throw new InvalidClientException("Bad client credentials");
        }
        // 生成token
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(clientId, clientSecret, Collections.emptyList());
        ResponseEntity<OAuth2AccessToken> responseEntity = endpoints.tokenEndpoint().postAccessToken(authRequest, postParameters);
        return responseEntity.getBody();
    }
}
