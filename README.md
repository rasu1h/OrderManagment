# 🚀 Order Management System

Простая система управления заказами с использованием Spring Boot и PostgreSQL.

---

## 🚀 Быстрый старт

### Предварительные условия
- Установите [Docker](https://www.docker.com/get-started)
- Установите [Docker Compose](https://docs.docker.com/compose/install/)

### Клонирование и запуск проекта

1. **Клонируйте репозиторий:**

    ```bash
    git clone https://github.com/rasu1h/OrderManagment.git
    cd OrderManagment
    ```

2. **Запустите сервисы через Docker Compose:**

    ```bash
    docker compose up --build
    ```

3. **Доступ к сервисам:**
    - **API:** [http://localhost:8080](http://localhost:8080)
    - **База данных:** порт `5432`  
      (логин/пароль: `postgres` / `secret`)

---

## 📚 Документация API

- **Swagger UI:**  
  [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

- **OpenAPI спецификация:**  
  [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---
## 📚 API Endpoints

### Base URL
**http://localhost:8080**

### Authorization
| Метод | Эндпоинт         | Описание                        |
|-------|------------------|---------------------------------|
| POST  | `/auth/login`    | Авторизация пользователя        |
| POST  | `/auth/register` | Регистрация нового пользователя |

### Orders
| Метод  | Эндпоинт                | Описание                                         |
|--------|-------------------------|--------------------------------------------------|
| GET    | `/orders`               | Получение заказов с фильтрацией                  |
| DELETE | `/orders/{orderId}`     | Удаление заказа по ID из кеша(мягкое удаление)   |
| GET    | `/orders/my/{orderId}`  | Получение заказа по ID для текущего пользователя |
| PUT    | `/orders/my/{orderId}`  | Обновление заказа                                |
| POST   | `/orders/my/create`     | Создание нового заказа                           |

### Products
| Метод | Эндпоинт             | Описание                     |
|-------|----------------------|------------------------------|
| POST  | `/products/create`   | Добавление нового продукта   |

---

## 🔐 JWT Token Authentication

Система использует JWT (JSON Web Token) для защиты API-эндпоинтов. После успешной авторизации через `/auth/login` сервер генерирует JWT, который клиент должен отправлять в заголовке `Authorization` при последующих запросах:


### Основные моменты:
- **Генерация токена:**  
  При логине генерируется JWT, содержащий идентификатор пользователя и дополнительные данные (claims), подписанный секретным ключом.

- **Структура JWT:**  
  Токен состоит из трёх частей:
    - **Header (Заголовок):** Содержит тип токена (`JWT`) и алгоритм подписи (например, `HS256`).
    - **Payload (Полезная нагрузка):** Содержит claims, такие как идентификатор пользователя, роли и время истечения срока действия.
    - **Signature (Подпись):** Обеспечивает целостность токена с помощью секретного ключа.

- **Истечение срока действия:**  
  Токен имеет время жизни, после которого он становится недействительным. После истечения срока действия пользователь должен заново пройти аутентификацию (либо использовать механизм обновления токена, если он реализован).

- **Валидация токена:**  
  При каждом запросе фильтр JWT проверяет корректность токена. Если токен действителен, Spring Security устанавливает контекст аутентификации для обработки запроса.

- **Рекомендации по безопасности:**
    - Передавайте токены только по HTTPS.
    - Надёжно храните секретный ключ и не раскрывайте его.
    - Реализуйте правильное управление временем жизни токена и, при необходимости, механизм обновления (refresh).

---

Этот документ служит справочным руководством для разработчиков, позволяя быстро ознакомиться с ключевыми эндпоинтами API и особенностями аутентификации через JWT.

## 🛠 Структура проекта

```plaintext
.
├── src
│   ├── main
│   │   ├── java
│   │   │   └── source
│   │   │       └── testmodule
│   │   │           ├── Application         # Бизнес-логика
│   │   │           │   └── Services
│   │   │           │       ├── Exceptions
│   │   │           │       └── Implement
│   │   │           ├── Domain              # Модель данных и связь с БД
│   │   │           │   ├── Entity
│   │   │           │   └── Enums
│   │   │           ├── Infrastructure      # Инфраструктурный слой (Security, Configurations)
│   │   │           │   ├── Configurations
│   │   │           │   │   ├── Jwt
│   │   │           │   │   └── Security
│   │   │           │   └── Repository
│   │   │           └── Presentation        # Controllers и DTO
│   │   │               ├── Controllers
│   │   │               └── DTO
│   │   │                   ├── Requests
│   │   │                   └── Responses
│   │   └── resources                      # Ресурсы проекта
│   │       ├── static
│   │       ├── data.sql                   # SQL-скрипт для инициализации БД
│   │       └── templates
│   └── test
│       └── java
│           ├── ControllersTest          # Тесты контроллеров
│           └── source
│               └── testmodule           # Тестовый код приложения
├── pom.xml                              # Maven-конфигурация
├── Dockerfile                           # Докеризация приложения
├── docker-compose.yml                   # Оркестрация сервисов
└── README.md                            # Документация проекта
```
# 🛠️ Используемые технологии

- **Backend:**  
  Spring Boot, Spring MVC, Spring Data JPA, Spring Security, Spring Boot Actuator
- **База данных:**  
  PostgreSQL
- **Документация API:**  
  springdoc-openapi (Swagger UI, OpenAPI)
- **Кэширование:**  
  Spring Cache (например, с использованием Caffeine)
- **Тестирование:**  
  JUnit, TestNG, Spring Boot Starter Test
- **Докеризация:**  
  Docker, Docker Compose

---

# ⚙️ Конфигурация приложения

### Переменные окружения (app-сервис)

| Переменная                      | Значение по умолчанию                    | Описание                                    |
|---------------------------------|------------------------------------------|---------------------------------------------|
| `SPRING_DATASOURCE_URL`         | `jdbc:postgresql://db:5432/testdb`         | URL для подключения к PostgreSQL            |
| `SPRING_DATASOURCE_USERNAME`    | `postgres`                               | Пользователь БД                             |
| `SPRING_DATASOURCE_PASSWORD`    | `secret`                                 | Пароль для БД                               |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update`                                 | Стратегия DDL Hibernate                       |

### Конфигурация PostgreSQL (db-сервис)

- **Образ:** `postgres:17.0`
- **База данных:** `testdb`
- **Данные:** Сохраняются в volume `postgres-data`
- **SQL-инициализация:** Файл `data.sql` выполняется при первом запуске PostgreSQL

---

# 🐳 Docker инфраструктура

### Сервисы

| Сервис | Порт | Описание                   |
|--------|------|----------------------------|
| `app`  | 8080 | Spring Boot приложение     |
| `db`   | 5432 | PostgreSQL база данных     |

### Команды Docker Compose

- **Сборка и запуск всех сервисов:**

  ```bash
  docker compose up --build
  ```
- **Остановка и удаление всех сервисов:**

```bash
docker compose down
```
Просмотр логов приложения:

```bash
docker compose logs -f app
```

# 🔄 Инициализация базы данных
При первом запуске PostgreSQL выполняется SQL-скрипт data.sql. Пример содержимого файла:

```sql
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

INSERT INTO users (name) VALUES ('Test User');
```
# 🛠️ Сборка и запуск без Docker
Сборка приложения:
```bash
mvn clean package
```
Запуск приложения:

```bash
java -jar target/your-app-name.jar
```
# 🔍 Доступные эндпоинты
Эндпоинт	Описание	URL
Health check	Проверка работоспособности приложения	GET http://localhost:8080/actuator/health
Metrics	Метрики приложения	GET http://localhost:8080/actuator/metrics
Swagger UI	Документация API	GET http://localhost:8080/swagger-ui.html
OpenAPI спецификация	OpenAPI спецификация	GET http://localhost:8080/v3/api-docs
## ⚠️ Возможные проблемы и их решение
Ошибки подключения к БД:
Убедитесь, что контейнер PostgreSQL полностью запущен перед стартом приложения.

# Занятые порты:
Проверьте, что порты 8080 (приложение) и 5432 (БД) не используются другими приложениями.

# Проблемы с Docker:
Ознакомьтесь с официальной документацией Docker по устранению неполадок.

# Проблемы с инициализацией БД:
Проверьте корректность файла data.sql и права доступа к volume postgres-data.

# Дополнительные настройки
Spring Boot Actuator и метрики
Если требуется настроить дополнительные метрики или изменить базовый путь для Actuator, используйте файл application.yml:

```yaml
management:
  endpoints:
    web:
      base-path: /actuator
      exposure:
        include: health,info,metrics
```
Swagger и OpenAPI
Для изменения настроек Swagger UI можно использовать следующие параметры:

```yaml
springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui
    enabled: true
    tags-sorter: alpha
    operations-sorter: alpha
```
# Контакты и поддержка
Если у вас возникли вопросы или проблемы с запуском, пожалуйста, создайте issue в репозитории или свяжитесь с поддержкой.

© 2025 Your Company Name. Все права защищены.