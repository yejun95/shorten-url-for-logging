package kr.co.shortenurlservice.presentation;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. 요청을 래핑하여 Body 캐싱
        // -> Body 읽기 제한: InputStream은 한 번만 읽을 수 있음
        // -> Stream 소진: Body를 읽으면 나중에 Controller에서 읽을 수 없음
        // -> 에러 발생: Controller에서 Body 읽기 시 Stream closed 에러
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

        // 모든 요청 로깅 - URL, Method, Body
        filterChain.doFilter(wrappedRequest, response);

        // 요청 처리 후 Body 읽기
        log.trace("{} {} | Body: {}",
                request.getMethod(),
                request.getRequestURI(),
                getRequestBody(wrappedRequest));
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            return new String(content, StandardCharsets.UTF_8);
        }
        return "Empty";
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/static/") ||
                path.startsWith("/css/") ||
                path.startsWith("/js/") ||
                path.startsWith("/images/");
    }
}
