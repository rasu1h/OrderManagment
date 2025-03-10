package source.testmodule.ControllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import source.testmodule.Application.Services.OrderService;
import source.testmodule.Domain.Entity.User;
import source.testmodule.Infrastructure.Configurations.Jwt.JwtAuthFilter;
import source.testmodule.Infrastructure.Configurations.Jwt.JwtTokenProvider;
import source.testmodule.Infrastructure.Configurations.WebConfig;
import source.testmodule.Infrastructure.Repository.UserRepository;
import source.testmodule.Presentation.Controllers.OrderController;
import source.testmodule.Presentation.DTO.OrderDTO;
import source.testmodule.Presentation.DTO.Requests.OrderRequest;
import source.testmodule.TestModuleApplication;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthFilter.class))
@ContextConfiguration(classes = {TestModuleApplication.class})
@Import({OrderControllerTest.TestConfig.class})

public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserRepository userRepository() {
            UserRepository mockRepo = Mockito.mock(UserRepository.class);
            User testUser = new User();
            testUser.setId(1L);
            testUser.setEmail("user");
            // Настройте остальные поля по необходимости
            Mockito.when(mockRepo.findByEmail("user")).thenReturn(Optional.of(testUser));
            return mockRepo;
        }


        @Bean
        public JwtTokenProvider jwtTokenProvider() {
            // Можно вернуть настоящую реализацию или простую "заглушку"
            return new JwtTokenProvider();
        }

        @Bean
        public WebConfig webConfig(UserRepository userRepository) {
            return new WebConfig(userRepository);
        }

    }


    /**
     * Тест для получения заказа по ID.
     * Пользователь с ролью USER должен иметь доступ к данному эндпоинту.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetOrderById() throws Exception {
        Long orderId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(orderId);
        orderDTO.setDescription("Test Order");

        Mockito.when(orderService.getOrderById(eq(orderId), any(User.class)))
                .thenReturn(orderDTO);

        mockMvc.perform(get("/orders/my/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(orderDTO)));
    }



    /**
     * Тест для попытки удаления заказа обычным пользователем.
     * Пользователь с ролью USER не должен иметь доступ к эндпоинту для удаления.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteOrderAsUserForbidden() throws Exception {
        Long orderId = 1L;
        // Эндпоинт для удаления требует роль ADMIN, поэтому ожидание статус Forbidden
        mockMvc.perform(delete("/orders/{orderId}", orderId))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест для получения списка заказов с фильтрами (доступен только администратору).
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetFilteredOrdersAsAdmin() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setDescription("Filtered Order");

        Mockito.when(orderService.getFilteredOrders(null, null, null))
                .thenReturn(Collections.singletonList(orderDTO));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(orderDTO))));
    }

}
