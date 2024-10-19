package com.sky.service.impl;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;

import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    //请求微信服务器的接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    //请求微信服务器接口的grant_type授权类型 此处固定值
    public static final String GRANT_TYPE = "authorization_code";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserMapper userMapper;
    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {

        //方法在下面
        String openId = getOpenId(userLoginDTO.getCode());

        //判断OpenId是否成功获取,如果为空则登录失败
        if (openId == null ) {
            throw new DeletionNotAllowedException(MessageConstant.LOGIN_FAILED);
        }

        //判断当前用户是不是新用户(OpenId是否存在user表中)
        User user = userMapper.selectByOpenId(openId);

        //如果是新用户.则完成注册
        if (user == null) {
            user  = User.builder()
                    .openid(openId)
                            .createTime(LocalDateTime.now()).build();

            userMapper.insert(user);
        }

        //如果不是,则将查到的数据封装入UserLoginVO返回

        //创建一个哈希,设置一个jwt工具类的载荷(令牌中存的东西)
        HashMap<String, Object> claimsUser = new HashMap<>();
        claimsUser.put(JwtClaimsConstant.USER_ID,user.getId());

        //通过JwtUtil工具类,生成令牌
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claimsUser);

        //将令牌、OpenID、用户id封装入UserLoginVO返回
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token).build();


        return userLoginVO;
    }

    /**
     * 调用微信接口服务,获取微信用户id
     * @param code
     * @return
     */
    private String getOpenId(String code){
        //调用微信接口服务,获得当前微信用户的OpenId

        Map<String, String> claimsWX = new HashMap<>();
        claimsWX.put("appid",weChatProperties.getAppid());
        claimsWX.put("secret",weChatProperties.getSecret());
        claimsWX.put("js_code",code);
        claimsWX.put("grant_type",GRANT_TYPE);

        //返回json字符串
        String body = HttpClientUtil.doGet(WX_LOGIN, claimsWX);
        //转化为json对象,从中获取openId
        JSONObject jsonObject = JSON.parseObject(body);
        String openId = jsonObject.getString("openid");
        return openId;
    }
}
