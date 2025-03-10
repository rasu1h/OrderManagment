package source.testmodule.ControllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import source.testmodule.Application.Services.OrderService;
import source.testmodule.Application.Services.UserService;
import source.testmodule.Domain.Entity.User;
import source.testmodule.Domain.Enums.OrderStatus;
import source.testmodule.Domain.Enums.UserRole;
import source.testmodule.Infrastructure.Configurations.Jwt.JwtAuthFilter;
import source.testmodule.Infrastructure.Configurations.Jwt.JwtTokenProvider;
import source.testmodule.Infrastructure.Configurations.Security.SecurityConfig;
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
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
@AutoConfigureMockMvc(addFilters = false) // Отключаем фильтры безопасности для тестов
@Import({
        SecurityConfig.class,
        JwtTokenProvider.class,
        OrderControllerTest.TestConfig.class
})
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public UserRepository userRepository() {
            UserRepository mockRepo = Mockito.mock(UserRepository.class);
            User testUser = new User();
            testUser.setId(1L);
            testUser.setEmail("user");
            Mockito.when(mockRepo.findByEmail("user")).thenReturn(Optional.of(testUser));
            return mockRepo;
        }
    }

    /**
     * Тест для создания заказа.
     * Пользователь с ролью USER должен иметь доступ к данному эндпоинту.
     */
    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void testCreateOrder() throws Exception {
        OrderRequest orderRequest = new OrderRequest();
        // Заполните orderRequest нужными данными для теста
        orderRequest.setProductId(1L);
        orderRequest.setQuantity(2);
        orderRequest.setDescription("Test Order");
        orderRequest.setStatus(OrderStatus.PENDING);
        orderRequest.setProductId(1L);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setDescription("Test Order");

        Mockito.when(orderService.createOrder(any(OrderRequest.class), any(User.class)))
                .thenReturn(orderDTO);

        mockMvc.perform(post("/orders/my/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * Тест для получения заказа по ID.
     * Пользователь с ролью USER должен иметь доступ к данному эндпоинту.
     */
    @Test
    @WithMockUser(username = "user", authorities = {"ROlE_USER"})
    public void testGetOrderById() throws Exception {
        Long orderId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(orderId);
        orderDTO.setDescription("Test Order");

        Mockito.when(orderService.getOrderById(eq(orderId), any(User.class)))
                .thenReturn(orderDTO);

        mockMvc.perform(get("/orders/my/{orderId}", orderId))
                .andExpect(status().isOk());
    }

    /**
     * Тест для удаления заказа администратором.
     * Пользователь с ролью ADMIN должен иметь доступ к эндпоинту для удаления.
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void testDeleteOrderAsAdmin() throws Exception {
        Long orderId = 1L;
        // Настройка заглушки для метода удаления
        Mockito.doNothing().when(orderService).softDelete(orderId);

        mockMvc.perform(delete("/orders/{orderId}", orderId))
                .andExpect(status().isNoContent());
    }

    /**
     * Тест для попытки удаления заказа обычным пользователем.
     * Пользователь с ролью USER не должен иметь доступ к эндпоинту для удаления.
     */
    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void testDeleteOrderAsUserForbidden() throws Exception {
        Long orderId = 1L;
        mockMvc.perform(delete("/orders/{orderId}", orderId))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест для получения списка заказов с фильтрами (доступен только администратору).
     */
    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void testGetFilteredOrdersAsAdmin() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setDescription("Filtered Order");

        Mockito.when(orderService.getFilteredOrders(OrderStatus.PENDING, 0.0, 1600.0))
                .thenReturn(Collections.singletonList(orderDTO));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk());
                   }
    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void testGetFilteredOrdersAsUser() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setDescription("Filtered Order");

        Mockito.when(orderService.getFilteredOrders(OrderStatus.PENDING, 0.0, 1600.0))
                .thenReturn(Collections.singletonList(orderDTO));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест для обновления заказа.
     * Пользователь с ролью USER должен иметь доступ к обновлению своего заказа.
     */
    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    public void testUpdateOrder() throws Exception {
        Long orderId = 1L;
        OrderRequest updateRequest = new OrderRequest();
        updateRequest.setQuantity(3);
        updateRequest.setDescription("Updated Order");
        updateRequest.setProductId(1L);

        OrderDTO updatedOrderDTO = new OrderDTO();
        updatedOrderDTO.setId(orderId);
        updatedOrderDTO.setDescription("Updated Order");
        updatedOrderDTO.setQuantity(3);
        updatedOrderDTO.setProducts(1L);


        Mockito.when(orderService.updateOrder(eq(orderId), any(OrderRequest.class), any(User.class)))
                .thenReturn(updatedOrderDTO);

        mockMvc.perform(put("/orders/my/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
