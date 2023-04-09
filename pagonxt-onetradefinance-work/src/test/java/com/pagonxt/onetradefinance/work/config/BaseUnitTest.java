package com.pagonxt.onetradefinance.work.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@SuppressWarnings("unchecked")
public abstract class BaseUnitTest {

    @Spy
    public ObjectMapper objectMapper = new ObjectMapper();

    public BaseUnitTest() {
        JavaTimeModule module = new JavaTimeModule();
        objectMapper.registerModule(module);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public JsonNode readJsonFileToJsonNode(String fileLocation) throws IOException {
        File requestFile = new ClassPathResource(fileLocation).getFile();

        return objectMapper.readTree(requestFile);
    }

    public Map<String, Object> readJsonFileToMap(String fileLocation) throws IOException {
        File file = new ClassPathResource(fileLocation).getFile();

        return objectMapper.readValue(file, Map.class);
    }

    public String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void mockUser(String userName) {
        Authentication authentication = mock(Authentication.class);
        doReturn(userName).when(authentication).getName();

        SecurityContext securityContext = mock(SecurityContext.class);
        doReturn(authentication).when(securityContext).getAuthentication();
        SecurityContextHolder.setContext(securityContext);
    }

    public void mockIsAdmin() {
        Authentication authentication = mock(Authentication.class);
        GrantedAuthority grantedAuthority = mock(GrantedAuthority.class);
        doReturn("GROUP_flowableAdministrator").when(grantedAuthority).getAuthority();
        List<GrantedAuthority> grantedAuthorities = List.of(grantedAuthority);
        doReturn(grantedAuthorities).when(authentication).getAuthorities();
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        doReturn(authentication).when(securityContext).getAuthentication();
        SecurityContextHolder.setContext(securityContext);
    }

    public void mockNotAdmin() {
        Authentication authentication = mock(Authentication.class);
        GrantedAuthority grantedAuthority = mock(GrantedAuthority.class);
        doReturn("GROUP_foo").when(grantedAuthority).getAuthority();
        List<GrantedAuthority> grantedAuthorities = List.of(grantedAuthority);
        doReturn(grantedAuthorities).when(authentication).getAuthorities();
        SecurityContext securityContext = mock(SecurityContext.class);
        doReturn(authentication).when(securityContext).getAuthentication();
        SecurityContextHolder.setContext(securityContext);
    }
}
