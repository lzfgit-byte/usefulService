package com.ilzf.base.argumentResolvers;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ilzf.base.annotation.RequestBodyJson;
import com.ilzf.utils.StringUtilIZLF;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class RequestBodyJsonArgumentResolvers implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestBodyJson.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Class<?> parameterType = parameter.getParameterType();
        RequestBodyJson parameterAnnotation = parameter.getParameterAnnotation(RequestBodyJson.class);
        String value = parameterAnnotation.value();
        String str = StringUtilIZLF.readStrFormStream(Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class)).getInputStream());
        JSONObject jsonObject = JSONUtil.parseObj(str);
        String value_ = StringUtilIZLF.wrapperString(jsonObject.get(value));
        if(StringUtilIZLF.isBlankOrEmpty(value_)){
            throw new Exception("RequestBodyJson 参数解析失败" + str);
        }
        if(String.class == parameterType){
            return  value_;
        }else if (Integer.class == parameterType){
            return Integer.valueOf(jsonObject.get(value).toString());
        }else {
            throw new Exception("RequestBodyJson 参数解析失败" + str);
        }
    }
}
